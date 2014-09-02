package com.schr0.schr0box.livingutility.entity.chest.inventory;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by Furia on 14/09/02.
 */
public class InventoryLivingEnderChest extends InventoryLivingChest {
    public InventoryLivingEnderChest(EntityLivingChest basechest) {
        super(basechest);
    }

    public IInventory getEnderInventory(){
        EntityLivingBase owner = this.baseChest.getOwner();
        if(owner != null && owner instanceof EntityPlayer)
            return ((EntityPlayer) owner).getInventoryEnderChest();
        else
            return null;
    }

    @Override
    public int getSizeInventory() {
        IInventory enderInv = getEnderInventory();

        if(enderInv != null)
            return enderInv.getSizeInventory();
        else
            return super.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int par1) {
        IInventory enderInv = getEnderInventory();

        if(enderInv != null)
            return enderInv.getStackInSlot(par1);
        else
            return super.getStackInSlot(par1);
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2) {
        IInventory enderInv = getEnderInventory();

        if(enderInv != null)
            return enderInv.decrStackSize(par1, par2);
        else
            return super.decrStackSize(par1, par2);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1) {
        IInventory enderInv = getEnderInventory();

        if(enderInv != null)
            return enderInv.getStackInSlotOnClosing(par1);
        else
            return super.getStackInSlotOnClosing(par1);
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        IInventory enderInv = getEnderInventory();

        if(enderInv != null)
            enderInv.setInventorySlotContents(par1, par2ItemStack);
        else
            super.setInventorySlotContents(par1, par2ItemStack);
    }

    @Override
    public int getInventoryStackLimit() {
        IInventory enderInv = getEnderInventory();

        if(enderInv != null)
            return enderInv.getInventoryStackLimit();
        else
            return super.getInventoryStackLimit();
    }

    @Override
    public void save() {
        IInventory enderInv = getEnderInventory();

        if(enderInv != null){
            for(int i = this.containerItems.length; 0 < i--;){
                ItemStack stack = this.containerItems[i];
                if(stack != null){
                    for(int ei = enderInv.getSizeInventory(); 0 < ei--;){
                        if(enderInv.getStackInSlot(ei) == null){
                            enderInv.setInventorySlotContents(ei,stack);
                            this.containerItems[i] = null;
                            break;
                        }
                    }
                }
            }

            enderInv.markDirty();
        }

        super.save();
    }

    @Override
    public void openInventory() {
        IInventory enderInv = getEnderInventory();

        if(enderInv != null)
            enderInv.openInventory();

        super.openInventory();
    }

    @Override
    public void closeInventory() {
        IInventory enderInv = getEnderInventory();

        if(enderInv != null)
            enderInv.closeInventory();

        super.closeInventory();
    }

    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
        IInventory enderInv = getEnderInventory();

        if(enderInv != null)
            return enderInv.isItemValidForSlot(par1, par2ItemStack);
        else
            return super.isItemValidForSlot(par1, par2ItemStack);
    }
}
