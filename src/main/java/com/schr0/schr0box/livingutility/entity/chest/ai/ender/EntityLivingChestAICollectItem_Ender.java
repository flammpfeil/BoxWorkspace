package com.schr0.schr0box.livingutility.entity.chest.ai.ender;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;

import com.schr0.schr0box.livingutility.core.InventoryLib;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest;
import com.schr0.schr0box.livingutility.entity.chest.ai.AIBaseEntityLivingChest;

public class EntityLivingChestAICollectItem_Ender extends AIBaseEntityLivingChest
{
    private float moveSpeed;
    private double searchRange;
    private double searchHeight;
    private float canCollectRange;

    private EntityItem targetItem;
    private PathEntity pathEntity;

    public EntityLivingChestAICollectItem_Ender(EntityLivingChest basechest, float speed, double range, double height, float collectrange)
    {
	super(basechest);
	this.setMutexBits(3);

	this.moveSpeed = speed;
	this.searchRange = range;
	this.searchHeight = height;
	this.canCollectRange = collectrange;
    }

    // AIが始まる判定
    @Override
    public boolean shouldExecute()
    {
	// お座り状態 or 取引状態の場合
	if (this.baseChest.isSitting() || this.baseChest.isTrading())
	{
	    return false;
	}

	// ターゲットの初期化
	this.targetItem = null;

	int tx = (int) this.baseChest.posX;
	int ty = (int) this.baseChest.posY;
	int tz = (int) this.baseChest.posZ;

	// ホームインベントリがある場合
	if (this.homePoint.getHomeInventory() != null)
	{
	    tx = this.homePoint.getHomePosX();
	    ty = this.homePoint.getHomePosY();
	    tz = this.homePoint.getHomePosZ();
	}

	// 走査範囲のList
	List<EntityItem> itemlist = this.theWorld.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(tx - this.searchRange, ty - this.searchHeight, tz - this.searchRange, tx + this.searchRange, ty + this.searchHeight, tz + this.searchRange));

	// ターゲットを登録
	this.targetItem = this.getTargetEntityItem(itemlist, this.baseChest, this.baseChest.getHeldItem());

	// 主人のエンダーチェスト
	InventoryEnderChest enderinventory = (this.baseChest.getOwner() instanceof EntityPlayer) ? ((EntityPlayer) this.baseChest.getOwner()).getInventoryEnderChest() : null;

	// ターゲットが登録されている ＆ エンダーチェストが存在する ＆ ターゲットを入れるだけの空きスロットが存在する場合
	if (this.targetItem != null && enderinventory != null && !InventoryLib.isFullInventory(enderinventory, this.targetItem.getEntityItem()))
	{
	    return true;
	}

	return false;
    }

    // AIが継続する際の判定
    @Override
    public boolean continueExecuting()
    {
	// ターゲットが登録されていない場合
	if (this.targetItem == null)
	{
	    return false;
	}

	// ターゲットが存在していない or 見えていない場合
	if (this.targetItem.isDead || !this.baseChest.getEntitySenses().canSee(this.targetItem))
	{
	    return false;
	}

	// tickCounterが60以上になる場合
	if (60 < this.getTickCounter())
	{
	    // ----------------- ログ ----------------- //
	    System.out.println(this.getClass().getName() + " !! ORVER !! ");
	    return false;
	}

	return true;
    }

    // AIが始まった際に呼ばれる処理
    @Override
    public void startExecuting()
    {
	super.startExecuting();
    }

    // AIが終了する際に呼ばれる処理
    @Override
    public void resetTask()
    {
	super.resetTask();

	// ターゲットの初期化
	this.targetItem = null;

	// 蓋を閉じる
	this.baseChest.setOpen(false);
    }

    // AIの処理
    @Override
    public void updateTask()
    {
	super.updateTask();

	// ターゲットと0.5D以上離れている場合
	if (0.5D < this.baseChest.getDistanceSq(this.targetItem.posX, this.targetItem.posY, this.targetItem.posZ))
	{
	    // pathNavigaterがない場合
	    if (this.pathNavigater.noPath())
	    {
		// ターゲットを注視
		this.baseChest.getLookHelper().setLookPositionWithEntity(this.targetItem, 10.0F, this.baseChest.getVerticalFaceSpeed());

		// ターゲットに近付く
		this.pathNavigater.tryMoveToXYZ(this.targetItem.posX, this.targetItem.posY, this.targetItem.posZ, this.moveSpeed);
	    }

	    // 走査範囲のList（canCollectRange, canCollectRange/2, canCollectRange）
	    List<EntityItem> itemlist = this.theWorld.getEntitiesWithinAABB(EntityItem.class, this.baseChest.boundingBox.expand(this.canCollectRange, this.canCollectRange / 2, this.canCollectRange));
	    EntityItem nearitem = this.getTargetEntityItem(itemlist, this.baseChest, (ItemStack) null);

	    // nearitemとtargetItemが完全一致している場合
	    if (nearitem != null && ItemStack.areItemStackTagsEqual(this.targetItem.getEntityItem(), nearitem.getEntityItem()))
	    {
		// エンダーチェスト
		InventoryEnderChest enderinventory = null;

		if (this.baseChest.getOwner() instanceof EntityPlayer)
		{
		    enderinventory = ((EntityPlayer) this.baseChest.getOwner()).getInventoryEnderChest();
		}

		if (enderinventory != null)
		{
		    // EntityItemをInventoryに搬入（TileEntityHopper）
		    TileEntityHopper.func_145898_a(enderinventory, nearitem);

		    // ターゲットの初期化
		    this.targetItem = null;
		}
	    }
	}

	// 蓋の開閉処理
	this.baseChest.modelMotion.setCoverMotion(this.baseChest, this.targetItem == null);
    }

    // ------------------------- ↓独自の実装↓ -------------------------//

    // 対象となるアイテムを登録
    private EntityItem getTargetEntityItem(List<EntityItem> itemlist, EntityLiving living, ItemStack itemstack)
    {
	// itemlistの範囲を走査
	for (EntityItem entityitem : itemlist)
	{
	    // 対象が存在している && livingから見えている場合
	    if (entityitem.isEntityAlive() && living.getEntitySenses().canSee(entityitem))
	    {
		ItemStack entityitemstack = entityitem.getEntityItem();

		// itemstackが存在しない場合
		if (itemstack == null)
		{
		    return entityitem;
		}
		// entityitemstackとitemstackが一致している場合
		else if (itemstack.isItemEqual(entityitemstack))
		{
		    return entityitem;
		}
	    }
	}

	return (EntityItem) null;
    }

}
