package com.hrkalk.zetapower.client.render.tileentities;

import com.hrkalk.zetapower.tileentities.ZetaChest;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class ZetaTileEntityRendererReload extends TileEntitySpecialRenderer<ZetaChest> {

    private static final ResourceLocation texture = new ResourceLocation("zetapower", "textures/tileentities/zeta_chest.png");
    private ModelChest model = new ModelChest();

    public static void main(String[] args) {
        System.out.println("Class reloaded");
    }

    @Override
    public void renderTileEntityAt(ZetaChest te, double x, double y, double z, float partialTicks, int destroyStage) {

    }

    float ticks = 0;

    /*
     * NORTH  =  Z-
     * EAST   =  X+
     * SOUTH  =  Z+
     * WEST   =  X-
     */

    public void renderTileEntityAt2(double x, double y, double z, float partialTicks, int destroyStage, int facing) {
        ticks++;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z); //final move

        //troll
        /*GlStateManager.translate(.5f, .5f, .5f);
        GlStateManager.scale(
                1 + .1f * Math.sin(ticks / 60 + Math.PI * 0 / 3),
                1 + .1f * Math.sin(ticks / 60 + Math.PI * 2 / 3),
                1 + .1f * Math.sin(ticks / 60 + Math.PI * 4 / 3));
        GlStateManager.translate(-.5f, -.5f, -.5f);*/

        model.chestLid.rotateAngleX = -1;

        //basic

        EnumFacing side = EnumFacing.getHorizontal(facing);
        float angleCW = side.getHorizontalIndex() - EnumFacing.SOUTH.getHorizontalIndex();
        angleCW *= 90;

        GlStateManager.translate(.5f, .5f, .5f); //move center back
        GlStateManager.rotate(angleCW, 0, -1, 0);
        GlStateManager.rotate(180, 1, 0, 0); //rotate from upside-down, now facing z+ (south)
        GlStateManager.translate(-.5f, -.5f, -.5f); //move center to 0

        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

        model.renderAll();

        GlStateManager.popMatrix();
    }

}
