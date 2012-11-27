package ccs.mods.books;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;

public class TileEntityBookPress extends TileEntity implements IInventory {

	/**
	 * chand to 11
	 */
	private ItemStack[] ItemStacks = new ItemStack[16];

	@Override
	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory() {
		return this.ItemStacks.length;
	}

	@Override
	/**
	 * Returns the stack in slot i
	 */
	public ItemStack getStackInSlot(int par1) {
		return this.ItemStacks[par1];
	}

	@Override
	/**
	 * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
	 * new stack.
	 */
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

	@Override
	/**
	 * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
	 * like when you close a workbench GUI.
	 */
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (this.ItemStacks[par1] != null) {
			ItemStack var2 = this.ItemStacks[par1];
			this.ItemStacks[par1] = null;
			return var2;
		} else
			return null;
	}

	@Override
	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		this.ItemStacks[par1] = par2ItemStack;

		if (par2ItemStack != null
				&& par2ItemStack.stackSize > this.getInventoryStackLimit()) {
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	/**
	 * Returns the name of the inventory.
	 */
	public String getInvName() {
		return "BookPress";
	}

	@Override
	/**
	 * Reads a tile entity from NBT.
	 */
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.ItemStacks = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.ItemStacks.length) {
				this.ItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
	}

	@Override
	/**
	 * Writes a tile entity to NBT.
	 */
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
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

	@Override
	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
	 * this more of a set than a get?*
	 */
	public int getInventoryStackLimit() {
		return 64;
	}

	public int printTime = 0;

	@Override
	/**
	 * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
	 * ticks and creates a new spawn inside its implementation.
	 */
	public void updateEntity() {
		if (!this.worldObj.isRemote) {
			if (ItemStacks[0] != null
					&& ItemStacks[1] != null
					&& ItemStacks[1].isItemEqual(new ItemStack(Item.dyePowder,
							1, 0)) && ItemStacks[2] != null
							&& getFirstReturnSlotID() != -1) {
				if (ItemStacks[0].itemID == BookMod.bookSigned.shiftedIndex && ItemStacks[2].itemID == BookMod.bookColour.shiftedIndex
						|| ItemStacks[0].itemID == BookMod.scrollSigned.shiftedIndex && ItemStacks[2].itemID == Item.paper.shiftedIndex) {

					printTime++;
					if (printTime >= 250 && getFirstReturnSlotID() != -1) {
						int slot = getFirstReturnSlotID();
						ItemStacks[slot] = ItemStacks[0].copy();
						ItemStacks[slot].setItemDamage(ItemStacks[2]
								.getItemDamage());
						ItemStacks[slot].getTagCompound().setBoolean("IScopy",
								true);

						decrStackSize(1, 1);
						decrStackSize(2, 2);
						
						
					}
				} else if (printTime != 0) {
					if (printTime-- < 0) {
						printTime = 0;
					}
				}
			} else if (printTime != 0) {
				if (printTime-- < 0) {
					printTime = 0;
				}
			}
		}
	}

	public int getFirstReturnSlotID() {
		for (int i = 3; i < 11; i++) {
			if (this.ItemStacks[i] == null)
				return i;
		}
		return -1;
	}

	@Override
	/**
	 * Do not make give this method the name canInteractWith because it clashes with Container
	 */
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord,
				this.zCoord) != this ? false
						: par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D,
								this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}
}
