package org.usfirst.frc.team4253.robot2017;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;

public class Drive implements Part {
    
    private static final double LEFT_PEAK_VOLTAGE = 11.7;
    private static final double RIGHT_PEAK_VOLTAGE = 12;
    private static final double TELEOP_ROTATE_MULTIPLIER = 0.8;
    private static final int STRAIGHT_PROFILE = 0;
    private static final int TURN_PROFILE = 1;
    
    private static final double STRAIGHT_P, STRAIGHT_I, STRAIGHT_D;
    static {
        switch (Robot.VERSION) {
            case V2:
                STRAIGHT_P = 0.1;
                break;
            case V3:
                STRAIGHT_P = 0.19;
                break;
            case V4:
            default:
                STRAIGHT_P = 0.2;
        }
        STRAIGHT_I = 0;
        STRAIGHT_D = 0;
    }
    
    private static final double TURN_P, TURN_I, TURN_D;
    static {
        switch (Robot.VERSION) {
            case V2:
                TURN_P = 0.3;
                TURN_I = 0;
                TURN_D = 0.6;
                break;
            case V3:
                TURN_P = 0.32;
                TURN_I = 0.0006;
                TURN_D = 33;
                break;
            case V4:
            default:
                TURN_P = 0.41;
                TURN_I = 0.0006;
                TURN_D = 33;
        }
    }
    
    private CANTalon left;
    private CANTalon right;
    private RobotDrive robotDrive;
    private DoubleSolenoid gearShift;
    
    private Timer timer;

    public Drive(
        int left1, int left2, int left3,
        int right1, int right2, int right3,
        int gearShiftForward, int gearShiftReverse
    ) {
        left = initLeader(left1);
        right = initLeader(right1);
        left.configPeakOutputVoltage(LEFT_PEAK_VOLTAGE, -LEFT_PEAK_VOLTAGE);
        right.configPeakOutputVoltage(RIGHT_PEAK_VOLTAGE, -RIGHT_PEAK_VOLTAGE);
        left.reverseOutput(true);
        left.reverseSensor(true);
        resetEncoders();
        
        initFollower(left2, left);
        initFollower(left3, left);
        initFollower(right2, right);
        initFollower(right3, right);
        
        robotDrive = new RobotDrive(left, right);
        
        gearShift = new DoubleSolenoid(gearShiftForward, gearShiftReverse);
        setLowGear();
        
        timer = new Timer();
    }
    
    private CANTalon initLeader(int deviceNumber) {
        CANTalon motor = new CANTalon(deviceNumber);
        motor.changeControlMode(TalonControlMode.PercentVbus);
        motor.configNominalOutputVoltage(0, 0);
        motor.setVoltageRampRate(50);
        motor.enableBrakeMode(true);
        motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        motor.setPID(STRAIGHT_P, STRAIGHT_I, STRAIGHT_D, 0, 0, 0, STRAIGHT_PROFILE);
        motor.setPID(TURN_P, TURN_I, TURN_D, 0, 0, 0, TURN_PROFILE);
        return motor;
    }
    
    private void initFollower(int deviceNumber, CANTalon leader) {
        CANTalon motor = new CANTalon(deviceNumber);
        motor.enableBrakeMode(true);
        motor.set(leader.getDeviceID());
    }
    
    @Override
    public void autoStart() {
        setControlMode(TalonControlMode.Position);
        setLowGear();
    }
    
    public void moveStraight(double meters, double timeLimit) {
        setProfile(STRAIGHT_PROFILE);
        resetEncoders();
        timer.start();
        
    }
    
    public void teleopDrive(double yAxis, double xAxis) {
        robotDrive.arcadeDrive(yAxis, xAxis * TELEOP_ROTATE_MULTIPLIER, false);
    }
    
    public void setLowGear() {
        gearShift.set(DoubleSolenoid.Value.kForward);
    }
    
    public void setHighGear() {
        gearShift.set(DoubleSolenoid.Value.kReverse);
    }
    
    private void setControlMode(TalonControlMode mode) {
        left.changeControlMode(mode);
        right.changeControlMode(mode);
    }
    
    private void setProfile(int profile) {
        left.setProfile(profile);
        right.setProfile(profile);
    }
    
    private void resetEncoders() {
        left.setEncPosition(0);
        right.setEncPosition(0);
    }
    
}
