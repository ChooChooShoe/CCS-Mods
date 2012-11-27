package ccs.mods.books.client;

import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.StatCollector;

import org.lwjgl.opengl.GL11;

import ccs.mods.books.ContainerBookshelf;
import ccs.mods.books.ItemRainbowBooks;
import ccs.mods.books.TileEntityBookshelf;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBookshelf extends GuiContainer {

	private TileEntityBookshelf shelf;
	private EntityPlayer thePlayer;

	public GuiBookshelf(EntityPlayer player, TileEntityBookshelf shelf) {
		super(new ContainerBookshelf(player, shelf));
		this.shelf = shelf;
		thePlayer = player;
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRenderer.drawString("Bookshelf", 60, 6, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {

		int var4 = this.mc.renderEngine.getTexture("/ccs/res/books/Bookshelf.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		int xPos = (this.width - this.xSize) / 2;
		int yPos = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);
		ItemStack clickOn = null;
		for (int var31 = 0; var31 < this.inventorySlots.inventorySlots.size(); ++var31)
		{
			Slot slot = (Slot)this.inventorySlots.inventorySlots.get(var31);

			if (this.func_74188_c(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, var2, var3))
				clickOn = slot.getStack();
		}
		
		if(clickOn != null && clickOn.getItem() instanceof ItemRainbowBooks){
		}
	}
}
