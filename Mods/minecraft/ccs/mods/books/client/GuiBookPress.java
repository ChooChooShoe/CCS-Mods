package ccs.mods.books.client;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.StatCollector;

import org.lwjgl.opengl.GL11;

import ccs.mods.books.ContainerBookPress;
import ccs.mods.books.ContainerPaperMill;
import ccs.mods.books.TileEntityBookPress;
import ccs.mods.books.TileEntityPaperMill;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBookPress extends GuiContainer
{
	private TileEntityBookPress tepMill;

	public GuiBookPress(EntityPlayer player, TileEntityBookPress entity)
	{
		super(new ContainerBookPress(player, entity));
		this.tepMill = entity;
	}
	@Override
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString("Book Press", 60, 6, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int var4 = this.mc.renderEngine.getTexture("/ccs/res/books/BookPress.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		int xPos = (this.width - this.xSize) / 2;
		int yPos = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);
		int var7;

		var7 = this.tepMill.printTime * 24 / 250;
		this.drawTexturedModalRect(xPos + 92, yPos + 35, 176, 0, var7, 24);
	}
}

