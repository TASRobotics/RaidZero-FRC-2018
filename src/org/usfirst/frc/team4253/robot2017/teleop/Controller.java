package org.usfirst.frc.team4253.robot2017.teleop;

import edu.wpi.first.wpilibj.Joystick;
import java.util.stream.Stream;

public class Controller {

    private Joystick joystick;

    public Controller(int port) {
        joystick = new Joystick(port);
    }

    public double getAxisValue(Axis axis) {
        return joystick.getRawAxis(axis.raw);
    }

    public boolean isPressed(Button button) {
        return joystick.getRawButton(button.raw);
    }

    public boolean isPressed(DPad dPad) {
        return joystick.getPOV() == dPad.angle;
    }

    public boolean isAnyPressed(Button... buttons) {
        return Stream.of(buttons).anyMatch(this::isPressed);
    }

    public boolean isAnyPressed(DPad... dPads) {
        return Stream.of(dPads).anyMatch(this::isPressed);
    }

    public static enum Axis {

        LeftX(0),
        LeftY(1),
        RightX(2),
        RightY(3);

        public final int raw;

        Axis(int raw) {
            this.raw = raw;
        }

    }

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
        LeftStick(11),
        RightStick(12);

        public final int raw;

        Button(int raw) {
            this.raw = raw;
        }

    }

    public static enum DPad {

        Up(0),
        UpperRight(45),
        Right(90),
        LowerRight(135),
        Down(180),
        LowerLeft(225),
        Left(270),
        UpperLeft(315);

        public final int angle;

        DPad(int angle) {
            this.angle = angle;
        }

    }

}
