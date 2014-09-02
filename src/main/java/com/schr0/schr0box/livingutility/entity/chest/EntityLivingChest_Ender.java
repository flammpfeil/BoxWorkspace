package com.schr0.schr0box.livingutility.entity.chest;

import com.schr0.schr0box.livingutility.entity.chest.inventory.InventoryLivingEnderChest;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;


/**
 * Created by A.K. on 14/08/22.
 */
public class EntityLivingChest_Ender extends EntityLivingChest_Normal
{
    public EntityLivingChest_Ender(World par1World)
    {
	super(par1World);

        this.inventory = new InventoryLivingEnderChest(this);

	// 炎耐性を持つ
	this.isImmuneToFire = true;

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

}
