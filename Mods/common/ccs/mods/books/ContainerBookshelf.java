package ccs.mods.books;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerBookshelf extends Container {

	public class SlotBook extends Slot {

		public SlotBook(IInventory par1iInventory, int par2, int par3, int par4) {
			super(par1iInventory, par2, par3, par4);
		}

		@Override
		public int getSlotStackLimit() {
			return 64;
		}

		@Override
		public boolean isItemValid(ItemStack item) {
			return ContainerBookshelf.isValid(item);
		}

	}

	private TileEntityBookshelf shelf;

	public ContainerBookshelf(EntityPlayer player, TileEntityBookshelf mill) {
		this.shelf = mill;
		int var3, var4;

		/* 0 - 11 THE 12 books */
		for (var3 = 0; var3 < 3; ++var3) {
			for (var4 = 0; var4 < 4; ++var4) {
				this.addSlotToContainer(new SlotBook(mill, var4 + var3 * 4,
						48 + var4 * 18, 17 + var3 * 18));
			}
		}
		for (var3 = 0; var3 < 3; ++var3) {
			for (var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new Slot(player.inventory, var4 + var3
						* 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}
		for (var3 = 0; var3 < 9; ++var3) {
			this.addSlotToContainer(new Slot(player.inventory, var3,
					8 + var3 * 18, 142));
		}
	}

	public static boolean isValid(ItemStack item) {
		int id = item.itemID;
		return id == Item.book.shiftedIndex
				|| id == Item.writableBook.shiftedIndex
				|| id == Item.writtenBook.shiftedIndex
				|| id == BookMod.scrollSigned.shiftedIndex
				|| id == BookMod.scrollWriting.shiftedIndex
				|| id == BookMod.bookWriting.shiftedIndex
				|| id == BookMod.bookSigned.shiftedIndex
				|| id == BookMod.bookColour.shiftedIndex;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.shelf.isUseableByPlayer(player);
	}

	@Override
	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift clicking.
	 */
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
		ItemStack Stack = null;
		Slot clickedSlot = (Slot) this.inventorySlots.get(slotID);

		if (clickedSlot != null && clickedSlot.getHasStack()) {
			ItemStack clickedStack = clickedSlot.getStack();
			Stack = clickedStack.copy();

			if (slotID >= 0 && slotID <= 11) {
				if (!this.mergeItemStack(clickedStack, 12, 48, false))
					return null;
			} else if (isValid(clickedStack)) {
				if (!this.mergeItemStack(clickedStack, 0, 12, false))
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
