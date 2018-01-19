package org.usfirst.frc.team4253.robot2018.auto;

import org.usfirst.frc.team4253.robot2018.components.Drive;
import org.usfirst.frc.team4253.robot2018.components.MotorSettings;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

public class AutoDrive {
    
    private static final int DEFAULT_VEL = 1072;
    private static final int DEFAULT_ACCEL = 2144;
    private static final double AUTO_STRAIGHT_P = 0.08;
    private static final double AUTO_STRIAGHT_D = 0.0004;
    private static final int MAX_MODIFIER = 200;
    private double currentAngle;
    private double currentAngularRate;
    private double autoStraightModifier;
    
    private TalonSRX rightMotor;
    private TalonSRX leftMotor;
    private PigeonIMU pigeon;
    
    /**
     * Constructs an AutoDrive object.
     * 
     * @param drive the drive class used
     */
    public AutoDrive(Drive drive) {
        rightMotor = drive.getRightMotor();
        leftMotor = drive.getLeftMotor();
        pigeon = drive.getPigeon();
    }
    
    /**
     * Sets up any needed settings
     * 
     * TODO: Add anything else that's need in the future.
     */
    public void setUp() {
        pigeon.setFusedHeading(0, 100);
        rightMotor.setSelectedSensorPosition(0, MotorSettings.PID_IDX, MotorSettings.TIMEOUT);
        leftMotor.setSelectedSensorPosition(0, MotorSettings.PID_IDX, MotorSettings.TIMEOUT);
    }
    
    /**
     * Moves the robot to the target position
     * 
     * @param targetPos the target encoder position to move the robot to
     */
    public void moveStraight(int targetPos) {
        autoStraight();
        rightMotor.set(ControlMode.MotionMagic, targetPos); 
        leftMotor.set(ControlMode.MotionMagic, targetPos);
    }
    
    /**
     * Straightens the robot based of the gyro.
     */
    public void autoStraight() {
        PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
        double [] xyz_dps = new double [3];
        pigeon.getRawGyro(xyz_dps);
        pigeon.getFusedHeading(fusionStatus);
        
        currentAngle = fusionStatus.heading;
        currentAngularRate = xyz_dps[2];
        
        autoStraightModifier = (0 - currentAngle) * AUTO_STRAIGHT_P - (currentAngularRate) * AUTO_STRIAGHT_D;
        autoStraightModifier = limit(autoStraightModifier);
        
        rightMotor.configMotionCruiseVelocity(DEFAULT_VEL + (int) autoStraightModifier, MotorSettings.TIMEOUT);
        rightMotor.configMotionAcceleration(DEFAULT_ACCEL, MotorSettings.TIMEOUT);
        leftMotor.configMotionCruiseVelocity(DEFAULT_VEL + (int) autoStraightModifier, MotorSettings.TIMEOUT);
        leftMotor.configMotionAcceleration(DEFAULT_ACCEL, MotorSettings.TIMEOUT);
    }
    
    /**
     * Limits the modifier from being too big.
     * 
     * <p>This compares the modifier with {@link #MAX_MODIFIER} and checks which one is smaller. It returns whatever is less.
     * 
     * <p>This is used instead of Math.min or Math.max because you don't have to worry about negatives.
     * 
     * @param value the value to compare with the {@link #MAX_MODIFIER} and select which ever is less
     * @return the number to modify the velocity or acceleration 
     */
    public double limit(double value) {
        if (value < -MAX_MODIFIER) {
            return -MAX_MODIFIER;
        }
        
        if (value > +MAX_MODIFIER) {
            return +MAX_MODIFIER;
        }
        
        return value;
    }
}
