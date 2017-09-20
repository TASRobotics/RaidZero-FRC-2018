package org.usfirst.frc.team4253.robot2017;

import org.usfirst.frc.team4253.robot2017.auto.Auto;
import org.usfirst.frc.team4253.robot2017.components.Components;
import org.usfirst.frc.team4253.robot2017.teleop.Teleop;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot {

    @Override
    public void robotInit() {
        Components.initialize();
        Auto.initialize();
        Teleop.initialize();
    }

    @Override
    public void autonomous() {
        Auto.setup();
        Auto.run();
    }

    @Override
    public void operatorControl() {
        Teleop.setup();
        while (isOperatorControl() && isEnabled()) {
            Teleop.run();
            Timer.delay(0.005);
        }
    }

}
