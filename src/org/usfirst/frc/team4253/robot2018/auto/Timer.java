package org.usfirst.frc.team4253.robot2018.auto;

import edu.wpi.first.wpilibj.Utility;

/**
 * A timer that counts up from zero.
 * 
 * <p>Actually, it's a stopwatch.
 */
public class Timer {

    private boolean running;
    private long startTime;
    private long time;

    /**
     * Constructs a timer that is initially paused with time = 0.
     */
    public Timer() {
        reset();
    }

    /**
     * Pauses the timer and resets the time to 0.
     */
    public void reset() {
        time = 0;
        running = false;
    }

    /**
     * Starts the timer.
     * 
     * <p>You should only call this method when the timer is not already running.
     */
    public void start() {
        if (!running) {
            startTime = Utility.getFPGATime();
            running = true;
        } else {
            System.err.println("[Timer] start() called on a timer that was already running");
        }
    }

    /**
     * Pauses the timer.
     * 
     * <p>You should only call this method when the timer is not already paused.
     */
    public void pause() {
        if (running) {
            time += getTimeSinceLastStart();
            running = false;
        } else {
            System.err.println("[Timer] pause() called on a timer that was not running");
        }
    }

    /**
     * Returns the elapsed time in seconds.
     * 
     * @return the elapsed time in seconds
     */
    public double getTime() {
        return (running
                ? time + getTimeSinceLastStart()
                : time) / 1e6; // convert microseconds to seconds
    }

    /**
     * Returns whether the elapsed time is less than the given time in seconds.
     * 
     * @param time2 a time to compare to, in seconds
     * @return whether the elapsed time is less than the given time
     */
    public boolean lessThan(double time2) {
        return getTime() < time2;
    }

    /**
     * Returns whether the timer is currently running (i.e. not paused).
     * 
     * @return whether the timer is currently running (i.e. not paused)
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Returns the time in microseconds since the last time start() was called.
     * 
     * @return the time in microseconds since the last time start() was called
     */
    private long getTimeSinceLastStart() {
        return Utility.getFPGATime() - startTime;
    }

    /**
     * Pauses the execution of the current thread for the given amount of seconds.
     * 
     * @param seconds how long to pause the thread, in seconds
     */
    public static void delay(double seconds) {
        edu.wpi.first.wpilibj.Timer.delay(seconds);
    }

}
