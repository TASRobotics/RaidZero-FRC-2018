package org.usfirst.frc.team4253.robot2018.auto;

import edu.wpi.first.wpilibj.DriverStation;

public class MatchData {

    public static PlateData getPlateData() {
        String message = DriverStation.getInstance().getGameSpecificMessage().toUpperCase();
        return new PlateData(
            parseSide(message.charAt(0)),
            parseSide(message.charAt(1)),
            parseSide(message.charAt(2)));
    }

    private static Side parseSide(char s) {
        switch (s) {
            case 'L':
                return Side.Left;
            case 'R':
                return Side.Right;
            default:
                throw new IllegalArgumentException("Invalid side: " + s);
        }
    }

}
