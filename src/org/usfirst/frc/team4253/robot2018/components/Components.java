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
    private static Climb climb;

    /**
     * Initializes each component by calling its constructor.
     * 
     * <p>Make sure this method has been called before accessing any components, as they will be
     * null before this method is called.
     */
    public static void initialize() {
        drive = new Drive(1, 2, 3, 4, 0, 1);
        intake = new Intake(8, 9, 2, 3);
        lift = new Lift(10, 11); // placeholder numbers
        climb = new Climb(12, 13, 4, 5);
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
     * Returns the climb component.
     * 
     * @return the climb component
     */
    public static Climb getClimb() {
        return climb;
    }

}
