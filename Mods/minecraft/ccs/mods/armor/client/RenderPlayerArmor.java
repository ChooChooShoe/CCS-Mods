package ccs.mods.armor.client;

import org.lwjgl.opengl.GL11;

import ccs.mods.armor.EnumMaterial;
import ccs.mods.armor.ItemArmorExtended;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EnumAction;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderPlayer;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntitySkullRenderer;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class RenderPlayerArmor extends RenderPlayer {

    protected ModelBiped armorChestPlate;
    protected ModelBiped armor;
    protected ModelBiped bipodModel;
    
	public RenderPlayerArmor(){
		super();
        this.bipodModel = (ModelBiped)this.mainModel;
        this.armorChestPlate = new ModelBiped(1.0F);
        this.armor = new ModelBiped(0.5F);
	}

    /**
     * Set the specified armor model as the player model. Args: player, armorSlot, partialTick
     */
    protected int setArmorModel(EntityPlayer player, int pass, float tick)
    {
    	this.setArmorModel2(player, pass + 4, tick);
        ItemStack var4 = player.inventory.armorItemInSlot(pass);

        if (var4 != null)
        {
            Item var5 = var4.getItem();

            if (var5 instanceof ItemArmorExtended)
            {
            	ItemArmorExtended armor = (ItemArmorExtended)var5;
                this.loadTexture(ForgeHooksClient.getArmorTexture(var4, "/armor/" + armorFilenamePrefix[armor.renderIndex] + "_" + (pass == 2 ? 2 : 1) + ".png"));
                ModelBiped var7 = pass == 2 ? this.armor : this.armorChestPlate;
                var7.bipedHead.showModel = pass == 0;
                var7.bipedHeadwear.showModel = pass == 0;
                var7.bipedBody.showModel = pass == 1 || pass == 2;
                var7.bipedRightArm.showModel = pass == 1;
                var7.bipedLeftArm.showModel = pass == 1;
                var7.bipedRightLeg.showModel = pass == 2 || pass == 3;
                var7.bipedLeftLeg.showModel = pass == 2 || pass == 3;
                this.setRenderPassModel(var7);

                if (var7 != null)
                {
                    var7.onGround = this.mainModel.onGround;
                }

                if (var7 != null)
                {
                    var7.isRiding = this.mainModel.isRiding;
                }

                if (var7 != null)
                {
                    var7.isChild = this.mainModel.isChild;
                }

                float var8 = 1.0F;

                if (armor.material == EnumMaterial.CLOTH)
                {
                    int var9 = armor.getColor(var4);
                    float var10 = (float)(var9 >> 16 & 255) / 255.0F;
                    float var11 = (float)(var9 >> 8 & 255) / 255.0F;
                    float var12 = (float)(var9 & 255) / 255.0F;
                    GL11.glColor3f(var8 * var10, var8 * var11, var8 * var12);

                    if (var4.isItemEnchanted())
                    {
                        return 31;
                    }

                    return 16;
                }

                GL11.glColor3f(var8, var8, var8);

                if (var4.isItemEnchanted())
                {
                    return 15;
                }

                return 1;
            }
        }

        return -1;
    }

    private int setArmorModel2(EntityPlayer player, int pass, float tick) {
        ItemStack var4 = player.inventory.armorItemInSlot(3 - pass);

        if (var4 != null)
        {
            Item var5 = var4.getItem();

            if (var5 instanceof ItemArmor)
            {
                ItemArmor var6 = (ItemArmor)var5;
                this.loadTexture(ForgeHooksClient.getArmorTexture(var4, "/armor/" + armorFilenamePrefix[var6.renderIndex] + "_" + (pass == 2 ? 2 : 1) + ".png"));
                ModelBiped var7 = pass == 2 ? this.armor : this.armorChestPlate;
                var7.bipedHead.showModel = pass == 0;
                var7.bipedHeadwear.showModel = pass == 0;
                var7.bipedBody.showModel = pass == 1 || pass == 2;
                var7.bipedRightArm.showModel = pass == 1;
                var7.bipedLeftArm.showModel = pass == 1;
                var7.bipedRightLeg.showModel = pass == 2 || pass == 3;
                var7.bipedLeftLeg.showModel = pass == 2 || pass == 3;
                this.setRenderPassModel(var7);

                if (var7 != null)
                {
                    var7.onGround = this.mainModel.onGround;
                }

                if (var7 != null)
                {
                    var7.isRiding = this.mainModel.isRiding;
                }

                if (var7 != null)
                {
                    var7.isChild = this.mainModel.isChild;
                }

                float var8 = 1.0F;

                if (var6.getArmorMaterial() == EnumArmorMaterial.CLOTH)
                {
                    int var9 = var6.getColor(var4);
                    float var10 = (float)(var9 >> 16 & 255) / 255.0F;
                    float var11 = (float)(var9 >> 8 & 255) / 255.0F;
                    float var12 = (float)(var9 & 255) / 255.0F;
                    GL11.glColor3f(var8 * var10, var8 * var11, var8 * var12);

                    if (var4.isItemEnchanted())
                    {
                        return 31;
                    }

                    return 16;
                }

                GL11.glColor3f(var8, var8, var8);

                if (var4.isItemEnchanted())
                {
                    return 15;
                }

                return 1;
            }
        }

        return -1;
	}

	protected void func_82439_b(EntityPlayer player, int pass, float par3)
    {
        ItemStack var4 = player.inventory.armorItemInSlot(3 - pass);

        if (var4 != null)
        {
            Item var5 = var4.getItem();

            if (var5 instanceof ItemArmor)
            {
                ItemArmor var6 = (ItemArmor)var5;
                this.loadTexture(ForgeHooksClient.getArmorTexture(var4, "/armor/" + armorFilenamePrefix[var6.renderIndex] + "_" + (pass == 2 ? 2 : 1) + "_b.png"));
                float var7 = 1.0F;
                GL11.glColor3f(var7, var7, var7);
            }
        }
    }
    public void renderPlayer(EntityPlayer player, double par2, double par4, double par6, float par8, float par9)
    {
        float var10 = 1.0F;
        GL11.glColor3f(var10, var10, var10);
        ItemStack var11 = player.inventory.getCurrentItem();
        this.armorChestPlate.heldItemRight = this.armor.heldItemRight = this.bipodModel.heldItemRight = var11 != null ? 1 : 0;

        if (var11 != null && player.getItemInUseCount() > 0)
        {
            EnumAction var12 = var11.getItemUseAction();

            if (var12 == EnumAction.block)
            {
                this.armorChestPlate.heldItemRight = this.armor.heldItemRight = this.bipodModel.heldItemRight = 3;
            }
            else if (var12 == EnumAction.bow)
            {
                this.armorChestPlate.aimedBow = this.armor.aimedBow = this.bipodModel.aimedBow = true;
            }
        }

        this.armorChestPlate.isSneak = this.armor.isSneak = this.bipodModel.isSneak = player.isSneaking();

        super.renderPlayer(player, par2, par4, par6, par8, par9);
        this.armorChestPlate.aimedBow = this.armor.aimedBow = this.bipodModel.aimedBow = false;
        this.armorChestPlate.isSneak = this.armor.isSneak = this.bipodModel.isSneak = false;
        this.armorChestPlate.heldItemRight = this.armor.heldItemRight = this.bipodModel.heldItemRight = 0;
    }

    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderPlayer((EntityPlayer)par1EntityLiving, par2, par4, par6, par8, par9);
    }

    @Override
    public void func_82441_a(EntityPlayer player)
    {
        float var2 = 1.0F;
        GL11.glColor3f(var2, var2, var2);
        this.bipodModel.onGround = 0.0F;
        this.bipodModel.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
        this.bipodModel.bipedRightArm.render(0.0625F);
        super.func_82441_a(player);
    }
}
