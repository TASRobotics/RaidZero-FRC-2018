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
    private static Plan plan;
    private static List<AutoPath> paths;
    private static int stage;
    private static int prevIndex;
    private static int sameIndexIterations;
    private static boolean abort;

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
        plan = AutoChooser.getPlan();
        StartingSide startingSide = AutoChooser.getStartingSide();
        PlateData plateData = MatchData.getPlateData();
        paths = GeoGebraReader.getPaths(plan, startingSide, plateData);
        Components.getIntake().closeClaw();
        stage = 0;
        prevIndex = 0;
        sameIndexIterations = 0;
        abort = false;
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
        switch (plan) {
            case SwitchOnly:
            case SwitchThenScale:
            case ActuallyScaleOnly:
            case ScaleThenSwitch:
            case DoubleScale:
            case Elims:
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
            autoDrive.moveCurve(path);
            if (index == prevIndex) {
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
                transition(path.getMode());
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
        switch (path.getMode()) {
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
            case SideSwitch:
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
                        if (autoDrive.getProgress(path) > 0.95) {
                            Components.getIntake().runWheelsOut(0.5);
                        } else if (autoDrive.getProgress(path) > 0.55) {
                            Components.getIntake().stopWheels();
                        } else if (autoDrive.getProgress(path) > 0.49) {
                            Components.getIntake().closeClaw();
                            Components.getIntake().runWheelsIn(0.2);
                        } else {
                            Components.getIntake().openClaw();
                            Components.getIntake().runWheelsIn(1.0);
                        }
                        if (autoDrive.getProgress(path) > 0.5) {
                            Components.getLift().move(Lift.SCALE_HEIGHT);
                        } else if (autoDrive.getProgress(path) > 0.4) {
                            Components.getLift().move(Lift.GRAB_CUBE_HEIGHT);
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
     * 
     * @param prevMode the mode of the stage that was just finished.
     */
    private static void transition(Mode prevMode) {
        switch (prevMode) {
            case SwitchScale:
            case SideSwitch:
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
