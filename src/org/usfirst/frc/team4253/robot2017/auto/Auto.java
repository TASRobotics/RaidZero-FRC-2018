package org.usfirst.frc.team4253.robot2017.auto;

import org.usfirst.frc.team4253.robot2017.auto.AutoChooser.Color;
import org.usfirst.frc.team4253.robot2017.components.Components;

/**
 * Autonomous specific code for the robot.
 */
public class Auto {

    private static AutoDrive autoDrive;

    /**
     * Initializes the autonomous-specific components.
     * 
     * <p>Make sure this method has been called before calling the other methods.
     * 
     * <p>The difference between this method and {@link #setup()} is that this should only be run
     * once at the beginning, but {@link #setup()} should be called once every time the robot is
     * switched to autonomous mode.
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
     * 
     * <p>The difference between this method and {@link #initialize()} is that this should be called
     * once every time the robot is switched to autonomous mode, but {@link #initialize()} should
     * only be run once at the beginning.
     */
    public static void setup() {
        autoDrive.setup();
        Components.getCompressor().start();
    }

    /**
     * Runs the autonomous code once.
     */
    public static void run() {
        Color color = AutoChooser.getColor();
        switch (AutoChooser.getMode()) {
            case DoNothing:
                break;
            case CrossLine:
                crossLine();
                break;
            case CenterGear:
                deliverCenterGear(color);
                break;
            case LeftGear:
                deliverSideGear(color, Side.Left);
                break;
            case RightGear:
                deliverSideGear(color, Side.Right);
                break;
        }
    }

    /**
     * Crosses the line.
     */
    private static void crossLine() {
        autoDrive.moveMeters(4, 5);
    }

    /**
     * Places the gear on the center peg of the airship, then crosses the line.
     * 
     * @param color the alliance the robot is on
     */
    private static void deliverCenterGear(Color color) {
        boolean red = color == Color.Red;
        autoDrive.moveInches(44, 1);
        Timer.delay(0.5);
        visionTurn();
        if (!visionForward()) {
            autoDrive.moveInches(29, 1.5);
        }
        Timer.delay(0.2);
        Components.getGearDoor().open();
        Timer.delay(1);
        autoDrive.moveInches(-50, 1.5);
        Components.getGearDoor().close();
        autoDrive.turn(red ? -90 : 90, 2.2);
        autoDrive.moveInches(90, 1.5);
        autoDrive.turn(red ? 90 : -90, 2.2);
        autoDrive.moveMeters(8, 3);
    }

    /**
     * Places the gear on a side peg of the airship, then crosses the line.
     * 
     * @param color the alliance the robot is on
     * @param side which peg to put the gear on (left or right)
     */
    private static void deliverSideGear(Color color, Side side) {
        boolean leftSide = side == Side.Left;
        // red left or blue right = gear loading side
        // red right or blue left = boiler side
        boolean gearStationSide = (color == Color.Red) == leftSide;
        autoDrive.moveInches(gearStationSide ? 72.06 : 70.69, 1.5);
        autoDrive.turn(leftSide ? 60 : -60, 3);
        autoDrive.moveInches(gearStationSide ? 41 : 37, 2);
        Timer.delay(0.5);
        visionTurn();
        if (!visionForward()) {
            autoDrive.moveInches(gearStationSide ? 20 : 27, 1.5);
        }
        Timer.delay(0.2);
        Components.getGearDoor().open();
        Timer.delay(1);
        autoDrive.moveInches(-60, 1);
        Components.getGearDoor().close();
        autoDrive.turn(leftSide ? -60 : 60, 2.2);
        autoDrive.moveMeters(7, 3);
    }

    private static void visionTurn() {
        // TODO
    }

    private static boolean visionForward() {
        // TODO
        return false;
    }

    /**
     * The side of a peg on the airship (left or right).
     */
    private static enum Side { Left, Right }

}
