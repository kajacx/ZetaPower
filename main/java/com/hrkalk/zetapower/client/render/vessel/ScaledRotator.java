package com.hrkalk.zetapower.client.render.vessel;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.hrkalk.zetapower.utils.MathUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;

public class ScaledRotator {
    private Vector3f forward = new Vector3f(1, 0, 0);
    private Vector3f up = new Vector3f(0, 1, 0);

    private Vector3f lookForw = new Vector3f(1, 0, 0);
    private Vector3f lookUp = new Vector3f(0, 1, 0);
    private Vector3f lookRight = new Vector3f(0, 0, 1);

    private Vector3f scaleBefore = new Vector3f(1, 1, 1);
    private Vector3f scaleAfter = new Vector3f(1, 1, 1);

    private Matrix4f tmpMatrix = new Matrix4f(); //temporal matrix to avoid memory allocation
    private Vector3f tmpVector = new Vector3f();
    private Vector3f tmpVector2 = new Vector3f();

    public ScaledRotator(NBTTagCompound nbt) {

    }

    public ScaledRotator(int horizontalFacing) {
        //this(switch(horizontalFacing) {}); 
    }

    public ScaledRotator(Vector3f originalForward) {
        originalForward.normalise(forward);
        lookForw.set(forward);
        MathUtils.cross(lookForw, lookUp, lookRight);
    }

    public Vector3f getScaleBefore() {
        return scaleBefore;
    }

    public void setScaleBefore(Vector3f scaleBefore) {
        this.scaleBefore = scaleBefore;
    }

    public Vector3f getScaleAfter() {
        return scaleAfter;
    }

    public void setScaleAfter(Vector3f scaleAfter) {
        this.scaleAfter = scaleAfter;
    }

    public Vector3f getLookForw() {
        return lookForw;
    }

    public Vector3f getLookUp() {
        return lookUp;
    }

    public Vector3f getLookRight() {
        return lookRight;
    }

    private int pollution = 0;
    private static final int MAX_POLLUTION = 1_000_000;

    private void pollute() {
        pollution++;
        if (pollution > MAX_POLLUTION) {
            fixPollution();
            fixPollution();

            pollution = 0;
        }
    }

    private void fixPollution() {
        lookForw.normalise();

        tmpVector.set(lookForw);
        tmpVector.scale(-Vector3f.dot(lookUp, lookForw));
        Vector3f.add(lookUp, tmpVector, lookUp);
        lookUp.normalise();

        //don't ask...
        tmpVector.set(lookForw);
        tmpVector.scale(-Vector3f.dot(lookRight, lookForw));
        Vector3f.add(lookRight, tmpVector, lookRight);
        tmpVector.set(lookUp);
        tmpVector.scale(-Vector3f.dot(lookRight, lookUp));
        Vector3f.add(lookRight, tmpVector, lookRight);
        lookRight.normalise();
    }

    public void rotateXYZAxis(Vector3f axis, float angle) {
        tmpMatrix.setIdentity();
        tmpMatrix.rotate(angle, axis);
        MathUtils.multiplyPos(tmpMatrix, lookForw);
        MathUtils.multiplyPos(tmpMatrix, lookUp);
        MathUtils.multiplyPos(tmpMatrix, lookRight);
        pollute();
    }

    public void rotateLookForward(float angle) {
        rotateXYZAxis(lookForw, angle);
    }

    public void rotateLookUp(float angle) {
        rotateXYZAxis(lookUp, angle);
    }

    public void rotateLookRight(float angle) {
        rotateXYZAxis(lookRight, angle);
    }

    public void rotateLookAxis(Vector3f axis, float angle) {
        float x = lookForw.x * axis.x + lookUp.x * axis.y + lookRight.x + axis.z;
        float y = lookForw.y * axis.x + lookUp.y * axis.y + lookRight.y + axis.z;
        float z = lookForw.z * axis.x + lookUp.z * axis.y + lookRight.z + axis.z;
        tmpVector.set(x, y, z);
        rotateXYZAxis(tmpVector, angle);
    }

    public void pushMatrixToGlStack() {
        // -- Last, scale after
        GlStateManager.scale(scaleAfter.x, scaleAfter.y, scaleAfter.z);

        // -- Rotate by using MAD SCIENCE!

        //First, rotate up
        float angle1 = Vector3f.angle(forward, lookForw);
        MathUtils.cross(forward, lookForw, tmpVector);

        //then, rotate up according to first rotation
        tmpMatrix.setIdentity();
        tmpMatrix.rotate(angle1, tmpVector);
        MathUtils.multiplyVec(tmpMatrix, up, tmpVector2);

        //after we have rotated up in tmp2, we need to rotate again, to move it to lookUp
        float angle2 = Vector3f.angle(tmpVector2, lookUp);
        //L.s("Angle2: " + angle2);
        if (Math.abs(angle2) < Math.PI - MathUtils.EPSILON) {
            MathUtils.cross(tmpVector2, lookUp, tmpVector2);
        } else {
            //full 180 degree rotation, use rotForward instead
            tmpVector2.set(lookForw);
        }

        //Lastly, apply those rotations
        GlStateManager.rotate(angle2 * MathUtils.radToDegF, tmpVector2.x, tmpVector2.y, tmpVector2.z);//*/

        GlStateManager.rotate(angle1 * MathUtils.radToDegF, tmpVector.x, tmpVector.y, tmpVector.z);//*/

        // -- First, scale before
        GlStateManager.scale(scaleBefore.x, scaleBefore.y, scaleBefore.z);
    }
}
