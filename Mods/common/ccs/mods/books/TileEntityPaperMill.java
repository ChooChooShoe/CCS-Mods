package ccs.mods.books;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityFurnace;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

import com.google.common.io.ByteArrayDataInput;

public class TileEntityPaperMill extends TileEntity implements IInventory,
ISidedInventory {

	/**
	 * millWaterLvl 0 to 500, millPowerTime, fuelPowerTime, pulpWoodTime,
	 * pulpItemTime
	 */
	public int[] topSpecs = new int[5];

	/**
	 * The ItemStacks that hold the items currently stored in the shelf
	 */
	private ItemStack[] ItemStacks = new ItemStack[20];

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return this.ItemStacks.length;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int par1) {
		return this.ItemStacks[par1];
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if (this.ItemStacks[par1] != null) {
			ItemStack var3;

			if (this.ItemStacks[par1].stackSize <= par2) {
				var3 = this.ItemStacks[par1];
				this.ItemStacks[par1] = null;
				return var3;
			} else {
				var3 = this.ItemStacks[par1].splitStack(par2);

				if (this.ItemStacks[par1].stackSize == 0) {
					this.ItemStacks[par1] = null;
				}

				return var3;
			}
		} else
			return null;
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (this.ItemStacks[par1] != null) {
			ItemStack var2 = this.ItemStacks[par1];
			this.ItemStacks[par1] = null;
			return var2;
		} else
			return null;
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		this.ItemStacks[par1] = par2ItemStack;

		if (par2ItemStack != null
				&& par2ItemStack.stackSize > this.getInventoryStackLimit()) {
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	/**
	 * Returns the name of the inventory.
	 */
	@Override
	public String getInvName() {
		return "Bookshelf";
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.topSpecs[2] = par1NBTTagCompound.getInteger("topSpecs[2]");
		this.topSpecs[1] = par1NBTTagCompound.getInteger("topSpecs[1]");
		this.topSpecs[0] = par1NBTTagCompound.getInteger("topSpecs[0]");
		this.topSpecs[4] = par1NBTTagCompound.getInteger("topSpecs[4]");
		this.topSpecs[3] = par1NBTTagCompound.getInteger("topSpecs[3]");
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.ItemStacks = new ItemStack[20];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.ItemStacks.length) {
				this.ItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("topSpecs[2]", this.topSpecs[2]);
		par1NBTTagCompound.setInteger("topSpecs[1]", this.topSpecs[1]);
		par1NBTTagCompound.setInteger("topSpecs[0]", this.topSpecs[0]);
		par1NBTTagCompound.setInteger("topSpecs[4]", this.topSpecs[4]);
		par1NBTTagCompound.setInteger("topSpecs[3]", this.topSpecs[3]);

		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.ItemStacks.length; ++var3) {
			if (this.ItemStacks[var3] != null) {
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.ItemStacks[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be
	 * 64, possibly will be extended. *Isn't this more of a set than a get?*
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public float light = topSpecs[1] > 0 ? 1.0F : 0.0F;
	public boolean fireOn = this.topSpecs[1] > 0;

	/**
	 * Allows the entity to update its state. Overridden in most subclasses,
	 * e.g. the mob spawner uses this to count ticks and creates a new spawn
	 * inside its implementation.
	 */
	@Override
	public void updateEntity() {
		fireOn = this.topSpecs[1] > 0;
		if (this.getBlockType().getLightValue(worldObj, xCoord, yCoord, zCoord) != light) {
			this.getBlockType().setLightValue(light);
			worldObj.updateAllLightTypes(xCoord, yCoord, zCoord);
		}
		boolean didCook = false;

		if (fireOn) {
			--this.topSpecs[1];
		}
		if (!this.worldObj.isRemote) {
			if (this.topSpecs[0] <= 400) {
				ItemStack water = this.ItemStacks[19];
				if (water != null
						&& water.itemID == Item.bucketWater.shiftedIndex) {
					topSpecs[0] += MathHelper.getRandomIntegerInRange(
							new Random(), 190, 220);
					if (topSpecs[0] > 500) {
						topSpecs[0] = 500;
					}
					this.setInventorySlotContents(19, new ItemStack(
							Item.bucketEmpty));
					didCook = true;
				}
			}
			if (topSpecs[1] == 0 && canCook()) {
				this.topSpecs[2] = this.topSpecs[1] = TileEntityFurnace
						.getItemBurnTime(ItemStacks[18]);

				if (fireOn) {
					didCook = true;

					ItemStack fuel = this.ItemStacks[18];
					if (fuel != null) {
						--fuel.stackSize;

						if (fuel.stackSize == 0) {
							fuel = fuel.getItem().getContainerItemStack(fuel);
						}
					}
				}
			}
			if (fireOn && canCook() && this.topSpecs[0] > 10) {
				if (topSpecs[3] < 0) {
					this.topSpecs[3] = this.topSpecs[4] = TileEntityPaperMill
							.getItemToPulpAmount(ItemStacks[getWoodSlot()]);
				}
				this.topSpecs[3]--;

				if (this.topSpecs[3] == 0) {
					int slot = getEmptyPaperSlot();
					int woodSlot = getWoodSlot();
					if (this.ItemStacks[slot] == null) {
						this.ItemStacks[slot] = new ItemStack(Item.paper);
						this.topSpecs[3] = this.topSpecs[4] = TileEntityPaperMill
								.getItemToPulpAmount(ItemStacks[woodSlot]);
					} else {
						this.ItemStacks[slot].stackSize++;
						this.topSpecs[3] = this.topSpecs[4] = TileEntityPaperMill
								.getItemToPulpAmount(ItemStacks[woodSlot]);
					}
					this.ItemStacks[woodSlot].stackSize--;
					this.topSpecs[0] -= getItemToPulpAmount(ItemStacks[woodSlot]) / 50;

					if (this.ItemStacks[woodSlot].stackSize <= 0) {
						this.ItemStacks[woodSlot] = null;
					}
				}
			}
		}
		if (didCook) {
			this.onInventoryChanged();
		}
	}

	/**
	 * Returns true if the furnace can smelt an item, i.e. has a source item,
	 * destination stack isn't full, etc.
	 */
	public boolean canCook() {
		return getWoodSlot() >= 0 && getEmptyPaperSlot() > 0;
	}

	/** Returns the first slot witch paper can go in or -1 */
	public int getEmptyPaperSlot() {
		for (int slot = 9; slot < 17; slot++) {
			ItemStack stack = this.ItemStacks[slot];
			if (stack == null)
				return slot;
			else if (stack.itemID == Item.paper.shiftedIndex) {
				if (stack.stackSize < stack.getMaxStackSize())
					return slot;
			}
		}
		return -1;
	}

	/** Returns the first slot with wood/reeds or -1 */
	public int getWoodSlot() {
		for (int slot = 0; slot < 9; slot++) {
			ItemStack stack = this.ItemStacks[slot];
			if (getItemToPulpAmount(stack) > 0)
				return slot;
		}
		return -1;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord,
				this.zCoord) != this ? false
						: par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D,
								this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	public static int getItemToPulpAmount(ItemStack stack) {
		if (stack == null)
			return -1;
		else {
			Item item = stack.getItem();
			int index = item.shiftedIndex;

			if (item instanceof ItemBlock && Block.blocksList[index] != null) {
				Block block = Block.blocksList[index];

				if (block == Block.woodSingleSlab)
					return 200;
				if (block == Block.planks)
					return 150;
				if (block == Block.wood)
					return 100;

				if (block.blockMaterial == Material.wood)
					return 150;
			}
			if (index == Item.stick.shiftedIndex)
				return 200;
			if (index == Item.reed.shiftedIndex)
				return 40;
			return -1;
		}
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	@Override
	public int getStartInventorySide(ForgeDirection side) {
		if (side == ForgeDirection.DOWN)
			return 1;
		if (side == ForgeDirection.UP)
			return 0;
		return 2;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 1;
	}

	public void handlePacketData(ByteArrayDataInput data) {

	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData data) {
		System.out.println("Got Packet" + data);
		this.readFromNBT(data.customParam1);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound var1 = new NBTTagCompound();
		this.writeToNBT(var1);
		return new Packet132TileEntityData(this.xCoord, this.yCoord,
				this.zCoord, 7, var1);
	}
}
