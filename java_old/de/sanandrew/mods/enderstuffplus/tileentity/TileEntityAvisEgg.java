package de.sanandrew.mods.enderstuffplus.tileentity;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import de.sanandrew.mods.enderstuffplus.entity.living.EntityEnderAvis;
import de.sanandrew.mods.enderstuffplus.item.ItemAvisCompass;

public class TileEntityAvisEgg
    extends TileEntity
{
    private boolean belongsToPlayer = false;
    private String playerName = "";
    private Random rand = new Random();
    private int ticksUntilEnemy;
    private int ticksUntilHatch;

    public TileEntityAvisEgg() {
        this.setEnemySpawnTicks();
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public Packet getDescriptionPacket() {
        if( !this.worldObj.isRemote ) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setBoolean("belongsToPlayer", !this.playerName.isEmpty());
            return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbt);
        } else {
            return null;
        }
    }

    @Override
    public void invalidate() {
        if( this.worldObj.isRemote ) {
            ItemAvisCompass.removeEgg(this);
        }
        super.invalidate();
    }

    private boolean isValidDarkness() {
        return this.worldObj.getBlockLightValue(this.xCoord, this.yCoord, this.zCoord) <= 1;
    }

    @Override
    public void onChunkUnload() {
        if( this.worldObj.isRemote ) {
            ItemAvisCompass.removeEgg(this);
        }
        super.onChunkUnload();
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        if( this.worldObj.isRemote ) {
            this.belongsToPlayer = pkt.func_148857_g().getBoolean("belongsToPlayer");
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
        super.readFromNBT(par1nbtTagCompound);

        this.ticksUntilHatch = par1nbtTagCompound.getInteger("ticksHatch");
        this.playerName = par1nbtTagCompound.getString("playerName");
    }

    private void setEnemySpawnTicks() {
        this.ticksUntilEnemy = this.rand.nextInt(200) + 100;
    }

    @Override
    public void updateEntity() {
        if( this.worldObj.isRemote ) {
            if( !this.belongsToPlayer ) {
                ItemAvisCompass.addEgg(this);
            } else if( this.isValidDarkness() ) {
                this.worldObj.spawnParticle("portal", this.xCoord + 0.5D + (this.rand.nextDouble() - 0.5D) * 1.5D,
                                            this.yCoord + this.rand.nextDouble() * 1.5D - 0.25D,
                                            this.zCoord + 0.5D + (this.rand.nextDouble() - 0.5D) * 1.5D,
                                            (this.rand.nextDouble() - 0.5D) * 2D, -this.rand.nextDouble(),
                                            (this.rand.nextDouble() - 0.5D) * 2D);
                ItemAvisCompass.removeEgg(this);
            }
        } else {
            if( this.isValidDarkness() && !this.playerName.isEmpty() ) {
                if( ++this.ticksUntilHatch >= 12000 ) {
                    if( !this.worldObj.isRemote ) {
                        EntityEnderAvis avis = new EntityEnderAvis(this.worldObj);
                        avis.setTamed(true);
                        avis.setOwnerName(this.playerName);
                        avis.setPosition(this.xCoord + 0.5D, this.yCoord, this.zCoord + 0.5D);
                        this.worldObj.spawnEntityInWorld(avis);
                        avis.playLivingSound();
                    }
                    this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
                    this.worldObj.removeTileEntity(this.xCoord, this.yCoord, this.zCoord);
                }
            } else if( this.playerName.isEmpty()
                       && this.worldObj.getBlockLightValue(this.xCoord, this.yCoord, this.zCoord) <= 7 ) {
                AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(this.xCoord - 5D, this.yCoord, this.zCoord - 5D,
                                                                this.xCoord + 6D, this.yCoord + 3D, this.zCoord + 6D);
                if( this.ticksUntilEnemy <= 0
                    && (this.worldObj.getEntitiesWithinAABB(EntityEnderAvis.class, bb)).size() < 5 ) {
                    for( int i = 0; i <= this.rand.nextInt(3); i++ ) {
                        EntityEnderAvis avis = new EntityEnderAvis(this.worldObj);
                        avis.setAggressive(true);
                        avis.setPosition(this.xCoord - this.rand.nextInt(3) + 1, this.yCoord,
                                         this.zCoord - this.rand.nextInt(3) + 1);
                        this.worldObj.spawnEntityInWorld(avis);
                        avis.spawnExplosionParticle();
                        this.ticksUntilEnemy = this.rand.nextInt(200) + 100;
                    }
                } else {
                    this.ticksUntilEnemy--;
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
        super.writeToNBT(par1nbtTagCompound);

        par1nbtTagCompound.setInteger("ticksHatch", this.ticksUntilHatch);
        par1nbtTagCompound.setString("playerName", this.playerName);
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
