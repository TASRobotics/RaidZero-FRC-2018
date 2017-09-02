package org.usfirst.frc.team4253.robot2017;

import org.usfirst.frc.team4253.robot2017.Controller.Axis;
import org.usfirst.frc.team4253.robot2017.Controller.Button;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot {

    public static enum Version { V2, V3, V4 }
    public static final Version VERSION = Version.V4;
    
    private Controller firstController;
    private Controller secondController;
    private Drive drive;
    
    private Part[] parts;

	@Override
	public void robotInit() {
	    firstController = new Controller(0);
        secondController = new Controller(1);
        drive = new Drive(1, 2, 3, 4, 5, 6, 0, 1);
        
        parts = new Part[] {
            drive
        };
	}

	@Override
	public void autonomous() {
	    for (Part part : parts) {
	        part.autoStart();
	    }
	}
	
	private void runControls() {
	    firstController
	        .ifPressed(Button.RB, drive::setHighGear)
	        .ifPressed(Button.RT, drive::setLowGear);
	    drive.teleopDrive(
	        firstController.getAxisValue(Axis.LeftY),
	        firstController.getAxisValue(Axis.LeftX));
	    
	}

	@Override
	public void operatorControl() {
	    for (Part part : parts) {
	        part.teleopStart();
	    }
		while (isOperatorControl() && isEnabled()) {
			for (Part part : parts) {
			    part.teleopLoop();
			}
			runControls();
			Timer.delay(0.005);
		}
	}

	@Override
	public void test() {
	    
	}
	
}
