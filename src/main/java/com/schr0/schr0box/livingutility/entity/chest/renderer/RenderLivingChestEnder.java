package com.schr0.schr0box.livingutility.entity.chest.renderer;

import net.minecraft.util.ResourceLocation;

import com.schr0.schr0box.livingutility.LivingUtility;

/**
 * Created by A.K. on 14/08/22.
 */
public class RenderLivingChestEnder extends RenderLivingChest_Base
{
    private final static ResourceLocation ENDER_CHEST_TEXTURE = new ResourceLocation(LivingUtility.TEXTURE_DOMAIN + "textures/mobs/chest/ender/core.png");
    private final static ResourceLocation ENDER_CHEST_CLOR_TEXTURE = new ResourceLocation(LivingUtility.TEXTURE_DOMAIN + "textures/mobs/chest/ender/color.png");

    @Override
    public ResourceLocation getBodyResourceLocation()
    {
	return this.ENDER_CHEST_TEXTURE;
    }

    @Override
    public ResourceLocation getColorResourceLocation()
    {
	return this.ENDER_CHEST_CLOR_TEXTURE;
    }
}
