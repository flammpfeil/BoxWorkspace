package com.schr0.schr0box.livingutility.core;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryLib
{
    // -------------- インベントリが一杯の場合の判定 -------------- //
    public static boolean isFullInventory(IInventory inventory, ItemStack par1ItemStack)
    {
	return (getFirstEmptyStack(inventory) == -1 && storeItemStack(inventory, par1ItemStack) == -1);
    }

    private static int getFirstEmptyStack(IInventory inventory)
    {
	for (int i = 0; i < inventory.getSizeInventory(); ++i)
	{
	    if (inventory.getStackInSlot(i) == null)
	    {
		return i;
	    }
	}

	return -1;
    }

    private static int storeItemStack(IInventory inventory, ItemStack par1ItemStack)
    {
	for (int i = 0; i < inventory.getSizeInventory(); ++i)
	{
	    if (inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).getItem() == par1ItemStack.getItem() && inventory.getStackInSlot(i).isStackable() && inventory.getStackInSlot(i).stackSize < inventory.getStackInSlot(i).getMaxStackSize() && inventory.getStackInSlot(i).stackSize < inventory.getInventoryStackLimit() && (!inventory.getStackInSlot(i).getHasSubtypes() || inventory.getStackInSlot(i).getItemDamage() == par1ItemStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(inventory.getStackInSlot(i), par1ItemStack))
	    {
		return i;
	    }
	}

	return -1;
    }

    // -------------- 搬入可能なインベントリのget -------------- //
    public static IInventory getCanInsertInventory(IInventory myinv, IInventory targetinv, int side)
    {
	// 空のIInventory
	IInventory inventory = null;

	// Inventryを開く
	myinv.openInventory();
	targetinv.openInventory();

	// myinvを走査
	for (int slotM = 0; slotM < myinv.getSizeInventory(); slotM++)
	{
	    // taegetinvを走査
	    for (int slotT = 0; slotT < targetinv.getSizeInventory(); slotT++)
	    {
		ItemStack inItem = myinv.getStackInSlot(slotM);

		if (inItem != null && canInsertItems(targetinv, inItem, slotT, side))
		{
		    inventory = targetinv;
		}
	    }
	}

	// Inventryを閉じる
	myinv.closeInventory();
	targetinv.closeInventory();

	return inventory;
    }

    private static boolean canInsertItems(IInventory par1IInventory, ItemStack par2ItemStack, int slot, int side)
    {
	boolean flag = false;

	ItemStack slotItemStack = par1IInventory.getStackInSlot(slot);

	if (canInsertItemToInventory(par1IInventory, par2ItemStack, slot, side))
	{
	    if (slotItemStack == null)
	    {
		int max = Math.min(par2ItemStack.getMaxStackSize(), par1IInventory.getInventoryStackLimit());

		if (max >= par2ItemStack.stackSize)
		{
		    flag = true;
		}
	    }
	    else if (areItemStacksEqualItem(slotItemStack, par2ItemStack))
	    {
		int max = Math.min(par2ItemStack.getMaxStackSize(), par1IInventory.getInventoryStackLimit());

		if (max > slotItemStack.stackSize)
		{
		    flag = 0 < Math.min(par2ItemStack.stackSize, max - slotItemStack.stackSize);
		}
	    }
	}

	return flag;
    }

    private static boolean canInsertItemToInventory(IInventory par1IInventory, ItemStack par2ItemStack, int slot, int side)
    {
	return !par1IInventory.isItemValidForSlot(slot, par2ItemStack) ? false : !(par1IInventory instanceof ISidedInventory) || ((ISidedInventory) par1IInventory).canInsertItem(slot, par2ItemStack, side);
    }

    private static boolean areItemStacksEqualItem(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
	return par1ItemStack.getItem() != par2ItemStack.getItem() ? false : (par1ItemStack.getItemDamage() != par2ItemStack.getItemDamage() ? false : (par1ItemStack.stackSize > par1ItemStack.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(par1ItemStack, par2ItemStack)));
    }

    // -------------- インベントリ -> インベントリの搬入 -------------- //
    public static boolean insertInvToInv(IInventory myinv, IInventory targetinv, int side)
    {
	boolean flag = false;

	// Inventryを開く
	myinv.openInventory();
	targetinv.openInventory();

	// myinvを走査
	for (int mslot = 0; mslot < myinv.getSizeInventory(); mslot++)
	{
	    ItemStack myitemstack = myinv.getStackInSlot(mslot);

	    // アイテムをインベントリへ搬入
	    if (insertItemToInv(targetinv, myitemstack))
	    {
		if (myitemstack == null || myitemstack.stackSize == 0)
		{
		    myinv.setInventorySlotContents(mslot, (ItemStack) null);
		}

		flag = true;
	    }
	}

	// Inventryを閉じる
	myinv.closeInventory();
	targetinv.closeInventory();

	return flag;
    }

    // -------------- アイテム -> インベントリの搬入（InventoryPlayer） -------------- //
    public static boolean insertItemToInv(IInventory inventory, ItemStack itemstack)
    {
	int slot;

	if (itemstack == null)
	{
	    return false;
	}
	else if (itemstack.stackSize == 0)
	{
	    return false;
	}
	else
	{
	    if (itemstack.isItemDamaged())
	    {
		slot = getFirstEmptyStack(inventory);

		if (slot >= 0)
		{
		    inventory.setInventorySlotContents(slot, ItemStack.copyItemStack(itemstack));
		    itemstack.stackSize = 0;
		    return true;
		}
		else
		{
		    return false;
		}
	    }
	    else
	    {
		do
		{
		    slot = itemstack.stackSize;
		    itemstack.stackSize = storePartialItemStack(inventory, itemstack);
		}
		while (itemstack.stackSize > 0 && itemstack.stackSize < slot);

		return itemstack.stackSize < slot;
	    }
	}
    }

    private static int storePartialItemStack(IInventory inventory, ItemStack itemstack)
    {
	int slot;
	Item item = itemstack.getItem();
	int size = itemstack.stackSize;

	if (itemstack.getMaxStackSize() == 1)
	{
	    slot = getFirstEmptyStack(inventory);

	    if (slot < 0)
	    {
		return size;
	    }
	    else
	    {
		if (inventory.getStackInSlot(slot) == null)
		{
		    inventory.setInventorySlotContents(slot, ItemStack.copyItemStack(itemstack));
		}

		return 0;
	    }
	}
	else
	{
	    slot = storeItemStack(inventory, itemstack);

	    if (slot < 0)
	    {
		slot = getFirstEmptyStack(inventory);
	    }

	    if (slot < 0)
	    {
		return size;
	    }
	    else
	    {
		if (inventory.getStackInSlot(slot) == null)
		{
		    inventory.setInventorySlotContents(slot, new ItemStack(item, 0, itemstack.getItemDamage()));

		    if (itemstack.hasTagCompound())
		    {
			inventory.getStackInSlot(slot).setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
		    }
		}

		int i = size;

		if (size > inventory.getStackInSlot(slot).getMaxStackSize() - inventory.getStackInSlot(slot).stackSize)
		{
		    i = inventory.getStackInSlot(slot).getMaxStackSize() - inventory.getStackInSlot(slot).stackSize;
		}

		if (i > inventory.getInventoryStackLimit() - inventory.getStackInSlot(slot).stackSize)
		{
		    i = inventory.getInventoryStackLimit() - inventory.getStackInSlot(slot).stackSize;
		}

		if (i == 0)
		{
		    return size;
		}
		else
		{
		    size -= i;
		    inventory.getStackInSlot(slot).stackSize += i;

		    return size;
		}
	    }
	}
    }

}
