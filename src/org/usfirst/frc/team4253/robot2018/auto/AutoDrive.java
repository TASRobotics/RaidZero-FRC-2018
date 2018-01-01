package org.usfirst.frc.team4253.robot2018.auto;

import com.ctre.CANTalon.TalonControlMode;
import org.usfirst.frc.team4253.robot2018.Settings;
import org.usfirst.frc.team4253.robot2018.components.Drive;

/**
 * Autonomous-specific functionality for the drive.
 */
public class AutoDrive {

    /**
     * The minimum accumulated time in seconds that the robot has to be within tolerance of the
     * target before it can move on to the next step.
     */
    private static final double ON_TARGET_TIME = 0.5;
    /**
     * The maximum distance in ticks a value can be from the target and still be considered "on
     * target".
     */
    private static final int ON_TARGET_TOLERANCE = 200;
    /**
     * The number of ticks in a meter.
     */
    private static final double METER_TO_TICKS_MULTIPLIER = 18000;
    /**
     * The number of ticks in a degree.
     */
    private static final double DEGREE_TO_TICKS_MULTIPLIER = 125;
    /**
     * The PID profile for storing the PID values for moving forward/backward.
     */
    private static final int STRAIGHT_PROFILE = 0;
    /**
     * The PID profile for storing the PID values for turning.
     */
    private static final int TURN_PROFILE = 1;
    /**
     * The PID values for moving forward/backward.
     */
    private static final double[] STRAIGHT_PID;
    /**
     * The PID values for turning.
     */
    private static final double[] TURN_PID;
    static {
        switch (Settings.VERSION) {
            case V2:
                STRAIGHT_PID = new double[] { 0.1, 0, 0 };
                TURN_PID = new double[] { 0.3, 0, 0.6 };
                break;
            case V3:
                STRAIGHT_PID = new double[] { 0.19, 0, 0 };
                TURN_PID = new double[] { 0.32, 0.0006, 33 };
                break;
            case V4: default:
                STRAIGHT_PID = new double[] { 0.2, 0, 0 };
                TURN_PID = new double[] { 0.41, 0.0006, 33 };
        }
    }

    private Drive drive;
    /**
     * The timer for keeping track of the total time for each step.
     */
    private Timer moveTimer;
    /**
     * The timer for keeping track of the total accumulated time the robot is on target.
     */
    private Timer onTargetTimer;

    /**
     * Constructs an AutoDrive object which uses the given Drive object.
     * 
     * <p>This does not create a new drive. Instead, it uses the existing Drive object that you pass
     * in. So when you call methods on a AutoDrive instance, it controls the motors of the
     * underlying Drive object.
     * 
     * @param drive the Drive object to use
     */
    public AutoDrive(Drive drive) {
        this.drive = drive;
        saveProfile(STRAIGHT_PID, STRAIGHT_PROFILE);
        saveProfile(TURN_PID, TURN_PROFILE);
        moveTimer = new Timer();
        onTargetTimer = new Timer();
    }

    /**
     * Configures the drive for autonomous driving.
     * 
     * <p>This should be called when autonomous starts, before any of the movement methods are
     * called.
     */
    public void setup() {
        drive.setLowGear();
        drive.setControlMode(TalonControlMode.Position);
    }

    /**
     * Moves forward for the given distance in inches.
     * 
     * @param distance the distance to move in inches (if this is negative, the robot will move
     *            backwards)
     * @param timeLimit the maximum time to run this method (i.e. if it takes longer than this time,
     *            then stop this method and move on no matter if it reached the distance or not)
     */
    public void moveInches(double distance, double timeLimit) {
        moveMeters(distance * 2.54 / 100, timeLimit);
    }

    /**
     * Moves forward for the given distance in meters.
     * 
     * @param distance the distance to move in meters (if this is negative, the robot will move
     *            backwards)
     * @param timeLimit the maximum time to run this method (i.e. if it takes longer than this time,
     *            then stop this method and move on no matter if it reached the distance or not)
     */
    public void moveMeters(double distance, double timeLimit) {
        switchProfile(STRAIGHT_PROFILE);
        int ticks = (int) (distance * METER_TO_TICKS_MULTIPLIER);
        move(ticks, ticks, timeLimit);
    }

    /**
     * Rotates the robot for the given angle in degrees.
     * 
     * @param angle the angle to turn in degrees (positive is turn right (clockwise), negative is
     *            turn left (counterclockwise))
     * @param timeLimit the maximum time to run this method (i.e. if it takes longer than this time,
     *            then stop this method and move on no matter if it reached the angle or not)
     */
    public void turn(double angle, double timeLimit) {
        switchProfile(TURN_PROFILE);
        int ticks = (int) (angle * DEGREE_TO_TICKS_MULTIPLIER);
        move(ticks, -ticks, timeLimit);
    }

    /**
     * Moves the drive motors for the given amount of left and right ticks.
     * 
     * @param leftTicks the amount of ticks the left motors should move (if this is negative, they
     *            will move backwards)
     * @param rightTicks the amount of ticks the right motors should move (if this is negative, they
     *            will move backwards)
     * @param timeLimit the maximum time to run this method (i.e. if it takes longer than this time,
     *            then stop this method and move on no matter if it reached the target or not)
     */
    private void move(int leftTicks, int rightTicks, double timeLimit) {
        moveTimer.reset();
        onTargetTimer.reset();
        drive.getLeftMotor().setEncPosition(0);
        drive.getRightMotor().setEncPosition(0);
        moveTimer.start();
        while (moveTimer.lessThan(timeLimit) && onTargetTimer.lessThan(ON_TARGET_TIME)) {
            drive.getLeftMotor().setSetpoint(leftTicks);
            drive.getRightMotor().setSetpoint(rightTicks);
            if (onTarget(drive.getLeftMotor().getEncPosition(), leftTicks)
                    || onTarget(drive.getRightMotor().getEncPosition(), rightTicks)) {
                if (!onTargetTimer.isRunning()) {
                    onTargetTimer.start();
                }
            } else if (onTargetTimer.isRunning()) {
                onTargetTimer.pause();
            }
        }
    }

    /**
     * Returns whether the absolute difference between the given current and target values is within
     * the tolerance.
     * 
     * @param current the value to compare to the target
     * @param target the goal value
     * @return whether the absolute difference between current and target is within the tolerance
     */
    private static boolean onTarget(double current, int target) {
        return Math.abs(current - target) < ON_TARGET_TOLERANCE;
    }

    /**
     * Saves the given PID values to a PID profile.
     * 
     * @param pid an array of 3 doubles, representing the P, I, and D values
     * @param profile the PID profile to save the PID values to
     */
    private void saveProfile(double[] pid, int profile) {
        drive.getLeftMotor().setPID(pid[0], pid[1], pid[2], 0, 0, 0, profile);
        drive.getRightMotor().setPID(pid[0], pid[1], pid[2], 0, 0, 0, profile);
    }

    /**
     * Switches PID profiles.
     * 
     * @param profile the PID profile to switch to
     */
    private void switchProfile(int profile) {
        drive.getLeftMotor().setProfile(profile);
        drive.getRightMotor().setProfile(profile);
    }

}
