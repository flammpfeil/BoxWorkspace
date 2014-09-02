package com.schr0.schr0box.livingutility.entity.chest.renderer;

import net.minecraft.util.ResourceLocation;

import com.schr0.schr0box.livingutility.LivingUtility;

public class RenderLivingChest extends RenderLivingChest_Base
{
    private final static ResourceLocation CHEST_TEXTURE = new ResourceLocation(LivingUtility.TEXTURE_DOMAIN + "textures/mobs/chest/nomal/core.png");
    private final static ResourceLocation CHEST_COLOR_TEXTURE = new ResourceLocation(LivingUtility.TEXTURE_DOMAIN + "textures/mobs/chest/nomal/color.png");

    @Override
    public ResourceLocation getBodyResourceLocation()
    {
	return this.CHEST_TEXTURE;
    }

    @Override
    public ResourceLocation getColorResourceLocation()
    {
	return this.CHEST_COLOR_TEXTURE;
    }
}
