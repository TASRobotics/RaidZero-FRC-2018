package org.usfirst.frc.team4253.robot2018.auto;

/**
 * Autonomous plan.
 */
public enum Plan {
    /**
     * Switch then scale.
     * 
     * <p>Unless we start from the side and the switch is on the opposite side. Then we do scale
     * first.
     */
    SwitchThenScale,
    /**
     * Scale then switch.
     */
    ScaleThenSwitch,
    /**
     * If the switch and scale are on the same side, then scale then switch, else switch then scale.
     */
    ScaleFirstIfSameSide,
    /**
     * Switch then stop.
     */
    SwitchOnly,
    /**
     * Scale then stop.
     */
    ActuallyScaleOnly,
    /**
     * Cross the line (only works if we start at the side).
     */
    CrossLine,
    /**
     * Do nothing.
     */
    DoNothing
}
