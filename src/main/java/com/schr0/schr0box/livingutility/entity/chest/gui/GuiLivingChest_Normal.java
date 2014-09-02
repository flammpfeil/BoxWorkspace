package com.schr0.schr0box.livingutility.entity.chest.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.schr0.schr0box.livingutility.LivingUtility;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Normal;
import com.schr0.schr0box.livingutility.entity.chest.inventory.container.ContainerLivingChest_Normal;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLivingChest_Normal extends GuiContainer
{
    private EntityLivingChest_Normal normalChest;
    private float showSizeX, showSizeY;

    // ResourceLocation
    private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation(LivingUtility.TEXTURE_DOMAIN + "textures/gui/livingchest_Collect.png");
    private static final ResourceLocation GUI_ICONS = new ResourceLocation("textures/gui/icons.png");

    public GuiLivingChest_Normal(InventoryPlayer inventoryplayer, EntityLivingChest_Normal normalchest)
    {
	super(new ContainerLivingChest_Normal(inventoryplayer, normalchest));
	this.normalChest = normalchest;

	// GUI背景のサイズ
	this.xSize = 176;
	this.ySize = 223;
    }

    // Guiに文字を書いたりするメソッド
    @Override
    protected void drawGuiContainerForegroundLayer(int xMouse, int yMouse)
    {
	// LivingChestのインベントリ名
	this.fontRendererObj.drawString(this.normalChest.getCommandSenderName(), 8, this.ySize - 216, 0x404040);

	// プレイヤーのインベントリ名
	this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 0x404040);
    }

    // Guiの全体（？）を描画するメソッド (描画のtick, マウスカーソルの座標X, マウスカーソルの座標Y)
    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
	super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);

	this.showSizeX = (float) p_73863_1_;
	this.showSizeY = (float) p_73863_2_;
    }

    // Guiの背景を描画するメソッド (描画のtick, マウスカーソルの座標X, マウスカーソルの座標Y)
    @Override
    protected void drawGuiContainerBackgroundLayer(float renderPartialTicks, int xMouse, int yMouse)
    {
	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

	// 原点x, 原点yを設定
	int x = (this.width - this.xSize) / 2;
	int y = (this.height - this.ySize) / 2;

	// GUI_BACKGROUNDをbindTextureに渡す
	this.mc.getTextureManager().bindTexture(GUI_BACKGROUND);

	// bindされたテクスチャを四角形で描画する
	// drawTexturedModalRect( 原点x, 原点y, 指定TextureのX, 指定TextureのY, xSize, ySize)

	// 全体の表示
	this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

	// 画面上にmobを表示
	// func_147046_a( 表示物のX中心, 表示物のY終点, 表示物のサイズ, 目線移動？のX, 目線移動？のY, 表示物)
	GuiInventory.func_147046_a(x + 51, y + 67, 30, (float) (x + 51) - this.showSizeX, (float) (y + 25) - this.showSizeY, this.normalChest);

	// GUI_ICONSをbindTextureに渡す
	this.mc.getTextureManager().bindTexture(GUI_ICONS);

	// 体力の表示
	int getHealth = this.normalChest.getHealthBar();
	int getHealthBar = this.normalChest.getHealthBar() / 2;

	if (10 < getHealth)
	{
	    getHealthBar = 5;
	}

	int backIconPosX = 16;
	int backIconPosY = 0;
	int iconPosX = 53;
	int iconPosY = 1;

	// ハートの背景
	for (int i = 0; i < 5; i++)
	{
	    this.drawTexturedModalRect(x + 107 + (12 * i), y + 21, backIconPosX, backIconPosY, 9, 9);
	}

	// 『通常』のハート
	for (int i = 0; i < getHealthBar; i++)
	{
	    this.drawTexturedModalRect(x + 108 + (12 * i), y + 22, iconPosX, iconPosY, 7, 7);
	}

	// 残り体力が『奇数』の場合には最大値を『ひび割れ』ハートに
	if (getHealth % 2 != 0)
	{
	    this.drawTexturedModalRect(x + 108 + (12 * getHealthBar), y + 22, iconPosX + 9, iconPosY, 7, 7);
	}
    }
}
