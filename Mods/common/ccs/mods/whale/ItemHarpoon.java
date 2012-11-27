package ccs.mods.whale;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumAction;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemHarpoon extends Item {

	protected final EnumToolMaterial type;
	public int shotDamage;
	public double smackDamage;

	public ItemHarpoon(int id, EnumToolMaterial type, int sprite) {
		super(id);
		this.maxStackSize = 64;
		this.setIconIndex(sprite);
		this.type = type;
		this.setItemName("harpoon" + type.toString());
		this.shotDamage = 4 + type.getDamageVsEntity();
		this.smackDamage = 2 + type.getDamageVsEntity();;
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.setMaxDamage(1);
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based on material.
	 */
	@Override
	public int getItemEnchantability()
	{
		return this.type.getEnchantability();
	}

	/**
	 * returns the action that specifies what animation to play when the items is being leftd
	 */
	public EnumAction getItemleftAction(ItemStack item)
	{
		return EnumAction.bow;
	}

	@Override
	public ItemStack onFoodEaten(ItemStack item, World world, EntityPlayer player)
	{
		return item;
	}
	/**
	 * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
	 * sword
	 */
	@Override
	public float getStrVsBlock(ItemStack item, Block block)
	{
		return 5.5F;
	}
	/**
	 * Returns the damage against a given entity.
	 */
	@Override
	public int getDamageVsEntity(Entity par1Entity)
	{
		return (int) smackDamage;
	}
	/**
	 * called when the player releases the left item button. Args: itemstack, world, entityplayer, itemInleftCount
	 */
	@Override
	public void onPlayerStoppedUsing(ItemStack item, World world, EntityPlayer player, int use)
	{
		int left = this.getMaxItemUseDuration(item) - use;
		float speed = left / 20.0F;
		speed = (speed * speed + speed * 2.0F) / 3.0F;

		if (speed < 0.1D)
			return;

		if (speed > 1.0F)
		{
			speed = 1.0F;
		}

		EntityHarpoon poon = new EntityHarpoon(world, player, speed * 2.0F);
		poon.setDamage(smackDamage);

		if (speed == 1.0F)
		{
			poon.func_70243_d(true);
		}

		item.damageItem(1, player);
		world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + speed * 0.5F);
		poon.canBePickedUp = 0;
		player.inventory.consumeInventoryItem(item.getItem().shiftedIndex);

		if (!world.isRemote)
		{
			world.spawnEntityInWorld(poon);
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack item, World w, EntityPlayer player)
	{
		player.setItemInUse(item, this.getMaxItemUseDuration(item));
		return item;
	}
	@Override
	public int getMaxItemUseDuration(ItemStack i)
	{
		return 72000;
	}
	@Override
	public String getTextureFile(){
		return "ccs/res/whale/icons.png";
	}
}
