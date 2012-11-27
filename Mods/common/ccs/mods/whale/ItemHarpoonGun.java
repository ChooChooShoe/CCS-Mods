package ccs.mods.whale;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumAction;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBow;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemHarpoonGun extends ItemBow {

	private boolean isGunLoaded = false;
	private ItemHarpoon loadedWith;

	protected ItemHarpoonGun(int id, int sprite) {
		super(id);
		this.setIconIndex(sprite);
		this.maxStackSize = 1;
		this.setMaxDamage(450);
		this.setCreativeTab(CreativeTabs.tabCombat);

		// TODO Auto-generated constructor stub
	}
	/**
	 * called when the player releases the use item button. Args: itemstack, world, entityplayer, itemInUseCount
	 */
	@Override
	public void onPlayerStoppedUsing(ItemStack item, World world, EntityPlayer player, int use)
	{
		if(loadedWith != null && player.inventory.hasItem(this.loadedWith.shiftedIndex)){
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
			poon.setDamage(loadedWith.shotDamage);

			if (speed == 1.0F)
			{
				poon.func_70243_d(true);
			}
			int power = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, item);
			if (power > 0) {
				poon.setDamage(poon.getDamage() + power * 0.5D + 0.5D);
			}
			int punch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, item);
			if (punch > 0) {
				poon.setKnockbackStrength(punch);
			}
			if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, item) > 0) {
				poon.setFire(100);
			}

			item.damageItem(1, player);
			world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + speed * 0.5F);
			poon.canBePickedUp = 2;
			player.inventory.consumeInventoryItem(loadedWith.shiftedIndex);

			if (!world.isRemote)
			{
				world.spawnEntityInWorld(poon);
			}
		}
		else{
			for(ItemStack stack : player.inventory.mainInventory){
				if(stack != null){
					Item harpoon = stack.getItem();
					if(harpoon instanceof ItemHarpoon){
						this.loadedWith = (ItemHarpoon)harpoon;
						player.inventory.consumeInventoryItem(harpoon.shiftedIndex);
					}
				}
			}
		}
	}

	@Override
	public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		return par1ItemStack;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 72000;
	}

	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	@Override
	public EnumAction getItemUseAction(ItemStack item)
	{
		if(isGunLoaded)
			return EnumAction.bow;
		return EnumAction.block;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack item, World w, EntityPlayer player)
	{
		player.setItemInUse(item, this.getMaxItemUseDuration(item));
		return item;
	}

	@Override
	public int getItemEnchantability()
	{
		return 1;
	}
	@Override
	public String getTextureFile(){
		return "ccs/res/whale/icons.png";
	}
}
