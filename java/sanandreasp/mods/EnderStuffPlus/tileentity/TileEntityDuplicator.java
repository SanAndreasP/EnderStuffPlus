package sanandreasp.mods.EnderStuffPlus.tileentity;

import sanandreasp.core.manpack.helpers.SAPUtils;
import sanandreasp.mods.EnderStuffPlus.registry.RegistryDuplicator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityDuplicator extends TileEntity implements IInventory {
    private long ticksExisted = 0;

    private ItemStack[] dupeInv = new ItemStack[6];
    private int burnTime = 0;
    private int levels = 0;
    private int procTime = 0;
    public int maxBurnTime = 0;

    public TileEntityDuplicator() {
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    private boolean checkActivation() {
        boolean isInputDupable = RegistryDuplicator.isItemDupable(this.dupeInv[0]);
        boolean areInputsEqual = isInputDupable && RegistryDuplicator.areInputsEqual(this.dupeInv[0], this.dupeInv[1]);
        boolean canOrDoesBurn = RegistryDuplicator.getBurnTime(this.dupeInv[2]) > 0 || this.burnTime > 0;
        boolean isOutput1Valid = areInputsEqual && RegistryDuplicator.isOutputValid(this.dupeInv[0], this.dupeInv[3]);
        boolean isOutput2Valid = areInputsEqual && RegistryDuplicator.isOutputValid(this.dupeInv[0], this.dupeInv[4]);
        boolean isOutput3Valid = areInputsEqual && RegistryDuplicator.isOutputValid(this.dupeInv[0], this.dupeInv[5]);
        boolean hasNeededXP = RegistryDuplicator.getNeededExp(this.dupeInv[0]) <= this.levels;

        return canOrDoesBurn && isOutput1Valid && isOutput2Valid & isOutput3Valid && hasNeededXP;
    }

    @Override
    public void updateEntity() {
        ++this.ticksExisted;
        if( this.ticksExisted > Integer.MAX_VALUE ) {
            this.ticksExisted = 0;
        }

        if( !this.worldObj.isRemote ) {
            int sideMeta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) & 3;
            boolean state = (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) & 4) == 4;
            boolean active = this.checkActivation();

            if( active ) {
                if( this.burnTime > 0 ) {
                    --this.burnTime;
                } else {
                    this.maxBurnTime = this.burnTime = RegistryDuplicator.getBurnTime(this.dupeInv[2])*20;
                    this.dupeInv[2] = SAPUtils.decrStackSize(this.dupeInv[2]);
                }
                if( this.procTime < 20 ) {
                    if( this.ticksExisted % 10 == 0 ) {
                        ++this.procTime;
                    }
                } else {
                    ItemStack dupedItm = this.dupeInv[0].copy();
                    dupedItm.stackSize = 1;
                    this.dupeInv[0] = SAPUtils.decrStackSize(this.dupeInv[0]);
                    this.dupeInv[1] = SAPUtils.decrStackSize(this.dupeInv[1]);
                    this.dupeInv[3] = this.incrStack(this.dupeInv[3], dupedItm);
                    this.dupeInv[4] = this.incrStack(this.dupeInv[4], dupedItm);
                    this.dupeInv[5] = this.incrStack(this.dupeInv[5], dupedItm);

                    this.levels -= RegistryDuplicator.getNeededExp(dupedItm);

                    this.procTime = 0;
                }
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            } else if( state && !active ) {
                this.procTime = 0;
                this.maxBurnTime = this.burnTime = 0;
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }

            if( !state && active ) {
                this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, sideMeta | 4, 3);
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            } else if( state && !active ) {
                this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, sideMeta, 3);
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
        }
    }

    @Override
    public boolean shouldRefresh(int oldID, int newID, int oldMeta, int newMeta, World world, int x, int y, int z) {
//        System.out.println("remote: " + this.worldObj.isRemote);
//        System.out.println("refresh: " + (oldID != newID));
        return (oldID != newID);
    }

    public int getBurnTime() {
        return this.burnTime;
    }

    public int getProcTime() {
        return this.procTime;
    }

    public int getStoredLvl() {
        return this.levels;
    }

    public void addLevels(int lvl) {
        this.levels = Math.min(lvl + this.levels, 99999);
    }

    private ItemStack incrStack(ItemStack par1Stack, ItemStack par2Stack) {
        if( par1Stack == null) {
            return new ItemStack(par2Stack.itemID, 1, par2Stack.getItemDamage() );
        }
        par1Stack.stackSize++;
        if( par1Stack.stackSize > par1Stack.getMaxStackSize()) {
            par1Stack.stackSize = par1Stack.getMaxStackSize( );
        }
        return par1Stack;
    }

    private NBTTagCompound writeDupeNBT(NBTTagCompound par1Compound) {
        par1Compound.setInteger("BurnTime", this.burnTime);
        par1Compound.setInteger("MaxBurnTime", this.maxBurnTime);
        par1Compound.setInteger("StoredLvl", this.levels);
        par1Compound.setInteger("ProcTime", this.procTime);
        return par1Compound;
    }

    private void readDupeNBT(NBTTagCompound par1Compound) {
        this.burnTime = par1Compound.getInteger("BurnTime");
        this.maxBurnTime = par1Compound.getInteger("MaxBurnTime");
        this.levels = par1Compound.getInteger("StoredLvl");
        this.procTime = par1Compound.getInteger("ProcTime");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
        super.writeToNBT(par1nbtTagCompound);

        par1nbtTagCompound = this.writeDupeNBT(par1nbtTagCompound);

        NBTTagList var2 = new NBTTagList();
        for( int var3 = 0; var3 < this.dupeInv.length; ++var3 )
        {
            if( this.dupeInv[var3] != null )
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.dupeInv[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        par1nbtTagCompound.setTag("Items", var2);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
        super.readFromNBT(par1nbtTagCompound);

        this.readDupeNBT(par1nbtTagCompound);

        NBTTagList var2 = par1nbtTagCompound.getTagList("Items");
        this.dupeInv = new ItemStack[this.getSizeInventory()];

        for( int var3 = 0; var3 < var2.tagCount(); ++var3 )
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);

            byte var5 = var4.getByte("Slot");

            if( var5 >= 0 && var5 < this.dupeInv.length )
            {
                this.dupeInv[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
        this.readDupeNBT(pkt.data);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt = this.writeDupeNBT(nbt);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, nbt);
    }

    @Override
    public int getSizeInventory() {
        return 6;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return this.dupeInv[var1];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if( this.dupeInv[par1] != null )
        {
            ItemStack var3;

            if( this.dupeInv[par1].stackSize <= par2 )
            {
                var3 = this.dupeInv[par1];
                this.dupeInv[par1] = null;
                this.onInventoryChanged();
                return var3;
            }
            else
            {
                var3 = this.dupeInv[par1].splitStack(par2);

                if( this.dupeInv[par1].stackSize == 0 )
                {
                    this.dupeInv[par1] = null;
                }

                this.onInventoryChanged();
                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        ItemStack is = this.dupeInv[var1];
        this.setInventorySlotContents(var1, null);
        return is;
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        this.dupeInv[var1] = var2;

        if( var2 != null && var2.stackSize > this.getInventoryStackLimit() )
        {
            var2.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInvName() {
        return "tile.duplicator.name";
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this &&
                var1.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) < 64;
    }

    @Override
    public void openChest() { }

    @Override
    public void closeChest() { }

    @Override
    public boolean isInvNameLocalized() {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return false;
    }

}
