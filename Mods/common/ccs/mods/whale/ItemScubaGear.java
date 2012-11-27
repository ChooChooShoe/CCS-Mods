package ccs.mods.whale;

import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;

public class ItemScubaGear extends ItemArmor implements IArmorTextureProvider {

	public ItemScubaGear(int id, int type, int index) {
		super(id, EnumArmorMaterial.GOLD, 1, type);
		this.setIconIndex(index);
		this.setItemName("scuba" + this.armorType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getArmorTextureFile(ItemStack itemstack) {
		return "ccs/res/whale/ScubaGear" + (this.armorType == 2 ? "Legs" : "") + ".png";
	}
	@Override
	public String getTextureFile(){
		return "ccs/res/whale/icons.png";
	}
}
