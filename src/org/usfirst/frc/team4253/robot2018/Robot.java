package org.usfirst.frc.team4253.robot2018;

import edu.wpi.first.wpilibj.IterativeRobot;
import org.usfirst.frc.team4253.robot2018.auto.Auto;
import org.usfirst.frc.team4253.robot2018.components.Components;
import org.usfirst.frc.team4253.robot2018.teleop.Teleop;

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
     * Runs autonomous code.
     * 
     * <p>This is called once when autonomous mode begins.
     */
    @Override
    public void autonomousInit() {
        Auto.setup();
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
     * <p>This method is called repeatedly in a loop during teleop mode.
     */
    @Override
    public void teleopPeriodic() {
        Teleop.run();
    }

}
