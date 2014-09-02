package com.schr0.schr0box.livingutility.entity.chest;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.world.World;

import com.schr0.schr0box.livingutility.entity.base.ai.EntityLivingUtilityAISit;
import com.schr0.schr0box.livingutility.entity.base.ai.EntityLivingUtilityAITrade;
import com.schr0.schr0box.livingutility.entity.chest.ai.EntityLivingChestAIGoBackHome;
import com.schr0.schr0box.livingutility.entity.chest.ai.ender.EntityLivingChestAICollectItem_Ender;
import com.schr0.schr0box.livingutility.entity.chest.ai.ender.EntityLivingChestAITransportInv_Ender;

/**
 * Created by A.K. on 14/08/22.
 */
public class EntityLivingChest_Ender extends EntityLivingChest
{
    public EntityLivingChest_Ender(World par1World)
    {
	super(par1World);

	// Entityのサイズ（Y軸, X軸）
	this.setSize(0.9F, 1.35F);

	// 水を避けるかどうかの判定
	this.getNavigator().setAvoidsWater(true);

	// 炎耐性を持つ
	this.isImmuneToFire = true;

	// -----AIの設定----- //
	float movespeed = 1.25F;

	// 1 水泳 [4]
	// 2 お座り（独自）[5]
	// 3 取引中（独自） [3]
	this.tasks.addTask(1, new EntityAISwimming(this));
	this.tasks.addTask(2, new EntityLivingUtilityAISit(this));
	this.tasks.addTask(3, new EntityLivingUtilityAITrade(this));

	// 5 ホームポイントへの帰還（独自） [3]
	// 5 Enderのインベントリへの輸送（独自） [3]
	// 5 Enderのアイテム回収（独自） [3]
	this.tasks.addTask(5, new EntityLivingChestAIGoBackHome(this, movespeed, 8, 2));
	this.tasks.addTask(5, new EntityLivingChestAITransportInv_Ender(this, movespeed, 2));
	this.tasks.addTask(5, new EntityLivingChestAICollectItem_Ender(this, movespeed, 8.0D, 1.0D, 1.0F));

	// 6 自由行動 [3]
	// 7 他Entityへの注視 [2]
	// 8 アイドル状態の注視 [3]
	this.tasks.addTask(6, new EntityAIWander(this, movespeed));
	this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityLiving.class, 6.0F));
	this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    // 属性の管理
    @Override
    protected void applyEntityAttributes()
    {
	super.applyEntityAttributes();

	// 最大体力（30.0）
	this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);

	// 移動速度（0.15D）
	this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.15D);
    }

    // AIの適用
    @Override
    public boolean isAIEnabled()
    {
	return true;
    }

    // ------------------------- ↓独自の実装↓ -------------------------//

}
