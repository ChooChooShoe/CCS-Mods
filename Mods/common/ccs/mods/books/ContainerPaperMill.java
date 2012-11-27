package ccs.mods.books;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.TileEntityFurnace;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class ContainerPaperMill extends Container {

	private TileEntityPaperMill tepMill;
	private int[] lastSpecs = new int[5];

	public ContainerPaperMill(EntityPlayer player, TileEntityPaperMill mill) {
		this.tepMill = mill;
		int var3, var4;

		/* 0 - 8 Wood item that become paper */
		for (var3 = 0; var3 < 3; ++var3) {
			for (var4 = 0; var4 < 3; ++var4) {
				this.addSlotToContainer(new Slot(mill, var4 + var3 * 3,
						8 + var4 * 18, 17 + var3 * 18));
			}
		}
		/* 9 - 17 paper */
		for (var3 = 0; var3 < 3; ++var3) {
			for (var4 = 0; var4 < 3; ++var4) {
				this.addSlotToContainer(new Slot(mill, var4 + var3 * 3 + 9,
						91 + var4 * 18, 17 + var3 * 18));
			}
		}
		/* 18 Fuels */
		this.addSlotToContainer(new Slot(mill, 18, 67, 53));
		/* 19 WaterBucket */
		this.addSlotToContainer(new Slot(mill, 19, 152, 63));

		/* 20 - 38 Wood item that become paper */
		for (var3 = 0; var3 < 3; ++var3) {
			for (var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new Slot(player.inventory, var4 + var3
						* 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}
		/* 39 - 48 Hot bar */
		for (var3 = 0; var3 < 9; ++var3) {
			this.addSlotToContainer(new Slot(player.inventory, var3,
					8 + var3 * 18, 142));
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting caft) {
		super.addCraftingToCrafters(caft);
		for (int i = 0; i < this.tepMill.topSpecs.length; i++) {
			caft.updateCraftingInventoryInfo(this, i, this.tepMill.topSpecs[i]);
		}
	}

	@Override
	/**
	 * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
	 */
	public void updateCraftingResults() {
		super.updateCraftingResults();
		for (Object craft : this.crafters.toArray()) {
			for (int i = 0; i < this.tepMill.topSpecs.length
					&& craft instanceof ICrafting; i++) {
				if (this.lastSpecs[i] != this.tepMill.topSpecs[i]) {
					((ICrafting) craft).updateCraftingInventoryInfo(this, i,
							this.tepMill.topSpecs[i]);
				}
			}
		}
		for (int i = 0; i < this.tepMill.topSpecs.length; i++) {
			this.lastSpecs[i] = this.tepMill.topSpecs[i];
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		this.tepMill.topSpecs[par1] = par2;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tepMill.isUseableByPlayer(player);
	}

	@Override
	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift clicking.
	 */
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
		System.out.println("int slotID = " + slotID);
		ItemStack Stack = null;
		Slot clickedSlot = (Slot) this.inventorySlots.get(slotID);

		if (clickedSlot != null && clickedSlot.getHasStack()) {
			ItemStack clickedStack = clickedSlot.getStack();
			Stack = clickedStack.copy();

			if (slotID >= 0 && slotID <= 19) {
				if (!this.mergeItemStack(clickedStack, 20, 56, false))
					return null;
				clickedSlot.onSlotChange(clickedStack, Stack);
			} else {
				if (TileEntityPaperMill.getItemToPulpAmount(clickedStack) > 0) {
					if (!this.mergeItemStack(clickedStack, 0, 9, false))
						return null;
				} else if (TileEntityFurnace.isItemFuel(clickedStack)) {
					if (!this.mergeItemStack(clickedStack, 18, 19, false))
						return null;
				} else if (clickedStack.itemID == Item.bucketWater.shiftedIndex) {
					if (!this.mergeItemStack(clickedStack, 19, 20, false))
						return null;
				} else if (slotID >= 20 && slotID < 39) {
					if (!this.mergeItemStack(clickedStack, 39, 48, false))
						return null;
				} else if (slotID >= 30 && slotID < 39
						&& !this.mergeItemStack(clickedStack, 3, 30, false))
					return null;
			}
			if (clickedStack.stackSize == 0) {
				clickedSlot.putStack((ItemStack) null);
			} else {
				clickedSlot.onSlotChanged();
			}

			if (clickedStack.stackSize == Stack.stackSize)
				return null;

			clickedSlot.onPickupFromSlot(player, clickedStack);
		}

		return Stack;
	}

}
