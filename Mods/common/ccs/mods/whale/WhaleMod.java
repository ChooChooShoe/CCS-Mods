package ccs.mods.whale;

import ccs.mods.whale.client.ModelWhale;
import ccs.mods.whale.client.RenderHarpoon;
import ccs.mods.whale.client.RenderWhale;
import net.minecraft.src.Block;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;


@Mod( modid = "ccsWhaleMod", name="Whale Mod", version="1.0.0" )
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {
		"WhaleMod1" }, packetHandler = PacketHandler.class)
public class WhaleMod
{
	@Instance(value = "WhaleMod")
	public static WhaleMod instance;
	private static Configuration config;

	// public static final Block IronPumpkin = (new BlockPumpkin(231, 32, false)).setBlockName("IronPumpkin").setHardness(5.0F).setResistance(10.0F);
	/*public static final Block IronPumpkinON = (new BlockIronPumpkin(232, 32, true)).setBlockName("IronPumpkinON").setHardness(5.0F).setResistance(10.0F).setLightValue(1.0F);
    public static final Block NetherPumpkin = (new BlockNetherPumpkin(234, 16, false)).setBlockName("NetherPumpkin").setHardness(2.0F).setResistance(10.0F);
    public static final Block NetherPumpkinON = (new BlockNetherPumpkin(235, 16, true)).setBlockName("NetherPumpkinON").setHardness(1.5F).setResistance(10.0F).setLightValue(0.7F);
    public static final Block WoodPumpkin = (new BlockWoodPumpkin(237, 0, false)).setBlockName("WoodPumpkin").setHardness(1.0F).setResistance(5.0F);
    public static final Block WoodPumpkinON = (new BlockWoodPumpkin(238, 0, true)).setBlockName("WoodPumpkinON").setHardness(0.5F).setResistance(5.0F).setLightValue(1.0F);*/
	public static Item harpoonGun;
	public static Item harpoonD;
	public static Item harpoonG;
	public static Item harpoonI;
	public static Item harpoonS;
	public static Item harpoonW;
	public static Item scubaHead;
	public static Item scubaChest;
	public static Item scubaLegs;
	public static Item scubaFlippers;

	@Init
	public void loadMod(FMLInitializationEvent event)
	{
		//this.instance = new ModGolem();
		/*LanguageRegistry lang = LanguageRegistry.instance();
        lang.addStringLocalization("entity.Wood_Golem.name", "en_US", TransByKey("golem.wood", "Wood Goelm"));
        lang.addStringLocalization("entity.Nether_Golem.name", "en_US", TransByKey("golem.nether", "Nether Golem"));
        lang.addStringLocalization("entity.Giant.name", "en_US", TransByKey("golem.nether", "Giant"));
        LanguageRegistry.addName(IronRing, TransByKey("Item.IronRing", "Iron Ring"));
        LanguageRegistry.addName(GoldRing, TransByKey("Item.GoldRing", "Gold Ring"));
        LanguageRegistry.addName(lapisRing, TransByKey("Item.lapisRing", "lapis Ring"));
        LanguageRegistry.addName(EmeraldRing, TransByKey("Item.EmeraldRing", "Emerald Ring"));
        LanguageRegistry.addName(DiamondRing,   TransByKey("Item.DiamondRing", "Diamond Ring"));
        LanguageRegistry.addName(IronPumpkin, TransByKey("Block.IronPumpkin", "Steel Pumpkin"));
        LanguageRegistry.addName(NetherPumpkin, TransByKey("Block.NetherPumpkin", "Neth \'O\' Lantern"));
        LanguageRegistry.addName(WoodPumpkin, TransByKey("Block.WoodPumpkin", "Carved CraftingTable"));
        LanguageRegistry.addName(IronPumpkinON,TransByKey("Block.IronPumpkinON", "Steel Pumpkin"));
        LanguageRegistry.addName(NetherPumpkinON, TransByKey("Block.NetherPumpkinON", "Neth \'O\' Lantern"));
        LanguageRegistry.addName(WoodPumpkinON, TransByKey("Block.WoodPumpkinON", "Carved CraftingTable"));*/
		//GameRegistry.registerBlock(IronPumpkin);
		harpoonGun = new ItemHarpoonGun(2099, 0).setItemName("harpoonGun");
		harpoonD = new ItemHarpoon(config.getItem("DiamondHarpoonID", 2100).getInt(), EnumToolMaterial.EMERALD, 1);
		harpoonG = new ItemHarpoon(config.getItem("GoldHarpoonID", 2101).getInt(), EnumToolMaterial.GOLD, 2);
		harpoonI = new ItemHarpoon(config.getItem("IronHarpoonID", 2102).getInt(), EnumToolMaterial.IRON, 3);
		harpoonS = new ItemHarpoon(config.getItem("StoneHarpoonID", 2103).getInt(), EnumToolMaterial.STONE, 4);
		harpoonW = new ItemHarpoon(config.getItem("WoodHarpoonID", 2104).getInt(), EnumToolMaterial.WOOD, 5);
		scubaHead = new ItemScubaGear(config.getItem("ScubaHeadID", 2105).getInt(), 0, 0);
		scubaChest = new ItemScubaGear(config.getItem("ScubaChestID", 2106).getInt(), 1, 16);
		scubaLegs = new ItemScubaGear(config.getItem("ScubaLegsID", 2017).getInt(), 2, 32);
		scubaFlippers = new ItemScubaGear(config.getItem("ScubaFlippersID", 2108).getInt(), 3, 48);

		LanguageRegistry.addName(harpoonGun, "Harpoon Gun");
		LanguageRegistry.addName(harpoonD, "Diamond Harpoon");
		LanguageRegistry.addName(harpoonG, "Golden Harpoon");
		LanguageRegistry.addName(harpoonI, "Iron Harpoon");
		LanguageRegistry.addName(harpoonS, "Stone Harpoon");
		LanguageRegistry.addName(harpoonW, "Wood Harpoon");
		LanguageRegistry.addName(scubaHead, "Snorkal");
		LanguageRegistry.addName(scubaChest, "WetSuit Jacket");
		LanguageRegistry.addName(scubaLegs, "WetSuit Pants");
		LanguageRegistry.addName(scubaFlippers, "Flippers");

		GameRegistry.addRecipe(new ItemStack(harpoonD, 1), new Object[] {"  I", " L ", "L  ", 'I', Item.diamond, 'L', Item.stick});
		GameRegistry.addRecipe(new ItemStack(harpoonG, 1), new Object[] {"  I", " L ", "L  ", 'I', Item.ingotGold, 'L', Item.stick});
		GameRegistry.addRecipe(new ItemStack(harpoonI, 1), new Object[] {"  I", " L ", "L  ", 'I', Item.ingotIron, 'L', Item.stick});
		GameRegistry.addRecipe(new ItemStack(harpoonS, 1), new Object[] {"  I", " L ", "L  ", 'I', Block.cobblestone, 'L', Item.stick});
		GameRegistry.addRecipe(new ItemStack(harpoonW, 1), new Object[] {"  I", " L ", "L  ", 'I', Block.planks, 'L', Item.stick});
		GameRegistry.addRecipe(new ItemStack(scubaHead, 1), new Object[] {"RRR", "R R", 'R', Block.planks,});
		GameRegistry.addRecipe(new ItemStack(scubaChest, 1), new Object[] {"R R", "RRR", "RRR", 'R', Block.planks,});
		GameRegistry.addRecipe(new ItemStack(scubaLegs, 1), new Object[] {"RRR", "R R", "R R", 'R', Block.planks,});
		GameRegistry.addRecipe(new ItemStack(scubaFlippers, 1), new Object[] {"R R", "R R", 'R', Block.planks,});
		GameRegistry.addRecipe(new ItemStack(harpoonGun, 1), new Object[] {"III", "  L", 'I', Item.ingotIron, 'L', Item.stick});

		EntityRegistry.registerGlobalEntityID(EntityHarpoon.class, "Flying_Harpoon", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerGlobalEntityID(EntityWhale.class, "Whale", EntityRegistry.findGlobalUniqueEntityId(), 0x334455, 0x757365);
		/*EntityRegistry.registerModEntity(EntityWoodGolem.class, "Wood_Golem", 1, this, 250, 5, false);
    	EntityRegistry.registerModEntity(EntityNetherGolem.class, "Nether_Golem", 2, this, 250, 5, false);
    	EntityRegistry.registerModEntity(EntityGiant.class, "Giant", 3, this, 250, 5, false);
    	EntityRegistry.registerModEntity(EntityFlameJet.class, "Flames", 4, this, 250, 5, false);
    	EntityRegistry.registerGlobalEntityID(EntityWoodGolem.class, "Wood_Golem2", EntityRegistry.findGlobalUniqueEntityId(), 0x64FF60, 0x44FF40);
    	EntityRegistry.registerGlobalEntityID(EntityNetherGolem.class, "Nether_Golem2", EntityRegistry.findGlobalUniqueEntityId(), 0x000000, 0xFF0000);
    	EntityRegistry.registerGlobalEntityID(EntityGiant.class, "Giant2", EntityRegistry.findGlobalUniqueEntityId(), 0x223344, 0xFF0044);
    	EntityRegistry.registerGlobalEntityID(EntityFlameJet.class, "Flames2", EntityRegistry.findGlobalUniqueEntityId());*/
		config.save();
	}
	@PreInit
	public void PreLoadMod(FMLPreInitializationEvent evt) {
		config = new Configuration(evt.getSuggestedConfigurationFile());

		config.load();
	}

	@PostInit
	public void PostLoadMod(FMLPostInitializationEvent evt) {}
}
