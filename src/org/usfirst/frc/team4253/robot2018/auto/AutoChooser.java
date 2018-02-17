package org.usfirst.frc.team4253.robot2018.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * SmartDashboard controls for selecting autonomous information.
 */
public class AutoChooser {

    private static SendableChooser<StartingSide> startingSideChooser;
    private static SendableChooser<Mode> modeChooser;

    /**
     * Creates the SendableChoosers and puts them onto the Smart Dashboard.
     */
    public static void initialize() {
        startingSideChooser = new SendableChooser<>();
        startingSideChooser.addDefault("Center", StartingSide.Center);
        startingSideChooser.addObject("Left", StartingSide.Left);
        startingSideChooser.addObject("Right", StartingSide.Right);
        SmartDashboard.putData("Starting side", startingSideChooser);

        modeChooser = new SendableChooser<>();
        modeChooser.addDefault("Switch and scale", Mode.SwitchScale);
        modeChooser.addObject("Cross Line", Mode.CrossLine);
        modeChooser.addObject("Scale Only", Mode.ScaleOnly);
        modeChooser.addObject("Do nothing", Mode.DoNothing);
        SmartDashboard.putData("Auto mode", modeChooser);
    }

    /**
     * Returns the selected starting side of the robot.
     * 
     * @return the starting side
     */
    public static StartingSide getStartingSide() {
        return startingSideChooser.getSelected();
    }

    /**
     * Returns the selected autonomous mode that the robot should run.
     * 
     * @return the auto mode
     */
    public static Mode getMode() {
        return modeChooser.getSelected();
    }

}
