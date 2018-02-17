package org.usfirst.frc.team4253.robot2018.teleop;

import org.usfirst.frc.team4253.robot2018.components.Components;
import org.usfirst.frc.team4253.robot2018.components.Lift;

import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Teleop specific code for the robot.
 */
public class Teleop {

    private static XboxController controller;
    private static XboxController controller2;
    private static TeleopDrive teleopDrive;

    private static final double AXIS_TO_LIFT = 1000;
    private static final double INTAKE_WHEEL_POWER = 0.9;
    private static final double ANALOG_THRESHOLD = 0.8;
    private static boolean released = false;
    private static boolean timeToClimb = false;

    /**
     * Initializes the teleop-specific components.
     * 
     * <p>This should be called when the robot starts up.
     */
    public static void initialize() {
        controller = new XboxController(0);
        controller2 = new XboxController(1);
        teleopDrive = new TeleopDrive(Components.getDrive());
    }

    /**
     * Configures the components for use in teleop mode.
     * 
     * <p>This should be called once every time the robot is switched to teleop mode, before calling
     * {@link #run()}.
     */
    public static void setup() {
        teleopDrive.setup();
        Components.getLift().resetEnc();
        Components.getClimb().setup();
        released = false;
        timeToClimb = false;
    }

    /**
     * Runs the teleop code.
     * 
     * <p>This should be called repeatedly during teleop mode.
     */
    public static void run() {
        // Drive
        if (controller.getBumperPressed(kRight)) {
            Components.getDrive().setHighGear();
        }
        if (controller.getBumperReleased(kRight)) {
            Components.getDrive().setLowGear();
        }
        teleopDrive.drive(controller.getY(kLeft), controller.getY(kRight));

        // Lift
        double rightTriggerAxis = controller.getTriggerAxis(kRight);
        double leftTriggerAxis = controller.getTriggerAxis(kLeft);
        double player2LiftInput = 0;
        if (!timeToClimb) {
            player2LiftInput = -controller2.getY(kRight);
        } else {
            player2LiftInput = 0;
        }

        if (rightTriggerAxis > leftTriggerAxis) {
            Components.getLift()
                .movePWM(controller.getTriggerAxis(kRight) + player2LiftInput);
        } else {
            Components.getLift()
                .movePWM(-controller.getTriggerAxis(kLeft) + player2LiftInput);
        }

        // Motion Magic Lift
        if (controller2.getXButton()) {
            // for lowest target position for the lift
            Components.getLift()
                .move(Lift.GRAB_CUBE_HEIGHT + controller.getY(kLeft) * AXIS_TO_LIFT);
        }
        if (controller2.getYButton()) {
            // for switch target position for the lift
            Components.getLift().move(Lift.SWITCH_HEIGHT + controller.getY(kLeft) * AXIS_TO_LIFT);
        }
        if (controller2.getBButton()) {
            // for scale target position for the lift
            Components.getLift().move(Lift.SCALE_HEIGHT + controller.getY(kLeft) * AXIS_TO_LIFT);
        }

        // Intake
        if (controller2.getTriggerAxis(kRight) >= 0.8) {
            Components.getIntake().runWheelsIn(INTAKE_WHEEL_POWER);
        } else if (controller.getBumper(kLeft)
            || controller2.getTriggerAxis(kLeft) >= ANALOG_THRESHOLD) {
            Components.getIntake().runWheelsOut(INTAKE_WHEEL_POWER);
        } else {
            Components.getIntake().stopWheels();
        }
        if (controller2.getY(kLeft) >= ANALOG_THRESHOLD) {
            Components.getIntake().openClaw();
        }
        if (controller2.getY(kLeft) <= -ANALOG_THRESHOLD) {
            Components.getIntake().closeClaw();
        }

        // Winch
        if (controller2.getBumper(kRight) && released) {
            Components.getClimb().moveWinch();
        } else {
            Components.getClimb().stopWinch();
        }

        // Arm
        if (released && timeToClimb) {
            if (Math.abs(controller2.getY(kRight)) <= 0.1) {
                Components.getClimb().stopArm();
            } else {
                Components.getClimb().moveArm(controller2.getY(kRight));
            }
        } else {
            Components.getClimb().stopArm();
        }
        if (controller.getBackButton() && controller.getStartButton()) {
            Components.getClimb().releaseArm();
            released = true;
        }

        // Switch between lift or arm
        if (controller2.getStartButtonPressed()) {
            timeToClimb = !timeToClimb;
        }
        SmartDashboard.putBoolean("Climb Mode", timeToClimb);
        SmartDashboard.putNumber("Lift Pos", Components.getLift().getEncoderPos());
        SmartDashboard.putNumber("Drive Left Vel",
            Components.getDrive().getLeftMotor().getSelectedSensorVelocity(0));
    }
}
