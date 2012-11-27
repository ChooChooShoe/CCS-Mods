package ccs.mods.books.client;

import org.lwjgl.opengl.GL11;

import ccs.mods.books.BookMod;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.ItemDye;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagString;
import net.minecraft.src.StatCollector;

public class GuiRainbowBooks extends GuiScreenWriteable {

	public GuiRainbowBooks(EntityPlayer player, ItemStack book, boolean unsigned) {
		super(player, book, unsigned);
		this.maxPages = 50;
		bookImageWidth = 195;
		bookImageHeight = 192;
	}
	public String getBookTexture(){
		return "/ccs/res/books/DullBook.png";
	}
	public void drawBackGround() {
		float[] color = EntitySheep.fleeceColorTable[~this.bookStack.getItemDamage() & 15];
		int var4 = this.mc.renderEngine.getTexture(this.getBookTexture());
		this.mc.renderEngine.bindTexture(var4);
		GL11.glColor3f(color[0], color[1], color[2]);
		this.drawTexturedModalRect(posX+ 20, posY, 0, 0, 78, this.bookImageHeight);
		this.drawTexturedModalRect(posX+ 98, posY, 186, 0, 69, this.bookImageHeight);
		GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
		this.drawTexturedModalRect(posX+ 26, posY+ 8, 64, 8, 134, this.bookImageHeight - 27);
	}
	public int getNewBookID(){
		return BookMod.bookSigned.shiftedIndex;
	}
}
