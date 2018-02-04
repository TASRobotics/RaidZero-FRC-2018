package org.usfirst.frc.team4253.robot2018.auto;

public class AutoPath {

    private int stage;
    private StartingSide start; // can only be center when stage == 0
    private Side end;
    private boolean reverse;
    private GeoGebraEntry[] motorData;

    public AutoPath(int stage, StartingSide start, Side end, boolean reverse,
        GeoGebraEntry[] motorData) {
        this.stage = stage;
        this.start = start;
        this.end = end;
        this.reverse = reverse;
        this.motorData = motorData;
    }

    public int getStage() {
        return stage;
    }

    public StartingSide getStart() {
        return start;
    }

    public Side getEnd() {
        return end;
    }

    public boolean getReverse() {
        return reverse;
    }

    public GeoGebraEntry[] getMotorData() {
        return motorData;
    }

}
