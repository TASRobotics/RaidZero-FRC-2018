package org.usfirst.frc.team4253.robot2017.components;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;

public class Drive {

    private static final double LEFT_PEAK_VOLTAGE = 11.7;
    private static final double RIGHT_PEAK_VOLTAGE = 12;

    private CANTalon leftMotor;
    private CANTalon rightMotor;
    private DoubleSolenoid gearShift;

    public Drive(
        int left1ID, int left2ID, int left3ID,
        int right1ID, int right2ID, int right3ID,
        int gearShiftForward, int gearShiftReverse
    ) {
        leftMotor = initLeader(left1ID);
        rightMotor = initLeader(right1ID);
        leftMotor.configPeakOutputVoltage(LEFT_PEAK_VOLTAGE, -LEFT_PEAK_VOLTAGE);
        rightMotor.configPeakOutputVoltage(RIGHT_PEAK_VOLTAGE, -RIGHT_PEAK_VOLTAGE);
        leftMotor.reverseOutput(true);
        leftMotor.reverseSensor(true);

        initFollower(left2ID, leftMotor);
        initFollower(left3ID, leftMotor);
        initFollower(right2ID, rightMotor);
        initFollower(right3ID, rightMotor);

        gearShift = new DoubleSolenoid(gearShiftForward, gearShiftReverse);
    }

    private CANTalon initLeader(int id) {
        CANTalon motor = new CANTalon(id);
        motor.changeControlMode(TalonControlMode.PercentVbus);
        motor.configNominalOutputVoltage(0, 0);
        motor.setVoltageRampRate(50);
        motor.enableBrakeMode(true);
        motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        return motor;
    }

    private void initFollower(int id, CANTalon leader) {
        CANTalon motor = new CANTalon(id);
        motor.changeControlMode(TalonControlMode.Follower);
        motor.enableBrakeMode(true);
        motor.set(leader.getDeviceID());
    }

    public CANTalon getLeftMotor() {
        return leftMotor;
    }

    public CANTalon getRightMotor() {
        return rightMotor;
    }

    public void setLowGear() {
        gearShift.set(DoubleSolenoid.Value.kForward);
    }

    public void setHighGear() {
        gearShift.set(DoubleSolenoid.Value.kReverse);
    }

    public void setControlMode(TalonControlMode mode) {
        leftMotor.changeControlMode(mode);
        rightMotor.changeControlMode(mode);
    }

}
