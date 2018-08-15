package org.usfirst.frc.team4253.robot2018.camera;

import edu.wpi.first.wpilibj.CameraServer;

/**
 * Camera stuff.
 */
public class Camera {

    private static final double DARK_THRESHOLD = 50; // 255 max
    private static final int LED_CHANNEL = 0;

    /**
     * Starts the camera.
     */
    public static void start() {
        CameraServer.getInstance().startAutomaticCapture();
        // new Thread(() -> {
        // UsbCamera cam =
        // CvSink sink = CameraServer.getInstance().getVideo(cam);
        // Mat mat = new Mat();
        // DigitalOutput led = new DigitalOutput(LED_CHANNEL);
        // while (!Thread.interrupted()) {
        // if (sink.grabFrame(mat) == 0) {
        // System.err.println("Failed to grab camera frame: " + sink.getError());
        // continue;
        // }
        // Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2HSV);
        // led.set(Core.mean(mat).val[2] < DARK_THRESHOLD);
        // }
        // }).start();
    }

}
