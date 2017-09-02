package org.usfirst.frc.team4253.robot2017;

import edu.wpi.first.wpilibj.Joystick;

public class Controller {

    private Joystick joystick;
    
    public Controller(int port) {
        joystick = new Joystick(port);
    }
    
    public double getAxisValue(Axis axis) {
        return joystick.getRawAxis(axis.raw);
    }
    
    public Controller ifPressed(Button button, Runnable action) {
        if (joystick.getRawButton(button.raw)) {
            action.run();
        }
        return this;
    }
    
    public Controller ifPressed(DPad dPad, Runnable action) {
        if (joystick.getPOV() == dPad.angle) {
            action.run();
        }
        return this;
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
