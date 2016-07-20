package com.hrkalk.zetapower.utils;

import java.io.Serializable;

public class Vector3d implements Serializable {

    private static final long serialVersionUID = 6258350977594266664L;

    public double x, y, z;

    /**
     * Constructor for Vector3d.
     */
    public Vector3d() {
    }

    /**
     * Constructor
     */
    public Vector3d(Vector3d src) {
        set(src);
    }

    /**
     * Constructor
     */
    public Vector3d(double x, double y, double z) {
        set(x, y, z);
    }

    /* 
        doesnt touch the z value
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /* (non-Javadoc)
     * @see org.lwjgl.util.vector.WritableVector3d#set(double, double, double)
     */
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Load from another Vector3d
     * @param src The source vector
     * @return this
     */
    public Vector3d set(Vector3d src) {
        x = src.getX();
        y = src.getY();
        z = src.getZ();
        return this;
    }

    /**
     * @return the length squared of the vector
     */
    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    /**
     * Translate a vector
     * @param x The translation in x
     * @param y the translation in y
     * @return this
     */
    public Vector3d translate(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    /**
     * Add a vector to another vector and place the result in a destination
     * vector.
     * @param left The LHS vector
     * @param right The RHS vector
     * @param dest The destination vector, or null if a new vector is to be created
     * @return the sum of left and right in dest
     */
    public static Vector3d add(Vector3d left, Vector3d right, Vector3d dest) {
        if (dest == null)
            return new Vector3d(left.x + right.x, left.y + right.y, left.z + right.z);
        else {
            dest.set(left.x + right.x, left.y + right.y, left.z + right.z);
            return dest;
        }
    }

    /**
     * Subtract a vector from another vector and place the result in a destination
     * vector.
     * @param left The LHS vector
     * @param right The RHS vector
     * @param dest The destination vector, or null if a new vector is to be created
     * @return left minus right in dest
     */
    public static Vector3d sub(Vector3d left, Vector3d right, Vector3d dest) {
        if (dest == null)
            return new Vector3d(left.x - right.x, left.y - right.y, left.z - right.z);
        else {
            dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
            return dest;
        }
    }

    /**
     * The cross product of two vectors using the right-hand rule: if the first vetor goest from your thumb,
     * and the second vector from our other fingers, then the resulting vector goes from your palm. <br>
     * Example: cross(right, forward, up)
     *
     * @param left The LHS vector
     * @param right The RHS vector
     * @param dest The destination result, or null if a new vector is to be created
     * @return left cross right
     */
    public static Vector3d cross(Vector3d left, Vector3d right, Vector3d dest) {

        if (dest == null)
            dest = new Vector3d();

        dest.set(left.y * right.z - left.z * right.y, right.x * left.z - right.z * left.x, left.x * right.y - left.y * right.x);

        return dest;
    }



    /**
     * Negate a vector
     * @return this
     */
    public Vector3d negate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    /**
     * Negate a vector and place the result in a destination vector.
     * @param dest The destination vector or null if a new vector is to be created
     * @return the negated vector
     */
    public Vector3d negate(Vector3d dest) {
        if (dest == null)
            dest = new Vector3d();
        dest.x = -x;
        dest.y = -y;
        dest.z = -z;
        return dest;
    }

    /**
     * adds the argument vector to this vector
     * @param add vector to add
     * @return this for chaining
     */
    public Vector3d add(Vector3d add) {
        return Vector3d.add(this, add, this);
    }

    /**
     * Normalise this vector.
     * @return this for chaining
     */
    public Vector3d normalise() {
        return normalise(this);
    }


    /**
     * Normalise this vector and place the result in another vector.
     * @param dest The destination vector, or null if a new vector is to be created
     * @return the normalised vector
     */
    public Vector3d normalise(Vector3d dest) {
        double l = length();

        if (l < 1e-6) {
            l = 1;
        }

        if (dest == null)
            dest = new Vector3d(x / l, y / l, z / l);
        else
            dest.set(x / l, y / l, z / l);

        return dest;
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * The dot product of two vectors is calculated as
     * v1.x * v2.x + v1.y * v2.y + v1.z * v2.z
     * @param left The LHS vector
     * @param right The RHS vector
     * @return left dot right
     */
    public static double dot(Vector3d left, Vector3d right) {
        return left.x * right.x + left.y * right.y + left.z * right.z;
    }

    /**
     * Calculate the angle between two vectors, in radians
     * @param a A vector
     * @param b The other vector
     * @return the angle between the two vectors, in radians
     */
    public static double angle(Vector3d a, Vector3d b) {
        double dls = dot(a, b) / (a.length() * b.length());
        if (dls < -1f)
            dls = -1f;
        else if (dls > 1.0f)
            dls = 1.0f;
        return (double) Math.acos(dls);
    }

    /* (non-Javadoc)
     * @see org.lwjgl.vector.Vector#scale(double)
     */
    public Vector3d scale(double scale) {

        x *= scale;
        y *= scale;
        z *= scale;

        return this;

    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(64);

        sb.append("Vector3d[");
        sb.append(x);
        sb.append(", ");
        sb.append(y);
        sb.append(", ");
        sb.append(z);
        sb.append(']');
        return sb.toString();
    }

    /**
     * @return x
     */
    public final double getX() {
        return x;
    }

    /**
     * @return y
     */
    public final double getY() {
        return y;
    }

    /**
     * Set X
     * @param x
     */
    public final void setX(double x) {
        this.x = x;
    }

    /**
     * Set Y
     * @param y
     */
    public final void setY(double y) {
        this.y = y;
    }

    /**
     * Set Z
     * @param z
     */
    public void setZ(double z) {
        this.z = z;
    }

    /* (Overrides)
     * @see org.lwjgl.vector.ReadableVector3d#getZ()
     */
    public double getZ() {
        return z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vector3d other = (Vector3d) obj;

        if (x == other.x && y == other.y && z == other.z)
            return true;

        return false;
    }
}

