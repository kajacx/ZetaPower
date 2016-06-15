package com.hrkalk.zetapower.client.render.tileentities;

import org.lwjgl.util.vector.Vector3f;

import com.hrkalk.zetapower.client.render.helper.EntityRotator;
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
    EntityRotator rotator = new EntityRotator();

    public static void main(String[] args) {
        System.out.println("Class reloaded");
    }

    @Override
    public void renderTileEntityAt(ZetaChest te, double x, double y, double z, float partialTicks, int destroyStage) {

    }

    /*
     * NORTH  =  Z-
     * EAST   =  X+  
     * SOUTH  =  Z+
     * WEST   =  X-
     */

    private Vector3f tmp = new Vector3f();

    public void renderTileEntityAt2(double x, double y, double z, float partialTicks, int destroyStage, int facing) {

        rotator.cameraLeft(.01f);
        rotator.cameraSpin(.02f);
        rotator.cameraUp(.003f);

        float time = (float) (System.currentTimeMillis() % 10000000 / 1000d);

        rotator.pushTransformMatrix();
        tmp.set((float) x, (float) (y + Math.sin(time) / 5), (float) z);
        rotator.transform(tmp);
        renderTileEntityAt3(tmp.x, tmp.y, tmp.z, partialTicks, destroyStage, facing);

        rotator.popTransformMatrix();
    }

    public void renderTileEntityAt3(double x, double y, double z, float partialTicks, int destroyStage, int facing) {
        // -- NORMAL RENDER CODE --  

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z); //final move

        //model.chestLid.rotateAngleX = -1;

        //basic

        EnumFacing side = EnumFacing.getHorizontal(facing);
        float angleCW = side.getHorizontalIndex() - EnumFacing.SOUTH.getHorizontalIndex();
        angleCW *= 90;

        GlStateManager.translate(.5f, .5f, .5f); //move center back
        GlStateManager.rotate(angleCW, 0, -1, 0);

        //troll
        /*GlStateManager.scale(
                1 + .2f * Math.sin(time + Math.PI * 0 / 3),
                1 + .2f * Math.sin(time + Math.PI * 2 / 3),
                1 + .2f * Math.sin(time + Math.PI * 4 / 3));//*/

        GlStateManager.rotate(180, 1, 0, 0); //rotate from upside-down, now facing z+ (south)
        GlStateManager.translate(-.5f, -.5f, -.5f); //move center to 0

        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

        model.renderAll();

        GlStateManager.popMatrix();

        // -- END OF NORMAL REDER CODE --
    }

}
