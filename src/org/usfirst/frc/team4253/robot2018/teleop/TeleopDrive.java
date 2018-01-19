package org.usfirst.frc.team4253.robot2018.teleop;

import org.usfirst.frc.team4253.robot2018.components.Drive;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class TeleopDrive {

    private Drive drive;
    private DifferentialDrive differentialDrive;

    /**
     * Constructs a TeleopDrive object and assigns the motors from the drive param to
     * differentialDrive.
     * 
     * @param drive the Drive object to use
     */
    public TeleopDrive(Drive drive) {
        this.drive = drive;
        differentialDrive = new DifferentialDrive(drive.getLeftMotor(), drive.getRightMotor());
    }

    /**
     * Sets up the TeleopDrive settings.
     * 
     * <p> This should be called when teleop starts.
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
        differentialDrive.tankDrive(-leftInput, -rightInput);
    }

}
