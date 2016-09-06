package com.hrkalk.zetapower.client.render.tileentities;

import org.lwjgl.util.vector.Vector3f;

import com.hrkalk.zetapower.client.render.helper.EntityRotator;
import com.hrkalk.zetapower.tileentities.ZetaChest;
import com.hrkalk.zetapower.utils.L;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class ZetaTileEntityRenderer_Reload {
    private static final ResourceLocation texture = new ResourceLocation("zetapower", "textures/tileentities/zeta_chest.png");
    private ModelChest model = new ModelChest();
    private EntityRotator rotator = new EntityRotator();

    public ZetaTileEntityRenderer thiz;

    public void renderTileEntityAt(TileEntity arg1, double arg2, double arg3, double arg4, float arg5, int arg6) {
        renderTileEntityAt((ZetaChest) arg1, arg2, arg3, arg4, arg5, arg6);
    }

    public void renderTileEntityAt(ZetaChest arg1, double arg2, double arg3, double arg4, float arg5, int arg6) {
        int facing = 0;
        if (arg1.getEnumFacing() != null) {
            facing = arg1.getEnumFacing().getHorizontalIndex();
        } else {
            L.e("Enum facing is null");
        }
        renderTileEntityAt2(arg2, arg3, arg4, facing);
    }

    private Vector3f tmp = new Vector3f();

    public void renderTileEntityAt2(double x, double y, double z, int facing) {
        //L.s("Facing is: " + facing);

        // rotator.cameraLeft(.002f);
        //rotator.cameraSpin(.02f);
        //rotator.cameraUp(.003f);

        renderTileEntityAt3(x, y, z, facing);

        /*float time = (float) (System.currentTimeMillis() % 10000000 / 1000d);
        
        rotator.reset();
        rotator.cameraLeft(time / 10);
        //rotator.cameraSpin(time);
        
        rotator.pushTransformMatrix();
        
        tmp.set((float) x, (float) (y + Math.sin(time) / 5 + .3f), (float) z);
        rotator.transform(tmp);
        
        renderTileEntityAt3(tmp.x, tmp.y, tmp.z, facing);
        
        rotator.popTransformMatrix();*/
    }

    public void renderTileEntityAt3(double x, double y, double z, int facing) {
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
