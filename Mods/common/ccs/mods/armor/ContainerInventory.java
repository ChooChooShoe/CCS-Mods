package ccs.mods.armor;

import java.util.ArrayList;
import java.util.List;

import ccs.mods.armor.EnumEquipment.Slots;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Container;
import net.minecraft.src.ContainerPlayer;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryCraftResult;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.SlotCrafting;

public class ContainerInventory extends Container {

	public boolean isLocalWorld;
	public EntityPlayer thePlayer;
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
    public IInventory craftResult = new InventoryCraftResult();
    
	public ContainerInventory(EntityPlayer player) {
        this.isLocalWorld = !player.worldObj.isRemote;
        this.thePlayer = player;
        int var4;
        int var5;
        for(Slots type : Slots.values()) 
        	this.addSlotToContainer(new SlotArmor(player.inventory, type));
        this.addSlotToContainer(new SlotCrafting(player, this.craftMatrix, this.craftResult, 0, 152, 8));
        for (var4 = 0; var4 < 2; ++var4)
        {
            for (var5 = 0; var5 < 2; ++var5)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix, var5 + var4 * 2, 134 + var5 * 18, 43 + var4 * 18));
            }
        }
        for (var4 = 0; var4 < 3; ++var4)
        {
            for (var5 = 0; var5 < 9; ++var5)
            {
                this.addSlotToContainer(new Slot(player.inventory, var5 + (var4 + 1) * 9, 8 + var5 * 18, 84 + var4 * 18));
            }
        }

        for (var4 = 0; var4 < 9; ++var4)
        {
            this.addSlotToContainer(new Slot(player.inventory, var4, 8 + var4 * 18, 142));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
	}
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.thePlayer.worldObj));
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
    {
        super.onCraftGuiClosed(par1EntityPlayer);

        for (int var2 = 0; var2 < 4; ++var2)
        {
            ItemStack var3 = this.craftMatrix.getStackInSlotOnClosing(var2);

            if (var3 != null)
            {
                par1EntityPlayer.dropPlayerItem(var3);
            }
        }

        this.craftResult.setInventorySlotContents(0, (ItemStack)null);
    }
    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
    	System.out.println("Clicked SlotID = "+par2);
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(par2);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (par2 == 0)
            {
                if (!this.mergeItemStack(var5, 16, 52, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (par2 >= 1 && par2 < 12)
            {
                if (!this.mergeItemStack(var5, 16, 52, false))
                {
                    return null;
                }
            }
            else if (par2 >= 12 && par2 < 16)
            {
                if (!this.mergeItemStack(var5, 9, 45, false))
                {
                    return null;
                }
            }
            else if (var3.getItem() instanceof ItemArmorExtended && !((Slot)this.inventorySlots.get(5 + ((ItemArmor)var3.getItem()).armorType)).getHasStack())
            {
            	int slotID = ((ItemArmorExtended)var3.getItem()).equip.slot.slotID;
                if (!this.mergeItemStack(var5, slotID, slotID + 1, false))
                {
                    return null;
                }
            }
            else if (par2 >= 9 && par2 < 36)
            {
                if (!this.mergeItemStack(var5, 36, 45, false))
                {
                    return null;
                }
            }
            else if (par2 >= 36 && par2 < 45)
            {
                if (!this.mergeItemStack(var5, 9, 36, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 9, 45, false))
            {
                return null;
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }

            var4.onPickupFromSlot(par1EntityPlayer, var5);
        }

        return var3;
    }
	public class SlotArmor extends Slot{
		private Slots slotEquip;
		public SlotArmor(InventoryPlayer inventory, Slots slotEquip){
			super(inventory, slotEquip.slotID + 36, slotEquip.xPos, slotEquip.yPos);
			this.slotEquip = slotEquip;
			texture = "/ccs/res/armor/InventoryPlus.png";
		}
	    public boolean isItemValid(ItemStack stack) {
	    	if(this.slotEquip == Slots.lockItem)return true;
	    	if(stack.getItem() instanceof ItemArmorExtended)
	    		return ((ItemArmorExtended)stack.getItem()).equip.slot == this.slotEquip;
	        return false;
	    }
	    public int getSlotStackLimit() { 
	    	return 64;
	    }
	    @SideOnly(Side.CLIENT)
	    public int getBackgroundIconIndex() {
	        return slotEquip.iconIndex;
	    }
	}
}
