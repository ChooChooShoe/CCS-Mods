package ccs.mods.armor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ccs.mods.armor.client.OpenInventoryKey;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagString;
import net.minecraft.src.StatCollector;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

@Mod(modid = "ccsEquipment", name = "Equipment and Armor", version = "1.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {
		"eq|base"}, packetHandler = ServerHandler.class)
public class EquipmentMod {
	@SidedProxy(clientSide = "ccs.mods.armor.client.ClientProxy", serverSide = "ccs.mods.armor.CommonProxy")
	public static CommonProxy proxy;
	@Instance("ccsEquipment")
	public static EquipmentMod instance = new EquipmentMod();
	private static Configuration config;
	
	public static ArrayList<ItemArmorExtended> armors = new ArrayList<ItemArmorExtended>();

	
	@Init
	public void loadMod(FMLInitializationEvent event) {
		EnumHelper.addArmorMaterial("NULL", 1, new int[] {0, 0, 0, 0}, 0);
		System.out.println("LOADING MY MOD");
		Map<EnumArmorMaterial, String> matsMap = new HashMap();
		for(EnumArmorMaterial mat : EnumArmorMaterial.values()){
			char[] string = mat.toString().toLowerCase().toCharArray();
			string[0] = Character.toUpperCase(string[0]);
			matsMap.put(mat, String.copyValueOf(string));
		}
		proxy.registerRenderInformation();
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		LanguageRegistry lang = LanguageRegistry.instance();
		lang.addStringLocalization("tool.diamond", "Diamond");
		lang.addStringLocalization("tool.gold", "Gold");
		lang.addStringLocalization("tool.iron", "Iron");
		lang.addStringLocalization("tool.cloth", "Cloth");
		lang.addStringLocalization("tool.chain", "Chain");
		int baseID = 1200;
		for(Item item : Item.itemsList){
			if(item instanceof ItemArmor)
				item = null;
		}
		for(EnumEquipment equip : EnumEquipment.values()){
			for(EnumMaterial armor : EnumMaterial.values()){
				baseID++;
				if(equip.craftGrid != null && !equip.isVanila){
					int id = config.getItem(armor.toString().toLowerCase() + "."+ equip.toString(), baseID).getInt();
					ItemArmorExtended item = new ItemArmorExtended(id, armor, equip);
					item.setItemName(armor.name() + "."+ equip.name());
					LanguageRegistry.addName(item, matsMap.get(armor) + " @ " + equip.title);
					armors.add(item);
					GameRegistry.addRecipe(new ItemStack(item), new Object[] {equip.craftGrid, 'X', Item.itemsList[armor.craftID]});
				}
				else if( armor == EnumMaterial.IRON ){
				}
			}
		}
		for(int id = Item.helmetLeather.shiftedIndex ; id <= Item.bootsGold.shiftedIndex; id++){
			Item oldI = Item.itemsList[id];
			ItemArmorExtended newI = null;
			if(oldI instanceof ItemArmor){
				ItemArmor armor = (ItemArmor)oldI;
				newI = new ItemArmorExtended(id, EnumMaterial.getEnumFromArmor(armor.getArmorMaterial()), EnumEquipment.getEnumFromSlotType(armor.armorType));
				newI.setIconIndex(armor.getIconFromDamage(0));
				newI.setTextureFile("/gui/items.png");
				newI.setItemName(armor.getItemName().replaceFirst("item.", ""));
				Item.itemsList[id] = newI;
				System.out.println("RE-Created Item " + new ItemStack(armor).toString());
				System.out.println(Item.helmetLeather);
			}
		}
		TickRegistry.registerTickHandler(new InventoryTick(), Side.CLIENT);
		TickRegistry.registerTickHandler(new InventoryTick(), Side.SERVER);
		config.save();
	}
	@PreInit
	public void PreLoadMod(FMLPreInitializationEvent evt) {
		config = new Configuration(evt.getSuggestedConfigurationFile());
		config.load();
	}

	@PostInit
	public void PostLoadMod(FMLPostInitializationEvent evt) {
	}
}
