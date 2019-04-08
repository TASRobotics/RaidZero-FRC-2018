package org.usfirst.frc.team4253.robot2018.auto;

/**
 * Plate assignment data.
 */
public class PlateData {

    private Side nearSwitchSide;
    private Side scaleSide;
    private Side farSwitchSide;

    /**
     * Constructs a PlateData object.
     * 
     * @param nearSwitchSide the side of the near switch that we are assigned.
     * @param scaleSide the side of the scale that we are assigned.
     * @param farSwitchSide the side of the far switch that we are assigned.
     */
    public PlateData(Side nearSwitchSide, Side scaleSide, Side farSwitchSide) {
        this.nearSwitchSide = nearSwitchSide;
        this.scaleSide = scaleSide;
        this.farSwitchSide = farSwitchSide;
    }

    /**
     * Returns the side of the near switch that we are assigned.
     * 
     * @return the side of the near switch
     */
    public Side getNearSwitchSide() {
        return nearSwitchSide;
    }

    /**
     * Returns the side of the scale that we are assigned.
     * 
     * @return the side of the scale
     */
    public Side getScaleSide() {
        return scaleSide;
    }

    /**
     * Returns the side of the far switch that we are assigned.
     * 
     * @return the side of the far switch
     */
    public Side getFarSwitchSide() {
        return farSwitchSide;
    }

}
