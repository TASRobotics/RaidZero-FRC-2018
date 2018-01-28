package org.usfirst.frc.team4253.robot2018.auto;

import org.usfirst.frc.team4253.robot2018.components.Components;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Optional;

/**
 * Autonomous specific code for the robot.
 */
public class Auto {

    private static String gameData;
    private static AutoDrive autoDrive;
    private static SendableChooser<String> autonChoose;
    private static Optional<GeoGebraEntry[]> geoGebraData;

    /**
     * The enum for auton sections.
     */
    private enum Sections {
        Switches, CrossLine, PickUpCube, Scale;

        private static Sections[] vals = values();

        public Sections next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }
    }

    private static Sections sections;

    /**
     * Initializes the autonomous-specific components.
     * 
     * <p>This should be called when the robot starts up.
     */
    public static void initialize() {
        autoDrive = new AutoDrive(Components.getDrive());
        autonChoose = new SendableChooser<String>();
        autonChoose.addDefault("Do Nothing", "Do Nothing");
        autonChoose.addObject("Left", "Left");
        autonChoose.addObject("Center", "Center");
        autonChoose.addObject("Right", "Right");
        SmartDashboard.putData("Auton Starting Chooser", autonChoose);
    }

    /**
     * Configures the components for use in autonomous mode.
     * 
     * <p>This should be called once every time the robot is switched to autonomous mode, before
     * calling {@link #run()}.
     */
    public static void setup() {
        autoDrive.setUp();
        geoGebraData = GeoGebraReader.readFile();
        sections = Sections.Switches;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
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
                if (gameData.charAt(0) == 'L') {
                    switch (sections) {
                        case Switches:
                            // Run center left
                            break;
                        case CrossLine:
                            // Run Left Switch to Cross Line
                            break;
                        case PickUpCube:
                            // Pick Up Cube
                            break;
                        case Scale:
                            if (gameData.charAt(1) == 'L') {
                                // Score Left Scale
                            } else if (gameData.charAt(1) == 'R') {
                                // Score Right Scale
                            }
                            break;
                    }
                } else if (gameData.charAt(0) == 'R') {
                    switch (sections) {
                        case Switches:
                            // Run center right
                            break;
                        case CrossLine:
                            // Run right Switch to Cross Line
                            break;
                        case PickUpCube:
                            // Pick Up Cube
                            break;
                        case Scale:
                            if (gameData.charAt(1) == 'L') {
                                // Score Left Scale
                            } else if (gameData.charAt(1) == 'R') {
                                // Score Right Scale
                            }
                            break;
                    }
                }
                break;

            case "Right":
                break;
        }

        geoGebraData.ifPresent(data -> {
            autoDrive.moveCurve(data);
        });

        if (autoDrive.check()) {
            sections = sections.next();
        }
    }
}
