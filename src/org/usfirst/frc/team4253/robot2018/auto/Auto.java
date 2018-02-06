package org.usfirst.frc.team4253.robot2018.auto;

import org.usfirst.frc.team4253.robot2018.components.Components;
import org.usfirst.frc.team4253.robot2018.components.Lift;

import java.util.List;

/**
 * Autonomous specific code for the robot.
 */
public class Auto {

    private static AutoDrive autoDrive;
    private static Mode mode;
    private static List<AutoPath> paths;
    private static int stage;

    /**
     * Initializes the autonomous-specific components.
     * 
     * <p>This should be called when the robot starts up.
     */
    public static void initialize() {
        AutoChooser.initialize();
        autoDrive = new AutoDrive(Components.getDrive());
    }

    /**
     * Configures the components for use in autonomous mode.
     * 
     * <p>This should be called once every time the robot is switched to autonomous mode, before
     * calling {@link #run()}.
     */
    public static void setup() {
        autoDrive.setup();
        Components.getLift().resetEnc();
        mode = AutoChooser.getMode();
        paths = GeoGebraReader.getPaths(AutoChooser.getStartingSide(), MatchData.getPlateData());
        stage = 0;
    }

    /**
     * Runs the autonomous code.
     * 
     * <p>This should be called repeatedly during autonomous mode.
     */
    public static void run() {
        switch (mode) {
            case SwitchScale:
                runSwitchScale();
                break;
            case DoNothing:
            default:
                break;
        }
    }

    private static void runSwitchScale() {
        if (stage < paths.size()) {
            AutoPath path = paths.get(stage);
            if (autoDrive.moveCurve(path.getMotorData(), path.getReverse()) && transition()) {
                stage++;
                autoDrive.resetEncoders();
            }
        }
    }

    private static boolean transition() {
        switch (stage) {
            case 0:
                return Components.getLift().move(Lift.SWITCH_HEIGHT);
            default:
                return true;
        }
    }

}
