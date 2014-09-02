package com.schr0.schr0box.livingutility.entity.chest;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;

public class HomePoint
{
    private EntityLivingChest baseChest;

    public HomePoint(EntityLivingChest basechest)
    {
	this.baseChest = basechest;
    }

    // 地点登録のItemStack
    public ItemStack getHomePointItem()
    {
	return this.baseChest.specialItems.getStackInSlot(0);
    }

    // 登録インベントリの取得
    public IInventory getHomeInventory()
    {
	if (this.getHomePointItem() != null)
	{
	    return TileEntityHopper.func_145893_b(this.baseChest.worldObj, (double) this.getHomePosX(), (double) this.getHomePosY(), (double) this.getHomePosZ());
	}

	return null;
    }

    // 登録地点の登録名
    public String getHomeName()
    {
	if (this.getHomePointItem() != null)
	{
	    return this.getHomePointItem().getTagCompound().getString("TargetName");
	}

	return "null";
    }

    // 登録地点のX座標
    public int getHomePosX()
    {
	if (this.getHomePointItem() != null)
	{
	    return this.getHomePointItem().getTagCompound().getInteger("TargetPosX");
	}

	return -1;
    }

    // 登録地点のY座標
    public int getHomePosY()
    {
	if (this.getHomePointItem() != null)
	{

	    return this.getHomePointItem().getTagCompound().getInteger("TargetPosY");
	}

	return -1;
    }

    // 登録地点のZ座標
    public int getHomePosZ()
    {
	if (this.getHomePointItem() != null)
	{

	    return this.getHomePointItem().getTagCompound().getInteger("TargetPosZ");
	}

	return -1;
    }

    // 登録地点の面
    public int getHomeSide()
    {
	if (this.getHomePointItem() != null)
	{
	    return this.getHomePointItem().getTagCompound().getInteger("TargetSide");
	}

	return -1;
    }

    // 周囲の登録インベントリ走査
    public IInventory getNearHomeInventory(double hX, double hY, double hZ, int tX, int tY, int tZ)
    {
	for (int cX = (int) (hX - tX); cX < (hX + tX); ++cX)
	{
	    for (int cY = (int) (hY - tY); cY < (hY + tY); ++cY)
	    {
		for (int cZ = (int) (hZ - tZ); cZ < (hZ + tZ); ++cZ)
		{
		    if (cX == this.getHomePosX() && cY == this.getHomePosY() && cZ == this.getHomePosZ())
		    {
			IInventory inventory = TileEntityHopper.func_145893_b(this.baseChest.worldObj, (double) cX, (double) cY, (double) cZ);

			if (inventory != null)
			{
			    return inventory;
			}
		    }
		}
	    }
	}

	return null;
    }

}
