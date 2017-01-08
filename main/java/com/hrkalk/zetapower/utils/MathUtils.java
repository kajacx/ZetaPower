package com.hrkalk.zetapower.utils;

import java.util.Random;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class MathUtils {
    public static final double EPSILON = 1e-6;

    public static Vector3f vectorX = new Vector3f(1, 0, 0);
    public static Vector3f vectorY = new Vector3f(0, 1, 0);
    public static Vector3f vectorZ = new Vector3f(0, 0, 1);
    public static Vector3f vectorXNeg = new Vector3f(-1, 0, 0);
    public static Vector3f vectorYNeg = new Vector3f(0, -1, 0);
    public static Vector3f vectorZNeg = new Vector3f(0, 0, -1);

    /**
     * Readily avaliable random generator
     */
    public static Random random = new Random();

    /**
     * Multiply by this to convert radians to degrees
     */
    public static double radToDeg = 180 / Math.PI;

    /**
     * Multiply by this to convert degrees to radians
     */
    public static double degToRad = Math.PI / 180;

    /**
     * Multiply by this to convert radians to degrees
     */
    public static float radToDegF = (float) radToDeg;

    /**
     * Multiply by this to convert degrees to radians
     */
    public static float degToRadF = (float) degToRad;

    /**
     * Multiplies matrix with a vector, including transposition
     * @param matrix
     * @param vector
     * result is stored in here
     * @return
     * Result stored in vector parameter
     */
    public static Vector3f multiplyPos(Matrix4f matrix, Vector3f vector) {
        return multiplyPos(matrix, vector, vector);
    }

    /**
     * Multiplies matrix with a vector, including transposition
     * @param matrix
     * @param vector
     * input vector, can be the same object as result
     * @param result
     * Result is stored in here
     * @return
     * Result
     */
    public static Vector3f multiplyPos(Matrix4f matrix, Vector3f vector, Vector3f result) {
        float x = vector.x;
        float y = vector.y;
        float z = vector.z;

        result.x = matrix.m00 * x + matrix.m10 * y + matrix.m20 * z + matrix.m30;
        result.y = matrix.m01 * x + matrix.m11 * y + matrix.m21 * z + matrix.m31;
        result.z = matrix.m02 * x + matrix.m12 * y + matrix.m22 * z + matrix.m32;

        return result;
    }

    /**
     * Multiplies matrix with a vector, ignoring transposition
     * @param matrix
     * @param vector
     * result is stored in here
     * @return
     * Result stored in vector parameter
     */
    public static Vector3f multiplyVec(Matrix4f matrix, Vector3f vector) {
        return multiplyVec(matrix, vector, vector);
    }

    /**
     * Multiplies matrix with a vector, ignoring transposition
     * @param matrix
     * @param vector
     * input vector, can be the same object as result
     * @param result
     * Result is stored in here
     * @return
     * Result
     */
    public static Vector3f multiplyVec(Matrix4f matrix, Vector3f vector, Vector3f result) {
        float x = vector.x;
        float y = vector.y;
        float z = vector.z;

        result.x = matrix.m00 * x + matrix.m10 * y + matrix.m20 * z;
        result.y = matrix.m01 * x + matrix.m11 * y + matrix.m21 * z;
        result.z = matrix.m02 * x + matrix.m12 * y + matrix.m22 * z;

        return result;
    }

    /**
     * Returns the left cross product of 2 vectors, or any orthogonal vector if they are linearly dependent.
     * Uses left-hand rule: if the first vector is pointing as your fingers and up vector as your thumb,
     * then the result vector will go from the palm of your left hand.
     * @param v1
     * @param v2
     * @param result
     * Result is stored here, this can be the same object as either of the parameters
     * @return
     * Result
     */
    public static Vector3f cross(Vector3f v1, Vector3f v2, Vector3f result) {
        float y = v1.y;
        float z = v1.z;

        Vector3f.cross(v1, v2, result);
        if (result.lengthSquared() > MathUtils.EPSILON)
            return result;

        result.x = 0;
        if (v1.y == 0) {
            result.y = 1;
            result.z = 0;
            return result;
        }
        if (v1.z == 0) {
            result.y = 0;
            result.z = 1;
            return result;
        }

        result.y = z;
        result.z = -y;
        return result;
    }

    private static Vector3f tmp = new Vector3f();

    public static float distanceSquared(Vector3f v1, Vector3f v2) {
        tmp.scale(0);
        Vector3f.add(v1, tmp, tmp);
        tmp.scale(-1);
        Vector3f.add(v2, tmp, tmp);
        return tmp.lengthSquared();
    }

    /**
     * interpolates between <code>v1</code> and <code>v2</code>
     * @param v1
     * @param v2
     * @param coef from range [0,1], 0 for <code>v1</code>, 1 for <code>v2</code>
     * @param target result is stored here
     * @return <code>target</code>
     */
    public static Vector3f interpolate(Vector3f v1, Vector3f v2, float coef, Vector3f target) {
        float x = (float) (v1.x + (v2.x - v1.x) * coef);
        float y = (float) (v1.y + (v2.y - v1.y) * coef);
        float z = (float) (v1.z + (v2.z - v1.z) * coef);
        target.set(x, y, z);
        return target;
    }
}
