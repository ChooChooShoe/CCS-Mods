package ccs.mods.books;

import net.minecraft.src.CreativeTabs;

final class CreativeTabBooks extends CreativeTabs {

	public static final CreativeTabs tabBooks = new CreativeTabBooks("books");
	
	public CreativeTabBooks(String label) {
		super(getNextID(), label);
	}
	
	public int getTabIconItemIndex()
	{
		return BookMod.bookColour.shiftedIndex;
	}
}
