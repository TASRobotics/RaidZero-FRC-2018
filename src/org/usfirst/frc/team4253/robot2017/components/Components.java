package org.usfirst.frc.team4253.robot2017.components;

import edu.wpi.first.wpilibj.Compressor;

/**
 * The components of the robot.
 * 
 * <p>All the variables and methods in this class should be static, because there is only one set of
 * components throughout the whole robot. Do not construct an instance of this class.
 * 
 * <p>Make sure you call the {@code initialize()} method before accessing any components.
 */
public class Components {

    private static Drive drive;
    private static Climb climb;
    private static GearDoor gearDoor;
    private static Intake intake;
    private static Compressor compressor;

    /**
     * Initializes each component by calling its constructor.
     * 
     * <p>Make sure you call this method before accessing any components, as they will be null
     * before this method is called.
     */
    public static void initialize() {
        drive = new Drive(1, 2, 3, 4, 5, 6, 0, 1);
        climb = new Climb(15, 13);
        gearDoor = new GearDoor(2, 3);
        intake = new Intake(11);
        compressor = new Compressor();
        compressor.stop();
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
     * Returns the climb component.
     * 
     * @return the climb component
     */
    public static Climb getClimb() {
        return climb;
    }

    /**
     * Returns the gear door component.
     * 
     * @return the gear door component
     */
    public static GearDoor getGearDoor() {
        return gearDoor;
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
     * Returns the compressor component.
     * 
     * @return the compressor component
     */
    public static Compressor getCompressor() {
        return compressor;
    }

}
