package org.usfirst.frc.team4253.robot2018.auto;

/**
 * Autonomous plan.
 */
public enum Plan {
    /**
     * Switch then stop.
     */
    SwitchOnly,
    /**
     * Switch then scale.
     */
    SwitchThenScale,
    /**
     * Scale then move back and stop.
     */
    ActuallyScaleOnly,
    /**
     * Scale then switch.
     * 
     * <p>Unless we start from the side and the switch is on the same side as us and the scale is on
     * the opposite side. Then we do switch first.
     */
    ScaleThenSwitch,
    /**
     * Double scale.
     */
    DoubleScale,
    /**
     * Cross the line (only works if we start at the side).
     */
    CrossLine,
    /**
     * BARKER
     */
    Elims,
    /**
     * Do nothing.
     */
    DoNothing
}
