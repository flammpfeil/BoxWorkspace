package com.schr0.schr0box.livingutility.entity.chest.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest;

public class EquipmentLivingChest implements IInventory
{
    // 装備アイテム
    // 0 : 手持ち
    // 1 : 胴装備
    // 2 : 手装備
    // 3 : 足装備
    // 4 : 靴装備
    public ItemStack[] containerItems = new ItemStack[5];

    public EntityLivingChest baseChest;

    public EquipmentLivingChest(EntityLivingChest basechest)
    {
	this.baseChest = basechest;
    }

    // インベントリのSize
    @Override
    public int getSizeInventory()
    {
	return this.containerItems.length;
    }

    // 中身のItemStack
    @Override
    public ItemStack getStackInSlot(int par1)
    {
	return this.containerItems[par1];
    }

    // ???
    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
	if (this.containerItems[par1] != null)
	{
	    ItemStack itemstack;

	    if (this.containerItems[par1].stackSize <= par2)
	    {
		itemstack = this.containerItems[par1];
		this.containerItems[par1] = null;
		return itemstack;
	    }
	    else
	    {
		itemstack = this.containerItems[par1].splitStack(par2);

		if (this.containerItems[par1].stackSize == 0)
		{
		    this.containerItems[par1] = null;
		}

		return itemstack;
	    }
	}
	else
	{
	    return null;
	}
    }

    // Slotから読み込む中身のItemStack
    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
	if (this.containerItems[par1] != null)
	{
	    ItemStack itemstack = this.containerItems[par1];
	    this.containerItems[par1] = null;
	    return itemstack;
	}
	else
	{
	    return null;
	}
    }

    // インベントリへの搬入
    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
	this.containerItems[par1] = par2ItemStack;

	if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
	{
	    par2ItemStack.stackSize = this.getInventoryStackLimit();
	}
    }

    // インベントリの名称
    @Override
    public String getInventoryName()
    {
	return this.baseChest.getCommandSenderName();
    }

    // インベントリ名が変更可能かの判定
    @Override
    public boolean hasCustomInventoryName()
    {
	return true;
    }

    // 搬入されるItemStackの最大数
    @Override
    public int getInventoryStackLimit()
    {
	return 64;
    }

    // 中身が変化する際に呼ばれる
    @Override
    public void markDirty()
    {
	// 内部インベントリの保存
	this.save();
    }

    // インベントリを開ける際の条件
    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
	return this.baseChest.isDead ? false : par1EntityPlayer.getDistanceSqToEntity(this.baseChest) <= 64.0D;
    }

    // 開く際に呼ばれる
    @Override
    public void openInventory()
    {
	// 開く
	this.baseChest.setOpen(true);

	// インベントリの読み込み
	this.load();
    }

    // 閉じる際に呼ばれる
    @Override
    public void closeInventory()
    {
	// 閉じる
	this.baseChest.setOpen(false);

	// インベントリの保存
	this.save();
    }

    // 搬入可能なItemStackの判定
    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
	return true;
    }

    // ------------------------- ↓独自の実装↓ -------------------------//

    // インベントリの保存
    public void save()
    {
	NBTTagList nbttaglist = new NBTTagList();

	for (int i = 0; i < this.containerItems.length; i++)
	{
	    if (this.containerItems[i] != null)
	    {
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
		nbttagcompound1.setByte("EquipmentSlot", (byte) i);
		this.containerItems[i].writeToNBT(nbttagcompound1);
		nbttaglist.appendTag(nbttagcompound1);
	    }
	}

	// ItemStackのNBTに中身を保存
	NBTTagCompound nbttagcompound = this.baseChest.getEntityData();

	if (nbttagcompound == null)
	{
	    nbttagcompound = new NBTTagCompound();
	}

	nbttagcompound.setTag("EquipmentItems", nbttaglist);

	// EntityLivingのequipmentと同期
	for (int slot = 0; slot < this.containerItems.length; slot++)
	{
	    this.baseChest.setCurrentItemOrArmor(slot, this.containerItems[slot]);
	}
    }

    // インベントリの読み込み
    public void load()
    {
	// ItemStackのNBTを取得、空の中身を作成しておく
	NBTTagCompound nbttagcompound = this.baseChest.getEntityData();
	this.containerItems = new ItemStack[this.getSizeInventory()];

	// NBTが無ければ中身は空のままで
	if (nbttagcompound == null)
	{
	    return;
	}

	NBTTagList nbttaglist = nbttagcompound.getTagList("EquipmentItems", 10);

	for (int i = 0; i < nbttaglist.tagCount(); i++)
	{
	    NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
	    int j = nbttagcompound1.getByte("EquipmentSlot") & 0xff;

	    if (j >= 0 && j < this.containerItems.length)
	    {
		this.containerItems[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
	    }
	}

	// EntityLivingのequipmentと同期
	for (int slot = 0; slot < this.containerItems.length; slot++)
	{
	    this.baseChest.setCurrentItemOrArmor(slot, this.containerItems[slot]);
	}
    }

}
