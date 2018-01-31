package org.usfirst.frc.team4253.robot2018;

import org.usfirst.frc.team4253.robot2018.auto.Auto;
import org.usfirst.frc.team4253.robot2018.components.Components;
import org.usfirst.frc.team4253.robot2018.teleop.Teleop;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The main class of the program.
 */
public class Robot extends IterativeRobot {

    /**
     * Initializes everything.
     */
    @Override
    public void robotInit() {
        Components.initialize();
        Auto.initialize();
        Teleop.initialize();
    }

    /**
     * Runs setup code for autonomous mode.
     * 
     * <p>This is called once when autonomous mode begins.
     */
    @Override
    public void autonomousInit() {
        Auto.setup();
    }

    /**
     * Runs periodic code for autonomous mode.
     * 
     * <p>This is called repeatedly during autonomous mode.
     */
    @Override
    public void autonomousPeriodic() {
        Auto.run();
    }

    /**
     * Runs setup code for teleop mode.
     * 
     * <p>This is called once when teleop mode begins.
     */
    @Override
    public void teleopInit() {
        Teleop.setup();
    }

    /**
     * Runs periodic code for teleop mode.
     * 
     * <p>This is called repeatedly during teleop mode.
     */
    @Override
    public void teleopPeriodic() {
        Teleop.run();
    }

}
