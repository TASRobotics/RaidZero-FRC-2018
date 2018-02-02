package org.usfirst.frc.team4253.robot2018.components;

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
    private static Ramps ramps;

    /**
     * Initializes each component by calling its constructor.
     * 
     * <p>Make sure this method has been called before accessing any components, as they will be
     * null before this method is called.
     */
    public static void initialize() {
        drive = new Drive(1, 2, 3, 4, 0, 1);
        intake = new Intake(10, 11, 4, 5);
        lift = new Lift(12);
        ramps = new Ramps(13, 14, 2);
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
    public static Ramps getRamps() {
        return ramps;
    }
}
