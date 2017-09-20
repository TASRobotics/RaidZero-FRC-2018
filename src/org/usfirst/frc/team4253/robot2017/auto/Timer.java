package org.usfirst.frc.team4253.robot2017.auto;

import edu.wpi.first.wpilibj.Utility;

public class Timer {

    private boolean running;
    private long startTime;
    private long time;

    public Timer() {
        reset();
    }

    public void reset() {
        time = 0;
        running = false;
    }

    public void start() {
        if (!running) {
            startTime = Utility.getFPGATime();
        } else {
            System.err.println("[Timer] start() called on a timer that was already running");
        }
    }

    public void pause() {
        if (running) {
            time += getTimeSinceLastStart();
            running = false;
        } else {
            System.err.println("[Timer] pause() called on a timer that was not running");
        }
    }

    public double getTime() {
        return (running
                ? time + getTimeSinceLastStart()
                : time) / 1e6; // convert microseconds to seconds
    }

    public boolean lessThan(double time2) {
        return getTime() < time2;
    }

    public boolean isRunning() {
        return running;
    }

    private long getTimeSinceLastStart() {
        return Utility.getFPGATime() - startTime;
    }

    public static void delay(double seconds) {
        edu.wpi.first.wpilibj.Timer.delay(seconds);
    }

}
