package org.usfirst.frc.team4253.robot2018.auto;

/**
 * A row of data from GeoGebra, with angle and percent difference.
 */
public class GeoGebraEntry {

    private final double angle;
    private final double percentDifference;

    /**
     * Constructs a GeoGebraEntry object.
     * 
     * @param angle the angle of the robot
     * @param percentDifference the percent difference between the velocities of the two sides
     */
    public GeoGebraEntry(double angle, double percentDifference) {
        this.angle = angle;
        this.percentDifference = percentDifference;
    }

    /**
     * Returns the angle.
     * 
     * @return the angle
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Returns the percent difference.
     * 
     * @return the percent difference
     */
    public double getPercentDifference() {
        return percentDifference;
    }

}
