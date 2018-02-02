package org.usfirst.frc.team4253.robot2018.auto;


public class Path {

    private int stage;
    private Side start;
    private Side end;
    private boolean reverse;
    private GeoGebraEntry[] motorData;

    public Path(int stage, Side start, Side end, boolean reverse, GeoGebraEntry[] motorData) {
        this.stage = stage;
        this.start = start;
        this.end = end;
        this.reverse = reverse;
        this.motorData = motorData;
    }

    public int getStage() {
        return stage;
    }

    public Side getStart() {
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
