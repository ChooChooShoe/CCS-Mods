package ccs.mods.books.client;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.StatCollector;

import org.lwjgl.opengl.GL11;

import ccs.mods.books.ContainerPaperMill;
import ccs.mods.books.TileEntityPaperMill;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPaperMill extends GuiContainer
{
	private TileEntityPaperMill tepMill;

	public GuiPaperMill(EntityPlayer player, TileEntityPaperMill mill)
	{
		super(new ContainerPaperMill(player, mill));
		this.tepMill = mill;
	}
	@Override
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString("Paper Mill", 60, 6, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int var4 = this.mc.renderEngine.getTexture("/ccs/res/books/PaperMill.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		int xPos = (this.width - this.xSize) / 2;
		int yPos = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);
		int var7;

		if (tepMill.fireOn) {
			var7 = this.tepMill.topSpecs[1] * 12 / this.tepMill.topSpecs[2];
			this.drawTexturedModalRect(xPos + 68, yPos + 37 + 12 - var7, 176, 12 - var7, 14, var7 + 2);
		}

		if (this.tepMill.topSpecs[3] > 0 && this.tepMill.topSpecs[4] != 0) {
			var7 = (this.tepMill.topSpecs[4] - this.tepMill.topSpecs[3]) * 24 / this.tepMill.topSpecs[4];
			this.drawTexturedModalRect(xPos + 64, yPos + 17, 176, 14, var7 + 1, 16);
		}

		var7 = this.tepMill.topSpecs[0] * 50 / 500;
		this.drawTexturedModalRect(xPos + 153, yPos + 60 - var7, 176, 81 - var7, 13, var7);
	}
}

