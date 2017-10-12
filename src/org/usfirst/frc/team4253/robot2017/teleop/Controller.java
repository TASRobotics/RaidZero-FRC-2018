package org.usfirst.frc.team4253.robot2017.teleop;

import edu.wpi.first.wpilibj.Joystick;
import java.util.stream.Stream;

/**
 * A Logitech Dual Action (with colored buttons) controller.
 * 
 * <p>Make sure that the switch on the back is set to "D" if you are using this class.
 */
public class Controller {

    private Joystick joystick;

    /**
     * Constructs a Controller object.
     * 
     * @param port the USB port of the controller on the Driver Station
     */
    public Controller(int port) {
        joystick = new Joystick(port);
    }

    /**
     * Returns the value of the given joystick axis.
     * 
     * @param axis the axis
     * @return the value of the axis, between -1 and 1
     */
    public double getAxisValue(Axis axis) {
        return joystick.getRawAxis(axis.raw);
    }

    /**
     * Returns whether the given controller button is pressed.
     * 
     * @param button the controller button
     * @return whether the button is pressed
     */
    public boolean isPressed(Button button) {
        return joystick.getRawButton(button.raw);
    }

    /**
     * Returns whether the given D-pad button is pressed.
     * 
     * @param dPad the D-pad button
     * @return whether the button is pressed
     */
    public boolean isPressed(DPad dPad) {
        return joystick.getPOV() == dPad.angle;
    }

    /**
     * Returns whether any of the given controller buttons are pressed.
     * 
     * <p>This is a varargs method, so you can call it with any number of arguments.
     * 
     * @param buttons the controller buttons to check
     * @return whether any of the buttons are pressed
     */
    public boolean isAnyPressed(Button... buttons) {
        return Stream.of(buttons).anyMatch(this::isPressed);
    }

    /**
     * Returns whether any of the given D-pad buttons are pressed.
     * 
     * <p>This is a varargs method, so you can call it with any number of arguments.
     * 
     * @param dPads the D-pad buttons to check
     * @return whether any of the buttons are pressed
     */
    public boolean isAnyPressed(DPad... dPads) {
        return Stream.of(dPads).anyMatch(this::isPressed);
    }

    /**
     * A joystick axis.
     */
    public static enum Axis {

        /**
         * The X axis of the left joystick.
         */
        LeftX(0),
        /**
         * The Y axis of the left joystick.
         */
        LeftY(1),
        /**
         * The X axis of the right joystick.
         */
        RightX(2),
        /**
         * The Y axis of the right joystick.
         */
        RightY(3);

        /**
         * The raw number of the axis.
         */
        public final int raw;

        Axis(int raw) {
            this.raw = raw;
        }

    }

    /**
     * A controller button.
     */
    public static enum Button {

        X(1),
        A(2),
        B(3),
        Y(4),
        LB(5),
        RB(6),
        LT(7),
        RT(8),
        Back(9),
        Start(10),
        /**
         * The left joystick button (you can press down on the joystick).
         */
        LeftStick(11),
        /**
         * The right joystick button (you can press down on the joystick).
         */
        RightStick(12);

        /**
         * The raw number of the button.
         */
        public final int raw;

        Button(int raw) {
            this.raw = raw;
        }

    }

    /**
     * A D-pad button (the + on a circle thing on the left).
     */
    public static enum DPad {

        Up(0),
        UpperRight(45),
        Right(90),
        LowerRight(135),
        Down(180),
        LowerLeft(225),
        Left(270),
        UpperLeft(315);

        /**
         * The POV angle of the button (in degrees).
         */
        public final int angle;

        DPad(int angle) {
            this.angle = angle;
        }

    }

}
