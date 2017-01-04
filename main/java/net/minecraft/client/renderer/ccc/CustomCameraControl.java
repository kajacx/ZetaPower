package net.minecraft.client.renderer.ccc;

import java.util.ArrayList;

import net.minecraft.client.settings.GameSettings;

public class CustomCameraControl {
    public static ArrayList<IPlayerCamera> cameras = new ArrayList<>();

    static {
        cameras.add(new VanillaPlayerCamera(VanillaPlayerCamera.MODE_FIRST_PERSON));
        cameras.add(new VanillaPlayerCamera(VanillaPlayerCamera.MODE_3RD_PERSON_BEHIND));
        cameras.add(new VanillaPlayerCamera(VanillaPlayerCamera.MODE_3RD_PERSON_FRONT));
    }

    public static boolean isIndexValid(int index) {
        return index >= 0 && index < cameras.size();
    }

    public static void changeCameraTo(GameSettings settings, IPlayerCamera camera) {
        changeCameraTo(settings, cameras.indexOf(camera));
    }

    public static void changeCameraTo(GameSettings settings, int cameraIndex) {
        if (!isIndexValid(cameraIndex)) {
            //TODO: what to do with error reporting?
            System.out.println("invalid camera index: " + cameraIndex);
            return;
        }

        //if(cameraIndex == settings.thirdPersonView) {
        //don't ignore setting camera to the same camera
        //the cameras in the array might have changed places
        //}

        if (isIndexValid(settings.thirdPersonView)) {
            cameras.get(settings.thirdPersonView).onDeselected();
        }

        cameras.get(cameraIndex).onSelected();
        settings.thirdPersonView = cameraIndex;
    }

    public static IPlayerCamera getCamera(GameSettings settings) {
        if (!isIndexValid(settings.thirdPersonView)) {
            //TODO: what to do with error reporting?
            System.out.println("invalid camera index: " + settings.thirdPersonView);
            settings.thirdPersonView = 0;
        }

        return cameras.get(settings.thirdPersonView);
    }
}
