package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * The drive system.
 */
public class Drive {

    private WPI_TalonSRX leftMotor;
    private WPI_TalonSRX rightMotor;
    private DoubleSolenoid gearShift;

    /**
     * Constructs a Drive object and sets up the motors and gear shift.
     * 
     * @param leftLeaderID the ID of the left leader motor
     * @param rightLeaderID the ID of the right leader motor
     * @param leftFollowerID the ID of the left follower motor
     * @param rightFollowerID the ID of the right follower motor
     * @param gearShiftForward the forward channel for the gear shift
     * @param gearShiftReverse the reverse channel for the gear shift
     */
    //@formatter:off
    public Drive(
        int leftLeaderID, int rightLeaderID,
        int leftFollowerID, int rightFollowerID,
        int gearShiftForward, int gearShiftReverse
    ) {
        //@formatter:on
        leftMotor = initSide(leftLeaderID, leftFollowerID);
        rightMotor = initSide(rightLeaderID, rightFollowerID);
        gearShift = new DoubleSolenoid(gearShiftForward, gearShiftReverse);
    }

    /**
     * Constructs and configures the motors for one side of the robot (i.e. one leader and one
     * follower), and returns the leader motor object.
     * 
     * <p>TODO: Make sure allowing the follower motor to be garbage collected is OK.
     * 
     * @param leaderID the ID of the leader motor
     * @param followerID the ID of the follower motor
     * @return the newly constructed leader motor object
     */
    private WPI_TalonSRX initSide(int leaderID, int followerID) {
        WPI_TalonSRX leader = new WPI_TalonSRX(leaderID);
        WPI_TalonSRX follower = new WPI_TalonSRX(followerID);

        leader.setNeutralMode(NeutralMode.Brake);
        follower.setNeutralMode(NeutralMode.Brake);

        leader.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, MotorSettings.PID_IDX,
            MotorSettings.TIMEOUT);

        follower.set(ControlMode.Follower, leaderID);

        return leader;
    }

    /**
     * Returns the left leader motor.
     * 
     * <p>Anything done to this motor will also be followed by the other left motor.
     * 
     * @return the left leader motor
     */
    public WPI_TalonSRX getLeftMotor() {
        return leftMotor;
    }

    /**
     * Returns the right leader motor.
     * 
     * <p>Anything done to this motor will also be followed by the other right motor.
     * 
     * @return the right leader motor
     */
    public WPI_TalonSRX getRightMotor() {
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

}
