package org.usfirst.frc.team4253.robot2018.components;

import edu.wpi.first.wpilibj.CameraServer;

/**
 * The components of the robot.
 * 
 * <p>Do not construct an instance of this class.
 * 
 * <p>Make sure the {@link #initialize()} method has been called before accessing any components.
 */
public class Components {

    private static Drive drive;
    private static Intake intake;
    private static Lift lift;
    private static Climb climb;

    /**
     * Initializes each component by calling its constructor.
     * 
     * <p>Also starts video capture from camera.
     * 
     * <p>Make sure this method has been called before accessing any components, as they will be
     * null before this method is called.
     */
    public static void initialize() {
        intake = new Intake(10, 11, 0, 1); 
        drive = new Drive(1, 2, 3, 4, 4, 5);
        lift = new Lift(12, 15);
        climb = new Climb(13, 14, 2, 3);
        CameraServer.getInstance().startAutomaticCapture();
    }

    /**
     * Returns the drive component.
     * 
     * @return the drive component
     */
    public static Drive getDrive() {
        return drive;
    }

    /**
     * Returns the intake component.
     * 
     * @return the intake component
     */
    public static Intake getIntake() {
        return intake;
    }

    /**
     * Returns the lift component.
     * 
     * @return the lift component
     */
    public static Lift getLift() {
        return lift;
    }

    /**
     * Returns the ramps component.
     * 
     * @return the ramps component
     */
    public static Climb getClimb() {
        return climb;
    }
}
