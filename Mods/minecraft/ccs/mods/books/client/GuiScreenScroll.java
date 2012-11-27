package ccs.mods.books.client;

import ccs.mods.books.BookMod;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagString;
import net.minecraft.src.StatCollector;

public class GuiScreenScroll extends GuiScreenWriteable {

	public GuiScreenScroll(EntityPlayer player, ItemStack book, boolean unsigned) {
		super(player, book, unsigned);
		this.maxPages = 1;
		bookImageWidth = 195;
		bookImageHeight = 192;
	}
	public String getBookTexture(){
		return "/ccs/res/books/Scroll.png";
	}
	public void drawSigned(){
		String var7 = this.bookTitle;

		if (this.bookIsUnsigned) {
			if (this.updateCount / 6 % 2 == 0)
			{
				var7 = var7 + "\u00a70_";
			}
			else
			{
				var7 = var7 + "\u00a77_";
			}
		}

		String var8 = "Edit Scroll Title:";
		int var9 = this.fontRenderer.getStringWidth(var8);
		this.fontRenderer.drawString(var8, posX + 36 + (116 - var9) / 2, posY + 16 + 16, 0);
		int var10 = this.fontRenderer.getStringWidth(var7);
		this.fontRenderer.drawString(var7, posX + 36 + (116 - var10) / 2, posY + 48, 0);
		String var11 = String.format(StatCollector.translateToLocal("book.byAuthor"), new Object[] {this.editingPlayer.username});
		int var12 = this.fontRenderer.getStringWidth(var11);
		this.fontRenderer.drawString("\u00a78" + var11, posX + 36 + (116 - var12) / 2, posY + 48 + 10, 0);
		String var13 = StatCollector.translateToLocal("book.finalizeWarning");
		this.fontRenderer.drawSplitString(var13, posX + 36, posY + 80, 116, 0);
	}
	public void drawUnsigned(){
		this.buttonNextPage.drawButton = false;
		String var8 = "";
		String title = "";

		if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount())
		{
			NBTTagString var14 = (NBTTagString)this.bookPages.tagAt(this.currPage);
			var8 = var14.toString();
		}

		if (this.bookIsUnsigned)
		{
			if (this.fontRenderer.getBidiFlag())
			{
				var8 = var8 + "_";
			}
			else if (this.updateCount / 6 % 2 == 0)
			{
				var8 = var8 + "\u00a70_";
			}
			else
			{
				var8 = var8 + "\u00a77_";
			}
			title = "Untitled";
		}
		else{
			if(bookStack.hasTagCompound())
				title = bookStack.getTagCompound().getString("title");
		}

		String var11 = String.format("- %S", new Object[] {this.editingPlayer.username});
		int var12 = this.fontRenderer.getStringWidth(var11);
		this.fontRenderer.drawString("\u00a78" + var11, posX + 174 - var12, posY + 155, 0);
		int split = this.fontRenderer.getStringWidth(title);
		this.fontRenderer.drawString("\u00a78" + title, (this.width - split) / 2, posY + 22, 0);
		this.fontRenderer.drawSplitString(var8, posX + 24, posY + 36, 150, 0);
	}
	public void typeString(String par1Str)
	{
		String var2 = this.oldLine();
		String var3 = var2 + par1Str;
		int var4 = this.fontRenderer.splitStringWidth(var3 + "\u00a70_", 150);

		if (var4 <= 118 && var3.length() < 322)
		{
			this.writeToBook(var3);
		}
	}
	public int getNewBookID(){
		return BookMod.scrollSigned.shiftedIndex;
	}
}
