package com.schr0.schr0box.livingutility.entity.base.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.world.World;

import com.schr0.schr0box.livingutility.entity.base.EntityLivingUtility;
import com.schr0.schr0box.livingutility.entity.chest.HomePoint;

public abstract class AIBaseEntityLivingUtility extends EntityAIBase
{
    public EntityLivingUtility baseUtility;
    public HomePoint homePoint;
    public PathNavigate pathNavigater;
    public World theWorld;

    private int tickCounter;

    public AIBaseEntityLivingUtility(EntityLivingUtility baseutility)
    {
	this.baseUtility = baseutility;
	this.pathNavigater = baseutility.getNavigator();
	this.theWorld = baseutility.worldObj;
    }

    // AIが始まる判定
    @Override
    public boolean shouldExecute()
    {
	return false;
    }

    // AIが始まった際に呼ばれる処理
    @Override
    public void startExecuting()
    {
	this.tickCounter = 0;
	this.pathNavigater.clearPathEntity();
    }

    // AIが終了する際に呼ばれる処理
    @Override
    public void resetTask()
    {
	this.tickCounter = 0;
	this.pathNavigater.clearPathEntity();
    }

    // AIの処理
    @Override
    public void updateTask()
    {
	// tickCounterの加算（ ++ ）
	this.tickCounter++;
    }

    // ------------------------- ↓独自の実装↓ -------------------------//

    // tickCounterのget
    public int getTickCounter()
    {
	return this.tickCounter;
    }

}
