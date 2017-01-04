package net.minecraft.client.renderer.ccc;

import org.lwjgl.util.vector.Vector3f;

import net.minecraft.entity.Entity;

/**
 * General camera, given Entity you wish to observe (ususally the player), computes camera position, look vector and lookUp vector.
 * Store needed vectors in the <code>update()</code> method and then return them in <code>get</code> methods.
 * 
 * @author kajacx
 *
 */
public interface IPlayerCamera {

    /**
     * Gets called at the start of each render frame. Compute needed position and look vectors here from provided data.
     * <code>onSelected()</code> will be called before the first update.
     * @param player The entity we wish to observe
     * @param partialTicks Use this to interpolate between any <code>param</code> and <code>prevParam</code>
     */
    public void update(Entity player, float partialTicks);

    /**
     * Returns position of the camera, that is the point from where is the camera looking at the world.
     * @return position of the camera
     */
    public Vector3f getCameraPosition();

    /**
     * Returns the forward look vector of the camera, that is the direction the camera is facing.
     * @return forward look vector of the camera
     */
    public Vector3f getLookVector();

    /**
     * Returns the up look vector of the camera. This is the vector that the camera sees as "up".
     * In vanilla implementation, this is important when the camera is looking directly upwards of downwards
     * as this vector will then in what direction will the "spin" of the camera be.
     * However, custom implementations can return any vector, as long as it is orthogonal to the forward look vector
     * @return up look vector of the camera, must be othogonal to the forward look vector
     */
    public Vector3f getLookUpVector();

    /**
     * Returns whether this camera is a first-person camera. Used for deciding if only player's hand or entire model should be rendered, for example.
     * @return
     */
    public boolean isFirstPerson();

    /**
     * A handly handler called when this camera is selected as the active camera. This is called before the <code>update()</code> function.
     */
    public void onSelected();


    /**
     * A handly handler called when this camera is deselected and stops being the active camera.
     * This is called before the <code>onSelected()</code> function is called on the newly selected camera.
     */
    public void onDeselected();

}
