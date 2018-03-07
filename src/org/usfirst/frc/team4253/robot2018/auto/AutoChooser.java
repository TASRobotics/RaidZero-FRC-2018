package org.usfirst.frc.team4253.robot2018.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * SmartDashboard controls for selecting autonomous information.
 */
public class AutoChooser {

    private static SendableChooser<StartingSide> startingSideChooser;
    private static SendableChooser<Plan> planChooser;

    /**
     * Creates the SendableChoosers and puts them onto the Smart Dashboard.
     */
    public static void initialize() {
        startingSideChooser = new SendableChooser<>();
        startingSideChooser.addDefault("Center", StartingSide.Center);
        startingSideChooser.addObject("Left", StartingSide.Left);
        startingSideChooser.addObject("Right", StartingSide.Right);
        SmartDashboard.putData("Starting side", startingSideChooser);

        planChooser = new SendableChooser<>();
        planChooser.addDefault("Switch then scale", Plan.SwitchThenScale);
        planChooser.addObject("Scale then switch", Plan.ScaleThenSwitch);
        planChooser.addObject("Scale first if same side", Plan.ScaleFirstIfSameSide);
        planChooser.addObject("Switch only", Plan.SwitchOnly);
        planChooser.addObject("Actually scale only", Plan.ActuallyScaleOnly);
        planChooser.addObject("Cross line", Plan.CrossLine);
        planChooser.addObject("Do nothing", Plan.DoNothing);
        SmartDashboard.putData("Auto plan", planChooser);
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
     * Returns the selected autonomous plan that the robot should run.
     * 
     * @return the auto plan
     */
    public static Plan getPlan() {
        return planChooser.getSelected();
    }

}
