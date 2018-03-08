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

    private static XboxController controller1;
    private static XboxController controller2;
    private static TeleopDrive teleopDrive;

    private static final double INTAKE_WHEEL_POWER = 0.9;

    private static boolean climbMode = false;
    private static boolean defeatRamp = false;

    /**
     * Initializes the teleop-specific components.
     * 
     * <p>This should be called when the robot starts up.
     */
    public static void initialize() {
        controller1 = new XboxController(0);
        controller2 = new XboxController(1);
        teleopDrive = new TeleopDrive(Components.getDrive(), Components.getLift());
    }

    /**
     * Configures the components for use in teleop mode.
     * 
     * <p>This should be called once every time the robot is switched to teleop mode, before calling
     * {@link #run()}.
     */
    public static void setup() {
        teleopDrive.setup();
        Components.getClimb().setup();
        climbMode = false;
        defeatRamp = false;
    }

    /**
     * Runs the teleop code.
     * 
     * <p>This should be called repeatedly during teleop mode.
     */
    public static void run() {
        // Player 1
        // defeat Ramp timing
        if (controller1.getStartButton() && controller1.getBackButton()) {
            defeatRamp = true;
        }
        // Drive
        if (controller1.getBumperPressed(kLeft)) {
            Components.getDrive().setHighGear();
        }
        if (controller1.getBumperPressed(kRight)) {
            Components.getDrive().setLowGear();
        }
        teleopDrive.drive(controller1.getY(kLeft), controller1.getY(kRight), defeatRamp);

        // Lift
        boolean player1IntakeActive = true;
        double rightTriggerAxis = controller1.getTriggerAxis(kRight);
        double leftTriggerAxis = controller1.getTriggerAxis(kLeft);

        if (rightTriggerAxis > 0.1) {
            Components.getLift().movePWM(controller1.getTriggerAxis(kRight));
        } else if (leftTriggerAxis > 0.1) {
            Components.getLift().movePWM(-controller1.getTriggerAxis(kLeft) * 0.5);
        } else if (controller2.getAButton()) {
            Components.getLift().move(Lift.GRAB_CUBE_HEIGHT);
        } else if (controller2.getXButton()) {
            Components.getLift().move(Lift.SWITCH_HEIGHT);
        } else if (controller2.getYButton()) {
            Components.getLift().move(Lift.SCALE_HEIGHT);
        } else {
            Components.getLift().movePWM(0);
        }

        // Intake
        if (controller1.getBumper(kLeft)) {
            Components.getIntake().runWheelsOut(INTAKE_WHEEL_POWER);
            player1IntakeActive = true;
        } else {
            player1IntakeActive = false;
        }
        
        // Switch between lift or climb
        if (controller2.getStartButtonPressed()) {
            climbMode = !climbMode;
        }
        
        // Player 2
        if (climbMode) {
            // Arm
            if (Math.abs(controller2.getY(kRight)) <= 0.1) {
                Components.getClimb().stopArm();
            } else if (controller2.getTriggerAxis(kRight) > 0.8 && controller2.getY(kRight) <= 0) {
                Components.getClimb().moveArmTurbo(controller2.getY(kRight));
            } else {
                Components.getClimb().moveArm(controller2.getY(kRight));
            }
            
            // Winch
            if (Math.abs(controller2.getY(kLeft)) <= 0.1) {
                Components.getClimb().stopWinch();
            } else {
                Components.getClimb().moveWinch(controller2.getY(kLeft));
            }
        } else {
            // Intake
            if (!player1IntakeActive) {
                Components.getIntake().runWheelsIn(-controller2.getY(kRight) * 0.8);
            }
            if (controller2.getY(kLeft) >= 0.8) {
                Components.getIntake().openClaw();
            }
            if (controller2.getY(kLeft) <= -0.8) {
                Components.getIntake().closeClaw();
            }
            // Lift
            if ((controller2.getTriggerAxis(kRight) >= 0.05) && (rightTriggerAxis < 0.1)) {
                Components.getLift().movePWM(controller2.getTriggerAxis(kRight) / 2);
            }
            if ((controller2.getTriggerAxis(kLeft) >= 0.05) && (leftTriggerAxis < 0.1)) {
                Components.getLift().movePWM(-controller2.getTriggerAxis(kLeft) / 2);
            }
        }

        SmartDashboard.putBoolean("Climb Mode", climbMode);
        SmartDashboard.putNumber("Lift Pos", Components.getLift().getEncoderPos());
        SmartDashboard.putNumber("Drive Left Vel",
            Components.getDrive().getLeftMotor().getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Drive Right Vel",
            Components.getDrive().getRightMotor().getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Arm Pos", Components.getClimb().getEncoderPos());
        SmartDashboard.putNumber("Controller2 Right getY", controller2.getY(kRight));
    }
}
