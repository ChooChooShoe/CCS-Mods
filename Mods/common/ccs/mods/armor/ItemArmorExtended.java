package ccs.mods.armor;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import ccs.mods.armor.EnumEquipment.Slots;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraftforge.common.IArmorTextureProvider;
import net.minecraftforge.common.ISpecialArmor;

public class ItemArmorExtended extends ItemArmor implements IArmorTextureProvider, ISpecialArmor {

	public EnumEquipment equip;
	public EnumMaterial material;

	public ItemArmorExtended(int id, EnumMaterial baseMat, EnumEquipment equip) {
		super(id, EnumArmorMaterial.valueOf("NULL"), 0, 0);
		this.material = baseMat;
		this.equip = equip;
		this.setMaxStackSize(1);
		this.setIconCoord(equip.ordinal(), material.ordinal());
		this.setTextureFile("/ccs/res/armor/item.png");
	}

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return this.material == EnumMaterial.CLOTH;
    }
	@Override
    public int getItemEnchantability()
    {
        return this.material.enchantability;
    }
	@Override
    public EnumArmorMaterial getArmorMaterial()
    {
        return material == EnumMaterial.CLOTH ? EnumArmorMaterial.CLOTH : super.getArmorMaterial();
    }
	@Override
    public boolean hasColor(ItemStack stack)
    {
        return this.getColor(stack) > 0 && this.getColor(stack) != 10511680;
    }
	@Override
    public int getColor(ItemStack par1ItemStack)
    {
        if (this.material != EnumMaterial.CLOTH)
        {
            return -1;
        }
        else
        {
            NBTTagCompound var2 = par1ItemStack.getTagCompound();

            if (var2 == null)
            {
                return 10511680;
            }
            else
            {
                NBTTagCompound var3 = var2.getCompoundTag("display");
                return var3 == null ? 10511680 : (var3.hasKey("color") ? var3.getInteger("color") : 10511680);
            }
        }
    }

    @SideOnly(Side.CLIENT)
	@Override
    public int getIconFromDamageForRenderPass(int par1, int par2)
    {
        return par2 == 1 ? this.iconIndex + 144 : super.getIconFromDamageForRenderPass(par1, par2);
    }

	@Override
    public void removeColor(ItemStack par1ItemStack)
    {
        if (this.material == EnumMaterial.CLOTH)
        {
            NBTTagCompound var2 = par1ItemStack.getTagCompound();

            if (var2 != null)
            {
                NBTTagCompound var3 = var2.getCompoundTag("display");

                if (var3.hasKey("color"))
                {
                    var3.removeTag("color");
                }
            }
        }
    }

	@Override
    public void func_82813_b(ItemStack par1ItemStack, int par2)
    {
        if (this.material == EnumMaterial.CLOTH)
        {
            NBTTagCompound var3 = par1ItemStack.getTagCompound();

            if (var3 == null)
            {
                var3 = new NBTTagCompound();
                par1ItemStack.setTagCompound(var3);
            }

            NBTTagCompound var4 = var3.getCompoundTag("display");

            if (!var3.hasKey("display"))
            {
                var3.setCompoundTag("display", var4);
            }

            var4.setInteger("color", par2);
        }
    }

	@Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
        return this.material.craftID == par2ItemStack.itemID ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
    }
	@Override
	public String getArmorTextureFile(ItemStack itemstack) {
		return equip.getTexture(material);
	}

	@Override
	public ArmorProperties getProperties(EntityLiving player, ItemStack armor,
			DamageSource source, double damage, int slot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return this.material.getShieldsFor(slot);
	}

	@Override
	public void damageArmor(EntityLiving entity, ItemStack stack,
			DamageSource source, int damage, int slot) {
		// TODO Auto-generated method stub
		
	}
}
