package ccs.mods.armor;

import java.util.EnumSet;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.PlayerControllerMP;
import net.minecraft.src.World;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class InventoryTick implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer player = (EntityPlayer) tickData[0];
		if(!(player.inventory instanceof InventorySave)){
			System.out.println("New player has joined Expanding Inventory...");
			NBTTagList tag = new NBTTagList();
			player.inventory.writeToNBT(tag);
			player.inventory = new InventorySave(player);
			player.inventory.readFromNBT(tag);
		}
		if(!(player.inventorySlots instanceof ContainerInventory)){
			System.out.println("Now Updating to ContainerInventory...");
			ContainerInventory newCont = new ContainerInventory(player);
			player.inventorySlots = newCont;
			player.craftingInventory = newCont;
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "UpdatePlayerInventory";
	}

}
