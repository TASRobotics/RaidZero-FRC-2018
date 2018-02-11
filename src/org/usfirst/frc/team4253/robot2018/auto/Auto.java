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

    /**
     * Runs the standard switch and scale autonomous.
     */
    private static void runSwitchScale() {
        if (stage < paths.size()) {
            AutoPath path = paths.get(stage);
            autoDrive.moveCurve(path);
            moveOtherComponents(path);
            if (autoDrive.checkFinished(path) && transition()) {
                stage++;
                autoDrive.resetEncoders();
            }
        }
    }

    private static void moveOtherComponents(AutoPath path) {
        switch (stage) {
            case 0:
                if (autoDrive.getProgress(path) > 0.5) {
                    Components.getLift().move(Lift.SWITCH_HEIGHT);
                }
                break;
            case 1:
                Components.getLift().move(Lift.GRAB_CUBE_HEIGHT);
                break;
            case 2:
                if (autoDrive.getEncoderPos() > 7000) {
                    Components.getIntake().idle();
                }
                if (autoDrive.getProgress(path) > 0.7) {
                    Components.getLift().move(Lift.SCALE_HEIGHT);
                }
                break;
            default:
                break;
        }
    }

    private static boolean transition() {
        switch (stage) {
            case 0:
                if (Components.getLift().checkFinished(Lift.SWITCH_HEIGHT)) {
                    Components.getIntake().release();
                    return true;
                }
                return false;
            case 1:
                return Components.getLift().checkFinished(Lift.GRAB_CUBE_HEIGHT);
            case 2:
                if (Components.getLift().checkFinished(Lift.SCALE_HEIGHT)) {
                    Components.getIntake().release();
                    return true;
                }
                return false;
            default:
                return true;
        }
    }

}
