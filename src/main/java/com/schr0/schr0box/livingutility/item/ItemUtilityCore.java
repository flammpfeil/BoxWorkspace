package com.schr0.schr0box.livingutility.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.world.World;

import com.schr0.schr0box.livingutility.LivingUtility;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Ender;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Normal;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemUtilityCore extends Item
{
    public ItemUtilityCore()
    {
	super();
	this.maxStackSize = 1;
	this.setUnlocalizedName("itemUtilityCore");
	this.setCreativeTab(LivingUtility.tab_LivingUtility);
	this.setTextureName(LivingUtility.TEXTURE_DOMAIN + "UtilityCore");
    }

    // 手に持った際に３Ｄ表示にするかの判定
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
	return true;
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

	// Blockの読み込み
	Block block = world.getBlock(x, y, z);

	// TileEntityの読み込み
	TileEntity tile = world.getTileEntity(x, y, z);

	// TileEntityChestの場合
	if (tile instanceof TileEntityChest)
	{
	    TileEntityChest tileChest = (TileEntityChest) tile;

	    if (tileChest.numPlayersUsing > 0)
	    {
		return false;
	    }

	    return createEntityLivingChest_Normal(EntityLivingChest_Normal.class, stack, player, world, tileChest, x, y, z);
	}

	if (tile instanceof TileEntityEnderChest)
	{
	    return createEntityLivingChest_Ender(stack, player, world, x, y, z);
	}

	return false;
    }

    // 流れがどうの、ついでに下をまとめた
    private boolean createEntityLivingChest_Normal(Class<? extends EntityLivingChest_Normal> clazz, ItemStack stack, EntityPlayer player, World world, TileEntityChest tilechest, int x, int y, int z)
    {
	try
	{
	    EntityLivingChest_Normal normalchest = clazz.getConstructor(World.class).newInstance(world);

	    // normalchestのinventory呼び出し
	    normalchest.inventory.openInventory();

	    // tilechestに中身がある場合には保持
	    int newSize = normalchest.inventory.getSizeInventory();
	    ItemStack[] chestContents = ObfuscationReflectionHelper.getPrivateValue(TileEntityChest.class, tilechest, 0);
	    System.arraycopy(chestContents, 0, normalchest.inventory.containerItems, 0, Math.min(newSize, chestContents.length));
	    for (int i = 0; i < Math.min(newSize, chestContents.length); i++)
	    {
		chestContents[i] = null;
	    }

	    // normalchestのinventory保持
	    normalchest.inventory.closeInventory();

	    // tilechestの更新
	    tilechest.updateContainingBlockInfo();
	    tilechest.checkForAdjacentChests();

	    // 生成元のアイテムをセット
	    ItemStack material = new ItemStack(world.getBlock(x, y, z), 1, world.getBlockMetadata(x, y, z));
	    normalchest.setUtilStack(material);

	    // ブロックの破壊
	    world.func_147480_a(x, y, z, false);

	    // UtilityCoreによりスポーンした際の処理
	    normalchest.onSpawnWithUtilityCore(normalchest, player, world, x, y, z);

	    // スタックを減らす処理
	    if (!player.capabilities.isCreativeMode)
	    {
		--stack.stackSize;
	    }

	    return true;
	}
	catch (Exception e)
	{
	    // 必要があれば適当に分けて
	    e.printStackTrace();
	}

	return false;
    }

    /*うえにまとめた

    //流れを追いやすくするため、処理をメソッドに分割
    private boolean createEntityLivingChestFromNormalChest(ItemStack stack, EntityPlayer player, World world, TileEntityChest tileChest, int x, int y, int z) {
        // EntityLivingChest_Followを宣言
        EntityLivingChest_Follow followChest = new EntityLivingChest_Follow(world);

        // followChestのinventory呼び出し
        followChest.inventory.openInventory();

        // tileChestに中身がある場合には保持
        int newSize = followChest.inventory.getSizeInventory();
        ItemStack[] chestContents = ObfuscationReflectionHelper.getPrivateValue(TileEntityChest.class, tileChest, 0);
        System.arraycopy(chestContents, 0, followChest.inventory.containerItems, 0, Math.min(newSize, chestContents.length));
        for (int i = 0; i < Math.min(newSize, chestContents.length); i++)
        {
            chestContents[i] = null;
        }

        // followChestのinventory保持
        followChest.inventory.closeInventory();

        // tileChestの更新
        tileChest.updateContainingBlockInfo();
        tileChest.checkForAdjacentChests();

        // ブロックの破壊
        world.func_147480_a(x, y, z, false);

        // UtilityCoreによりスポーンした際の処理
        followChest.onSpawnWithUtilityCore(followChest, player, world, x, y, z);

        // スタックを減らす処理
        if (!player.capabilities.isCreativeMode)
        {
            --stack.stackSize;
        }

        return true;
    }

    //流れを追いやすくするため、処理をメソッドに分割
    private boolean createEntityLivingChestFromTrapChest(ItemStack stack, EntityPlayer player, World world, TileEntityChest tileChest, int x, int y, int z) {
        // EntityLivingChest_Collectを宣言
        EntityLivingChest_Collect collectChest = new EntityLivingChest_Collect(world);

        // collectChestのinventory呼び出し
        collectChest.inventory.openInventory();

        // tileChestに中身がある場合には保持
        int newSize = collectChest.inventory.getSizeInventory();
        ItemStack[] chestContents = ObfuscationReflectionHelper.getPrivateValue(TileEntityChest.class, tileChest, 0);
        System.arraycopy(chestContents, 0, collectChest.inventory.containerItems, 0, Math.min(newSize, chestContents.length));
        for (int i = 0; i < Math.min(newSize, chestContents.length); i++)
        {
            chestContents[i] = null;
        }

        // collectChestのinventory保持
        collectChest.inventory.closeInventory();

        // tileChestの更新
        tileChest.updateContainingBlockInfo();
        tileChest.checkForAdjacentChests();

        // ブロックの破壊
        world.func_147480_a(x, y, z, false);

        // UtilityCoreによりスポーンした際の処理
        collectChest.onSpawnWithUtilityCore(collectChest, player, world, x, y, z);

        // スタックを減らす処理
        if (!player.capabilities.isCreativeMode)
        {
            --stack.stackSize;
        }

        return true;
    }
    */

    private boolean createEntityLivingChest_Ender(ItemStack stack, EntityPlayer player, World world, int x, int y, int z)
    {
	EntityLivingChest_Ender enderchest = new EntityLivingChest_Ender(world);

	// 生成元のアイテムをセット
	ItemStack material = new ItemStack(world.getBlock(x, y, z), 1, world.getBlockMetadata(x, y, z));
	enderchest.setUtilStack(material);

	// ブロックの破壊
	world.func_147480_a(x, y, z, false);

	// UtilityCoreによりスポーンした際の処理
	enderchest.onSpawnWithUtilityCore(enderchest, player, world, x, y, z);

	// スタックを減らす処理
	if (!player.capabilities.isCreativeMode)
	{
	    --stack.stackSize;
	}

	return true;
    }
}
