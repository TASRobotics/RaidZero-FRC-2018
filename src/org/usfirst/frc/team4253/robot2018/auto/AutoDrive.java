package org.usfirst.frc.team4253.robot2018.auto;

import org.usfirst.frc.team4253.robot2018.components.Drive;
import org.usfirst.frc.team4253.robot2018.components.MotorSettings;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

public class AutoDrive {
    
    private static final int DEFAULTVEL = 1072;
    private static final int DEFAULTACCEL = 2144;
    private static final double AUTOSTRAIGHT_P = 0.08;
    private static final double AUTOSTRIAGHT_D = 0.0004;
    private static final int MAXMODIFIER = 200;
    private double currentAngle;
    private double currentAngularRate;
    private double autoStraightModifier;
    
    private Drive drive;
    private TalonSRX rightMotor;
    private TalonSRX leftMotor;
    private PigeonIMU pigeon;
    
    public AutoDrive(Drive drive) {
        this.drive = drive;
        rightMotor = drive.getRightMotor();
        leftMotor = drive.getLeftMotor();
        pigeon = drive.getPigeon();
    }
    
    public void setUp() {
        pigeon.setFusedHeading(0, 100);
    }
    
    public void moveStraight(int targetPos) {
        autoStraight();
        rightMotor.set(ControlMode.MotionMagic, targetPos); 
        leftMotor.set(ControlMode.MotionMagic, targetPos);
    }
    
    public void autoStraight() {
        PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
        double [] xyz_dps = new double [3];
        pigeon.getRawGyro(xyz_dps);
        pigeon.getFusedHeading(fusionStatus);
        
        currentAngle = fusionStatus.heading;
        currentAngularRate = xyz_dps[2];
        
        autoStraightModifier = (0 - currentAngle) * AUTOSTRAIGHT_P - (currentAngularRate) * AUTOSTRIAGHT_D;
        autoStraightModifier = limit(autoStraightModifier);
        
        rightMotor.configMotionCruiseVelocity(DEFAULTVEL + (int) autoStraightModifier, MotorSettings.TIMEOUT);
        rightMotor.configMotionAcceleration(DEFAULTACCEL, MotorSettings.TIMEOUT);
        leftMotor.configMotionCruiseVelocity(DEFAULTVEL + (int) autoStraightModifier, MotorSettings.TIMEOUT);
        leftMotor.configMotionAcceleration(DEFAULTACCEL, MotorSettings.TIMEOUT);
    }
    
    public double limit(double value) {
        if (value < -MAXMODIFIER)
            return -MAXMODIFIER;
        if (value > +MAXMODIFIER)
            return +MAXMODIFIER;
        
        return value;
    }
}
