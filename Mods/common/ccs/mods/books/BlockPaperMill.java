package ccs.mods.books;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class BlockPaperMill extends BlockContainer {

	public BlockPaperMill(int par1, int x) {
		super(par1, x, Material.iron);
		this.setCreativeTab(CreativeTabBooks.tabBooks);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int media) {
		if (side == 1)
			return 0;
		else if (side == 0)
			return 0;
		else if (side == media)
			return 2;
		else
			return 1;
	}

	@Override
	public int getBlockTextureFromSide(int side) {
		if (side == 1)
			return 0;
		else if (side == 0)
			return 0;
		else if (side == 2)
			return 2;
		else
			return 1;
	}

	@Override
	public TileEntity createNewTileEntity(World par1World) {
		return new TileEntityPaperMill();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		player.openGui(BookMod.instance, 0, world, x, y, z);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
	 */
	public int getBlockTexture(IBlockAccess block, int x, int y, int z, int side) {
		int rotation = block.getBlockMetadata(x, y, z);
		if (side == 1)
			return 0;
		else if (side == 0)
			return 0;
		else if (side == rotation) {
			TileEntityPaperMill mill = (TileEntityPaperMill) block
					.getBlockTileEntity(x, y, z);
			return mill.topSpecs[1] > 1 ? 3 : 2;
		} else
			return 1;
	}

	@Override
	public String getTextureFile() {
		return CommonProxy.ICONS_PNG;
	}

	@Override
	/**
	 * ejects contained items into the world, and notifies neighbours of an update, as appropriate
	 */
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		TileEntityPaperMill mill = (TileEntityPaperMill) world
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
			if (mill.topSpecs[4] > 100) {
				world.setBlock(x, y, z, Block.waterStill.blockID);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	public void randomDisplayTick(World world, int x, int y, int z,
			Random par5Random) {
		TileEntityPaperMill mill = (TileEntityPaperMill) world
				.getBlockTileEntity(x, y, z);

		if (mill.topSpecs[1] > 1) {
			int var6 = world.getBlockMetadata(x, y, z);
			float var7 = x + 0.5F;
			float var8 = y + 0.0F + par5Random.nextFloat() * 6.0F / 16.0F;
			float var9 = z + 0.5F;
			float var10 = 0.52F;
			float var11 = par5Random.nextFloat() * 0.6F - 0.3F;

			if (var6 == 4) {
				world.spawnParticle("smoke", var7 - var10, var8, var9 + var11,
						0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", var7 - var10, var8, var9 + var11,
						0.0D, 0.0D, 0.0D);
			} else if (var6 == 5) {
				world.spawnParticle("smoke", var7 + var10, var8, var9 + var11,
						0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", var7 + var10, var8, var9 + var11,
						0.0D, 0.0D, 0.0D);
			} else if (var6 == 2) {
				world.spawnParticle("smoke", var7 + var11, var8, var9 - var10,
						0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", var7 + var11, var8, var9 - var10,
						0.0D, 0.0D, 0.0D);
			} else if (var6 == 3) {
				world.spawnParticle("smoke", var7 + var11, var8, var9 + var10,
						0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", var7 + var11, var8, var9 + var10,
						0.0D, 0.0D, 0.0D);
			}
		}
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLiving par5EntityLiving) {
		int var6 = MathHelper
				.floor_double(par5EntityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		if (var6 == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2);
		}

		if (var6 == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5);
		}

		if (var6 == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3);
		}

		if (var6 == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4);
		}
	}
}
