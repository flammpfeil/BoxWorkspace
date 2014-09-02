package com.schr0.schr0box.livingutility.core;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.schr0.schr0box.livingutility.LivingUtility;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Normal;
import com.schr0.schr0box.livingutility.entity.chest.gui.GuiLivingChest_Normal;
import com.schr0.schr0box.livingutility.entity.chest.inventory.container.ContainerLivingChest_Normal;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    // サーバー側でGUIが開かれたときに呼ばれる
    // Containerを返す.
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {

	if (id == LivingUtility.CHEST_GUI_ID)
	{
		return new ContainerLivingChest_Normal(player.inventory, (EntityLivingChest_Normal)world.getEntityByID(x));
	}

	return null;
    }

    // クライアント側で開かれたときに呼ばれる
    // Guiを返す
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
	if (id == LivingUtility.CHEST_GUI_ID)
	{
		return new GuiLivingChest_Normal(player.inventory, (EntityLivingChest_Normal)world.getEntityByID(x));
	}

	return null;
    }
}
