package ccs.mods.books;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {

	public static final String ICONS_PNG = "/ccs/res/books/icons.png";

	public void registerRenderInformation() {
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		Container jar = null;
		if (entity != null) {
			if (ID == 0) {
				jar = new ContainerPaperMill(player,
						(TileEntityPaperMill) entity);
			}
			if (ID == 1) {
				jar = new ContainerBookshelf(player,
						(TileEntityBookshelf) entity);
			}
			if (ID == 5) {
				jar = new ContainerBookPress(player,
						(TileEntityBookPress) entity);
			}
		} else {
			if (ID == 2) {
				jar = null;
			}

		}
		return jar;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}

}
