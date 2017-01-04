package net.minecraft.client.renderer;

import org.lwjgl.util.vector.Vector3f;

import net.minecraft.entity.Entity;

/**
 * Default minecraft cameras, implemented to fit the IPlayerCamera interface
 * @author kajacx
 *
 */
public class VanillaPlayerCamera implements IPlayerCamera {

    /**
     * Standart first-person camera. Vanilla index (thirdPersonView) 0
     */
    public static int MODE_FIRST_PERSON = 0;

    /**
     * 3rd-person camera looking at player from behind. Vanilla index (thirdPersonView) 1
     */
    public static int MODE_3RD_PERSON_BEHIND = -1;

    /**
     * 3rd-person camera looking at player from the front. Vanilla index (thirdPersonView) 2
     */
    public static int MODE_3RD_PERSON_FRONT = 1;

    private Vector3f lookForward = new Vector3f(1, 0, 0);
    private Vector3f lookUp = new Vector3f(0, 1, 0);
    private Vector3f cameraPosition = new Vector3f(0, 80, 0);

    private int mode;

    public VanillaPlayerCamera(int mode) {
        this.mode = mode;
    }

    @Override
    public void update(Entity player, float partialTicks) {
        //first, interpolate player postition and look atributes using partial ticks
        double posX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
        double posY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
        double posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

        double yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;
        double pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;

        //then, set "flags" ready to simulate all 3 vanilla cameras
        double thirdPersonDistance = 4;
        double addDistance = Math.abs(mode) * thirdPersonDistance;
        double swapDirections = (mode == MODE_3RD_PERSON_FRONT) ? -1 : 1;

        //convert 2d spehere coords (like GPS) to 3d vectors
        double lookX = -Math.sin(yaw * Math.PI / 180);
        double lookZ = -Math.cos(yaw * Math.PI / 180);

        double lookY = -Math.sin(pitch * Math.PI / 180);
        double coefY = Math.cos(pitch * Math.PI / 180);

        //store forward vector, invert if needed
        lookForward.x = (float) (lookX * coefY * swapDirections);
        lookForward.y = (float) (lookY * swapDirections);
        lookForward.z = (float) (lookZ * coefY * swapDirections);

        //compute and store up vector
        double lookYup = coefY;
        double lookYUpCoef = -lookY;

        lookUp.x = (float) (lookX * lookYUpCoef);
        lookUp.y = (float) lookYup;
        lookUp.z = (float) (lookZ * lookYUpCoef);

        //finally, compute position by moving camera if needed
        cameraPosition.x = (float) (posX - lookForward.x * addDistance);
        cameraPosition.y = (float) (posY - lookForward.y * addDistance);
        cameraPosition.z = (float) (posZ - lookForward.z * addDistance);
    }

    @Override
    public Vector3f getCameraPosition() {
        return cameraPosition;
    }

    @Override
    public Vector3f getLookVector() {
        return lookForward;
    }

    @Override
    public Vector3f getLookUpVector() {
        return lookUp;
    }

    @Override
    public boolean isFirstPerson() {
        return mode == MODE_FIRST_PERSON;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onDeselected() {

    }

}
