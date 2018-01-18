package org.usfirst.frc.team4253.robot2018.teleop;

import org.usfirst.frc.team4253.robot2018.components.Components;
import org.usfirst.frc.team4253.robot2018.components.Lift;

import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

import edu.wpi.first.wpilibj.XboxController;

/**
 * Teleop specific code for the robot.
 */
public class Teleop {

    private static XboxController controller;
    private static TeleopDrive teleopDrive;
    private static Lift lift;
    
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
        lift = new Lift(10, 11);
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
            lift.move(0 + controller.getY(kLeft) * AXIS_TO_LIFT);  // for lowest target position for the lift
        }
        if (controller.getBButton()) {
            lift.move(SWITCH_POS + controller.getY(kLeft) * AXIS_TO_LIFT);  // for switch target position for the lift
        }
        if (controller.getXButton()) {
            lift.move(SCALE_POS + controller.getY(kLeft) * AXIS_TO_LIFT);  // for scale target position for the lift
        }
    }

}
