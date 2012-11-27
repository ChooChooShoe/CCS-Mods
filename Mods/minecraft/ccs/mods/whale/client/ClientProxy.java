package ccs.mods.whale.client;

import ccs.mods.whale.CommonProxy;
import ccs.mods.whale.EntityHarpoon;
import ccs.mods.whale.EntityWhale;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.TextureFXManager;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderInformation()  {
		MinecraftForgeClient.preloadTexture(ICONS_PNG);
		RenderingRegistry.registerEntityRenderingHandler(EntityHarpoon.class, new RenderHarpoon());
		RenderingRegistry.registerEntityRenderingHandler(EntityWhale.class, new RenderWhale(new ModelWhale(1.0F, 0.0F), 0.5F));
	}
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		TileEntity entity = world.getBlockTileEntity(x, y, z);
		GuiScreen gui = null;
		if (entity != null) {
		}
		else{
		}
		return gui;
	}
}
