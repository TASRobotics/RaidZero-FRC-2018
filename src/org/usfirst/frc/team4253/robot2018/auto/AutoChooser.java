package org.usfirst.frc.team4253.robot2018.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoChooser {

    private static SendableChooser<StartingSide> startingSideChooser;
    private static SendableChooser<Mode> modeChooser;

    public static void initialize() {
        startingSideChooser = new SendableChooser<>();
        startingSideChooser.addDefault("Center", StartingSide.Center);
        startingSideChooser.addObject("Left", StartingSide.Left);
        startingSideChooser.addObject("Right", StartingSide.Right);
        SmartDashboard.putData("Starting side", startingSideChooser);

        modeChooser = new SendableChooser<>();
        modeChooser.addDefault("Switch and scale", Mode.SwitchScale);
        modeChooser.addObject("Do nothing", Mode.DoNothing);
        SmartDashboard.putData("Auto mode", modeChooser);
    }

    public static StartingSide getStartingSide() {
        return startingSideChooser.getSelected();
    }

    public static Mode getMode() {
        return modeChooser.getSelected();
    }

}
