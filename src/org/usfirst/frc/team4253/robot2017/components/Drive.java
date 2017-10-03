package org.usfirst.frc.team4253.robot2017.components;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * The drive system (i.e. the six wheels and gear shift).
 */
public class Drive {

    /**
     * The peak output voltage for the left motor.
     */
    private static final double LEFT_PEAK_VOLTAGE = 11.7;
    /**
     * The peak output voltage for the right motor.
     */
    private static final double RIGHT_PEAK_VOLTAGE = 12;

    private CANTalon leftMotor;
    private CANTalon rightMotor;
    private DoubleSolenoid gearShift;

    /**
     * Constructs a Drive object and sets up the motors and gear shift.
     * 
     * @param left1ID the ID of the first left motor
     * @param right1ID the ID of the first right motor
     * @param left2ID the ID of the second left motor
     * @param right2ID the ID of the second right motor
     * @param left3ID the ID of the third left motor
     * @param right3ID the ID of the third right motor
     * @param gearShiftForward the forward channel for the gear shift
     * @param gearShiftReverse the reverse channel for the gear shift
     */
    //@formatter:off
    public Drive(
        int left1ID, int right1ID,
        int left2ID, int right2ID,
        int left3ID, int right3ID,
        int gearShiftForward, int gearShiftReverse
    ) {
        //@formatter:on
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

    /**
     * Constructs, configures, and returns a CANTalon object.
     * 
     * @param id the ID of the motor
     * @return the newly constructed CANTalon object
     */
    private CANTalon initLeader(int id) {
        CANTalon motor = new CANTalon(id);
        motor.changeControlMode(TalonControlMode.PercentVbus);
        motor.configNominalOutputVoltage(0, 0);
        motor.setVoltageRampRate(50);
        motor.enableBrakeMode(true);
        motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        return motor;
    }

    /**
     * Constructs and configures a follower CANTalon given the leader motor.
     * 
     * <p>Unlike {@link #initLeader(int) initLeader}, this method does not return the constructed
     * CANTalon object, because once it is set to follower mode there is no more use in keeping the
     * object around.
     * 
     * @param id the ID of the follower motor
     * @param leader the motor to follow
     */
    private void initFollower(int id, CANTalon leader) {
        CANTalon motor = new CANTalon(id);
        motor.changeControlMode(TalonControlMode.Follower);
        motor.enableBrakeMode(true);
        motor.set(leader.getDeviceID());
    }

    /**
     * Returns the left motor.
     * 
     * <p>Since the other two left motors are set to follow this one, anything done to this one will
     * also be done to the other two.
     * 
     * @return the left motor
     */
    public CANTalon getLeftMotor() {
        return leftMotor;
    }

    /**
     * Returns the right motor.
     * 
     * <p>Since the other two right motors are set to follow this one, anything done to this one
     * will also be done to the other two.
     * 
     * @return the right motor
     */
    public CANTalon getRightMotor() {
        return rightMotor;
    }

    /**
     * Sets the gear shift to low gear.
     */
    public void setLowGear() {
        gearShift.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Sets the gear shift to high gear.
     */
    public void setHighGear() {
        gearShift.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Sets the control mode of both drive motors.
     * 
     * @param mode the control mode
     */
    public void setControlMode(TalonControlMode mode) {
        leftMotor.changeControlMode(mode);
        rightMotor.changeControlMode(mode);
    }

}
