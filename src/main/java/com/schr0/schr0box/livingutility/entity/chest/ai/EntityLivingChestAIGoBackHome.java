package com.schr0.schr0box.livingutility.entity.chest.ai;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest;

public class EntityLivingChestAIGoBackHome extends AIBaseEntityLivingChest
{
    private float moveSpeed;
    private int searchRange;
    private int searchHeight;

    public EntityLivingChestAIGoBackHome(EntityLivingChest basechest, float speed, int range, int height)
    {
	super(basechest);
	this.setMutexBits(3);

	this.moveSpeed = speed;
	this.searchRange = range;
	this.searchHeight = height;
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

	// ホームインベントリがある場合
	if (this.homePoint.getHomeInventory() != null)
	{
	    // 周辺（8.0, 2.0, 8.0）にホームインベントリがない場合
	    return this.homePoint.getNearHomeInventory(this.baseChest.posX, this.baseChest.posY, this.baseChest.posZ, this.searchRange, this.searchHeight, this.searchRange) == null;
	}

	return false;
    }

    // AIが継続する際の判定
    @Override
    public boolean continueExecuting()
    {
	// tickCounterが60以上になる場合
	if (60 < this.getTickCounter())
	{
	    // ----------------- ログ ----------------- //
	    System.out.println(this.getClass().getName() + " !! ORVER !! ");
	    return false;
	}

	return super.continueExecuting();
    }

    // AIの処理
    @Override
    public void updateTask()
    {
	super.updateTask();

	// 座標の誤差修正
	double bx = ((double) (float) this.homePoint.getHomePosX()) + 0.5D;
	double by = (double) this.homePoint.getHomePosY();
	double bz = ((double) (float) this.homePoint.getHomePosZ()) + 0.5D;

	// ターゲットと0.5D以上離れている場合
	if (0.5D < this.baseChest.getDistanceSq(bx, by, bz))
	{
	    // pathNavigaterがない場合
	    if (this.pathNavigater.noPath())
	    {
		// ターゲットを注視
		this.baseChest.getLookHelper().setLookPosition(bx, by, bz, 10.0F, this.baseChest.getVerticalFaceSpeed());

		// ターゲットに近づく
		this.pathNavigater.tryMoveToXYZ(bx, by, bz, this.moveSpeed);
	    }
	}
    }
}
