package org.usfirst.frc.team4253.robot2018.auto;

import org.usfirst.frc.team4253.robot2018.components.Components;
import org.usfirst.frc.team4253.robot2018.components.Lift;
import org.usfirst.frc.team4253.robot2018.components.MotorSettings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.List;

/**
 * Autonomous specific code for the robot.
 */
public class Auto {

    private static final int TEST_FORWARD_DISTANCE = 80;
    private static final int BACKUP_DISTANCE = 10;
    private static final int DISTANCE_TOLERANCE = 1;

    private static AutoDrive autoDrive;
    private static Mode mode;
    private static List<AutoPath> paths;
    private static int stage;
    private static int prevIndex;
    private static int sameIndexIterations;
    private static boolean abort;
    // private static boolean testForwardSafe;
    private static int testForwardState;
    private static int saveLeftTicks = 0, saveRightTicks = 0;
    private static double saveAngle = 0;

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
        Plan plan = AutoChooser.getPlan();
        StartingSide startingSide = AutoChooser.getStartingSide();
        PlateData plateData = MatchData.getPlateData();
        switch (plan) {
            case SwitchThenScale:
                if (startingSide != StartingSide.Center
                    && plateData.getNearSwitchSide().toStartingSide() != startingSide) {
                    mode = Mode.ScaleOnly;
                } else {
                    mode = Mode.SwitchScale;
                }
                break;
            case SwitchOnly:
                mode = Mode.SwitchScale;
                break;
            case ScaleThenSwitch:
            case ActuallyScaleOnly:
                mode = Mode.ScaleOnly;
                break;
            case ScaleFirstIfSameSide:
                if (plateData.getNearSwitchSide() == plateData.getScaleSide()) {
                    mode = Mode.ScaleOnly;
                } else {
                    mode = Mode.SwitchScale;
                }
                break;
            case CrossLine:
                mode = Mode.CrossLine;
                break;
            case Barker:
                mode = Mode.Barker;
                break;
            case DoNothing:
                mode = Mode.DoNothing;
                break;
        }
        paths = GeoGebraReader.getPaths(plan, mode, startingSide, plateData);
        Components.getIntake().closeClaw();
        stage = 0;
        prevIndex = 0;
        sameIndexIterations = 0;
        abort = false;
        // testForwardSafe = false;
        testForwardState = 0;
        if (plateData.getNearSwitchSide() == Side.Left && mode == Mode.Barker) {
            mode = Mode.SwitchScale;
        } else if (plateData.getNearSwitchSide() == Side.Right && mode == Mode.Barker) {
            mode = Mode.ScaleOnly;
        }
    }

    /**
     * Runs the autonomous code.
     * 
     * <p>This should be called repeatedly during autonomous mode.
     */
    public static void run() {
        if (abort) {
            return;
        }
        switch (mode) {
            case Barker:
            case SwitchScale:
            case ScaleOnly:
                runPathAuto();
                break;
            case CrossLine:
                crossLine();
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
     * Runs autonomous with paths.
     */
    private static void runPathAuto() {
        if (stage < paths.size()) {
            AutoPath path = paths.get(stage);
            int index = autoDrive.getCurrentIndex(path);
            if (mode.equals(Mode.SwitchScale) && (stage == 2) && false) {
                System.out.println(testForwardState);
                switch (testForwardState) {
                    case 0:
                        if (index <= TEST_FORWARD_DISTANCE) {
                            autoDrive.moveCurve(path);
                        } else {
                            testForwardState = 1;
                            Components.getLift().move(Lift.GRAB_CUBE_HEIGHT);
                            saveAngle = Components.getDrive().getPigeon().getFusedHeading();
                            saveLeftTicks = Components.getDrive().getLeftMotor()
                                .getSelectedSensorPosition(MotorSettings.PID_IDX);
                            saveRightTicks = Components.getDrive().getRightMotor()
                                .getSelectedSensorPosition(MotorSettings.PID_IDX);
                        }
                        break;
                    case 1:
                        if (index >= (TEST_FORWARD_DISTANCE - BACKUP_DISTANCE
                            + DISTANCE_TOLERANCE)) {
                            autoDrive.moveStraight(TEST_FORWARD_DISTANCE - BACKUP_DISTANCE,
                                saveRightTicks, saveLeftTicks, saveAngle);
                        } else {
                            testForwardState = 2;
                        }
                        break;
                    case 2:
                        if (index <= (TEST_FORWARD_DISTANCE - DISTANCE_TOLERANCE)) {
                            autoDrive.moveStraight(TEST_FORWARD_DISTANCE, saveRightTicks,
                                saveLeftTicks, saveAngle);
                        } else {
                            testForwardState = 3;
                        }
                        break;
                    case 3:
                        autoDrive.moveCurve(path);
                        break;
                }
            } else {
                autoDrive.moveCurve(path);
            }
            if ((index == prevIndex) && testForwardState != 1 && testForwardState != 2) { // &&
                                                                                          // moveForward)
                                                                                          // {
                sameIndexIterations++;
                double progress = autoDrive.getProgress(path);
                // Safety code to stop drivetrain after a stopping collision or a de-alignment.
                if ((sameIndexIterations >= 500 / 20 || Math.abs(autoDrive.getCurrentAngle(path)
                        - Components.getDrive().getPigeon().getFusedHeading()) > 15)
                    && progress <= 0.9 && progress >= 0.1) {
                    autoDrive.pauseDrive();
                    Components.getLift().movePWM(0);
                    Components.getIntake().stopWheels();
                    abort = true;
                    System.out.println("ABORTED");
                    System.out.println("Angle Diff:" + Math.abs(autoDrive.getCurrentAngle(path)
                        - Components.getDrive().getPigeon().getFusedHeading()));
                    System.out.println("Number of stopped iterations:" + sameIndexIterations);
                    return;
                }
            } else {
                sameIndexIterations = 0;
            }
            prevIndex = index;
            moveOtherComponents(path);
            if (autoDrive.checkFinished(path)) {
                transition();
                autoDrive.finishPath(path);
                stage++;
                prevIndex = 0;
            }
        } else {
            autoDrive.pauseDrive();
        }
    }

    /**
     * Runs other components that are not the drive.
     * 
     * @param path the geogebra path.
     */
    private static void moveOtherComponents(AutoPath path) {
        if (stage == 0) {
            if (autoDrive.getCurrentIndex(path) == 0) {
                Components.getIntake().runWheelsIn(0.5);
            } else if (autoDrive.getCurrentIndex(path) == 1) {
                Components.getIntake().stopWheels();
            }
        }
        switch (mode) {
            case SwitchScale:
                switch (stage) {
                    case 0:
                        if (autoDrive.getProgress(path) > 0.3) {
                            Components.getLift().move(Lift.SWITCH_HEIGHT);
                        }
                        if (autoDrive.getProgress(path) > 0.98) {
                            Components.getIntake().runWheelsOut(0.4);
                        }
                        if (autoDrive.getProgress(path) > 0.99) {
                            Components.getIntake().openClaw();
                        }
                        break;
                    case 1:
                        Components.getLift().move(Lift.SAFE_HEIGHT);
                        Components.getIntake().stopWheels();
                        break;
                    case 2:
                        if (autoDrive.getProgress(path) > 0.5) {
                            Components.getIntake().stopWheels();
                        } else if (autoDrive.getProgress(path) > 0.35) {
                            Components.getIntake().closeClaw();
                            Components.getIntake().runWheelsIn(0.2);
                        } else {
                            Components.getIntake().openClaw();
                            Components.getIntake().runWheelsIn(1.0);
                        }
                        if (autoDrive.getProgress(path) > 0.5) {
                            Components.getLift().move(Lift.SCALE_HEIGHT);
                        }
                        if (autoDrive.getProgress(path) > 0.95) {
                            Components.getIntake().runWheelsOut(0.5);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case ScaleOnly:
                switch (stage) {
                    case 0:
                        if (autoDrive.getProgress(path) > 0.5) {
                            Components.getLift().move(Lift.SCALE_HEIGHT);
                        }
                        if (autoDrive.getProgress(path) > 0.95) {
                            Components.getIntake().runWheelsOut(0.5);
                        }
                        break;
                    case 1:
                        if (autoDrive.getProgress(path) > 0.1) {
                            Components.getLift().move(Lift.GRAB_CUBE_HEIGHT);
                        }
                        break;
                    case 2:
                        if (autoDrive.getProgress(path) > 0.5) {
                            Components.getIntake().runWheelsIn(1);
                        }
                        if (autoDrive.getProgress(path) > 0.9) {
                            Components.getLift().move(Lift.SWITCH_HEIGHT);
                        }
                        if (autoDrive.getProgress(path) > 0.98) {
                            Components.getIntake().runWheelsOut(0.4);
                        }
                        if (autoDrive.getProgress(path) > 0.99) {
                            Components.getIntake().openClaw();
                        }
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * Runs the transition phase between stages.
     */
    private static void transition() {
        switch (mode) {
            case SwitchScale:
                switch (stage) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        Components.getIntake().stopWheels();
                        break;
                    default:
                        break;
                }
                break;
            case ScaleOnly:
                switch (stage) {
                    case 0:
                        Components.getIntake().stopWheels();
                        break;
                    case 1:
                        break;
                    case 2:
                        Components.getIntake().stopWheels();
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * Runs the cross line autonomous.
     */
    private static void crossLine() {
        autoDrive.moveStraight(11000);
    }

}
