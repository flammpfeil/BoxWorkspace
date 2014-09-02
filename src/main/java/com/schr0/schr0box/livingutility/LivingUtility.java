package com.schr0.schr0box.livingutility;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.schr0.schr0box.livingutility.core.GuiHandler;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Ender;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Normal;
import com.schr0.schr0box.livingutility.item.ItemHomePointTicket;
import com.schr0.schr0box.livingutility.item.ItemUtilityCore;
import com.schr0.schr0box.livingutility.proxy.ServerProxy;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = LivingUtility.MODID, version = LivingUtility.VERSION)
public class LivingUtility
{
    // MODID・VERSION
    public static final String MODID = "schr0box.livingutility";
    public static final String VERSION = "0.1";

    // インスタンス
    @Mod.Instance(LivingUtility.MODID)
    public static LivingUtility instance;

    @SidedProxy(clientSide = "com.schr0.schr0box.livingutility.proxy.ClientProxy", serverSide = "com.schr0.schr0box.livingutility.proxy.ServerProxy")
    public static ServerProxy proxy;

    // テクスチャのdomain
    public static final String TEXTURE_DOMAIN = "schr0_livingutility:";

    // Item
    public static Item item_UtilityCore;
    public static Item item_HomePointTicket;

    // GUIのID
    public static int CHEST_GUI_ID = 0;

    // CreativeTabs
    public static final CreativeTabs tab_LivingUtility = new CreativeTabs("Living Utility")
    {
	// CreativeTabsのラベル
	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel()
	{
	    return "Living Utility";
	}

	// CreativeTabsのアイコン用のItem
	@Override
	public Item getTabIconItem()
	{
	    return item_UtilityCore;
	}
    };

    // 前処理
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
	// Configurationの生成
	Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "schr0box/" + MODID + ".conf"));

	// 例外処理
	try
	{
	    // configのロード
	    config.load();

	    // none

	}
	catch (Exception e)
	{
	    // エラーメッセージを出力
	    FMLLog.severe(LivingUtility.MODID + " is ERROR");
	}
	finally
	{
	    // configのセーブ
	    config.save();

	    // Itemの処理
	    this.buildItem();
	}
    }

    // Itemの処理
    private void buildItem()
    {
	this.item_UtilityCore = new ItemUtilityCore();
	this.item_HomePointTicket = new ItemHomePointTicket();

	GameRegistry.registerItem(this.item_UtilityCore, "item_UtilityCore");
	GameRegistry.registerItem(this.item_HomePointTicket, "item_HomePointTicket");
    }

    // 中処理
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
	// Entityの処理
	this.buildEntity();

	// GUIの処理
	this.buildGUI();

	// レシピの追加（独自）
	this.buildRecipe();
    }

    // Entityの処理
    private void buildEntity()
    {
	// mod内での同期ID
	int nomal_chest_id = 0;
	int ender_chest_id = 1;

	// -----registerModEntityの引数-----//
	// 1 Entityのclass
	// 2 内部名
	// 3 mod内での同期ID(※mod内で被らないければOK)
	// 4 @Modのclass(instance)
	// 5 更新可能な距離
	// 6 更新頻度
	// 7 速度情報を持つか否か
	EntityRegistry.registerModEntity(EntityLivingChest_Normal.class, "NormalChest", nomal_chest_id, this, 250, 1, true);
	EntityRegistry.registerModEntity(EntityLivingChest_Ender.class, "EnderChest", ender_chest_id, this, 250, 1, true);

	// クライアントでの処理
	this.proxy.registerClient();
    }

    // GUIの処理
    private void buildGUI()
    {
	// GUIの登録
	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }

    // レシピの処理
    private void buildRecipe()
    {
	// UtilityCore
	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(item_UtilityCore, 1), new Object[]
	{

	" Y ", "YXY", " Y ",

	'X', new ItemStack(Blocks.redstone_block, 1), 'Y', new ItemStack(Items.dye, 1, 4), }));

	// HomePointTicket
	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(item_HomePointTicket, 1), new Object[]
	{

	" Y ", "YXY", " Y ",

	'X', new ItemStack(Items.paper, 1), 'Y', new ItemStack(Items.dye, 1, 4), }));
    }

    // 後処理
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
	// none
    }

}
