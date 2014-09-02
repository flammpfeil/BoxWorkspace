package com.schr0.schr0box.livingutility.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.schr0.schr0box.livingutility.LivingUtility;

public class ItemHomePointTicket extends Item
{
    public ItemHomePointTicket()
    {
	super();
	this.maxStackSize = 1;
	this.setUnlocalizedName("itemHomePointTicket");
	this.setCreativeTab(LivingUtility.tab_LivingUtility);
	this.setTextureName(LivingUtility.TEXTURE_DOMAIN + "HomePointTicket");
    }

    // レアエフェクト
    @Override
    public boolean hasEffect(ItemStack par1ItemStack, int pass)
    {
	NBTTagCompound nbt = par1ItemStack.getTagCompound();

	if (nbt == null)
	{
	    return false;
	}

	return nbt.getTag("TargetName") != null;
    }

    // アイテム情報の表示
    // 座標が設定されている場合には表示
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
	// NBT値を取得
	NBTTagCompound nbt = par1ItemStack.getTagCompound();
	if (nbt == null)
	{
	    par3List.add("Target Name : NULL");
	    par3List.add(" ");
	    par3List.add("+----------------+");
	    par3List.add("Target PosX : NULL");
	    par3List.add("Target PosY : NULL");
	    par3List.add("Target PosZ : NULL");
	    par3List.add("Target Side : NULL");
	    par3List.add("+----------------+");
	}
	else
	{
	    String name;
	    int posx;
	    int posy;
	    int posz;
	    int side;

	    // NBT値を代入
	    name = nbt.getString("TargetName");
	    posx = nbt.getInteger("TargetPosX");
	    posy = nbt.getInteger("TargetPosY");
	    posz = nbt.getInteger("TargetPosZ");
	    side = nbt.getInteger("TargetSide");

	    // 登録インベントリ名がnullの場合
	    if (nbt.getTag("TargetName") == null)
	    {
		par3List.add("Target Name : NULL");
		par3List.add(" ");
		par3List.add("+----------------+");
		par3List.add("Target PosX : NULL");
		par3List.add("Target PosY : NULL");
		par3List.add("Target PosZ : NULL");
		par3List.add("Target Side : NULL");
		par3List.add("+----------------+");
	    }
	    else
	    {
		par3List.add("Target Name : " + StatCollector.translateToLocal(name));
		par3List.add(" ");
		par3List.add("+----------------+");
		par3List.add("Target PosX : " + posx);
		par3List.add("Target PosY : " + posy);
		par3List.add("Target PosZ : " + posz);
		par3List.add("Target Side : " + side);
		par3List.add("+----------------+");
	    }
	}
    }

    // インタラクトの前に呼ばれる処理
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
	// サーバーだけの処理
	if (world.isRemote)
	{
	    return false;
	}

	// NBTタグを取得
	NBTTagCompound nbt = stack.getTagCompound();
	if (nbt == null)
	{
	    nbt = new NBTTagCompound();
	    stack.setTagCompound(nbt);
	}

	// 登録インベントリ名がnullの場合にはreturn ;
	if (nbt.getTag("TargetName") != null)
	{
	    return false;
	}

	if (!world.canMineBlock(player, x, y, z))
	{
	    return false;
	}

	if (!player.canPlayerEdit(x, y, z, side, stack))
	{
	    return false;
	}

	IInventory inventory = TileEntityHopper.func_145893_b(world, (double) x, (double) y, (double) z);

	if (inventory != null)
	{
	    // NBT値へ書き込み
	    nbt.setInteger("TargetPosX", x);
	    nbt.setInteger("TargetPosY", y);
	    nbt.setInteger("TargetPosZ", z);
	    nbt.setInteger("TargetSide", side);
	    nbt.setString("TargetName", inventory.getInventoryName());

	    // メッセージを表示
	    String info = "Set Home Inventory -> " + StatCollector.translateToLocal(inventory.getInventoryName());
	    player.addChatComponentMessage(new ChatComponentText(info));

	    // 音を出す
	    world.playSoundAtEntity(player, "random.pop", 1.0F, 1.0F);

	    return true;
	}

	return false;
    }
}
