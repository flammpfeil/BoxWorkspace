package com.schr0.schr0box.livingutility.entity.base;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public abstract class EntityLivingUtility extends EntityTameable
{
    private EntityPlayer tradePlayer;

    // DataWatcherのID
    // UTIL_STACK（20） : 元となったUtility（ItemStack）
    private final static int UTIL_STACK_VALUE = 20;
    private final static int TRADE_VALUE = 21;

    public EntityLivingUtility(World par1World)
    {
	super(par1World);
    }

    // DataWatcherの処理
    @Override
    protected void entityInit()
    {
	super.entityInit();
	this.getDataWatcher().addObject(UTIL_STACK_VALUE, new ItemStack(Blocks.stone));
    }

    // UTIL_STACK_VALUE : 元となったItemStackのget
    public ItemStack getUtilStack()
    {
	return this.getDataWatcher().getWatchableObjectItemStack(UTIL_STACK_VALUE);
    }

    // UTIL_STACK_VALUE : 元となったItemStackのset
    public void setUtilStack(ItemStack is)
    {
	this.getDataWatcher().updateObject(UTIL_STACK_VALUE, is);
    }

    // NBTの書き込み
    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
	super.writeEntityToNBT(par1NBTTagCompound);
	NBTTagCompound utilnbt = new NBTTagCompound();
	this.getUtilStack().writeToNBT(utilnbt);
	par1NBTTagCompound.setTag("utilstack", utilnbt);
    }

    // NBTの読み込み
    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
	super.readEntityFromNBT(par1NBTTagCompound);
	this.setUtilStack(ItemStack.loadItemStackFromNBT((NBTTagCompound) par1NBTTagCompound.getTag("utilstack")));
    }

    // お座りの処理
    @Override
    public void setSitting(boolean par1)
    {
	super.setSitting(par1);

	// 追加の処理
	this.isJumping = false;
	this.setPathToEntity((PathEntity) null);
	this.setTarget((Entity) null);
	this.setAttackTarget((EntityLivingBase) null);
    }

    // （自らが）乗っている場合のアップデート
    @Override
    public void updateRidden()
    {
	super.updateRidden();

	// 何かに乗っている場合
	if (this.isRiding())
	{
	    EntityLivingBase livingbase = (EntityLivingBase) this.ridingEntity;

	    // 乗っている生物と同様の正面を向く
	    this.prevRotationYaw = this.rotationYaw = livingbase.rotationYaw;
	}
    }

    // 複数落とすアイテム
    @Override
    protected void dropFewItems(boolean par1, int par2)
    {
	super.dropFewItems(par1, par2);

	// 元となったUtility（ItemStack）がある場合にはドロップ
	if (this.getUtilStack() != null)
	{
	    this.entityDropItem(this.getUtilStack(), 0.5F);
	}
    }

    // 生まれる赤ん坊の設定
    @Override
    public EntityAgeable createChild(EntityAgeable par1EntityAgeable)
    {
	return null;
    }

    // ------------------------- ↓独自の実装↓ -------------------------//

    // 取引をしている間の判定
    public boolean isTrading()
    {
	return this.tradePlayer != null;
    }

    // Traderのget
    public EntityPlayer getTrader()
    {
	return this.tradePlayer;
    }

    // Traderのset
    public void setTrader(EntityPlayer par1EntityPlayer)
    {
	this.tradePlayer = par1EntityPlayer;
    }

    // メッセージの出力
    public void information(String message)
    {
	// オーナー（EntityPlayer）が存在している場合
	if (this.getOwner() instanceof EntityPlayer && !this.worldObj.isRemote)
	{
	    // メッセージを表示
	    ((EntityPlayer) this.getOwner()).addChatComponentMessage(new ChatComponentTranslation(message, new Object[0]));
	}
    }

    // SEの出力
    public void playSE(String type, float vol, float pitch)
    {
	this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, type, vol, pitch);
    }

}
