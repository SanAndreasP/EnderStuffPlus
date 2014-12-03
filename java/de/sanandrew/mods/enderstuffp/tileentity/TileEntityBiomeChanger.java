package de.sanandrew.mods.enderstuffp.tileentity;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.sanandrew.core.manpack.util.javatuples.Unit;
import de.sanandrew.mods.enderstuffp.network.EnumPacket;
import de.sanandrew.mods.enderstuffp.network.PacketProcessor;
import de.sanandrew.mods.enderstuffp.network.packet.PacketBiomeChangerActions;
import de.sanandrew.mods.enderstuffp.network.packet.PacketBiomeChangerActions.EnumAction;
import de.sanandrew.mods.enderstuffp.util.EnderStuffPlus;
import de.sanandrew.mods.enderstuffp.util.EnumParticleFx;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class TileEntityBiomeChanger
    extends TileEntity
    implements IEnergyReceiver
{
    private byte currRange = 0;
    private EnumPerimForm form = EnumPerimForm.CIRCLE;
    private boolean isActive = false;
    private boolean isReplacingBlocks = false;
    private byte maxRange = 16;
    private boolean prevIsActive = false;
    private Random rand = new Random();
    private long ticksActive = 0;
    private float renderBeamAngle = 360.0F;
    private float renderBeamHeight = 0.0F;
    private int currentRenderPass = 0;
    private int usedFlux = 0;

    public int fluxAmount = 0;
    private int prevFluxAmount = -1;

    public TileEntityBiomeChanger() {}

    @Override
    public boolean canUpdate() {
        return true;
    }

    public void changeBiome() {
        switch( this.form ){
            case SQUARE :
                this.changeBiomeSquare();
                break;
            case RHOMBUS :
                this.changeBiomeRhombus();
                break;
            default :
                this.changeBiomeCircle();
        }
    }

    private void changeBiomeCircle() {
        for( int x = -this.currRange; x <= this.currRange; x++ ) {
            for( int z = -this.currRange; z <= this.currRange; z++ ) {
                if( Math.sqrt(x * x + z * z) + 0.5F < this.currRange && Math.sqrt(x * x + z * z) + 1.5D > this.currRange ) {
                    this.changeBiomeBlock(x, z);
                }
            }
        }
    }

    private void changeBiomeRhombus() {
        for( int x = -this.currRange; x <= this.currRange; x++ ) {
            for( int z = -this.currRange; z <= this.currRange; z++ ) {
                if( MathHelper.abs_int(x) + MathHelper.abs_int(z) == this.currRange ) {
                    this.changeBiomeBlock(x, z);
                }
            }
        }
    }

    private void changeBiomeSquare() {
        for( int x = -this.currRange; x <= this.currRange; x++ ) {
            for( int z = -this.currRange; z <= this.currRange; z++ ) {
                if( MathHelper.abs_int(x) == this.currRange || MathHelper.abs_int(z) == this.currRange ) {
                    this.changeBiomeBlock(x, z);
                }
            }
        }
    }

    private void changeBiomeBlock(int x, int z) {
        int x1 = x + this.xCoord;
        int z1 = z + this.zCoord;
        int y = this.worldObj.getTopSolidOrLiquidBlock(x1, z1);

        Chunk chunk = this.worldObj.getChunkFromBlockCoords(x1, z1);
        byte[] biomeArray = chunk.getBiomeArray();
        int currBiome = this.getBiomeId();

        if( this.isReplacingBlocks && !this.worldObj.isRemote ) {
            BiomeGenBase prevBiome = BiomeGenBase.getBiome(biomeArray[(z1 & 0xF) << 4 | (x1 & 0xF)] & 255);

            if( this.worldObj.getBlock(x1, y - 1, z1) == prevBiome.topBlock && this.worldObj.canBlockSeeTheSky(x1, y, z1) ) {
                BiomeGenBase biome = BiomeGenBase.getBiome(currBiome);
                this.worldObj.setBlock(x1, y - 1, z1, biome.topBlock, biome.field_150604_aj, 3);
                for( int i = 0; i < 5 && y - 1 - i >= 0; i++ ) {
                    if( this.worldObj.getBlock(x1, y - 1 - i, z1) == prevBiome.fillerBlock ) {
                        this.worldObj.setBlock(x1, y - 1 - i, z1, biome.fillerBlock, 0, 3);
                    }
                }
            }
        }

        biomeArray[(z1 & 0xF) << 4 | (x1 & 0xF)] = (byte) currBiome;
        EnderStuffPlus.proxy.spawnParticle(EnumParticleFx.FX_BIOME_DATA, x1 + 0.5F, y, z1 + 0.5D,
                                           this.worldObj.provider.dimensionId, Unit.with((short) currBiome));

        chunk.setBiomeArray(biomeArray);
        chunk.setChunkModified();
        this.worldObj.markBlockForUpdate(x1, y, z1);
    }

    public short getCurrRange() {
        return (short) (this.currRange & 255);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("currRange", this.getCurrRange());
        nbt.setShort("maxRange", this.getMaxRange());
        nbt.setByte("radForm", this.getRadForm());
        nbt.setBoolean("isActive", this.isActive);
        nbt.setBoolean("isReplacingBlocks", this.isActive);
        nbt.setInteger("fluxAmount", this.fluxAmount);

        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.setCurrRange(nbt.getShort("currRange"));
        this.setMaxRange(nbt.getShort("maxRange"));
        this.form = EnumPerimForm.VALUES[nbt.getByte("radForm")];
        this.isActive = nbt.getBoolean("isActive");
        this.isReplacingBlocks = nbt.getBoolean("isReplacingBlocks");
        this.fluxAmount = nbt.getInteger("fluxAmount");
    }

    public String getName() {
        return "tile.biomeChanger.name";
    }

    public short getMaxRange() {
        return (short) (this.maxRange & 255);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 16384.0D;
    }

    public byte getRadForm() {
        return (byte) this.form.ordinal();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        this.setMaxRange(nbt.getShort("maxRange"));
        this.setCurrRange(nbt.getShort("currRange"));
        this.form = EnumPerimForm.VALUES[nbt.getByte("radiusForm")];
        this.isActive = nbt.getBoolean("isActive");
        this.prevIsActive = nbt.getBoolean("prevActive");
        this.isReplacingBlocks = nbt.getBoolean("isReplacingBlocks");
        this.fluxAmount = nbt.getInteger("fluxAmount");
        this.usedFlux = nbt.getInteger("usedFlux");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt.setShort("currRange", this.getCurrRange());
        nbt.setShort("maxRange", this.getMaxRange());
        nbt.setByte("radiusForm", this.getRadForm());
        nbt.setBoolean("isActive", this.isActive);
        nbt.setBoolean("prevActive", this.prevIsActive);
        nbt.setBoolean("isReplacingBlocks", this.isReplacingBlocks);
        nbt.setInteger("fluxAmount", this.fluxAmount);
        nbt.setInteger("usedFlux", this.usedFlux);
    }

    public void activate() {
        if( !this.worldObj.isRemote ) {
            if( this.getBiomeId() >= 0 && this.fluxAmount > 0 ) {
                this.isActive = true;
                PacketBiomeChangerActions.sendPacketClient(this, EnumAction.ACTIVATE, null);
            }
        } else {
            this.isActive = true;
        }
    }

    public void deactivate() {
        if( !this.worldObj.isRemote ) {
            this.isActive = false;
            this.ticksActive = 0;
            PacketBiomeChangerActions.sendPacketClient(this, EnumAction.DEACTIVATE, null);
        } else {
            this.isActive = false;
        }
    }

    public void setCurrRange(int par1CurrRange) {
        if( par1CurrRange > 255 || par1CurrRange < 0 ) {
            return;
        }

        this.currRange = (byte) (par1CurrRange & 255);
    }

    public void setMaxRange(int par1MaxRange) {
        if( par1MaxRange > 255 || par1MaxRange < 0 ) {
            return;
        }

        this.maxRange = (byte) (par1MaxRange & 255);
    }

    public void setRadForm(EnumPerimForm form) {
        this.form = form;
    }

    @Override
    public void updateEntity() {
        if( this.isActive ) {
//            this.ticksActive++;
            if( !this.worldObj.isRemote ) {
                int fluxUsage = this.getFluxUsage();

                if( this.currRange >= this.maxRange ) {
                    this.deactivate();
                    this.currRange = 0;
                } else if( fluxUsage > 0 && this.fluxAmount > 0 ) {
                    if( this.usedFlux >= fluxUsage * 20 ) {
                        this.changeBiome();
                        PacketBiomeChangerActions.sendPacketClient(this, EnumAction.CHANGE_BIOME, null);
                        this.currRange++;
                        this.usedFlux -= fluxUsage * 20;
                    } else {
                        int subtract = Math.min(fluxUsage, this.fluxAmount);
                        this.usedFlux += subtract;
                        this.fluxAmount -= subtract;
                    }
                }
            } else {
                this.renderBeamAngle += 2.0F;
                if( this.renderBeamAngle > 360.0F ) {
                    this.renderBeamAngle -= 360.0F;
                }

                if( this.renderBeamHeight < 10.0F ) {
                    this.renderBeamHeight += 0.5F;
                }
            }
        } else {
            if( this.worldObj.isRemote ) {
                if( this.renderBeamAngle < 360.0F ) {
                    this.renderBeamAngle += 2.0F;
                }

                if( this.renderBeamHeight > 0 ) {
                    this.renderBeamHeight -= 0.5F;
                }
            }
        }

        if( !worldObj.isRemote ) {
            if( this.fluxAmount < this.getMaxEnergyStored(ForgeDirection.UNKNOWN) ) {
                for( ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS ) {
                    TileEntity te = this.worldObj.getTileEntity(this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);

                    if( te instanceof IEnergyProvider ) {
                        IEnergyProvider provider = (IEnergyProvider) te;

                        if( !provider.canConnectEnergy(direction.getOpposite()) ) {
                            continue;
                        }

                        int extractable = provider.extractEnergy(direction, this.getMaxEnergyStored(ForgeDirection.UNKNOWN) - this.fluxAmount, true);
                        int receivable = this.receiveEnergy(direction.getOpposite(), extractable, false);

                        provider.extractEnergy(direction, receivable, false);
                    }
                }
            }

            if( this.prevFluxAmount != this.fluxAmount ) {
                this.prevFluxAmount = this.fluxAmount;
                PacketProcessor.sendToAllAround(EnumPacket.TILE_ENERGY_SYNC, this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 64.0F,
                                                Unit.with(this)
                );
            }
        }
    }

    public float getRenderBeamHeight() {
        return this.renderBeamHeight;
    }

    public float getRenderBeamAngle() {
        return this.renderBeamAngle;
    }

    public boolean isReplacingBlocks() {
        return this.isReplacingBlocks;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRenderInPass(int pass) {
        this.currentRenderPass = pass;
        return pass <= 1;
    }

    public int getRenderPass() {
        return this.currentRenderPass;
    }

    public void replaceBlocks(boolean isReplacingBlocks) {
        this.isReplacingBlocks = isReplacingBlocks;
    }

    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int maxReceive, boolean checkSize) {
        int energyReceived = Math.min(this.getMaxEnergyStored(forgeDirection) - this.fluxAmount, Math.min(500, maxReceive));

        if( !checkSize ) {
            this.fluxAmount += energyReceived;
        }

        return energyReceived;
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return this.fluxAmount;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return 51_200;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection forgeDirection) {
        return forgeDirection != ForgeDirection.UP && forgeDirection != ForgeDirection.DOWN;
    }

    public int getFluxUsage() {
        return this.isActive ? 20 * (this.isReplacingBlocks ? 4 : 1) : 0;
    }

    public int getBiomeId() {
        TileEntity te = this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
        if( te instanceof TileEntityBiomeDataCrystal ) {
            return ((TileEntityBiomeDataCrystal) te).biomeID;
        }

        return -1;
    }

    public static enum EnumPerimForm
    {
        CIRCLE, RHOMBUS, SQUARE;

        private static EnumPerimForm[] VALUES = values();
    }
}
