package org.usfirst.frc.team4253.robot2018.camera;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalOutput;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Camera stuff.
 */
public class Camera {

    private static final double DARK_THRESHOLD = 50; // 255 max
    private static final int LED_CHANNEL = 0;

    /**
     * Starts a new thread which captures from the camera and lights the LED if it is dark.
     */
    public static void start() {
        new Thread(() -> {
            UsbCamera cam = CameraServer.getInstance().startAutomaticCapture();
            CvSink sink = CameraServer.getInstance().getVideo(cam);
            Mat mat = new Mat();
            DigitalOutput led = new DigitalOutput(LED_CHANNEL);
            while (!Thread.interrupted()) {
                if (sink.grabFrame(mat) == 0) {
                    System.err.println("Failed to grab camera frame: " + sink.getError());
                    continue;
                }
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2HSV);
                led.set(Core.mean(mat).val[2] < DARK_THRESHOLD);
            }
        }).start();
    }

}
