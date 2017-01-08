package com.hrkalk.zetapower.client.render.vessel;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.hrkalk.zetapower.utils.MathUtils;
import com.hrkalk.zetapower.utils.NBTReader;
import com.hrkalk.zetapower.utils.loader.ReflectUtil;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class ScaledRotator extends Object {

    private DynamicReloader reloader = new DynamicReloader(ScaledRotator.class, "../bin");

    {
        reloader.reloadWhen.add(new ReloadOnChange(com.hrkalk.zetapower.client.render.vessel.ScaledRotator.class, "../bin"));
        reloader.reloadWhen.add(new ReloadEveryNTicks(20));

        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadTrigger");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange");
        reloader.addToBlacklist("com.hrkalk.zetapower.client.render.vessel.ScaledRotator");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.ReflectUtil");
        reloader.addToBlacklist("java.lang.Object");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader");

        reloader.addToBlacklistPrefix("net.minecraft");
        reloader.addToBlacklistPrefix("net.minecraftforge");
        reloader.addToBlacklistPrefix("com.hrkalk.zetapower.dimension");
    }

    public Vector3f forward = new Vector3f(1, 0, 0);
    public Vector3f up = new Vector3f(0, 1, 0);

    public Vector3f lookForw = new Vector3f(1, 0, 0);
    public Vector3f lookUp = new Vector3f(0, 1, 0);
    public Vector3f lookRight = new Vector3f(0, 0, 1);

    public Vector3f scaleBefore = new Vector3f(1, 1, 1);
    public Vector3f scaleAfter = new Vector3f(1, 1, 1);

    public Matrix4f tmpMatrix = new Matrix4f(); //temporal matrix to avoid memory allocation
    public Vector3f tmpVector = new Vector3f();
    public Vector3f tmpVector2 = new Vector3f();

    public ScaledRotator(NBTTagCompound nbt) {
        loadFromNBT(nbt);
    }

    private static Vector3f facingToVector(int horizontalFacing) {
        switch (EnumFacing.getHorizontal(horizontalFacing)) {
        case EAST:
            return new Vector3f(-1, 0, 0);
        case NORTH:
            return new Vector3f(0, 0, 1);
        case SOUTH:
            return new Vector3f(0, 0, -1);
        default:
        case WEST:
            return new Vector3f(1, 0, 0);
        }
    }

    public ScaledRotator(int horizontalFacing) {
        this(facingToVector(horizontalFacing));
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

    public void resetRotation() {
        lookForw.set(forward);
        lookUp.set(up);
        MathUtils.cross(lookForw, lookUp, lookRight);
    }

    public void rotateXYZAxis(Vector3f axis, float angle) {
        tmpMatrix.setIdentity();
        tmpMatrix.rotate(angle, axis);
        MathUtils.multiplyVec(tmpMatrix, lookForw);
        MathUtils.multiplyVec(tmpMatrix, lookUp);
        MathUtils.multiplyVec(tmpMatrix, lookRight);
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
        pushMatrixToGlStack(true);
    }

    public void popMatrixFromGlStack() {
        GlStateManager.popMatrix();
    }

    public void pushMatrixToGlStack(boolean pushNewMatrix) {
        try {
            ReflectUtil.invoke("pushMatrixToGlStack", reloader.getInstance(this), pushNewMatrix);
        } catch (Throwable t) {
            System.out.println("Exception while executing reloadable code.");
            t.printStackTrace(System.out);
            //Thanks for using the Zeta Power Reloadable class generator.
        }
    }

    public NBTTagCompound saveToNBT(NBTTagCompound tag) {
        NBTReader.writeVec3fWithPrefix(tag, scaleBefore, "scaleBefore");
        NBTReader.writeVec3fWithPrefix(tag, scaleAfter, "scaleAfter");
        NBTReader.writeVec3fWithPrefix(tag, forward, "forward");
        NBTReader.writeVec3fWithPrefix(tag, up, "up");
        NBTReader.writeVec3fWithPrefix(tag, lookForw, "lookForw");
        NBTReader.writeVec3fWithPrefix(tag, lookUp, "lookUp");
        return tag;
    }

    public NBTTagCompound loadFromNBT(NBTTagCompound tag) {
        scaleBefore = NBTReader.readVec3fWithPrefix(tag, "scaleBefore");
        scaleAfter = NBTReader.readVec3fWithPrefix(tag, "scaleAfter");
        forward = NBTReader.readVec3fWithPrefix(tag, "forward");
        up = NBTReader.readVec3fWithPrefix(tag, "up");
        lookForw = NBTReader.readVec3fWithPrefix(tag, "lookForw");
        lookUp = NBTReader.readVec3fWithPrefix(tag, "lookUp");
        MathUtils.cross(lookForw, lookUp, lookRight);
        //fixPollution();
        return tag;
    }

}
