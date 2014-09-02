package com.schr0.schr0box.livingutility.entity.chest.renderer;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.*;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.*;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest;
import com.schr0.schr0box.livingutility.entity.chest.model.ModelLivingChest;

public abstract class RenderLivingChest_Base extends RenderLiving
{
    protected ModelLivingChest modelLivingChestMain;

    public RenderLivingChest_Base()
    {
	super(new ModelLivingChest(), 0.5F);
	this.modelLivingChestMain = (ModelLivingChest) (this.mainModel);
	this.setRenderPassModel(new ModelLivingChest());
    }

    // EntityとResourceLocationを関連付け
    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
	// 胴体部分
	return this.getBodyResourceLocation();
    }

    // doRender（Entity）
    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
	super.doRender((EntityLivingChest) par1Entity, par2, par4, par6, par8, par9);
    }

    // doRender（EntityLivingBase）
    @Override
    public void doRender(EntityLivingBase par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
	this.doRender((EntityLivingChest) par1Entity, par2, par4, par6, par8, par9);
    }

    // doRender（EntityLivingChest_Base）
    private void doRender(EntityLivingChest basechest, double par2, double par4, double par6, float par8, float par9)
    {
	super.doRender(basechest, par2, par4, par6, par8, par9);
    }

    // レンダーパス（EntityLivingBase）
    @Override
    protected int shouldRenderPass(EntityLivingBase par1Entity, int par2, float par3)
    {
	return this.shouldRenderPass((EntityLivingChest) par1Entity, par2, par3);
    }

    // レンダーパス（EntityLivingChest_Base）
    private int shouldRenderPass(EntityLivingChest basechest, int par2, float par3)
    {
	if (par2 == 0)
	{
	    this.bindTexture(this.getColorResourceLocation());

	    int j = basechest.getColor();
	    GL11.glColor3f(EntitySheep.fleeceColorTable[j][0], EntitySheep.fleeceColorTable[j][1], EntitySheep.fleeceColorTable[j][2]);

	    return 1;
	}
	else
	{
	    return -1;
	}
    }

    // 手に持ったアイテムの描画（EntityLivingBase）
    @Override
    protected void renderEquippedItems(EntityLivingBase par1Entity, float par2)
    {
	this.renderEquippedItems((EntityLivingChest) par1Entity, par2);
    }

    // ------------------------- ↓独自の実装↓ -------------------------//

    // 胴体ResourceLocationのget
    public abstract ResourceLocation getBodyResourceLocation();

    // 染色ResourceLocationのget
    public abstract ResourceLocation getColorResourceLocation();

    // 独自の手に持ったアイテムの描画（ModelBiped）
    private void renderEquippedItems(EntityLiving par1Entity, float par2)
    {
	GL11.glColor3f(1.0F, 1.0F, 1.0F);
	super.renderEquippedItems(par1Entity, par2);
	ItemStack itemstack = par1Entity.getHeldItem();
	ItemStack itemstack1 = par1Entity.func_130225_q(3);
	Item item;
	float f1;

	if (itemstack1 != null)
	{
	    GL11.glPushMatrix();
	    // this.modelBipedMain.bipedHead.postRender( 0.0625F );
	    item = itemstack1.getItem();

	    IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack1, EQUIPPED);
	    boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, itemstack1, BLOCK_3D));

	    if (item instanceof ItemBlock)
	    {
		if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType()))
		{
		    f1 = 0.625F;
		    GL11.glTranslatef(0.0F, -0.25F, 0.0F);
		    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		    GL11.glScalef(f1, -f1, -f1);
		}

		this.renderManager.itemRenderer.renderItem(par1Entity, itemstack1, 0);
	    }
	    else if (item == Items.skull)
	    {
		f1 = 1.0625F;
		GL11.glScalef(f1, -f1, -f1);
		GameProfile gameprofile = null;

		if (itemstack1.hasTagCompound())
		{
		    NBTTagCompound nbttagcompound = itemstack1.getTagCompound();

		    if (nbttagcompound.hasKey("SkullOwner", 10))
		    {
			gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
		    }
		    else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkullOwner")))
		    {
			gameprofile = new GameProfile((UUID) null, nbttagcompound.getString("SkullOwner"));
		    }
		}

		TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, itemstack1.getItemDamage(), gameprofile);
	    }

	    GL11.glPopMatrix();
	}

	if (itemstack != null && itemstack.getItem() != null)
	{
	    item = itemstack.getItem();
	    GL11.glPushMatrix();

	    if (this.mainModel.isChild)
	    {
		f1 = 0.5F;
		GL11.glTranslatef(0.0F, 0.625F, 0.0F);
		GL11.glRotatef(-20.0F, -1.0F, 0.0F, 0.0F);
		GL11.glScalef(f1, f1, f1);
	    }

	    this.modelLivingChestMain.rightArm.postRender(0.0625F);
	    GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

	    IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, EQUIPPED);
	    boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, itemstack, BLOCK_3D));

	    if (item instanceof ItemBlock && (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType())))
	    {
		f1 = 0.5F;
		GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
		f1 *= 0.75F;
		GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(-f1, -f1, f1);
	    }
	    else if (item == Items.bow)
	    {
		f1 = 0.625F;
		GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
		GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(f1, -f1, f1);
		GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
	    }
	    else if (item.isFull3D())
	    {
		f1 = 0.625F;

		if (item.shouldRotateAroundWhenRendering())
		{
		    GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		    GL11.glTranslatef(0.0F, -0.125F, 0.0F);
		}

		this.func_82422_c();
		GL11.glScalef(f1, -f1, f1);
		GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
	    }
	    else
	    {
		f1 = 0.375F;
		GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
		GL11.glScalef(f1, f1, f1);
		GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
	    }

	    float f2;
	    float f3;
	    int i;

	    if (itemstack.getItem().requiresMultipleRenderPasses())
	    {
		for (i = 0; i < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++i)
		{
		    int j = itemstack.getItem().getColorFromItemStack(itemstack, i);
		    f2 = (j >> 16 & 255) / 255.0F;
		    f3 = (j >> 8 & 255) / 255.0F;
		    float f4 = (j & 255) / 255.0F;
		    GL11.glColor4f(f2, f3, f4, 1.0F);
		    this.renderManager.itemRenderer.renderItem(par1Entity, itemstack, i);
		}
	    }
	    else
	    {
		i = itemstack.getItem().getColorFromItemStack(itemstack, 0);
		float f5 = (i >> 16 & 255) / 255.0F;
		f2 = (i >> 8 & 255) / 255.0F;
		f3 = (i & 255) / 255.0F;
		GL11.glColor4f(f5, f2, f3, 1.0F);
		this.renderManager.itemRenderer.renderItem(par1Entity, itemstack, 0);
	    }

	    GL11.glPopMatrix();
	}
    }

    // 手に持ったアイテムの角度？（ModelBiped）
    protected void func_82422_c()
    {
	GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
    }
}
