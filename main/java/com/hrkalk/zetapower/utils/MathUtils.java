package com.hrkalk.zetapower.utils;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class MathUtils {

    /**
     * Multiply by this to convert radians to degrees
     */
    public static float radToDeg = 180 / (float) Math.PI;

    /**
     * Multiply by this to convert degrees to radians
     */
    public static float degToRad = (float) Math.PI / 180;

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
     * Returns the left cross product of 2 vectors or any ortogonal vector is they are lineary dependent
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
        if (result.lengthSquared() > 0)
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
}
