package org.usfirst.frc.team4253.robot2018.camera;

import edu.wpi.first.wpilibj.CameraServer;

/**
 * Camera stuff.
 */
public class Camera {

    /**
     * Starts the camera and displays the camera feed on the smart dashboard.
     */
    public static void start() {
        CameraServer.getInstance().startAutomaticCapture();
    }

}
