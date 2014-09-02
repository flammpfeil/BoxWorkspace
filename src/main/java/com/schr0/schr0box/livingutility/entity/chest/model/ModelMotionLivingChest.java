package com.schr0.schr0box.livingutility.entity.chest.model;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest;

public class ModelMotionLivingChest
{
    // 蓋の開閉の変数
    private float prev;
    private float lid;

    // 蓋の開閉角度の変数
    private float prevLidAngle;
    private float lidAngle;

    // 蓋の開閉角度の取得
    public float getCoverAngle(float par1)
    {
	return (this.prevLidAngle + (this.lidAngle - this.prevLidAngle) * par1) * 0.5F * (float) Math.PI;
    }

    // 蓋の開閉処理
    public void setCoverMotion(EntityLivingChest thechest, boolean isOpen)
    {
	// 蓋の角度・音声処理
	this.coverMotion(thechest);

	// 開閉速度 (0.1F)
	this.prev = this.lid;
	float f = 0.4F;

	if (isOpen && this.lid == 0.0F)
	{
	    // 開く
	    thechest.setOpen(true);
	    this.lid++;
	}

	if (!isOpen && this.lid > 0.0F || isOpen && this.lid < 1.0F)
	{
	    float f1 = this.lid;

	    if (isOpen)
	    {
		this.lid += f;
	    }
	    else
	    {
		this.lid -= f;
	    }

	    if (this.lid > 1.0F)
	    {
		this.lid = 1.0F;
	    }

	    float f2 = 0.5F;

	    if (this.lid < f2 && f1 >= f2)
	    {
		// 閉じる
		thechest.setOpen(false);
	    }

	    if (this.lid < 0.0F)
	    {
		this.lid = 0.0F;
	    }
	}
    }

    // 蓋の角度・音声処理
    private void coverMotion(EntityLivingChest thechest)
    {
	// 開閉速度 (0.1F)
	this.prevLidAngle = this.lidAngle;
	float f = 0.2F;

	if (thechest.isOpen() && this.lidAngle == 0.0F)
	{
	    // 音を出す
	    thechest.playSE("random.chestopen", 0.5F, thechest.worldObj.rand.nextFloat() * 0.1F + 0.9F);
	}

	if (!thechest.isOpen() && this.lidAngle > 0.0F || thechest.isOpen() && this.lidAngle < 1.0F)
	{
	    float f1 = this.lidAngle;

	    if (thechest.isOpen())
	    {
		this.lidAngle += f;
	    }
	    else
	    {
		this.lidAngle -= f;
	    }

	    if (this.lidAngle > 1.0F)
	    {
		this.lidAngle = 1.0F;
	    }

	    float f2 = 0.5F;

	    if (this.lidAngle < f2 && f1 >= f2)
	    {
		// 音を出す
		thechest.playSE("random.chestclosed", 0.5F, thechest.worldObj.rand.nextFloat() * 0.1F + 0.9F);
	    }

	    if (this.lidAngle < 0.0F)
	    {
		this.lidAngle = 0.0F;
	    }
	}
    }

}
