package com.schr0.schr0box.livingutility.entity.base.ai;

import net.minecraft.entity.Entity;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest;

public class EntityLivingUtilityAISit extends AIBaseEntityLivingUtility
{
    private Entity closestEntity;

    public EntityLivingUtilityAISit(EntityLivingChest basechest)
    {
	super(basechest);
	this.setMutexBits(3);
    }

    // AIが始まる判定
    @Override
    public boolean shouldExecute()
    {
	// 飼い慣らし状態 or 水中でない or 地上に居る場合
	if (this.baseUtility.isTamed() || !this.baseUtility.isInWater() || this.baseUtility.onGround)
	{
	    // 座っている場合
	    return this.baseUtility.isSitting();
	}

	return false;
    }

    // AIが始まった際に呼ばれる処理
    @Override
    public void startExecuting()
    {
	super.startExecuting();

	// お座りの処理
	this.baseUtility.setSitting(true);
    }

    // AIが終了する際に呼ばれる処理
    @Override
    public void resetTask()
    {
	super.resetTask();

	// お座りの処理
	this.baseUtility.setSitting(false);
    }

}
