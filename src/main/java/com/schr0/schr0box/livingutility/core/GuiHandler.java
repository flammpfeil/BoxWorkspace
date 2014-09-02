package com.schr0.schr0box.livingutility.core;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import com.schr0.schr0box.livingutility.LivingUtility;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Ender;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Normal;
import com.schr0.schr0box.livingutility.entity.chest.gui.GuiLivingChest_Ender;
import com.schr0.schr0box.livingutility.entity.chest.gui.GuiLivingChest_Normal;
import com.schr0.schr0box.livingutility.entity.chest.inventory.container.ContainerLivingChest_Ender;
import com.schr0.schr0box.livingutility.entity.chest.inventory.container.ContainerLivingChest_Normal;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    // サーバー側でGUIが開かれたときに呼ばれる
    // Containerを返す.
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
	List list = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1));

	if (id == LivingUtility.CHEST_GUI_ID)
	{
	    // Normal
	    if (list.get(0) instanceof EntityLivingChest_Normal)
	    {
		return new ContainerLivingChest_Normal(player.inventory, (EntityLivingChest_Normal) list.get(0));
	    }

	    // Ender
	    if (list.get(0) instanceof EntityLivingChest_Ender)
	    {
		return new ContainerLivingChest_Ender(player.inventory, (EntityLivingChest_Ender) list.get(0));
	    }
	}

	return null;
    }

    // クライアント側で開かれたときに呼ばれる
    // Guiを返す
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
	List list = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1));

	if (id == LivingUtility.CHEST_GUI_ID)
	{
	    // Normal
	    if (list.get(0) instanceof EntityLivingChest_Normal)
	    {
		return new GuiLivingChest_Normal(player.inventory, (EntityLivingChest_Normal) list.get(0));
	    }

	    // Ender
	    if (list.get(0) instanceof EntityLivingChest_Ender)
	    {
		return new GuiLivingChest_Ender(player.inventory, (EntityLivingChest_Ender) list.get(0));
	    }
	}

	return null;
    }
}
