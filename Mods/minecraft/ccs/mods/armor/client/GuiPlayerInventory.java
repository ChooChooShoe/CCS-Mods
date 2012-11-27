package ccs.mods.armor.client;

import net.minecraft.src.AchievementList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiContainerCreative;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.InventoryEffectRenderer;
import net.minecraft.src.StatCollector;

import org.lwjgl.opengl.GL11;

import ccs.mods.armor.ContainerInventory;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPlayerInventory extends InventoryEffectRenderer
{
	private EntityPlayer player;

	public GuiPlayerInventory(EntityPlayer player)
	{
		super(player.inventorySlots);
		System.out.println(player);
		this.player = player;
        this.allowUserInput = true;
        player.addStat(AchievementList.openInventory, 1);
	}
	@Override
    public void updateScreen()
    {
        if (this.mc.playerController.isInCreativeMode())
        {
    		//System.out.println("Swiching to Creative Inv");
            this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.thePlayer));
        }
    }
	@Override
    public void initGui()
    {
        this.controlList.clear();

        if (this.mc.playerController.isInCreativeMode())
        {
    		//System.out.println("Swiching to Creative Inv");
            this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.thePlayer));
        }
        else
        {
            super.initGui();
        }
    }
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int var4 = this.mc.renderEngine.getTexture("/ccs/res/armor/InventoryPlus.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
		GuiInventory.func_74223_a(this.mc, guiLeft + 33, guiTop + 75, 30, (float)(guiLeft + 33) - (float)par2, (float)(guiTop + 75 - 50) - (float)par3);
	}
}

