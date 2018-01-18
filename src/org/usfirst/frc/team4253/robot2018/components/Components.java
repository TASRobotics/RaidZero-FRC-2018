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

    /**
     * Initializes each component by calling its constructor.
     * 
     * <p>Make sure this method has been called before accessing any components, as they will be
     * null before this method is called.
     */
    public static void initialize() {
        drive = new Drive(1, 2, 3, 4, 0, 1);
    }

    /**
     * Returns the drive component.
     * 
     * @return the drive component
     */
    public static Drive getDrive() {
        return drive;
    }

}
