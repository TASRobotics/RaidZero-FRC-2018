package org.usfirst.frc.team4253.robot2018.auto;

import org.usfirst.frc.team4253.robot2018.components.Components;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import java.util.Optional;

/**
 * Autonomous specific code for the robot.
 */
public class Auto {

    private static AutoDrive autoDrive;
    private static SendableChooser<String> autonChoose;
    private static Optional<GeoGebraEntry[]> geoGebraData;

    /**
     * Initializes the autonomous-specific components.
     * 
     * <p>This should be called when the robot starts up.
     */
    public static void initialize() {
        autoDrive = new AutoDrive(Components.getDrive());
    }

    /**
     * Configures the components for use in autonomous mode.
     * 
     * <p>This should be called once every time the robot is switched to autonomous mode, before
     * calling {@link #run()}.
     */
    public static void setup() {
        autoDrive.setUp();
        autonChoose = new SendableChooser<String>();
        autonChoose.addDefault("Do Nothing", "Do Nothing");
        autonChoose.addObject("Left", "Left");
        autonChoose.addObject("Center", "Center");
        autonChoose.addObject("Right", "Right");
        geoGebraData = GeoGebraReader.readFile();
    }

    /**
     * Runs the autonomous code once.
     */
    public static void run() {
        String position = autonChoose.getSelected();
        switch (position) {
            case "Do Nothing":
                break;

            case "Left":
                break;

            case "Center":
                break;

            case "Right":
                break;
        }
        // autoDrive.moveStraight(10000);
        geoGebraData.ifPresent(data -> {
            autoDrive.moveCurve(data, 10500);
        });
    }
}
