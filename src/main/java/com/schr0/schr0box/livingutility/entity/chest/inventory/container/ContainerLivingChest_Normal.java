package com.schr0.schr0box.livingutility.entity.chest.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.schr0.schr0box.livingutility.LivingUtility;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest;
import com.schr0.schr0box.livingutility.entity.chest.inventory.EquipmentLivingChest;
import com.schr0.schr0box.livingutility.entity.chest.inventory.InventoryLivingChest;
import com.schr0.schr0box.livingutility.entity.chest.inventory.SpecialItemsLivingChest;

public class ContainerLivingChest_Normal extends Container
{
    private EntityLivingChest baseChest;
    private InventoryLivingChest baseChestInventory;
    private EquipmentLivingChest baseChestEquipment;
    private SpecialItemsLivingChest baseChestSpecialItems;

    public ContainerLivingChest_Normal(InventoryPlayer inventoryPlayer, EntityLivingChest basechest)
    {
	this.baseChest = basechest;
	int numRows = basechest.inventory.getSizeInventory() / 9;
	int slotX = 8;
	int slotY = 0;

	this.baseChestInventory = basechest.inventory;
	this.baseChestEquipment = basechest.equipment;
	this.baseChestSpecialItems = basechest.specialItems;

	this.baseChestInventory.load();
	this.baseChestEquipment.load();
	this.baseChestSpecialItems.load();

	// スロットを設置
	// addSlotToContainer( Slot(IInventory, slotIndex,xDisplayPosition, yDisplayPosition) )

	// 手持ちのアイテム
	this.addSlotToContainer(new Slot(this.baseChestEquipment, 0, 109, 46)
	{
	    @Override
	    public int getSlotStackLimit()
	    {
		return 1;
	    }
	});
	// 頭装備
	this.addSlotToContainer(new Slot(this.baseChestEquipment, 1, 8, 17)
	{
	    @Override
	    public int getSlotStackLimit()
	    {
		return 1;
	    }

	    @Override
	    public boolean isItemValid(ItemStack itemStack)
	    {
		if (itemStack == null)
		    return false;
		return itemStack.getItem().isValidArmor(itemStack, 0, null);
	    }

	});

	// 手装備
	this.addSlotToContainer(new Slot(this.baseChestEquipment, 2, 8, 53)
	{
	    @Override
	    public int getSlotStackLimit()
	    {
		return 1;
	    }

	    @Override
	    public boolean isItemValid(ItemStack itemStack)
	    {
		if (itemStack == null)
		    return false;
		return itemStack.getItem().isValidArmor(itemStack, 1, null);
	    }

	});

	// 足装備
	this.addSlotToContainer(new Slot(this.baseChestEquipment, 3, 81, 17)
	{
	    @Override
	    public int getSlotStackLimit()
	    {
		return 1;
	    }

	    @Override
	    public boolean isItemValid(ItemStack itemStack)
	    {
		if (itemStack == null)
		    return false;
		return itemStack.getItem().isValidArmor(itemStack, 2, null);
	    }

	});

	// 靴装備
	this.addSlotToContainer(new Slot(this.baseChestEquipment, 4, 81, 53)
	{
	    @Override
	    public int getSlotStackLimit()
	    {
		return 1;
	    }

	    @Override
	    public boolean isItemValid(ItemStack itemStack)
	    {
		if (itemStack == null)
		    return false;
		return itemStack.getItem().isValidArmor(itemStack, 3, null);
	    }

	});

	// 座標地点登録用
	this.addSlotToContainer(new Slot(this.baseChestSpecialItems, 0, 145, 47)
	{
	    @Override
	    public boolean isItemValid(ItemStack p_75214_1_)
	    {
		return super.isItemValid(p_75214_1_) && !this.getHasStack() && p_75214_1_.getItem() == LivingUtility.item_HomePointTicket;
	    }

	    @Override
	    public int getSlotStackLimit()
	    {
		return 1;
	    }
	});

	// LivingChestインベントリ（3 * 9）
	for (int slotCol = 0; slotCol < numRows; ++slotCol)
	{
	    for (int slotRow = 0; slotRow < 9; ++slotRow)
	    {
		slotY = 75;
		this.addSlotToContainer(new Slot(this.baseChestInventory, (slotRow + slotCol * 9), (slotX + slotRow * 18), (slotY + slotCol * 18)));
	    }
	}

	// プレイヤーインベントリ上部（3 * 9）
	for (int slotCol = 0; slotCol < 3; ++slotCol)
	{
	    for (int slotRow = 0; slotRow < 9; ++slotRow)
	    {
		slotY = 142;

		this.addSlotToContainer(new Slot(inventoryPlayer, (slotRow + slotCol * 9 + 9), (slotX + slotRow * 18), (slotY + slotCol * 18)));
	    }
	}

	// プレイヤーインベントリ下部（1 * 9）
	for (int slotRow = 0; slotRow < 9; ++slotRow)
	{
	    slotY = 199;

	    this.addSlotToContainer(new Slot(inventoryPlayer, slotRow, slotX + slotRow * 18, slotY));
	}
    }

    // 開けていられるかどうかの判定
    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
	return this.baseChestInventory.isUseableByPlayer(entityplayer) && this.baseChest.isEntityAlive() && this.baseChest.getDistanceToEntity(entityplayer) < 8.0F;
    }

    // Shift+左クリックしたときの処理
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
	Slot slot = (Slot) this.inventorySlots.get(slotIndex);
	ItemStack srcItemStack = null;

	// Shift+左クリックしたスロットに何かしらのアイテムがあった場合.
	if (slot != null && slot.getHasStack())
	{
	    // スロットからItemStackを取り出し, 移動先のdestItemStackに入れておく.
	    // mergeItemStack(移動するItemStack, 移動先の最小スロット番号, 移動先の最大スロット番号, 昇順or降順)

	    ItemStack destItemStack = slot.getStack();
	    srcItemStack = destItemStack.copy();

	    int minSlotSize = 6;
	    int maxSlotSize = this.baseChestInventory.getSizeInventory() + minSlotSize;

	    // 上インベントリ内なら, 下のインベントリに移動.
	    if (slotIndex < maxSlotSize && !this.mergeItemStack(destItemStack, maxSlotSize, this.inventorySlots.size(), false))
	    {
		return null;
	    }

	    // 下の1*9インベントリ内なら, 上のインベントリに移動
	    if (slotIndex >= maxSlotSize && !this.mergeItemStack(destItemStack, minSlotSize, maxSlotSize, false))
	    {
		return null;
	    }

	    // 移動後にスタック数がゼロならスロットを空にする.
	    if (destItemStack.stackSize == 0)
	    {
		slot.putStack((ItemStack) null);
	    }
	    else
	    {
		// スロット更新通知
		slot.onSlotChanged();
	    }
	}

	return srcItemStack;
    }

    // 閉じる際に呼ばれる処理
    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_)
    {
	super.onContainerClosed(p_75134_1_);

	// Traderの初期化
	this.baseChest.setTrader((EntityPlayer) null);

	// 各種インベントリのsave
	this.baseChestInventory.save();
	this.baseChestEquipment.save();
	this.baseChestSpecialItems.save();
    }
}
