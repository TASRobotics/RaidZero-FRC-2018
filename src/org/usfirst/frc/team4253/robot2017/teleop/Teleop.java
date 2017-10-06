package org.usfirst.frc.team4253.robot2017.teleop;

import org.usfirst.frc.team4253.robot2017.components.Components;
import org.usfirst.frc.team4253.robot2017.teleop.Controller.Axis;
import org.usfirst.frc.team4253.robot2017.teleop.Controller.Button;

/**
 * Teleop (remote control) specific code for the robot.
 */
public class Teleop {

    /**
     * The controller on port 0.
     */
    private static Controller firstController;
    /**
     * The controller on port 1.
     */
    private static Controller secondController;
    private static TeleopDrive teleopDrive;

    /**
     * Initializes the teleop-specific components.
     * 
     * <p>Make sure this method has been called before calling the other methods.
     * 
     * <p>The difference between this method and {@link #setup()} is that this should only be run
     * once at the beginning, but {@link #setup()} should be called once every time the robot is
     * switched to autonomous mode.
     */
    public static void initialize() {
        firstController = new Controller(0);
        secondController = new Controller(1);
        teleopDrive = new TeleopDrive(Components.getDrive());
    }

    /**
     * Configures the components for use in teleop mode.
     * 
     * <p>This should be called once every time the robot is switched to autonomous mode, before
     * calling {@link #run()}.
     * 
     * <p>The difference between this method and {@link #initialize()} is that this should be called
     * once every time the robot is switched to autonomous mode, but {@link #initialize()} should
     * only be run once at the beginning.
     */
    public static void setup() {
        teleopDrive.setup();
        Components.getCompressor().start();
    }

    /**
     * Runs the teleop code.
     * 
     * <p>This method should be called in a loop, after calling {@link #setup()} once.
     */
    public static void run() {
        // drive controls
        if (firstController.isPressed(Button.RB)) {
            Components.getDrive().setHighGear();
        }
        if (firstController.isPressed(Button.RT)) {
            Components.getDrive().setLowGear();
        }
        teleopDrive.drive(
            firstController.getAxisValue(Axis.LeftY),
            firstController.getAxisValue(Axis.LeftX)
        );

        // climb controls
        if (secondController.isPressed(Button.LB)) {
            Components.getClimb().upHalfPower();
        } else if (secondController.isPressed(Button.LT)) {
            Components.getClimb().up();
        } else {
            Components.getClimb().stop();
        }

        // gear door controls
        if (firstController.isAnyPressed(Button.X, Button.A, Button.B, Button.Y)
                || secondController.isPressed(Button.RB)) {
            Components.getGearDoor().open();
        }
        if (secondController.isPressed(Button.RT)) {
            Components.getGearDoor().close();
        }
        
        // intake controls
        if (secondController.isPressed(Button.X)) {
            Components.getIntake().run();
        } else {
            Components.getIntake().stop();
        }
    }

}
