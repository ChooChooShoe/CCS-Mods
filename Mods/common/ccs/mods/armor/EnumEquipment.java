package ccs.mods.armor;

import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;

public enum EnumEquipment {
    cape("Cape", 11),
    neckless("Neckless", 11),
    ringR("Ring", 0, 59, false, new String[] {" X ", "X X", " X "}),
    ringL("Ring", 0, 59, false, new String[] {" X ", "X X", " X "}),
    helm("Helmet", 1, 0, true, new String[] {"XXX", "X X"}),
    chestPlate("Chest Plate", 1, 1, true, new String[] {"X X", "XXX", "XXX"}),
    leggs("Leggings", 2, 2, true, new String[] {"XXX", "X X", "X X"}),
    boots("Boots", 1, 3, true, new String[] {"X X", "X X"}),
    gloves("Gloves", 2, 1, false, new String[] {"X X"}),
    shield("Shield", 0, 5, false, new String[] {"XXX", "XXX", " X "});
    
    public String title;
	private int textureID;
    public Slots slot;
    public int armorType;
    public boolean isVanila;
    /**How are the Ores Crafting, null for custom recipe*/
	public String[] craftGrid;
	
	private EnumEquipment(String title, int armorType)
    {
		this(title, 0, armorType, false, null);
    }
    private EnumEquipment(String title, int textureID, int armorType, boolean isVanila, String[] grid)
    {
        this.title = title;
        this.textureID = textureID;
        this.slot = Slots.valueOf(this.name());
        this.armorType = armorType;
        this.isVanila = isVanila;
        this.craftGrid = grid;
    }
    public static EnumEquipment getEnumFromSlotType(int armorType){
    	for(EnumEquipment equip : EnumEquipment.values())
    		if(equip.armorType == armorType)
    			return equip;
		return null;
    }
	public String getTexture(EnumMaterial material) {
		if(this.textureID == 0)
			return "NULL";
		return String.format("/ccs/res/armor/%s_%d.png", material.name().toLowerCase(), this.textureID);
	}
    public enum Slots {
        helm(0, 14, 88, 8),
        chestPlate(1, 30, 88, 26),
        leggs(2, 46, 88, 44),
        boots(3, 62, 88, 62),
        cape(4, 16, 67, 11),
        gloves(5, 16, 67, 35),
        ringL(6, 16, 67, 59),
        neckless(7, 15, 109, 11),
        shield(8, 16, 109, 35),
        ringR(9, 16, 109, 59),
        lockItem(10, 16, 130, 20);
        
        public int slotID;
        public int iconIndex;
        public int xPos;
        public int yPos;
    	
        private Slots(int slot, int iconIndex, int xPos, int yPos)
        {
            this.slotID = slot;
            this.iconIndex = iconIndex;
            this.xPos = xPos;
            this.yPos = yPos;
        }
    }
}
