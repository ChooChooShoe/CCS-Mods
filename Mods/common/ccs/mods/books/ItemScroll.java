package ccs.mods.books;

import java.util.List;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagString;
import net.minecraft.src.StatCollector;
import net.minecraft.src.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class ItemScroll extends Item {

	protected final boolean isDone;

	public ItemScroll(int par1, boolean isDone) {
		super(par1);
		this.isDone = isDone;
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabBooks.tabBooks);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World world,
			EntityPlayer player) {
		player.openGui(BookMod.instance, 3, world, isDone ? 0 : 1, 0, 0);
		return par1ItemStack;
	}

	@Override
	public String getTextureFile() {
		return CommonProxy.ICONS_PNG;
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		if (par1ItemStack.hasTagCompound() && this.isDone) {
			NBTTagCompound var2 = par1ItemStack.getTagCompound();
			NBTTagString var3 = (NBTTagString) var2.getTag("title");

			if (var3 != null)
				return var3.toString();
		}

		return super.getItemDisplayName(par1ItemStack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if (par1ItemStack.hasTagCompound() && this.isDone) {
			NBTTagCompound var5 = par1ItemStack.getTagCompound();
			NBTTagString var6 = (NBTTagString) var5.getTag("author");

			if (var6 != null) {
				par3List.add("\u00a77"
						+ String.format(StatCollector
								.translateToLocalFormatted("book.byAuthor",
										new Object[] { var6.data }),
										new Object[0]));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack) {
		return this.isDone;
	}

	public static boolean validBookTagPages(NBTTagCompound par0NBTTagCompound) {
		if (par0NBTTagCompound == null)
			return false;
		else if (!par0NBTTagCompound.hasKey("pages"))
			return false;
		else {
			NBTTagList var1 = (NBTTagList) par0NBTTagCompound.getTag("pages");

			for (int var2 = 0; var2 < var1.tagCount(); ++var2) {
				NBTTagString var3 = (NBTTagString) var1.tagAt(var2);

				if (var3.data == null)
					return false;
			}

			return true;
		}
	}
}
