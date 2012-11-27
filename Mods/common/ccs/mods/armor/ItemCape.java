package ccs.mods.armor;

import net.minecraft.src.Item;

public class ItemCape extends ItemArmorExtended {

	private int pattern;

	public ItemCape(int par1, int pattern) {
		super(par1, null, EnumEquipment.cape);
		this.pattern = pattern;
		this.setMaxStackSize(1);
	}
	public String getCapeTexture(){
		return "/ccs/res/armor/capes/cape_" + pattern + ".png";
	}
}
