package ccs.mods.books.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagString;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.StatCollector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import ccs.mods.books.BookMod;
import ccs.mods.books.ServerHandler;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiScreenWriteable extends GuiScreen
{
	/** The player editing the book */
	protected final EntityPlayer editingPlayer;
	protected final ItemStack bookStack;

	/** Whether the book is signed or can still be edited */
	protected final boolean bookIsUnsigned;
	protected boolean bookModified;
	protected boolean editingTitle;

	/** Update ticks since the gui was opened */
	protected int updateCount;
	protected int bookImageWidth = 192;
	protected int bookImageHeight = 192;
	protected int bookTotalPages = 1;
	protected int currPage;
	protected NBTTagList bookPages;
	protected String bookTitle = "";
	protected GuiButtonTurnPage buttonNextPage;
	protected GuiButtonTurnPage buttonPreviousPage;
	protected GuiButton buttonDone;

	/** The GuiButton to sign this book. */
	protected GuiButton buttonSign;
	protected GuiButton buttonFinalize;
	protected GuiButton buttonCancel;
	protected int maxPages = 50;
	protected int posX;
	protected int posY;

	public GuiScreenWriteable(EntityPlayer player, ItemStack book, boolean unsigned)
	{
		this.editingPlayer = player;
		this.bookStack = book;
		this.bookIsUnsigned = unsigned;

		if(book == null){
		}
		if (book.hasTagCompound())
		{
			NBTTagCompound var4 = book.getTagCompound();
			this.bookPages = var4.getTagList("pages");

			if (this.bookPages != null)
			{
				this.bookPages = (NBTTagList)this.bookPages.copy();
				this.bookTotalPages = this.bookPages.tagCount();

				if (this.bookTotalPages < 1)
				{
					this.bookTotalPages = 1;
				}
			}
		}

		if (this.bookPages == null && unsigned)
		{
			this.bookPages = new NBTTagList("pages");
			this.bookPages.appendTag(new NBTTagString("1", ""));
			this.bookTotalPages = 1;
		}
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		++this.updateCount;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui()
	{
		this.controlList.clear();
		Keyboard.enableRepeatEvents(true);
		
		this.posX = (this.width - this.bookImageWidth) / 2;
		this.posY = (this.height - this.bookImageHeight) / 2;
		int buttonLeft = this.width / 2 - 100;
		int buttonRight = this.width / 2 + 2;
		int buttonHight = this.bookImageHeight + posY;
		
		if (this.bookIsUnsigned)
		{
			this.controlList.add(this.buttonSign = new GuiButton(3, buttonLeft, buttonHight, 98, 20, StatCollector.translateToLocal("book.signButton")));
			this.controlList.add(this.buttonDone = new GuiButton(0, buttonRight, buttonHight, 98, 20, StatCollector.translateToLocal("gui.done")));
			this.controlList.add(this.buttonFinalize = new GuiButton(5, buttonLeft, buttonHight, 98, 20, StatCollector.translateToLocal("book.finalizeButton")));
			this.controlList.add(this.buttonCancel = new GuiButton(4, buttonRight, buttonHight, 98, 20, StatCollector.translateToLocal("gui.cancel")));
		}
		else
		{
			this.controlList.add(this.buttonDone = new GuiButton(0, buttonLeft, buttonHight, 200, 20, StatCollector.translateToLocal("gui.done")));
		}
		this.controlList.add(this.buttonNextPage = new GuiButtonTurnPage(1, posX + 120, posY + 154, true));
		this.controlList.add(this.buttonPreviousPage = new GuiButtonTurnPage(2, posX + 38, posY + 154, false));
		this.updateButtons();
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void updateButtons()
	{
		this.buttonNextPage.drawButton = !this.editingTitle && (this.currPage < this.bookTotalPages - 1 || this.bookIsUnsigned);
		this.buttonPreviousPage.drawButton = !this.editingTitle && this.currPage > 0;
		this.buttonDone.drawButton = !this.bookIsUnsigned || !this.editingTitle;

		if (this.bookIsUnsigned)
		{
			this.buttonSign.drawButton = !this.editingTitle;
			this.buttonCancel.drawButton = this.editingTitle;
			this.buttonFinalize.drawButton = this.editingTitle;
			this.buttonFinalize.enabled = this.bookTitle.trim().length() > 0;
		}
	}

	protected void sendBookToServer(boolean doSign)
	{
		if (this.bookIsUnsigned && this.bookModified)
		{
			if (this.bookPages != null)
			{
				while (this.bookPages.tagCount() > 1)
				{
					NBTTagString var2 = (NBTTagString)this.bookPages.tagAt(this.bookPages.tagCount() - 1);

					if (var2.data != null && var2.data.length() != 0)
					{
						break;
					}

					this.bookPages.removeTag(this.bookPages.tagCount() - 1);
				}

				if (this.bookStack.hasTagCompound())
				{
					NBTTagCompound var7 = this.bookStack.getTagCompound();
					var7.setTag("pages", this.bookPages);
				}
				else
				{
					this.bookStack.func_77983_a("pages", this.bookPages);
				}

				String var8 = "BM|BEdit";

				if (doSign)
				{
					var8 = "BM|BSign";
					this.bookStack.func_77983_a("author", new NBTTagString("author", this.editingPlayer.username));
					this.bookStack.func_77983_a("title", new NBTTagString("title", this.bookTitle.trim()));
					this.bookStack.itemID = this.getNewBookID();
				}
				ServerHandler.sendBookToServer(var8, bookStack, this.getNewBookID());
			}
		}
	}
	public int getNewBookID(){
		return BookMod.scrollSigned.shiftedIndex;
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.enabled)
		{
			if (par1GuiButton.id == 0)
			{
				this.mc.displayGuiScreen((GuiScreen)null);
				this.sendBookToServer(false);
			}
			else if (par1GuiButton.id == 3 && this.bookIsUnsigned)
			{
				this.editingTitle = true;
			}
			else if (par1GuiButton.id == 1)
			{
				if (this.currPage < this.bookTotalPages - 1)
				{
					++this.currPage;
				}
				else if (this.bookIsUnsigned)
				{
					this.addNewPage();

					if (this.currPage < this.bookTotalPages - 1)
					{
						++this.currPage;
					}
				}
			}
			else if (par1GuiButton.id == 2)
			{
				if (this.currPage > 0)
				{
					--this.currPage;
				}
			}
			else if (par1GuiButton.id == 5 && this.editingTitle)
			{
				this.sendBookToServer(true);
				this.mc.displayGuiScreen((GuiScreen)null);
			}
			else if (par1GuiButton.id == 4 && this.editingTitle)
			{
				this.editingTitle = false;
			}

			this.updateButtons();
		}
	}

	public void addNewPage()
	{
		if (this.bookPages != null && this.bookPages.tagCount() < this.maxPages)
		{
			this.bookPages.appendTag(new NBTTagString("" + (this.bookTotalPages + 1), ""));
			++this.bookTotalPages;
			this.bookModified = true;
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char par1, int par2)
	{
		super.keyTyped(par1, par2);

		if (this.bookIsUnsigned)
		{
			if (this.editingTitle)
			{
				this.keyTypedInTitle(par1, par2);
			}
			else
			{
				this.keyTypedInBook(par1, par2);
			}
		}
	}

	/**
	 * Processes keystrokes when editing the text of a book
	 */
	protected void keyTypedInBook(char par1, int par2)
	{
		switch (par1)
		{
		case 22:
			this.typeString(GuiScreen.getClipboardString());
			return;
		default:
			switch (par2)
			{
			case 14:
				String var3 = this.oldLine();

				if (var3.length() > 0)
				{
					this.writeToBook(var3.substring(0, var3.length() - 1));
				}

				return;
			case 28:
				this.typeString("\n");
				return;
			default:
				if (ChatAllowedCharacters.isAllowedCharacter(par1))
				{
					this.typeString(Character.toString(par1));
				}
			}
		}
	}

	protected void keyTypedInTitle(char par1, int par2)
	{
		switch (par2)
		{
		case 14:
			if (this.bookTitle.length() > 0)
			{
				this.bookTitle = this.bookTitle.substring(0, this.bookTitle.length() - 1);
				this.updateButtons();
			}

			return;
		case 28:
			if (this.bookTitle.length() > 0)
			{
				this.sendBookToServer(true);
				this.mc.displayGuiScreen((GuiScreen)null);
			}

			return;
		default:
			if (this.bookTitle.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(par1))
			{
				this.bookTitle = this.bookTitle + Character.toString(par1);
				this.updateButtons();
				this.bookModified = true;
			}
		}
	}

	protected String oldLine()
	{
		if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount())
		{
			NBTTagString var1 = (NBTTagString)this.bookPages.tagAt(this.currPage);
			return var1.toString();
		} else
			return "";
	}

	protected void writeToBook(String par1Str)
	{
		if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount())
		{
			NBTTagString var2 = (NBTTagString)this.bookPages.tagAt(this.currPage);
			var2.data = par1Str;
			this.bookModified = true;
		}
	}

	public void typeString(String par1Str)
	{
		String var2 = this.oldLine();
		String var3 = var2 + par1Str;
		int var4 = this.fontRenderer.splitStringWidth(var3 + "\u00a70_", 118);

		if (var4 <= 118 && var3.length() < 256)
		{
			this.writeToBook(var3);
		}
	}
	public String getBookTexture(){
		return "/gui/book.png";
	}
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		drawBackGround();
		if (this.editingTitle) {
			drawSigned();
		}
		else
		{
			drawUnsigned();
		}

		super.drawScreen(par1, par2, par3);
	}
	public void drawBackGround() {
		int var4 = this.mc.renderEngine.getTexture(this.getBookTexture());
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		this.drawTexturedModalRect(posX, posY, 0, 0, this.bookImageWidth, this.bookImageHeight);
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

		String var8 = StatCollector.translateToLocal("book.editTitle");
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
		String var7 = String.format(StatCollector.translateToLocal("book.pageIndicator"), new Object[] {Integer.valueOf(this.currPage + 1), Integer.valueOf(this.bookTotalPages)});
		String var8 = "";

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
		}

		int var9 = this.fontRenderer.getStringWidth(var7);
		this.fontRenderer.drawString(var7, posX - var9 + this.bookImageWidth - 44, posY + 16, 0);
		this.fontRenderer.drawSplitString(var8, posX + 36, posY + 16 + 16, 116, 0);
	}
}
