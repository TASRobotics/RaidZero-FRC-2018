package org.usfirst.frc.team4253.robot2017.teleop;

import org.usfirst.frc.team4253.robot2017.components.Components;
import org.usfirst.frc.team4253.robot2017.teleop.Controller.Axis;
import org.usfirst.frc.team4253.robot2017.teleop.Controller.Button;
import org.usfirst.frc.team4253.robot2017.teleop.Controller.DPad;

public class Teleop {

    private static Controller firstController;
    private static Controller secondController;
    private static TeleopDrive teleopDrive;

    public static void initialize() {
        firstController = new Controller(0);
        secondController = new Controller(1);
        teleopDrive = new TeleopDrive(Components.getDrive());
    }

    public static void setup() {
        teleopDrive.setup();
        Components.getCompressor().start();
    }

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
        } else if (secondController.isPressed(DPad.Down)) {
            Components.getClimb().down();
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
    }

}
