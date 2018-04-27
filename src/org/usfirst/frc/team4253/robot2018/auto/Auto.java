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
    private static List<Movement> movements;
    private static Movement currentMovement;
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
        movements = GeoGebraReader.getPaths(plan, startingSide, plateData);
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
            case ScaleOnly:
            case ScaleThenSwitch:
            case DoubleScale:
            case DoubleSwitch:
            case TripleSwitch:
                runPathAuto();
                break;
            case Elims:
                if (movements.isEmpty()) {
                    crossLine();
                    System.out.println("path is empty");
                } else {
                    runPathAuto();
                }
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
        if (stage < movements.size()) {
            currentMovement = movements.get(stage);
            currentMovement.run(autoDrive);
            if (currentMovement instanceof AutoPath) {
                AutoPath currentPath = (AutoPath) currentMovement;
                int index = autoDrive.getCurrentIndex(currentPath);
                if (index == prevIndex) {
                    sameIndexIterations++;
                    double progress = autoDrive.getPathProgress(currentPath);
                    // Safety code to stop drivetrain after a stopping collision or a de-alignment.
                    double angle = Components.getDrive().getPigeon().getFusedHeading();
                    if ((sameIndexIterations >= 500 / 20
                        || Math.abs(autoDrive.getCurrentAngle(currentPath) - angle) > 20)
                        && progress <= 0.9 && progress >= 0.1) {
                        autoDrive.pauseDrive();
                        Components.getLift().movePWM(0);
                        Components.getIntake().stopWheels();
                        abort = true;
                        System.out.println("ABORTED");
                        System.out.println("Angle Diff:"
                            + Math.abs(autoDrive.getCurrentAngle(currentPath) - angle));
                        System.out.println("Number of stopped iterations:" + sameIndexIterations);
                        return;
                    }
                } else {
                    sameIndexIterations = 0;
                }
                prevIndex = index;
            }
            moveOtherComponents(currentMovement);
            if (currentMovement.checkFinished(autoDrive) && transition(currentMovement.getMode())) {
                currentMovement.finish(autoDrive);
                stage++;
                prevIndex = 0;
                if (stage < movements.size()) {
                    Movement nextMovement = movements.get(stage);
                    if (nextMovement instanceof Turn) {
                        double angle = Components.getDrive().getPigeon().getFusedHeading();
                        ((Turn) nextMovement).startWithAngle(angle);
                    }
                }
            }
        }
        // else if (plan == Plan.ScaleThenSwitch && stage == 3
        // && currentMovement.getStart() == currentMovement.getEnd().toStartingSide()) {
        // autoDrive.moveStraight(10);
        // if (autoDrive.getEncoderPos() > 9) {
        // Components.getIntake().runWheelsOut(0.4);
        // if (autoDrive.getEncoderPos() > 9.5) {
        // Components.getIntake().openClaw();
        // }
        // }
        // }
        else {
            autoDrive.pauseDrive();
        }
    }

    /**
     * Runs other components that are not the drive.
     * 
     * @param movement the geogebra path.
     */
    private static void moveOtherComponents(Movement movement) {
        switch (movement.getMode()) {
            case SwitchScale:
                switch (stage) {
                    case 0:
                        if (movement.getProgress(autoDrive) > 0.3) {
                            Components.getLift().move(Lift.SWITCH_HEIGHT);
                        }
                        if (movement.getProgress(autoDrive) > 0.98) {
                            Components.getIntake().runWheelsOut(0.4);
                        }
                        if (movement.getProgress(autoDrive) > 0.99) {
                            Components.getIntake().openClaw();
                        }
                        break;
                    case 1:
                        Components.getLift().move(Lift.SAFE_HEIGHT);
                        Components.getIntake().stopWheels();
                        break;
                    case 2:
                        if (movement.getProgress(autoDrive) > 0.95) {
                            Components.getIntake().runWheelsOut(0.5);
                        } else if (movement.getProgress(autoDrive) > 0.55) {
                            Components.getIntake().stopWheels();
                        } else if (movement.getProgress(autoDrive) > 0.45) {
                            Components.getIntake().closeClaw();
                            Components.getIntake().runWheelsIn(0.2);
                        } else {
                            Components.getIntake().openClaw();
                            Components.getIntake().runWheelsIn(1.0);
                        }
                        if (movement.getProgress(autoDrive) > 0.5) {
                            Components.getLift().move(Lift.SCALE_HEIGHT);
                        } else if (movement.getProgress(autoDrive) > 0.3) {
                            Components.getLift().move(Lift.GRAB_CUBE_HEIGHT);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case SideSwitch:
                switch (stage) {
                    case 0:
                        if (movement.getProgress(autoDrive) > 0.3) {
                            Components.getLift().move(Lift.SWITCH_HEIGHT);
                        }
                        if (movement.getProgress(autoDrive) > 0.98) {
                            Components.getIntake().runWheelsOut(0.4);
                        }
                        if (movement.getProgress(autoDrive) > 0.99) {
                            Components.getIntake().openClaw();
                        }
                        break;
                    case 1:
                        Components.getLift().move(Lift.SAFE_HEIGHT);
                        Components.getIntake().stopWheels();
                        break;
                    case 2:
                        if (movement.getProgress(autoDrive) > 0.95) {
                            Components.getIntake().runWheelsOut(0.5);
                        } else if (movement.getProgress(autoDrive) > 0.55) {
                            Components.getIntake().stopWheels();
                        } else if (movement.getProgress(autoDrive) > 0.45) {
                            Components.getIntake().closeClaw();
                            Components.getIntake().runWheelsIn(0.2);
                        } else {
                            Components.getIntake().openClaw();
                            Components.getIntake().runWheelsIn(1.0);
                        }
                        if (movement.getProgress(autoDrive) > 0.5) {
                            Components.getLift().move(Lift.SCALE_HEIGHT);
                        } else if (movement.getProgress(autoDrive) > 0.3) {
                            Components.getLift().move(Lift.GRAB_CUBE_HEIGHT);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case ScaleFirst:
                switch (stage) {
                    case 0:
                        if (movement.getProgress(autoDrive) > 0.5) {
                            Components.getLift().move(Lift.SCALE_HEIGHT);
                        }
                        if (movement.getProgress(autoDrive) > 0.95) {
                            Components.getIntake().runWheelsOut(0.5);
                        }
                        break;
                    case 1:
                        if (movement.getProgress(autoDrive) > 0.2) {
                            Components.getLift().move(Lift.GRAB_CUBE_HEIGHT);
                        }
                        break;
                    case 2:
                        if (movement instanceof AutoPath && ((AutoPath) movement)
                            .getStart() == ((AutoPath) movement).getEnd().toStartingSide()) {
                            Components.getIntake().runWheelsIn(1);
                            if (movement.getProgress(autoDrive) > 0.95) {
                                Components.getIntake().closeClaw();
                            }
                        } else {
                            if (movement.getProgress(autoDrive) < 0.65) {
                                Components.getIntake().runWheelsIn(1);
                            }

                            if (movement.getProgress(autoDrive) > 0.65) {
                                // Components.getLift().move(Lift.SWITCH_HEIGHT);
                            }
                            if (movement.getProgress(autoDrive) > 0.98) {
                                // Components.getIntake().runWheelsOut(0.4);
                            }
                            if (movement.getProgress(autoDrive) > 0.99) {
                                Components.getIntake().closeClaw();
                            } else if (movement.getProgress(autoDrive) > 0.6) {
                                // Components.getIntake().openClaw();
                            }
                        }
                        break;
                    case 3:
                        Components.getIntake().stopWheels();
                        Components.getLift().move(Lift.SWITCH_HEIGHT);
                        break;
                    case 4:
                        Components.getLift().move(Lift.SCALE_HEIGHT);
                        if (movement.getProgress(autoDrive) > 0.95) {
                            Components.getIntake().runWheelsOut(0.5);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case TripleSwitch: {
                switch (stage) {
                    case 1:
                    case 5:
                        if (movement.getProgress(autoDrive) > 0.4) {
                            Components.getLift().move(Lift.GRAB_CUBE_HEIGHT);
                        }
                        break;
                    case 2:
                    case 6:
                        Components.getIntake().runWheelsIn(1);
                        if (movement.getProgress(autoDrive) < 0.5) {
                            Components.getIntake().openClaw();
                        } else {
                            Components.getIntake().closeClaw();
                        }
                        break;
                    case 3:
                    case 7:
                        if (movement.getProgress(autoDrive) > 0.5) {
                            Components.getLift().move(Lift.SWITCH_HEIGHT);
                            Components.getIntake().stopWheels();
                        }
                        break;
                    case 4:
                    case 8:
                        if (movement.getProgress(autoDrive) > 0.9) {
                            Components.getIntake().runWheelsOut(0.4);
                        }
                        break;
                }
                break;
            }
            default:
                break;
        }
    }

    /**
     * Runs the transition phase between stages.
     * 
     * @param prevMode the mode of the stage that was just finished
     * @return whether the transition is done and we can move to the next stage
     */
    private static boolean transition(Mode prevMode) {
        switch (prevMode) {
            case SwitchScale:
            case SideSwitch:
                switch (stage) {
                    case 2:
                        Components.getIntake().stopWheels();
                        return true;
                    default:
                        return true;
                }
            case ScaleFirst:
                switch (stage) {
                    case 0:
                        Components.getIntake().stopWheels();
                        return true;
                    case 1:
                        Components.getIntake().openClaw();
                        return true;
                    case 2:
                        Components.getIntake().stopWheels();
                        if (plan == Plan.ScaleThenSwitch) {
                            Components.getLift().move(Lift.SWITCH_HEIGHT);
                            if (Components.getLift().getEncoderPos() > Lift.SAFE_HEIGHT) {
                                autoDrive.resetEncoders();
                                autoDrive.resetPigeon();
                                return true;
                            }
                            return false;
                        }
                        return true;
                    default:
                        return true;
                }
            case TripleSwitch:
                switch (stage) {
                    case 6:
                        if (plan == Plan.DoubleSwitch) {
                            Components.getIntake().stopWheels();
                        }
                        return true;
                }
                return true;
            default:
                return true;
        }
    }

    /**
     * Runs the cross line autonomous.
     */
    private static void crossLine() {
        autoDrive.moveStraight(140);
    }

}
