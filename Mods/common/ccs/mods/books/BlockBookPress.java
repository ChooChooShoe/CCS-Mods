package ccs.mods.books;

import java.util.Random;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockBookPress extends BlockContainer {

	public BlockBookPress(int par1) {
		super(par1, 16, net.minecraft.src.Material.iron);
		this.setCreativeTab(CreativeTabBooks.tabBooks);
	}

	@Override
	public int getBlockTextureFromSide(int par1) {
		return par1 <= 1 ? 17 : 16;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		player.openGui(BookMod.instance, 5, world, x, y, z);
		return true;
	}

	@Override
	public String getTextureFile() {
		return CommonProxy.ICONS_PNG;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileEntityBookPress();
	}

	@Override
	/**
	 * ejects contained items into the world, and notifies neighbours of an update, as appropriate
	 */
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		TileEntityBookPress mill = (TileEntityBookPress) world
				.getBlockTileEntity(x, y, z);

		if (mill != null) {
			for (int var8 = 0; var8 < mill.getSizeInventory(); ++var8) {
				ItemStack var9 = mill.getStackInSlot(var8);

				if (var9 != null) {
					Random rand = new Random();
					float var10 = rand.nextFloat() * 0.8F + 0.1F;
					float var11 = rand.nextFloat() * 0.8F + 0.1F;
					float var12 = rand.nextFloat() * 0.8F + 0.1F;

					while (var9.stackSize > 0) {
						int var13 = rand.nextInt(21) + 10;

						if (var13 > var9.stackSize) {
							var13 = var9.stackSize;
						}

						var9.stackSize -= var13;
						EntityItem item = new EntityItem(world, x + var10, y
								+ var11, z + var12, new ItemStack(var9.itemID,
										var13, var9.getItemDamage()));

						if (var9.hasTagCompound()) {
							item.item.setTagCompound((NBTTagCompound) var9
									.getTagCompound().copy());
						}

						float var15 = 0.05F;
						item.motionX = (float) rand.nextGaussian() * var15;
						item.motionY = (float) rand.nextGaussian() * var15
								+ 0.2F;
						item.motionZ = (float) rand.nextGaussian() * var15;
						world.spawnEntityInWorld(item);
					}
				}
			}
		}
	}
}
