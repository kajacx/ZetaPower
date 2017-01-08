package com.hrkalk.zetapower.client.render.vessel;

import org.lwjgl.util.vector.Vector3f;

import com.hrkalk.zetapower.utils.MathUtils;

import net.minecraft.client.renderer.GlStateManager;

public class ScaledRotator_Reload {

    public ScaledRotator thiz;

    private Vector3f interLookForw = new Vector3f();
    private Vector3f interLookUp = new Vector3f();

    public void pushMatrixToGlStack(boolean pushNewMatrix, Vector3f prevLookForward, Vector3f prevLookUp, float partialTicks) {
        if (pushNewMatrix) {
            GlStateManager.pushMatrix();
        }

        if (prevLookForward != null) {
            MathUtils.interpolate(prevLookForward, thiz.lookForw, partialTicks, interLookForw);
        } else {
            interLookForw.set(thiz.lookForw);
        }
        if (prevLookUp != null) {
            MathUtils.interpolate(prevLookUp, thiz.lookUp, partialTicks, interLookUp);
        } else {
            interLookUp.set(thiz.lookUp);
        }

        // -- Last, scale after
        GlStateManager.scale(thiz.scaleAfter.x, thiz.scaleAfter.y, thiz.scaleAfter.z);

        // -- Rotate by using MAD SCIENCE!

        //First, rotate up
        float angle1 = Vector3f.angle(thiz.forward, interLookForw);
        MathUtils.cross(thiz.forward, interLookForw, thiz.tmpVector);
        thiz.tmpVector.normalise();

        //then, rotate up according to first rotation
        thiz.tmpMatrix.setIdentity();
        thiz.tmpMatrix.rotate(angle1, thiz.tmpVector);
        MathUtils.multiplyVec(thiz.tmpMatrix, thiz.up, thiz.tmpVector2);
        thiz.tmpVector2.normalise();

        //after we have rotated up in tmp2, we need to rotate again, to move it to lookUp
        float angle2 = Vector3f.angle(thiz.tmpVector2, interLookUp);
        //L.s("Angle2: " + angle2);
        if (Math.abs(angle2) < Math.PI - MathUtils.EPSILON) {
            MathUtils.cross(thiz.tmpVector2, interLookUp, thiz.tmpVector2);
        } else {
            //full 180 degree rotation, use rotForward instead
            thiz.tmpVector2.set(interLookForw);
        }

        //Lastly, apply those rotations
        GlStateManager.rotate(angle2 * MathUtils.radToDegF, thiz.tmpVector2.x, thiz.tmpVector2.y, thiz.tmpVector2.z);//*/

        GlStateManager.rotate(angle1 * MathUtils.radToDegF, thiz.tmpVector.x, thiz.tmpVector.y, thiz.tmpVector.z);//*/

        // -- First, scale before
        GlStateManager.scale(thiz.scaleBefore.x, thiz.scaleBefore.y, thiz.scaleBefore.z);
    }
}
