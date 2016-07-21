package com.hrkalk.zetapower.client.render.helper;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.hrkalk.zetapower.entities.RideableShip;
import com.hrkalk.zetapower.utils.MathUtils;

import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class EntityRotator extends TestHelper {
    private Vector3f forward = new Vector3f(1, 0, 0); //reference forward vector, stored at creation
    private Vector3f up = new Vector3f(0, 1, 0); //reference up vector, stored at creation

    //private Vector3f rotForward = new Vector3f(1, .01f, .01f); //actual forward vector for current rotation
    //private Vector3f rotUp = new Vector3f(.01f, -1, .01f); //actual up vector for current rotation
    private Vector3f rotForward = new Vector3f(1, 0, 0); //actual forward vector for current rotation
    private Vector3f rotUp = new Vector3f(0, 1, 0); //actual up vector for current rotation
    private Vector3f rotRight = new Vector3f();

    private Vector3f tmp1 = new Vector3f(); //tmp vector to prevent needless memory allocation
    private Vector3f tmp2 = new Vector3f(); //tmp vector to prevent needless memory allocation

    private Matrix4f invertedRotation = new Matrix4f(); //inverted rotation, call pushTransl.Matrix to (re)compute
    private Matrix4f tmpMatrix = new Matrix4f(); //temporal matrix to avoid memory allocation

    public EntityRotator() {
        System.out.println("Initializer called");
        rotForward.normalise();
        rotUp.normalise();
        Vector3f.cross(rotForward, rotUp, rotRight);
        rotRight.normalise();
    }

    /**
     * Creates rotation matrix accord to current rotation and pushes it into GlStateManager.
     * Don't forget to pop (remove) it once you are done rendering with this rotation.
     */
    public void pushTransformMatrix() {
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
        if (Math.abs(angle2) < Math.PI) {
            MathUtils.cross(tmp2, rotUp, tmp2);
            tmp2.normalise();
        } else {
            //full 180 degree rotation, use rotForward instead
            tmp2.set(rotForward);
        }
        //L.s("Tmp2: " + tmp2 + ", atan: " + Math.atan2(tmp2.z, tmp2.x) * MathUtils.radToDeg);
        GlStateManager.rotate(angle2 * MathUtils.radToDegF, tmp2.x, tmp2.y, tmp2.z);//*/

        GlStateManager.rotate(angle1 * MathUtils.radToDegF, tmp1.x, tmp1.y, tmp1.z);//*/

        invertedRotation.setIdentity();
        invertedRotation.rotate(angle2, tmp2);
        invertedRotation.rotate(angle1, tmp1);
        invertedRotation.invert();
    }

    /**
     * Transforms position of a block, so that it get's rendered at correct point.
     * If you wish to draw a block at a point, pass it to this method once you have called pushTransformMatrix()
     * and draw it at the result instead
     * @param vec
     * bottom-left corner of the place where you want to draw. Result is stored here.
     * @return
     * Result, coordinates that you should use so the rotation matrix moves it to correct place
     */
    public Vector3f transform(Vector3f vec) {
        return transform(vec, vec);
    }

    /**
     * Transforms position of a block, so that it get's rendered at correct point.
     * If you wish to draw a block at a point, pass it to this method once you have called pushTransformMatrix()
     * and draw it at the result instead
     * @param vec
     * bottom-left corner of the place where you want to draw
     * @param result
     * result is stored here: coordinates that you should use so the rotation matrix moves it to correct place. Use null to create new vector.
     * @return
     * result
     */
    public Vector3f transform(Vector3f vec, Vector3f result) {
        if (result == null)
            result = new Vector3f();

        result.set(vec.x + .5f, vec.y + .5f, vec.z + .5f);
        MathUtils.multiplyPos(invertedRotation, result);
        result.set(result.x - .5f, result.y - .5f, result.z - .5f);
        return result;
    }

    /**
     * Removes the transformation matrix from the GlStateManager stack
     */
    public void popTransformMatrix() {
        GlStateManager.popMatrix();
    }

    /**
     * rotates the camera up, moving the rotForward vector towards the rotUp vector
     * @param angle
     * rotation in radians
     */
    public void cameraUp(float angle) {
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
    public void cameraLeft(float angle) {
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
    public void cameraSpin(float angle) {
        tmpMatrix.setIdentity();
        tmpMatrix.rotate(angle, rotForward);
        MathUtils.multiplyPos(tmpMatrix, rotUp);
        MathUtils.multiplyPos(tmpMatrix, rotRight);
    }

    public void testtest(int a, Integer b, String c, RideableShip p, BlockFluidRenderer r) {

    }
}
