package ccs.mods.armor.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.TextureFXManager;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.registry.TickRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.RenderManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends ccs.mods.armor.CommonProxy {
	@Override
	public void registerRenderInformation()  {
		MinecraftForgeClient.preloadTexture(ICONS_PNG);
		TickRegistry.registerTickHandler(new OpenInventoryKey(), Side.CLIENT);
		/** Replaces the old Player Render With The Mod one*/
		RenderManager custom = RenderManager.instance;
		custom.entityRenderMap.remove(EntityPlayer.class);
		RenderPlayerArmor render = new RenderPlayerArmor();
		custom.entityRenderMap.put(EntityPlayer.class, render);
		render.setRenderManager(custom);
		System.out.println("Remade RenderPlayer");
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return new GuiPlayerInventory(player);
	}
}
