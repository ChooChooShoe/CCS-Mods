package ccs.mods.books;

import java.util.Iterator;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.Slot;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class ContainerBookPress extends Container {

	private TileEntityBookPress press;
	private int printTime = 0;

	public ContainerBookPress(EntityPlayer player, TileEntityBookPress press) {

		int var3, var4;
		this.press = press;
		// origanal
		this.addSlotToContainer(new Slot(press, 0, 37, 35));
		// inksack
		this.addSlotToContainer(new Slot(press, 1, 71, 17));
		// books
		this.addSlotToContainer(new Slot(press, 2, 71, 53));
		for (var3 = 0; var3 < 4; ++var3) {
			this.addSlotToContainer(new Slot(press, var3 + 3, 124,
					8 + var3 * 18));
			this.addSlotToContainer(new Slot(press, var3 + 7, 142,
					8 + var3 * 18));
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

	@Override
	public void addCraftingToCrafters(ICrafting caft) {
		super.addCraftingToCrafters(caft);
		caft.updateCraftingInventoryInfo(this, 0, this.press.printTime);
	}

	@Override
	public void updateCraftingResults() {
		super.updateCraftingResults();
		Iterator var1 = this.crafters.iterator();

		while (var1.hasNext()) {
			ICrafting var2 = (ICrafting) var1.next();

			if (this.printTime != this.press.printTime) {
				var2.updateCraftingInventoryInfo(this, 0, this.press.printTime);
			}
		}

		this.printTime = this.press.printTime;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		this.press.printTime = par2;
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}

}
