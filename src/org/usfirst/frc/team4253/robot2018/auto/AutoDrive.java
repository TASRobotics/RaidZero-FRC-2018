package org.usfirst.frc.team4253.robot2018.auto;

import org.usfirst.frc.team4253.robot2018.components.Drive;
import org.usfirst.frc.team4253.robot2018.components.MotorSettings;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Autonomous-specific functionality for the drive.
 */
public class AutoDrive {

    private static final double AUTO_STRAIGHT_P = 0.08;
    private static final double AUTO_STRIAGHT_D = 0.0004;
    private static final double AUTO_ANGLE_P = 4;
    private static final double AUTO_ANGLE_D = 0.0004;

    private static final double INCH_TO_TICKS = 2542 / 32;

    private static final int DEFAULT_VEL = 500;
    private static final int DEFAULT_ACCEL = 1000;

    private static final int VEL_TOLERANCE = 5;
    private static final int POS_TOLERANCE = 10;

    private static final int PIGEON_TIMEOUT = 100;

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
        setup();
    }

    /**
     * Sets up any needed settings.
     * 
     * <p>Call this when autonomous starts.
     * 
     * <p>TODO: Add anything else that's need in the future.
     */
    public void setup() {
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
        autoStraightModifier = 0;
        rightMotor.configMotionCruiseVelocity(DEFAULT_VEL + (int) autoStraightModifier,
            MotorSettings.TIMEOUT);
        rightMotor.configMotionAcceleration(DEFAULT_ACCEL + (int) autoStraightModifier,
            MotorSettings.TIMEOUT);
        leftMotor.configMotionCruiseVelocity(DEFAULT_VEL + (int) autoStraightModifier,
            MotorSettings.TIMEOUT);
        leftMotor.configMotionAcceleration(DEFAULT_ACCEL + (int) autoStraightModifier,
            MotorSettings.TIMEOUT);

        rightMotor.set(ControlMode.MotionMagic, targetPos);
        leftMotor.set(ControlMode.MotionMagic, targetPos);
    }

    /**
     * Moves the robot in a curve.
     * 
     * @param path the path to move
     */
    public void moveCurve(AutoPath path) {
        int targetPos = getTargetPos(path);
        GeoGebraEntry current = interpolate(path.getMotorData(), targetPos);
        if (path.getReverse()) {
            targetPos = -targetPos;
        }
        int[] currentTargets =
            convertToMotorValues(current.getPercentDifference(), path.getReverse());
        autoAngle(current.getAngle(), path.getReverse());
        leftMotor.configMotionCruiseVelocity(currentTargets[0] - (int) autoAngleModifier,
            MotorSettings.TIMEOUT);
        rightMotor.configMotionCruiseVelocity(currentTargets[1] + (int) autoAngleModifier,
            MotorSettings.TIMEOUT);
        leftMotor.configMotionAcceleration(currentTargets[2] - (int) autoAngleModifier,
            MotorSettings.TIMEOUT);
        rightMotor.configMotionAcceleration(currentTargets[3] + (int) autoAngleModifier,
            MotorSettings.TIMEOUT);

        rightMotor.set(ControlMode.MotionMagic, targetPos);
        leftMotor.set(ControlMode.MotionMagic, targetPos);
    }

    public double getProgress(AutoPath path) {
        return getEncoderPos() / Math.abs(getTargetPos(path));
    }

    public boolean checkFinished(AutoPath path) {
        int targetPos = Math.abs(getTargetPos(path));
        int averageCurrentVel = Math.abs((leftMotor.getSelectedSensorVelocity(MotorSettings.PID_IDX)
            + rightMotor.getSelectedSensorVelocity(MotorSettings.PID_IDX)) / 2);
        SmartDashboard.putNumber("Pos Difference", targetPos - getEncoderPos());
        return averageCurrentVel <= VEL_TOLERANCE
            && Math.abs(targetPos - getEncoderPos()) <= POS_TOLERANCE;
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
    }

    /**
     * Ensures the robot moves with the angle from the geogebra data.
     * 
     * <p>This changes the {@link #autoAngleModifier} so that the robot moves straight.
     * 
     * @param targetAngle the angle to try to reach
     * @param reverse the boolean to tell to reverse
     */
    private void autoAngle(double targetAngle, boolean reverse) {
        PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
        double[] xyz_dps = new double[3];
        pigeon.getRawGyro(xyz_dps);
        pigeon.getFusedHeading(fusionStatus);

        currentAngle = fusionStatus.heading;
        currentAngularRate = xyz_dps[2];

        autoAngleModifier =
            (targetAngle - currentAngle) * AUTO_ANGLE_P - currentAngularRate * AUTO_ANGLE_D;
        if (reverse) {
            autoAngleModifier = 0;
            // autoAngleModifier = -autoAngleModifier;
        }
    }

    /**
     * Resets the drive encoders.
     */
    public void resetEncoders() {
        rightMotor.setSelectedSensorPosition(0, MotorSettings.PID_IDX, MotorSettings.TIMEOUT);
        leftMotor.setSelectedSensorPosition(0, MotorSettings.PID_IDX, MotorSettings.TIMEOUT);
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
     * @param reverse the boolean to reverse
     * @return the array containing motor targets
     */
    private int[] convertToMotorValues(double percentDiff, boolean reverse) {
        int sign = reverse ? -1 : 1;
        return new int[] {
            (int) ((1 - sign * percentDiff) * DEFAULT_VEL),
            (int) ((1 + sign * percentDiff) * DEFAULT_VEL),
            (int) ((1 - sign * percentDiff) * DEFAULT_ACCEL),
            (int) ((1 + sign * percentDiff) * DEFAULT_ACCEL) };
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
        double currentPos = getEncoderPos();

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

    public double getEncoderPos() {
        return Math.abs((leftMotor.getSelectedSensorPosition(MotorSettings.PID_IDX)
            + rightMotor.getSelectedSensorPosition(MotorSettings.PID_IDX)) / 2.0);
    }

    private int getTargetPos(AutoPath path) {
        return (int) ((path.getMotorData().length - 1) * INCH_TO_TICKS);
    }
}
