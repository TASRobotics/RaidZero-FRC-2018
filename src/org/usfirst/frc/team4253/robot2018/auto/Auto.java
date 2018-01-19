package org.usfirst.frc.team4253.robot2018.auto;

import org.usfirst.frc.team4253.robot2018.components.Components;
import org.usfirst.frc.team4253.robot2018.components.Drive;

/**
 * Autonomous specific code for the robot.
 */
public class Auto {
    private static AutoDrive autoDrive;
    private static Drive drive;
    
    /**
     * Initializes the autonomous-specific components.
     * 
     * <p>This should be called when the robot starts up.
     */
    public static void initialize() {
        drive = Components.getDrive();
        autoDrive = new AutoDrive(drive);
    }

    /**
     * Configures the components for use in autonomous mode.
     * 
     * <p>This should be called once every time the robot is switched to autonomous mode, before
     * calling {@link #run()}.
     */
    public static void setup() {

    }

    /**
     * Runs the autonomous code once.
     */
    public static void run() {
        
    }
}
