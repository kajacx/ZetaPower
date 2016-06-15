package com.hrkalk.zetapower.client.render.tileentities;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.hrkalk.zetapower.tileentities.ZetaChest;
import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.utils.MathUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class ZetaTileEntityRendererReload extends TileEntitySpecialRenderer<ZetaChest> {

    private static final ResourceLocation texture = new ResourceLocation("zetapower", "textures/tileentities/zeta_chest.png");
    private ModelChest model = new ModelChest();
    private static float SPEED = .01f;

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

    private Vector3f forward = new Vector3f(1, 0, 0); //reference forward vector, stored at creation
    private Vector3f up = new Vector3f(0, 1, 0); //reference up vector, stored at creation

    private Vector3f rotForward = new Vector3f(0.9999955f, -0.0029998897f, 0.0f); //actual forward vector for current rotation
    private Vector3f rotUp = new Vector3f(-0.0029999958f, -0.9999602f, -0.008407306f); //actual up vector for current rotation
    //private Vector3f rotForward = new Vector3f(1, .01f, .01f); //actual forward vector for current rotation
    //private Vector3f rotUp = new Vector3f(.01f, -1, .01f); //actual up vector for current rotation
    private Vector3f rotRight = new Vector3f();

    private Vector3f tmp1 = new Vector3f(); //tmp vector to prevent needless memory allocation
    private Vector3f tmp2 = new Vector3f(); //tmp vector to prevent needless memory allocation
    private Vector3f tmp3 = new Vector3f(); //tmp vector to prevent needless memory allocation

    private Matrix4f invertedRotation = new Matrix4f(); //inverted rotation, call pushTransl.Matrix to (re)compute
    private Matrix4f tmpMatrix = new Matrix4f(); //temporal matrix to avoid memory allocation

    {
        System.out.println("Initializer called");
        rotForward.normalise();
        rotUp.normalise();
        Vector3f.cross(rotForward, rotUp, rotRight);
        rotRight.normalise();

        L.s("rotRight. " + rotRight);
        //cameraSpin(3.15f);
        cameraUp(-300 * SPEED);

        L.traceDepth = 0;
        L.d("FORWARD: " + rotForward);
        L.d("UP: " + rotUp);
        L.d("RIGHT: " + rotRight);
    }

    private void pushTranslationMatrix() {
        GlStateManager.pushMatrix();
        //add operations in reverse order

        //first, rotate forward vector to rotForward
        float angle1 = Vector3f.angle(forward, rotForward);
        MathUtils.cross(forward, rotForward, tmp1);
        tmp1.normalise();

        //rotate up and rotate up
        tmpMatrix.setIdentity();
        tmpMatrix.rotate(angle1, tmp1);
        MathUtils.multiplyVec(tmpMatrix, up, tmp2);

        //L.s("Diff1: " + MathUtils.distanceSquared(up, tmp2));
        //L.s("Diff2: " + MathUtils.distanceSquared(rotUp, tmp3));

        //second, rotate up to rotUp
        float angle2 = Vector3f.angle(tmp2, rotUp);
        //L.s("Angle2: " + angle2);
        MathUtils.cross(tmp2, rotUp, tmp2);
        tmp2.normalise();
        //L.s("Tmp2: " + tmp2 + ", atan: " + Math.atan2(tmp2.z, tmp2.x) * MathUtils.radToDeg);
        GlStateManager.rotate(angle2 * MathUtils.radToDeg, tmp2.x, tmp2.y, tmp2.z);//*/

        GlStateManager.rotate(angle1 * MathUtils.radToDeg, tmp1.x, tmp1.y, tmp1.z);//*/

        invertedRotation.setIdentity();
        invertedRotation.rotate(angle2, tmp2);
        invertedRotation.rotate(angle1, tmp1);
        invertedRotation.invert();

        //L.d("FORWARD: " + rotForward);
        //L.d("UP: " + rotUp);
        //L.d("RIGHT: " + rotRight);
    }

    /**
     * rotates the camera up, moving the rotForward vector towards the rotUp vector
     * @param angle
     * rotation in radians
     */
    private void cameraUp(float angle) {
        tmpMatrix.setIdentity();
        tmpMatrix.rotate(angle, rotRight);
        MathUtils.multiplyPos(tmpMatrix, rotForward);
        MathUtils.multiplyPos(tmpMatrix, rotUp);
    }

    /**
     * rotates the camera left, moving the rotRight vector towards the rotForward vector
     * @param angle
     * rotation in radians
     */
    private void cameraLeft(float angle) {
        tmpMatrix.setIdentity();
        tmpMatrix.rotate(angle, rotUp);
        MathUtils.multiplyPos(tmpMatrix, rotRight);
        MathUtils.multiplyPos(tmpMatrix, rotForward);
    }

    /**
     * spins the camera clockwise, moving the rotUp vector towards the rotRight vector,
     * but the world before you seems to spin counter-clockwise, don't be fooled!
     * @param angle
     * rotation in radians
     */
    private void cameraSpin(float angle) {
        tmpMatrix.setIdentity();
        tmpMatrix.rotate(angle, rotForward);
        MathUtils.multiplyPos(tmpMatrix, rotUp);
        MathUtils.multiplyPos(tmpMatrix, rotRight);
    }

    public void renderTileEntityAt2(double x, double y, double z, float partialTicks, int destroyStage, int facing) {

        cameraLeft(.01f);

        float time = (float) (System.currentTimeMillis() % 10000000 / 1000d);
        // -- CHECK --
        //L.s("Ortho check: " + Vector3f.dot(rotForward, rotUp) + ", " + Vector3f.dot(rotUp, rotRight) + ", " + Vector3f.dot(rotRight, rotForward));

        // -- RENDER --

        pushTranslationMatrix();

        //tmp1.set((float) x, (float) y, (float) z);
        tmp1.set((float) x + .5f, (float) (y + .5f + Math.sin(time) / 5), (float) z + .5f);
        MathUtils.multiplyPos(invertedRotation, tmp1, tmp2);
        tmp2.x -= .5f;
        tmp2.y -= .5f;
        tmp2.z -= .5f;//*/
        renderTileEntityAt3(tmp2.x, tmp2.y, tmp2.z, partialTicks, destroyStage, facing);

        GlStateManager.popMatrix();
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
