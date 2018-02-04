package org.usfirst.frc.team4253.robot2018.auto;

public class PlateData {

    private Side nearSwitchSide;
    private Side scaleSide;
    private Side farSwitchSide;

    public PlateData(Side nearSwitchSide, Side scaleSide, Side farSwitchSide) {
        this.nearSwitchSide = nearSwitchSide;
        this.scaleSide = scaleSide;
        this.farSwitchSide = farSwitchSide;
    }

    public Side getNearSwitchSide() {
        return nearSwitchSide;
    }

    public Side getScaleSide() {
        return scaleSide;
    }

    public Side getFarSwitchSide() {
        return farSwitchSide;
    }

}
