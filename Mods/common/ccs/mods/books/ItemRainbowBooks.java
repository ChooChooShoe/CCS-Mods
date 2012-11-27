package ccs.mods.books;

import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagString;
import net.minecraft.src.StatCollector;
import net.minecraft.src.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class ItemRainbowBooks extends ItemScroll {

	public static final String[] colorNames = new String[] { "Black", "Red",
		"Green", "Brown", "Blue", "Purple", "Cyan", "Light Gray", "Gray",
		"Pink", "Lime", "Yellow", "Light Blue", "Magenta", "Orange",
	"White" };
	public static String[] colorCodes = new String[] { "0", "4", "2", "c",
		"1", "5", "b", "7", "8", "d", "a", "e", "9", "d", "6", "f" };
	public boolean hasPen;

	public ItemRainbowBooks(int par1, boolean isDone, boolean hasPen) {
		super(par1, isDone);
		this.setCreativeTab(CreativeTabBooks.tabBooks);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.hasPen = hasPen;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int par1) {
		int var2 = MathHelper.clamp_int(par1, 0, 15);
		return this.iconIndex + var2 * 16;
	}

	@Override
	public String getTextureFile() {
		return CommonProxy.ICONS_PNG;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World world,
			EntityPlayer player) {
		if (hasPen || isDone) {
			player.openGui(BookMod.instance, 4, world, isDone ? 0 : 1, 0, 0);
		}
		return par1ItemStack;
	}

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		int var2 = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 15);
		if (par1ItemStack.hasTagCompound() && this.isDone) {
			NBTTagCompound tag = par1ItemStack.getTagCompound();
			NBTTagString var3 = (NBTTagString) tag.getTag("title");

			if (var3 != null)
				return String.valueOf((char)167) + colorCodes[var2] + var3.toString();
		} else if (!this.isDone) {
			String book = " Book";
			if (hasPen) {
				book = " Book & Quill";
			}
			return String.valueOf((char)167) + colorCodes[var2] + colorNames[var2] + book;

		}

		return super.getItemDisplayName(par1ItemStack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if (par1ItemStack.hasTagCompound() && this.isDone) {
			NBTTagCompound var5 = par1ItemStack.getTagCompound();
			NBTTagString var6 = (NBTTagString) var5.getTag("author");
			String color = "\u00a77";
			boolean bol = var5.getBoolean("IScopy");
			if (bol) {
				color = "";
			}

			if (var6 != null) {
				par3List.add(color
						+ String.format(StatCollector
								.translateToLocalFormatted("book.byAuthor",
										new Object[] { var6.data }),
										new Object[0]));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs teb, List list) {
		for (int var4 = 0; var4 < 16; ++var4)
			if (!isDone) {
				list.add(new ItemStack(id, 1, var4));
			}

		if(this.hasPen)
		list.addAll(BookMod.SignedStacks);
	}
}
