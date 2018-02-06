package org.usfirst.frc.team4253.robot2018.teleop;

import org.usfirst.frc.team4253.robot2018.components.Drive;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class TeleopDrive {

    private Drive drive;

    /**
     * Constructs a TeleopDrive object and assigns the motors from the drive param to
     * differentialDrive.
     * 
     * @param drive the Drive object to use
     */
    public TeleopDrive(Drive drive) {
        this.drive = drive;
    }

    /**
     * Sets up the TeleopDrive settings.
     * 
     * <p>This should be called when teleop starts.
     */
    public void setup() {
        drive.setLowGear();
    }

    /**
     * Drives the robot using tankDrive.
     * 
     * @param leftInput left input of the joystick from -1 to 1
     * @param rightInput right input of the joystick from -1 to 1
     */
    public void drive(double leftInput, double rightInput) {
        drive.getLeftMotor().set(ControlMode.PercentOutput, -leftInput);
        drive.getRightMotor().set(ControlMode.PercentOutput, -rightInput);
    }

}
