package org.usfirst.frc.team4253.robot2017;

/**
 * Some robot-wide settings.
 * 
 * <p>Other classes can use the constants defined in this class to have different behavior based on
 * different settings of the robot. This way the settings can be easily changed in one location.
 */
public class Settings {

    /**
     * The version of the robot.
     * 
     * <p>There are some minor differences between the several iterations of the robot that were
     * built. Code that is different depending on the version should check the value of this
     * constant to determine the robot version.
     */
    public static final Version VERSION = Version.V4;

    /**
     * The possible versions of the robot.
     */
    public static enum Version { V2, V3, V4 }

}
