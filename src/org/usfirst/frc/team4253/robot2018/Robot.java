package org.usfirst.frc.team4253.robot2018;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import org.usfirst.frc.team4253.robot2018.auto.Auto;
import org.usfirst.frc.team4253.robot2018.components.Components;
import org.usfirst.frc.team4253.robot2018.teleop.Teleop;

/**
 * The main class of the program.
 * 
 * <p>The methods in this class are automatically called by the WPI library when the robot switches
 * to a different mode.
 */
public class Robot extends SampleRobot {

    /**
     * Initializes everything.
     * 
     * <p>This method is automatically called when the roboRIO starts up. It will only be called
     * once.
     * 
     * <p>This is similar to a constructor, but it is better to put any code in here rather than the
     * constructor because WPILib does some initialization stuff before calling this method.
     */
    @Override
    public void robotInit() {
        Components.initialize();
        Auto.initialize();
        Teleop.initialize();
    }

    /**
     * Runs autonomous code.
     * 
     * <p>This method is automatically called once when autonomous mode is started.
     */
    @Override
    public void autonomous() {
        Auto.setup();
        Auto.run();
    }

    /**
     * Runs teleop (remote control) code.
     * 
     * <p>This method is automatically called once when teleop mode is started. However, it keeps
     * running until teleop mode is over, because it has a loop inside.
     */
    @Override
    public void operatorControl() {
        Teleop.setup();
        while (isOperatorControl() && isEnabled()) {
            Teleop.run();
            Timer.delay(0.005);
        }
    }

}
