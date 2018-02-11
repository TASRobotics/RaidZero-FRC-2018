package org.usfirst.frc.team4253.robot2018.auto;

import org.usfirst.frc.team4253.robot2018.components.Components;
import org.usfirst.frc.team4253.robot2018.components.Lift;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
        Components.getIntake().closeClaw();
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
        SmartDashboard.putNumber("Left Drive Pos",
            Components.getDrive().getLeftMotor().getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Right Drive Pos",
            Components.getDrive().getRightMotor().getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Lift Pos", Components.getLift().getEncoderPos());
        SmartDashboard.putNumber("Angle", Components.getDrive().getPigeon().getFusedHeading());
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
                if (autoDrive.getProgress(path) > 0.3) {
                    Components.getLift().move(Lift.SWITCH_HEIGHT);
                }
                if (autoDrive.getProgress(path) > 0.98) {
                    Components.getIntake().runWheelsOut(0.4);
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
                Components.getIntake().openClaw();
                return true;
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
