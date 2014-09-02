package com.schr0.schr0box.livingutility.entity.chest.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.world.World;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest;
import com.schr0.schr0box.livingutility.entity.chest.HomePoint;

public abstract class AIBaseEntityLivingChest extends EntityAIBase
{
    public EntityLivingChest baseChest;
    public HomePoint homePoint;
    public PathNavigate pathNavigater;
    public World theWorld;

    private int tickCounter;

    public AIBaseEntityLivingChest(EntityLivingChest basechest)
    {
	this.baseChest = basechest;
	this.homePoint = new HomePoint(basechest);
	this.pathNavigater = basechest.getNavigator();
	this.theWorld = baseChest.worldObj;
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
