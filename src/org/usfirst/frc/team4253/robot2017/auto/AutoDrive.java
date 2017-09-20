package org.usfirst.frc.team4253.robot2017.auto;

import org.usfirst.frc.team4253.robot2017.Settings;
import org.usfirst.frc.team4253.robot2017.components.Drive;

import com.ctre.CANTalon.TalonControlMode;

public class AutoDrive {
    
    private static final double ON_TARGET_TIME = 0.5;
    private static final int ON_TARGET_TOLERANCE = 200;
    private static final double METER_TO_TICKS_MULTIPLIER = 18000;
    private static final double DEGREE_TO_TICKS_MULTIPLIER = 125;
    private static final int STRAIGHT_PROFILE = 0;
    private static final int TURN_PROFILE = 1;
    private static final double[] STRAIGHT_PID;
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
    private Timer moveTimer;
    private Timer onTargetTimer;

    public AutoDrive(Drive drive) {
        this.drive = drive;
        saveProfile(STRAIGHT_PID, STRAIGHT_PROFILE);
        saveProfile(TURN_PID, TURN_PROFILE);
        moveTimer = new Timer();
        onTargetTimer = new Timer();
    }
    
    public void setup() {
        drive.setLowGear();
        drive.setControlMode(TalonControlMode.Position);
    }
    
    public void moveInches(double distance, double timeLimit) {
        moveMeters(distance * 2.54 / 100, timeLimit);
    }
    
    public void moveMeters(double distance, double timeLimit) {
        switchProfile(STRAIGHT_PROFILE);
        int ticks = (int) (distance * METER_TO_TICKS_MULTIPLIER);
        move(ticks, ticks, timeLimit);
    }
    
    public void turn(double angle, double timeLimit) {
        switchProfile(TURN_PROFILE);
        int ticks = (int) (angle * DEGREE_TO_TICKS_MULTIPLIER);
        move(ticks, -ticks, timeLimit);
    }
    
    private void move(int leftTicks, int rightTicks, double timeLimit) {
        moveTimer.reset();
        onTargetTimer.reset();
        drive.getLeftMotor().setEncPosition(0);
        drive.getRightMotor().setEncPosition(0);
        moveTimer.start();
        drive.getLeftMotor().setSetpoint(leftTicks);
        drive.getRightMotor().setSetpoint(rightTicks);
        while (moveTimer.lessThan(timeLimit) && onTargetTimer.lessThan(ON_TARGET_TIME)) {
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
    
    private static boolean onTarget(double current, int target) {
        return Math.abs(current - target) < ON_TARGET_TOLERANCE;
    }
    
    private void saveProfile(double[] pid, int profile) {
        drive.getLeftMotor().setPID(pid[0], pid[1], pid[2], 0, 0, 0, profile);
        drive.getRightMotor().setPID(pid[0], pid[1], pid[2], 0, 0, 0, profile);
    }
    
    private void switchProfile(int profile) {
        drive.getLeftMotor().setProfile(profile);
        drive.getRightMotor().setProfile(profile);
    }

}
