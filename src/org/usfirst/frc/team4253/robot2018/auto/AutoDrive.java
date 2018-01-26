package org.usfirst.frc.team4253.robot2018.auto;

import org.usfirst.frc.team4253.robot2018.components.Drive;
import org.usfirst.frc.team4253.robot2018.components.MotorSettings;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDrive {

    private static final double AUTO_STRAIGHT_P = 0.08;
    private static final double AUTO_STRIAGHT_D = 0.0004;
    private static final double AUTO_ANGLE_P = 0.6; // 0.08;
    private static final double AUTO_ANGLE_D = 0.0004;

    private static final int PIGEON_TIMEOUT = 100;
    private static final int MAX_MODIFIER = 200;
    private static final int DEFAULT_VEL = 500;
    private static final int DEFAULT_ACCEL = 1000;

    private double currentAngle;
    private double currentAngularRate;
    private double autoStraightModifier;
    private double autoAngleModifier;

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
        // Reset Encoder At Init
        setUp();
    }

    /**
     * Sets up any needed settings.
     * 
     * <p>Call this when autonomous starts.
     * 
     * <p>TODO: Add anything else that's need in the future.
     */
    public void setUp() {
        pigeon.setFusedHeading(0, PIGEON_TIMEOUT);
        rightMotor.setSelectedSensorPosition(0, MotorSettings.PID_IDX, MotorSettings.TIMEOUT);
        leftMotor.setSelectedSensorPosition(0, MotorSettings.PID_IDX, MotorSettings.TIMEOUT);
    }

    /**
     * Moves the robot to the target position.
     * 
     * @param targetPos the target encoder position to move the robot to
     */
    public void moveStraight(int targetPos) {
        // autoStraight();
        rightMotor.configMotionCruiseVelocity(DEFAULT_VEL + 0 * (int) autoStraightModifier,
            MotorSettings.TIMEOUT);
        rightMotor.configMotionAcceleration(DEFAULT_ACCEL + 0 * (int) autoStraightModifier,
            MotorSettings.TIMEOUT);
        leftMotor.configMotionCruiseVelocity(DEFAULT_VEL + 0 * (int) autoStraightModifier,
            MotorSettings.TIMEOUT);
        leftMotor.configMotionAcceleration(DEFAULT_ACCEL + 0 * (int) autoStraightModifier,
            MotorSettings.TIMEOUT);

        rightMotor.set(ControlMode.MotionMagic, targetPos);
        leftMotor.set(ControlMode.MotionMagic, targetPos);

        System.out.println(leftMotor.getMotorOutputPercent() + "\t"
                + leftMotor.getClosedLoopError(MotorSettings.PID_IDX) + "\t"
                + leftMotor.getSelectedSensorPosition(MotorSettings.PID_IDX) + "\t"
                + rightMotor.getMotorOutputPercent() + "\t"
            + rightMotor.getClosedLoopError(MotorSettings.PID_IDX) + "\t"
            + rightMotor.getSelectedSensorPosition(MotorSettings.PID_IDX));
    }

    /**
     * Moves the robot in a curve.
     * 
     * @param targets the geogebra data containing percent difference and angle
     * @param targetPos the target encoder position to move the robot to
     */
    public void moveCurve(GeoGebraEntry[] targets, int targetPos) {
        GeoGebraEntry current = interpolate(targets, targetPos);
        int[] currentTargets = convertToMotorValues(current.getPercentDifference(), targetPos);
        autoAngle(current.getAngle());

        autoAngleModifier *= 10;
        leftMotor.configMotionCruiseVelocity(DEFAULT_VEL + (int) autoAngleModifier,
            MotorSettings.TIMEOUT);
        rightMotor.configMotionCruiseVelocity(DEFAULT_VEL - (int) autoAngleModifier,
            MotorSettings.TIMEOUT);
        leftMotor.configMotionAcceleration(DEFAULT_ACCEL + (int) autoAngleModifier,
            MotorSettings.TIMEOUT);
        rightMotor.configMotionAcceleration(DEFAULT_ACCEL - (int) autoAngleModifier,
            MotorSettings.TIMEOUT);

        // leftMotor.configMotionCruiseVelocity(currentTargets[0] + (int) autoAngleModifier,
        // MotorSettings.TIMEOUT);
        // rightMotor.configMotionCruiseVelocity(currentTargets[1] - (int) autoAngleModifier,
        // MotorSettings.TIMEOUT);
        // leftMotor.configMotionAcceleration(currentTargets[2] + (int) autoAngleModifier,
        // MotorSettings.TIMEOUT);
        // rightMotor.configMotionAcceleration(currentTargets[3] - (int) autoAngleModifier,
        // MotorSettings.TIMEOUT);
        SmartDashboard.putNumber("LTargetVel", currentTargets[0]);
        SmartDashboard.putNumber("RTargetVel", currentTargets[1]);

        rightMotor.set(ControlMode.MotionMagic, targetPos);
        leftMotor.set(ControlMode.MotionMagic, targetPos);
    }

    /**
     * Straightens the robot based off the gyro.
     * 
     * <p>This changes the {@link #autoStraightModifier} so that the robot moves straight.
     */
    private void autoStraight() {
        PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
        double[] xyz_dps = new double[3];
        pigeon.getRawGyro(xyz_dps);
        pigeon.getFusedHeading(fusionStatus);

        currentAngle = fusionStatus.heading;
        currentAngularRate = xyz_dps[2];

        autoStraightModifier =
            (0 - currentAngle) * AUTO_STRAIGHT_P - currentAngularRate * AUTO_STRIAGHT_D;
        autoStraightModifier = limit(autoStraightModifier);
    }

    /**
     * Ensures the robot moves with the angle from the geogebra data.
     * 
     * <p>This changes the {@link #autoAngleModifier} so that the robot moves straight.
     * 
     * @param targetAngle the angle to try to reach
     */
    private void autoAngle(double targetAngle) {
        PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
        double[] xyz_dps = new double[3];
        pigeon.getRawGyro(xyz_dps);
        pigeon.getFusedHeading(fusionStatus);

        currentAngle = -fusionStatus.heading;
        currentAngularRate = xyz_dps[2];

        autoAngleModifier =
            (targetAngle - currentAngle) * AUTO_ANGLE_P - currentAngularRate * AUTO_ANGLE_D;
        System.out.println("autoAngleModifier: " + autoAngleModifier);
        // autoAngleModifier = limit(autoAngleModifier);
    }

    /**
     * Converts the geogebra data to targets for motors.
     * 
     * <p>The returned array contains:
     * 
     * <p>First index is left velocity. Second index is right velocity. Third index is left
     * acceleration. Fourth index is right acceleration
     * 
     * @param percentDiff the percentDiff from geogebra
     * @param targetPos the target position for the robot to go to
     * @return the array containing motor targets
     */
    private int[] convertToMotorValues(double percentDiff, int targetPos) {
        SmartDashboard.putNumber("Pdiff", percentDiff);
        return new int[] {
            (int) ((1 + percentDiff) * DEFAULT_VEL),
            (int) ((1 - percentDiff) * DEFAULT_VEL),
            (int) ((1 + percentDiff) * DEFAULT_ACCEL),
            (int) ((1 - percentDiff) * DEFAULT_ACCEL) };
    }

    /**
     * Linearly interpolates between GeoGebra entries to find current angle and percent difference
     * based on drive encoders.
     * 
     * @param data data from GeoGebra
     * @param targetPos total number of encoder ticks to travel
     * @return the current angle and percent difference
     */
    private GeoGebraEntry interpolate(GeoGebraEntry[] data, int targetPos) {
        double currentPos = (leftMotor.getSelectedSensorPosition(MotorSettings.PID_IDX)
            + rightMotor.getSelectedSensorPosition(MotorSettings.PID_IDX)) / 2;

        if (currentPos <= 0) {
            System.out.println("currentPos <= 0");
            return data[0];
        }

        double progress = currentPos * data.length / targetPos;
        int prevIndex = (int) progress;
        int nextIndex = prevIndex + 1;

        if (nextIndex >= data.length) {
            System.out.println("nextIndex >= data.length");
            return data[data.length - 1];
        }

        System.out.println("index: " + prevIndex);

        double prevAngle = data[prevIndex].getAngle();
        double nextAngle = data[nextIndex].getAngle();
        double prevPercentDifference = data[prevIndex].getPercentDifference();
        double nextPercentDifference = data[nextIndex].getPercentDifference();
        double x = progress - prevIndex;

        return new GeoGebraEntry((nextAngle - prevAngle) * x + prevAngle,
            (nextPercentDifference - prevPercentDifference) * x + prevPercentDifference);
    }

    /**
     * Limits the modifier from being too big.
     * 
     * <p>This compares the modifier with {@link #MAX_MODIFIER} and returns the one closer to zero.
     * 
     * <p>This is used instead of Math.min or Math.max because you don't have to worry about
     * negatives.
     * 
     * @param value the value to compare with the {@link #MAX_MODIFIER} and select which ever is
     *            less
     * @return the number to modify the velocity or acceleration
     */
    private double limit(double value) {
        if (value < -MAX_MODIFIER) {
            return -MAX_MODIFIER;
        }

        if (value > +MAX_MODIFIER) {
            return +MAX_MODIFIER;
        }

        return value;
    }
}
