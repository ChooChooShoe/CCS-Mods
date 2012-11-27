package ccs.mods.armor;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

public class InventorySave extends InventoryPlayer {

	public InventorySave(EntityPlayer par1EntityPlayer) {
		super(par1EntityPlayer);
	    armorInventory = new ItemStack[11];
	}
	public void readFromNBT(NBTTagList par1NBTTagList)
    {
        this.mainInventory = new ItemStack[36];
        this.armorInventory = new ItemStack[11];

        for (int var2 = 0; var2 < par1NBTTagList.tagCount(); ++var2)
        {
            NBTTagCompound var3 = (NBTTagCompound)par1NBTTagList.tagAt(var2);
            int var4 = var3.getByte("Slot") & 255;
            ItemStack var5 = ItemStack.loadItemStackFromNBT(var3);

            if (var5 != null)
            {
                if (var4 >= 0 && var4 < this.mainInventory.length)
                {
                    this.mainInventory[var4] = var5;
                }

                if (var4 >= 100 && var4 < this.armorInventory.length + 100)
                {
                    this.armorInventory[var4 - 100] = var5;
                }
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.mainInventory.length + 11;
    }

    /**
     * Based on the damage values and maximum damage values of each armor item, returns the current armor value.
     */
    public int getTotalArmorValue()
    {
        int var1 = 0;

        for (int var2 = 0; var2 < this.armorInventory.length; ++var2)
        {
            if (this.armorInventory[var2] != null && this.armorInventory[var2].getItem() instanceof ItemArmor)
            {
                int var3 = ((ItemArmor)this.armorInventory[var2].getItem()).damageReduceAmount;
                var1 += var3;
            }
        }

        return var1;
    }

    /**
     * Damages armor in each slot by the specified amount.
     */
    public void damageArmor(int par1)
    {
        par1 /= 4;

        if (par1 < 1)
        {
            par1 = 1;
        }

        for (int var2 = 0; var2 < this.armorInventory.length; ++var2)
        {
            if (this.armorInventory[var2] != null && this.armorInventory[var2].getItem() instanceof ItemArmor)
            {
                this.armorInventory[var2].damageItem(par1, this.player);

                if (this.armorInventory[var2].stackSize == 0)
                {
                    this.armorInventory[var2] = null;
                }
            }
        }
    }
}
