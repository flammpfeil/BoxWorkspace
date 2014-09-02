package com.schr0.schr0box.livingutility.entity.chest.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest;

public class ModelLivingChest extends ModelBase
{
    public ModelRenderer mainBody;
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public ModelRenderer rightArm;
    public ModelRenderer leftArm;
    public ModelRenderer core;
    public ModelRenderer cover;

    public ModelLivingChest()
    {
	this.textureWidth = 64;
	this.textureHeight = 64;
	this.setTextureOffset("Cover.cover1", 0, 0);
	this.setTextureOffset("Cover.cover2", 0, 0);

	// --------身体--------//
	this.mainBody = new ModelRenderer(this, 0, 19);
	this.mainBody.addBox(-7F, -4F, -7F, 14, 10, 14);
	this.mainBody.setRotationPoint(0F, 9F, 0F);
	this.mainBody.setTextureSize(64, 64);
	this.mainBody.mirror = true;
	this.setRotation(mainBody, 0F, 0F, 0F);

	// --------コア--------//
	this.core = new ModelRenderer(this, 36, 44);
	this.core.addBox(-2F, -2F, -1F, 4, 4, 1);
	this.core.setTextureSize(64, 64);
	this.core.mirror = true;

	// this.Body.setRotationPoint( 0F, 9F, 0F ); Core.setRotationPoint( 0F, 11F, -7F );
	this.core.setRotationPoint(0F, 2F, -7F);

	// this.setRotation(Body, 0F, 0F, 0F); setRotation(Core, 0F, 0F,-0.7853982F);
	this.setRotation(core, 0F, 0F, -0.7853982F);

	this.mainBody.addChild(core);

	// --------蓋--------//
	this.cover = new ModelRenderer(this, "Cover");
	this.cover.addBox("cover1", -7F, -5F, -14F, 14, 5, 14);
	this.cover.addBox("cover2", -1F, -2F, -15F, 2, 4, 1);
	this.cover.mirror = true;

	// this.Body.setRotationPoint(0F, 9F, 0F); Cover.setRotationPoint(0F, 5F, * 7F);
	this.cover.setRotationPoint(0F, -4F, 7F);

	// this.setRotation( Body, 0F, 0F, 0F ); setRotation( Cover, 0F, 0F, 0F );
	this.setRotation(cover, 0F, 0F, 0F);

	this.mainBody.addChild(cover);

	// --------右腕--------//
	this.rightArm = new ModelRenderer(this, 18, 44);
	this.rightArm.addBox(-1F, 0F, -1F, 2, 9, 2);
	this.rightArm.setTextureSize(64, 64);
	this.rightArm.mirror = true;
	this.rightArm.setRotationPoint(-7F, 0F, 0F);
	this.setRotation(rightArm, 0F, 0F, 0F);

	// --------左腕--------//
	this.leftArm = new ModelRenderer(this, 27, 44);
	this.leftArm.addBox(-1F, 0F, -1F, 2, 9, 2);
	this.leftArm.setRotationPoint(7F, 9F, 0F);
	this.leftArm.setTextureSize(64, 64);
	this.leftArm.mirror = true;
	this.setRotation(leftArm, 0F, 0F, -0.3665191F);

	// --------右足--------//
	this.rightLeg = new ModelRenderer(this, 0, 44);
	this.rightLeg.addBox(-1F, 0F, -1F, 2, 9, 2);
	this.rightLeg.setRotationPoint(-3F, 15F, 0F);
	this.rightLeg.setTextureSize(64, 64);
	this.rightLeg.mirror = true;
	this.setRotation(rightLeg, 0F, 0F, 0F);

	// --------左足--------//
	this.leftLeg = new ModelRenderer(this, 9, 44);
	this.leftLeg.addBox(-1F, 0F, -1F, 2, 9, 2);
	this.leftLeg.setRotationPoint(3F, 15F, 0F);
	this.leftLeg.setTextureSize(64, 64);
	this.leftLeg.mirror = true;
	this.setRotation(leftLeg, 0F, 0F, 0F);
    }

    // ???
    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
	model.rotateAngleX = x;
	model.rotateAngleY = y;
	model.rotateAngleZ = z;
    }

    // 描画の処理
    @Override
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
	super.render(par1Entity, par2, par3, par4, par5, par6, par7);

	// モーション
	this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);

	if (this.isChild)
	{
	    float scale = 2.0F;
	    GL11.glPushMatrix();
	    GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
	    GL11.glTranslatef(0.0F, 24.0F * par7, 0.0F);
	    this.mainBody.render(par7);
	    this.rightArm.render(par7);
	    this.leftArm.render(par7);
	    this.rightLeg.render(par7);
	    this.leftLeg.render(par7);
	    GL11.glPopMatrix();
	}
	else
	{
	    this.mainBody.render(par7);
	    this.rightArm.render(par7);
	    this.leftArm.render(par7);
	    this.rightLeg.render(par7);
	    this.leftLeg.render(par7);
	}
    }

    // モーションの処理
    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
	EntityLivingChest basechest = (EntityLivingChest) par7Entity;

	// お座りしている or 乗っている場合
	if (basechest.isSitting() || basechest.isRiding())
	{
	    // RotationPointを変更
	    this.mainBody.setRotationPoint(0F, 17F, 0F);
	    this.leftArm.setRotationPoint(7F, 17F, 0F);
	    this.rightArm.setRotationPoint(-7F, 17F, 0F);
	    this.rightLeg.setRotationPoint(-3F, 23F, 0F);
	    this.leftLeg.setRotationPoint(3F, 23F, 0F);

	    // 待機状態
	    this.rightLeg.rotateAngleX = -1.570796F;
	    this.leftLeg.rotateAngleX = -1.570796F;
	    this.rightArm.rotateAngleX = -0.9424778F;
	    this.leftArm.rotateAngleX = -0.9424778F;
	}
	// 通常時
	else
	{
	    // RotationPointを変更
	    this.mainBody.setRotationPoint(0F, 9F, 0F);
	    this.rightArm.setRotationPoint(-7F, 0F, 0F);
	    this.leftArm.setRotationPoint(7F, 9F, 0F);
	    this.rightArm.setRotationPoint(-7F, 9F, 0F);
	    this.rightLeg.setRotationPoint(-3F, 15F, 0F);
	    this.leftLeg.setRotationPoint(3F, 15F, 0F);

	    // 歩行動作
	    this.rightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
	    this.leftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
	    this.rightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
	    this.leftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
	}

	// 全身の向き
	this.mainBody.rotateAngleY = par4 / (180F / (float) Math.PI);
	this.mainBody.rotateAngleY = this.rightArm.rotateAngleY = this.leftArm.rotateAngleY = this.rightLeg.rotateAngleY = this.leftLeg.rotateAngleY;

	// 乗っていない場合
	if (!basechest.isRiding())
	{
	    this.mainBody.rotateAngleX = par5 / (180F / (float) Math.PI);
	}

	// コアの回転
	this.core.rotateAngleZ = par3 * 0.2F;

	// 腕の揺らめき
	this.rightArm.rotateAngleZ = (MathHelper.sin(par3 * 0.05F) * 0.05F) + 0.3665191F;
	this.leftArm.rotateAngleZ = -(MathHelper.sin(par3 * 0.05F) * 0.05F) - 0.3665191F;
    }

    // アニメーションの処理
    @Override
    public void setLivingAnimations(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4)
    {
	EntityLivingChest basechest = (EntityLivingChest) par1EntityLivingBase;

	// 蓋を開く
	float coverAngle = basechest.modelMotion.getCoverAngle(par4);
	this.cover.rotateAngleX = -coverAngle;
    }
}
