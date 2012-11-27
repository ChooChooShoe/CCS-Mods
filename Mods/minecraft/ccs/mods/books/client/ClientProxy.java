package ccs.mods.books.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.TextureFXManager;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import ccs.mods.books.CommonProxy;
import ccs.mods.books.TileEntityBookPress;
import ccs.mods.books.TileEntityBookshelf;
import ccs.mods.books.TileEntityPaperMill;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderInformation()  {
		MinecraftForgeClient.preloadTexture(ICONS_PNG);
	}
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		TileEntity entity = world.getBlockTileEntity(x, y, z);
		GuiScreen gui = null;
		if (entity != null) {
			if(ID == 0) {
				gui = new GuiPaperMill(player, (TileEntityPaperMill)entity);
			}
			if(ID == 1) {
				gui = new GuiBookshelf(player, (TileEntityBookshelf)entity);
			}
			if(ID == 5) {
				gui = new GuiBookPress(player, (TileEntityBookPress)entity);
			}
		}
		else{
			if(ID == 2) {
				gui = new GuiScreenWriteable(player, player.inventory.getCurrentItem(), x == 1);
			}
			if(ID == 3) {
				gui = new GuiScreenScroll(player, player.inventory.getCurrentItem(), x == 1);
			}
			if(ID == 4) {
				gui = new GuiRainbowBooks(player, player.inventory.getCurrentItem(), x == 1);
			}
		}
		return gui;
	}
}
