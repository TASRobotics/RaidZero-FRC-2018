package org.usfirst.frc.team4253.robot2018.auto;

import org.usfirst.frc.team4253.robot2018.components.Drive;
import org.usfirst.frc.team4253.robot2018.components.MotorSettings;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

public class AutoDrive {
    
    private static final double AUTO_STRAIGHT_P = 0.08;
    private static final double AUTO_STRIAGHT_D = 0.0004;
    private static final int MAX_MODIFIER = 200;
    
    private static final int DEFAULT_VEL = 1072;
    private static final int DEFAULT_ACCEL = 2144;
    
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
     * <p>TODO: Add anything else that's need in the future.
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
     * Moves the robot in a curve
     * 
     * @param targets the array containing target velocity and acceleration.
     * @param targetPos the target encoder position to move the robot to.
     */
    public void moveCurve(int[] targets, int targetPos) {
        rightMotor.configMotionCruiseVelocity(targets[0], MotorSettings.TIMEOUT);
        leftMotor.configMotionCruiseVelocity(targets[1], MotorSettings.TIMEOUT);
        rightMotor.configMotionAcceleration(targets[2], MotorSettings.TIMEOUT);
        leftMotor.configMotionAcceleration(targets[3], MotorSettings.TIMEOUT);

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
     * Converts the geogebra data to targets for motors.
     * <p>The returned array contains:
     * <p>First index is left velocity
     * <p>Second index is right velocity
     * <p>Third index is left acceleration
     * <p>Fourth index is right acceleration
     * 
     * @param geogebra the data from geogebra sorted into an array.
     * @param targetPos the target position for the robot to go to.
     * @return the array containing motor targets.
     */
    public int[] convertToMotorValues(double[] geogebra, int targetPos){
        int[] motorValues = new int[4];
        
        motorValues[0] = (int) ((1 + geogebra[determineInterval(targetPos, geogebra.length)]) * DEFAULT_VEL);
        motorValues[1] = (int) ((1 - geogebra[determineInterval(targetPos, geogebra.length)]) * DEFAULT_VEL);
        motorValues[2] = (int) ((1 + geogebra[determineInterval(targetPos, geogebra.length)]) * DEFAULT_ACCEL);
        motorValues[3] = (int) ((1 - geogebra[determineInterval(targetPos, geogebra.length)]) * DEFAULT_ACCEL);
        
        return motorValues;
    }
    
    /**
     * Determines the interval that the robot is in right now.
     * @param targetPos the target position for the robot to go to.
     * @return the interval that the robot is in right now.
     */
    public int determineInterval(int targetPos, int length) {
        int averagePos = 
            (leftMotor.getSelectedSensorPosition(MotorSettings.PID_IDX) + rightMotor.getSelectedSensorPosition(MotorSettings.PID_IDX)) / 2;
        int interval = (averagePos * length) / targetPos;
        return interval;
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
