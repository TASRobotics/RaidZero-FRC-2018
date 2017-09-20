package org.usfirst.frc.team4253.robot2017.teleop;

import org.usfirst.frc.team4253.robot2017.components.Drive;

import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.RobotDrive;

public class TeleopDrive {
    
    private static final double ROTATE_MULTIPLIER = 0.8;
    
    private Drive drive;
    private RobotDrive robotDrive;
    
    public TeleopDrive(Drive drive) {
        this.drive = drive;
        robotDrive = new RobotDrive(drive.getLeftMotor(), drive.getRightMotor());
    }
    
    public void setup() {
        drive.setControlMode(TalonControlMode.PercentVbus);
        drive.setLowGear();
    }
    
    public void drive(double yAxis, double xAxis) {
        robotDrive.arcadeDrive(yAxis, xAxis * ROTATE_MULTIPLIER, false);
    }
    
}
