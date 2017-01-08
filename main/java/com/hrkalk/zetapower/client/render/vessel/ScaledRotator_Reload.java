package com.hrkalk.zetapower.client.render.vessel;

import org.lwjgl.util.vector.Vector3f;

import com.hrkalk.zetapower.utils.MathUtils;

import net.minecraft.client.renderer.GlStateManager;

public class ScaledRotator_Reload {

    public ScaledRotator thiz;


    public void pushMatrixToGlStack(boolean pushNewMatrix) {
        if (pushNewMatrix) {
            GlStateManager.pushMatrix();
        }

        // -- Last, scale after
        GlStateManager.scale(thiz.scaleAfter.x, thiz.scaleAfter.y, thiz.scaleAfter.z);

        // -- Rotate by using MAD SCIENCE!

        //First, rotate up
        float angle1 = Vector3f.angle(thiz.forward, thiz.lookForw);
        MathUtils.cross(thiz.forward, thiz.lookForw, thiz.tmpVector);
        thiz.tmpVector.normalise();

        //then, rotate up according to first rotation
        thiz.tmpMatrix.setIdentity();
        thiz.tmpMatrix.rotate(angle1, thiz.tmpVector);
        MathUtils.multiplyVec(thiz.tmpMatrix, thiz.up, thiz.tmpVector2);
        thiz.tmpVector2.normalise();

        //after we have rotated up in tmp2, we need to rotate again, to move it to lookUp
        float angle2 = Vector3f.angle(thiz.tmpVector2, thiz.lookUp);
        //L.s("Angle2: " + angle2);
        if (Math.abs(angle2) < Math.PI - MathUtils.EPSILON) {
            MathUtils.cross(thiz.tmpVector2, thiz.lookUp, thiz.tmpVector2);
        } else {
            //full 180 degree rotation, use rotForward instead
            thiz.tmpVector2.set(thiz.lookForw);
        }

        //Lastly, apply those rotations
        GlStateManager.rotate(angle2 * MathUtils.radToDegF, thiz.tmpVector2.x, thiz.tmpVector2.y, thiz.tmpVector2.z);//*/

        GlStateManager.rotate(angle1 * MathUtils.radToDegF, thiz.tmpVector.x, thiz.tmpVector.y, thiz.tmpVector.z);//*/

        // -- First, scale before
        GlStateManager.scale(thiz.scaleBefore.x, thiz.scaleBefore.y, thiz.scaleBefore.z);
    }
}
