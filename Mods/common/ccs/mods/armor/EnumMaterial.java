package ccs.mods.armor;

import net.minecraft.src.Block;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import ccs.mods.armor.EnumEquipment.Slots;

public enum EnumMaterial {
    //WOOD("Wood", 0, Block.planks.blockID),
    //STONE("Stone", 1, Block.cobblestone.blockID),
    CLOTH("Cloth", 5, 15, Item.leather.shiftedIndex, 1, 3, 2, 1),
    CHAIN("Chain", 15, 12, Block.fire.blockID, 2, 5, 4, 1),
    IRON("Iron", 15, 9, Item.ingotIron.shiftedIndex, 2, 6, 5, 2),
    GOLD("Golden", 7, 25, Item.ingotGold.shiftedIndex, 2, 5, 3, 1),
    DIAMOND("Diamond", 33, 10, Item.diamond.shiftedIndex, 3, 8, 6, 3);
    
    public String title;
    public int matID;
    public int craftID;
    public int enchantability;
	private int[] shields;
    private EnumMaterial(String title, int maxDamageFactor, int enchantability, int itemID, int... shields) {
    	this.title = title;
    	this.enchantability = enchantability;
    	craftID = itemID;
    	this.shields = shields;
    	
    	
    }
	public static EnumMaterial getEnumFromArmor(EnumArmorMaterial armorMaterial) {
		EnumMaterial mat = EnumMaterial.valueOf(armorMaterial.name());
		if(mat == null)
			System.out.println("No Match Found!!!");
		return mat;
	}
	public int getShieldsFor(int slot) {
		return shields[slot];
	}
}
