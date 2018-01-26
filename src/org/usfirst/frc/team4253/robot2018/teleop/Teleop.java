package org.usfirst.frc.team4253.robot2018.teleop;

import org.usfirst.frc.team4253.robot2018.components.Components;

import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Teleop specific code for the robot.
 */
public class Teleop {

    private static XboxController controller;
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
        if (controller.getBumperPressed(kRight)) {
            Components.getDrive().setHighGear();
        }
        if (controller.getBumperReleased(kRight)) {
            Components.getDrive().setLowGear();
        }
        teleopDrive.drive(controller.getY(kLeft), controller.getY(kRight));

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
        System.out.println("Pigeon: " + Components.getDrive().getPigeon().getFusedHeading());
        System.out.println(
            "Left Enc: " + Components.getDrive().getLeftMotor().getSelectedSensorPosition(0));
        System.out.println("Right Enc: " + Components.getDrive().getRightMotor().getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Left Speed:",
            Components.getDrive().getLeftMotor().getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Right Speed:",
            Components.getDrive().getRightMotor().getSelectedSensorVelocity(0));
    }

}
