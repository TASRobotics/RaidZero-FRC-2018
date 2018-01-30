package org.usfirst.frc.team4253.robot2018.teleop;

import org.usfirst.frc.team4253.robot2018.components.Components;

import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

import edu.wpi.first.wpilibj.XboxController;

/**
 * Teleop specific code for the robot.
 */
public class Teleop {

    private static XboxController controller;
    private static XboxController controller2;
    private static TeleopDrive teleopDrive;

    // Note that the constants below should be fine tuned through testing
    private static final int SWITCH_POS = 3000;
    private static final int SCALE_POS = 6000;
    private static final double AXIS_TO_LIFT = 3000;

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

        if (rightTriggerAxis > leftTriggerAxis) {
            Components.getLift().movePWM(controller.getTriggerAxis(kRight));
        } else {
            Components.getLift().movePWM(-controller.getTriggerAxis(kLeft) / 2);
        }

        // Intake
        if (controller.getBButton() || controller2.getBumper(kRight)) {
            Components.getIntake().runIntakeWheelsIn();
        } else if (controller.getAButton() || controller2.getTriggerAxis(kRight) >= 0.8) {
            Components.getIntake().runIntakeWheelsOut();
        } else {
            Components.getIntake().stopWheels();
        }
        if (controller.getYButton()) {
            Components.getIntake().openClaw();
        }
        if (controller.getXButton()) {
            Components.getIntake().closeClaw();
        }

        if (controller2.getBumper(kLeft)) {
            Components.getIntake().openClaw();
        }
        if (controller2.getTriggerAxis(kLeft) >= 0.8) {
            Components.getIntake().closeClaw();
        }

        // Climb
        if (controller2.getBButton()) {
            Components.getRightClimb().move(0.5);
        } else {
            Components.getRightClimb().stop();
        }
        if (controller2.getXButton()) {
            Components.getLeftClimb().move(0.5);
        } else {
            Components.getLeftClimb().stop();
        }

        /* This if for motion magic in the future for the lift
        if (controller.getAButton()) {
            // for lowest target position for the lift
            Components.getLift().move(0 + controller.getY(kLeft) * AXIS_TO_LIFT);
        }
        if (controller.getBButton()) {
            // for switch target position for the lift
            Components.getLift().move(SWITCH_POS + controller.getY(kLeft) * AXIS_TO_LIFT);
        }
        if (controller.getXButton()) {
            // for scale target position for the lift
            Components.getLift().move(SCALE_POS + controller.getY(kLeft) * AXIS_TO_LIFT);
        }
        */
    }

}
