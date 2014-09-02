package com.schr0.schr0box.livingutility.entity.base.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import com.schr0.schr0box.livingutility.entity.base.EntityLivingUtility;

public class EntityLivingUtilityAITrade extends AIBaseEntityLivingUtility
{
    private Entity closestEntity;

    public EntityLivingUtilityAITrade(EntityLivingUtility baseutility)
    {
	super(baseutility);
	this.setMutexBits(3);
    }

    // AIが始まる判定
    @Override
    public boolean shouldExecute()
    {
	// ターゲットの初期化
	this.closestEntity = null;

	// ターゲットの登録
	this.closestEntity = this.baseUtility.getTrader();

	// ターゲットが登録してある & 取引中の場合
	if (this.closestEntity != null && this.baseUtility.isTrading())
	{
	    EntityPlayer player = this.baseUtility.getTrader();
	    return player != null && player.openContainer instanceof Container;
	}

	return false;
    }

    // AIが継続する際の判定
    @Override
    public boolean continueExecuting()
    {
	// ターゲットが登録されていない場合
	if (this.closestEntity == null)
	{
	    return false;
	}

	// ターゲットが存在していない or 見えていない場合
	if (this.closestEntity.isDead || !this.baseUtility.getEntitySenses().canSee(this.closestEntity))
	{
	    return false;
	}

	// tickCounterが100以上になる場合
	if (100 < this.getTickCounter())
	{
	    // ----------------- ログ ----------------- //
	    System.out.println(this.getClass().getName() + " !! ORVER !! ");

	    // お座りの処理
	    this.baseUtility.setSitting(true);

	    return false;
	}

	return super.continueExecuting();
    }

    // AIが終了する際に呼ばれる処理
    @Override
    public void resetTask()
    {
	super.resetTask();

	// ターゲットの初期化
	this.closestEntity = null;

	// Traderの初期化
	this.baseUtility.setTrader((EntityPlayer) null);
    }

    // AIの処理
    @Override
    public void updateTask()
    {
	super.updateTask();

	// pathNavigaterが存在しない場合
	if (this.pathNavigater.noPath())
	{
	    // ターゲットを注視
	    this.baseUtility.getLookHelper().setLookPositionWithEntity(this.closestEntity, 10.0F, this.baseUtility.getVerticalFaceSpeed());
	}
    }

}
