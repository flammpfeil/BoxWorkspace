package com.schr0.schr0box.livingutility.entity.chest;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.schr0.schr0box.livingutility.LivingUtility;
import com.schr0.schr0box.livingutility.entity.base.EntityLivingUtility;
import com.schr0.schr0box.livingutility.entity.chest.inventory.EquipmentLivingChest;
import com.schr0.schr0box.livingutility.entity.chest.inventory.InventoryLivingChest;
import com.schr0.schr0box.livingutility.entity.chest.inventory.SpecialItemsLivingChest;
import com.schr0.schr0box.livingutility.entity.chest.model.ModelMotionLivingChest;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class EntityLivingChest extends EntityLivingUtility
{
    // ModelMotionLivingChestの宣言
    public ModelMotionLivingChest modelMotion = new ModelMotionLivingChest();

    // InventoryLivingChestの宣言
    public InventoryLivingChest inventory = new InventoryLivingChest(this);

    // EquipmentLivingChestの宣言
    public EquipmentLivingChest equipment = new EquipmentLivingChest(this);

    // SpecialItemsLivingChestの宣言
    public SpecialItemsLivingChest specialItems = new SpecialItemsLivingChest(this);

    // DataWatcherのID
    // OPEN_VALUE(25) : 蓋開閉
    // COLOR_VALUE(26) : 染色
    private final static int OPEN_VALUE = 25;
    private final static int COLOR_VALUE = 26;

    public EntityLivingChest(World par1World)
    {
	super(par1World);
    }

    // DataWatcherの処理
    @Override
    protected void entityInit()
    {
	super.entityInit();
	this.getDataWatcher().addObject(OPEN_VALUE, Byte.valueOf((byte) 0));
	this.getDataWatcher().addObject(COLOR_VALUE, new Byte((byte) 0));
    }

    // OPEN_VALUE : 蓋開閉の判定
    public boolean isOpen()
    {
	return (this.getDataWatcher().getWatchableObjectByte(OPEN_VALUE) & 4) != 0;
    }

    // OPEN_VALUE : 蓋開閉のset
    public void setOpen(boolean par1)
    {
	byte b0 = this.getDataWatcher().getWatchableObjectByte(OPEN_VALUE);

	if (par1)
	{
	    this.getDataWatcher().updateObject(OPEN_VALUE, Byte.valueOf((byte) (b0 | 4)));
	}
	else
	{
	    this.getDataWatcher().updateObject(OPEN_VALUE, Byte.valueOf((byte) (b0 & -5)));
	}
    }

    // COLOR_VALUE : 染色
    public int getColor()
    {
	return this.getDataWatcher().getWatchableObjectByte(COLOR_VALUE) & 15;
    }

    // COLOR_VALUE : 染色のset
    public void setColor(int dye)
    {
	byte b0 = this.dataWatcher.getWatchableObjectByte(COLOR_VALUE);
	this.getDataWatcher().updateObject(COLOR_VALUE, Byte.valueOf((byte) (b0 & 240 | dye & 15)));
    }

    // NBTの書き込み
    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
	super.writeEntityToNBT(par1NBTTagCompound);

	// 蓋開閉の判定
	par1NBTTagCompound.setBoolean("Open", this.isOpen());

	// 染色
	par1NBTTagCompound.setByte("Color", (byte) this.getColor());

	// インベントリの保存
	this.inventory.save();
	this.equipment.save();
	this.specialItems.save();
    }

    // NBTの読み込み
    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
	super.readEntityFromNBT(par1NBTTagCompound);

	// 蓋開閉のset
	this.setOpen(par1NBTTagCompound.getBoolean("Open"));

	// 染色のset
	this.setColor(par1NBTTagCompound.getByte("Color"));

	// インベントリの読み込み
	this.inventory.load();
	this.equipment.load();
	this.specialItems.load();
    }

    // （自らが）乗っている場合のY座標
    @Override
    public double getYOffset()
    {
	if (this.ridingEntity != null)
	{
	    if (this.ridingEntity instanceof EntityPlayer)
	    {
		return this.yOffset - 1.2F;
	    }
	    else
	    {
		return this.yOffset + 0.15F;
	    }
	}

	return super.getYOffset();
    }

    // 被ダメージ時の音声
    @Override
    protected String getHurtSound()
    {
	return "dig.wood";
    }

    // 死亡時の音声
    @Override
    protected String getDeathSound()
    {
	return "random.break";
    }

    // 足音
    @Override
    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
    {
	this.playSound("step.wood", 0.25F, 1.0F);
    }

    // インタラクト
    @Override
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
	ItemStack currentItem = par1EntityPlayer.inventory.getCurrentItem();

	// 飼い慣らし状態 ＆ 飼い主である場合
	if (this.isTamed() && this.func_152114_e(par1EntityPlayer))
	{
	    // 染料である場合
	    if (currentItem != null && currentItem.getItem() == Items.dye)
	    {
		int color = BlockColored.func_150032_b(currentItem.getItemDamage());

		if (this.getColor() != color)
		{
		    // 染色のset
		    this.setColor(color);

		    --currentItem.stackSize;

		    // 音を出す
		    this.playSE("random.pop", 1.0F, 1.0F);
		}

		// Itemを振る動作
		par1EntityPlayer.swingItem();
		return true;
	    }

	    // スニーキング状態の場合
	    if (par1EntityPlayer.isSneaking())
	    {
		// お座りの処理
		if (!this.worldObj.isRemote)
		{
		    this.setSitting(!this.isSitting());
		}

		// Itemを振る動作
		par1EntityPlayer.swingItem();
		return true;
	    }
	    // スニーキング状態でない場合
	    else
	    {
		// クライアントだけの処理
		if (!this.worldObj.isRemote)
		{
		    // Traderのset（独自）
		    this.setTrader(par1EntityPlayer);

		    // 独自GUIを表示
		    par1EntityPlayer.openGui(LivingUtility.instance, LivingUtility.CHEST_GUI_ID, this.worldObj, this.getEntityId(),0,0);
		}

		// Itemを振る動作
		par1EntityPlayer.swingItem();
		return true;
	    }
	}

	return super.interact(par1EntityPlayer);
    }

    // Entityとしてのアップデート
    @Override
    public void onUpdate()
    {
	super.onUpdate();

	// 開閉モーション
	this.modelMotion.setCoverMotion(this, this.isOpen());
    }

    // 死んだ際にアイテムを落とす処理
    @Override
    protected void dropFewItems(boolean par1, int par2)
    {
	super.dropFewItems(par1, par2);

	float rX = this.rand.nextFloat() * 0.8F + 0.1F;
	float rY = this.rand.nextFloat() * 0.8F + 0.1F;
	float rZ = this.rand.nextFloat() * 0.8F + 0.1F;
	float rand = 0.05F;

	// インベントリの中身をドロップ
	for (int i = 0; i < this.inventory.getSizeInventory(); ++i)
	{
	    ItemStack itemstack = this.inventory.getStackInSlot(i);

	    if (itemstack != null)
	    {
		while (itemstack.stackSize > 0)
		{
		    int j = this.rand.nextInt(21) + 10;

		    if (j > itemstack.stackSize)
		    {
			j = itemstack.stackSize;
		    }

		    itemstack.stackSize -= j;

		    EntityItem entityitem = new EntityItem(this.worldObj, this.posX + rX, this.posY + rY, this.posZ + rZ, new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));

		    if (itemstack.hasTagCompound())
		    {
			entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
		    }

		    entityitem.motionX = (float) this.rand.nextGaussian() * rand;
		    entityitem.motionY = (float) this.rand.nextGaussian() * rand + 0.2F;
		    entityitem.motionZ = (float) this.rand.nextGaussian() * rand;

		    // クライアントだけの処理
		    if (!this.worldObj.isRemote)
		    {
			this.worldObj.spawnEntityInWorld(entityitem);
		    }
		}
	    }
	}

	// 装備インベントリの中身をドロップ
	for (int i = 0; i < this.equipment.getSizeInventory(); ++i)
	{
	    ItemStack itemstack = this.equipment.getStackInSlot(i);

	    if (itemstack != null)
	    {
		while (itemstack.stackSize > 0)
		{
		    int j = this.rand.nextInt(21) + 10;

		    if (j > itemstack.stackSize)
		    {
			j = itemstack.stackSize;
		    }

		    itemstack.stackSize -= j;

		    EntityItem entityitem = new EntityItem(this.worldObj, this.posX + rX, this.posY + rY, this.posZ + rZ, new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));

		    if (itemstack.hasTagCompound())
		    {
			entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
		    }

		    entityitem.motionX = (float) this.rand.nextGaussian() * rand;
		    entityitem.motionY = (float) this.rand.nextGaussian() * rand + 0.2F;
		    entityitem.motionZ = (float) this.rand.nextGaussian() * rand;

		    // クライアントだけの処理
		    if (!this.worldObj.isRemote)
		    {
			this.worldObj.spawnEntityInWorld(entityitem);
		    }
		}
	    }
	}

	// スペシャルアイテムの中身をドロップ
	for (int i = 0; i < this.specialItems.getSizeInventory(); ++i)
	{
	    ItemStack itemstack = this.specialItems.getStackInSlot(i);

	    if (itemstack != null)
	    {
		while (itemstack.stackSize > 0)
		{
		    int j = this.rand.nextInt(21) + 10;

		    if (j > itemstack.stackSize)
		    {
			j = itemstack.stackSize;
		    }

		    itemstack.stackSize -= j;

		    EntityItem entityitem = new EntityItem(this.worldObj, this.posX + rX, this.posY + rY, this.posZ + rZ, new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));

		    if (itemstack.hasTagCompound())
		    {
			entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
		    }

		    entityitem.motionX = (float) this.rand.nextGaussian() * rand;
		    entityitem.motionY = (float) this.rand.nextGaussian() * rand + 0.2F;
		    entityitem.motionZ = (float) this.rand.nextGaussian() * rand;

		    // クライアントだけの処理
		    if (!this.worldObj.isRemote)
		    {
			this.worldObj.spawnEntityInWorld(entityitem);
		    }
		}
	    }
	}

    }

    // ------------------------- ↓独自の実装↓ -------------------------//

    // 現在の体力（GUI用）
    @SideOnly(Side.CLIENT)
    public int getHealthBar()
    {
	return (int) this.getHealth();
    }

    // UtilityCoreによりスポーンした際の処理
    public void onSpawnWithUtilityCore(EntityLivingChest basechest, EntityPlayer player, World world, int px, int py, int pz)
    {
	// 向きの修正
	basechest.setLocationAndAngles(px + 0.5D, py, pz + 0.5D, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
	basechest.rotationYawHead = basechest.rotationYaw;
	basechest.renderYawOffset = basechest.rotationYaw;

	// mobEggから沸いた際に呼ばれる
	basechest.onSpawnWithEgg((IEntityLivingData) null);

	// 飼い慣らし
	basechest.setTamed(true);
	basechest.setPathToEntity((PathEntity) null);
	basechest.setAttackTarget((EntityLivingBase) null);
	basechest.setHealth(20.0F);
	basechest.func_152115_b(player.getUniqueID().toString());
	basechest.worldObj.setEntityState(this, (byte) 7);

	// スポーン
	world.spawnEntityInWorld(basechest);
    }

}
