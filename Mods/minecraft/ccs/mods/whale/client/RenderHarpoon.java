package ccs.mods.whale.client;

import net.minecraft.src.Entity;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ccs.mods.whale.EntityHarpoon;
import ccs.mods.whale.ItemHarpoon;

public class RenderHarpoon extends Render {

	@Override
	public void doRender(Entity var1, double var2, double var4, double var6,
			float var8, float var9) {
		this.renderHarpoon((EntityHarpoon)var1, var2, var4, var6, var8, var9);
	}

	public void renderHarpoon(EntityHarpoon harpoon, double d1, double d2,
			double d3, float f1, float f2) {
		ItemHarpoon ItemH = harpoon.itemHarpoon;
		int icon = ItemH.getIconIndex(new ItemStack(ItemH));
		this.loadTexture("/ccs/res/whale/icons.png");

		GL11.glPushMatrix();
		GL11.glTranslatef((float)d1, (float)d2, (float)d3);
		GL11.glRotatef(harpoon.prevRotationYaw + (harpoon.rotationYaw - harpoon.prevRotationYaw) * f2 - 90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(harpoon.prevRotationPitch + (harpoon.rotationPitch - harpoon.prevRotationPitch) * f2, 0.0F, 0.0F, 1.0F);
		Tessellator tes = Tessellator.instance;

		double X1 = icon / 16F;
		double X2 = icon / 16F;
		double Y1 = 0.0625F;
		double Y2 = 0.1875F;

		tes.addVertexWithUV(-7.0D, -2.0D, -2.0D, X1, Y1);
		tes.addVertexWithUV(-7.0D, -2.0D, 2.0D, X2, Y1);
		tes.addVertexWithUV(-7.0D, 2.0D, 2.0D, X1, Y2);
		tes.addVertexWithUV(-7.0D, 2.0D, -2.0D, X2, Y2);

		/** byte var11 = 0;
        float x1 = 0.0F;
        float x2 = 0.5F;
        float y1 = (float)(0 + var11 * 10) / 32.0F;
        float y2 = (float)(5 + var11 * 10) / 32.0F;
        float var16 = 0.0F;
        float var17 = 0.15625F;
        float var18 = (float)(5 + var11 * 10) / 32.0F;
        float var19 = (float)(10 + var11 * 10) / 32.0F;
        float var20 = 0.05625F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float var21 = (float)harpoon.arrowShake - f2;

        if (var21 > 0.0F)
        {
            float var22 = -MathHelper.sin(var21 * 3.0F) * var21;
            GL11.glRotatef(var22, 0.0F, 0.0F, 1.0F);
        }

        GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(var20, var20, var20);
        GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
        GL11.glNormal3f(var20, 0.0F, 0.0F);
        tes.startDrawingQuads();
        tes.addVertexWithUV(-7.0D, -2.0D, -2.0D, (double)var16, (double)var18);
        tes.addVertexWithUV(-7.0D, -2.0D, 2.0D, (double)var17, (double)var18);
        tes.addVertexWithUV(-7.0D, 2.0D, 2.0D, (double)var17, (double)var19);
        tes.addVertexWithUV(-7.0D, 2.0D, -2.0D, (double)var16, (double)var19);
        tes.draw();
        GL11.glNormal3f(-var20, 0.0F, 0.0F);
        tes.startDrawingQuads();
        tes.addVertexWithUV(-7.0D, 2.0D, -2.0D, (double)var16, (double)var18);
        tes.addVertexWithUV(-7.0D, 2.0D, 2.0D, (double)var17, (double)var18);
        tes.addVertexWithUV(-7.0D, -2.0D, 2.0D, (double)var17, (double)var19);
        tes.addVertexWithUV(-7.0D, -2.0D, -2.0D, (double)var16, (double)var19);
        tes.draw();

        for (int var23 = 0; var23 < 4; ++var23)
        {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, var20);
            tes.startDrawingQuads();
            tes.addVertexWithUV(-8.0D, -2.0D, 0.0D, (double)x1, (double)y1);
            tes.addVertexWithUV(8.0D, -2.0D, 0.0D, (double)x2, (double)y1);
            tes.addVertexWithUV(8.0D, 2.0D, 0.0D, (double)x2, (double)y2);
            tes.addVertexWithUV(-8.0D, 2.0D, 0.0D, (double)x1, (double)y2);
            tes.draw();
        }*/

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();

	}

}
