package org.usfirst.frc.team4253.robot2017.teleop;

import org.usfirst.frc.team4253.robot2017.components.Drive;

import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 * Teleop-specific functionality for the drive.
 */
public class TeleopDrive {

    /**
     * Scale factor for the rotation speed.
     */
    private static final double ROTATE_MULTIPLIER = 0.8;

    private Drive drive;
    private RobotDrive robotDrive;

    /**
     * Constructs a TeleopDrive object which uses the given Drive object.
     * 
     * <p>This does not create a new drive. Instead, it uses the existing Drive object that you pass
     * in. So when you call methods on a TeleopDrive instance, it controls the motors of the
     * underlying Drive object.
     * 
     * @param drive the Drive object to use
     */
    public TeleopDrive(Drive drive) {
        this.drive = drive;
        robotDrive = new RobotDrive(drive.getLeftMotor(), drive.getRightMotor());
    }

    /**
     * Configures the drive for teleop driving.
     * 
     * <p>This should be called when teleop starts, before {@link #drive(double, double) drive} is
     * called.
     */
    public void setup() {
        drive.setControlMode(TalonControlMode.PercentVbus);
        drive.setLowGear();
    }

    /**
     * Drives the robot given the y-axis and x-axis values of the input joystick in arcade drive
     * configuration.
     * 
     * <p>Note: y-axis value (controls forward/backward) is the first argument, and x-axis value
     * (controls rotation left/right) is the second argument.
     * 
     * <p>This method only sets the motor powers of the drive motors once. If you want to
     * continuously update the drive based on the joystick input, you should call this method
     * repeatedly with new input from the joystick.
     * 
     * @param yAxis the y-axis value of the input joystick (controls forward/backward)
     * @param xAxis the x-axis value of the input joystick (controls rotation left/right)
     */
    public void drive(double yAxis, double xAxis) {
        robotDrive.arcadeDrive(yAxis, xAxis * ROTATE_MULTIPLIER, false);
    }

}
