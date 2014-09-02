package com.schr0.schr0box.livingutility.proxy;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Ender;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Normal;
import com.schr0.schr0box.livingutility.entity.chest.renderer.RenderLivingChest;
import com.schr0.schr0box.livingutility.entity.chest.renderer.RenderLivingChestEnder;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy
{
    @Override
    public void registerClient()
    {
	// EntityとRenderを結びつける
	// RenderingRegistry.registerEntityRenderingHandler( class(Entity), Render);
	RenderingRegistry.registerEntityRenderingHandler(EntityLivingChest_Normal.class, new RenderLivingChest());
	RenderingRegistry.registerEntityRenderingHandler(EntityLivingChest_Ender.class, new RenderLivingChestEnder());
    }
}
