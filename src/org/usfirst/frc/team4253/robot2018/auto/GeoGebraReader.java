package org.usfirst.frc.team4253.robot2018.auto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Reader for CSV files generated from GeoGebra data.
 */
public class GeoGebraReader {

    private static final Path DIRECTORY = Paths.get("/home", "lvuser", "paths");

    /**
     * Reads the necessary CSV files and returns the autonomous paths.
     * 
     * <p>If there is any error reading a file, then the returned list will simply not contain the
     * data from that file and any subsequent files.
     * 
     * @param plan the autonomous plan
     * @param mode the autonomous mode
     * @param startingSide the starting position of the robot
     * @param plateData the plate assignment data
     * @return the paths in a list ordered by stage
     */
    public static List<AutoPath> getPaths(Plan plan, Mode mode, StartingSide startingSide,
        PlateData plateData) {
        ArrayList<AutoPath> paths = new ArrayList<>();
        try {
            switch (mode) {
                case SwitchScale:
                    paths.add(read(mode, 0, startingSide, plateData.getNearSwitchSide()));
                    if (plan == Plan.SwitchOnly) {
                        break;
                    }
                    if (plateData.getNearSwitchSide() == plateData.getScaleSide()) {
                        paths.add(read(mode, 1, plateData.getNearSwitchSide(),
                            plateData.getNearSwitchSide().opposite()));
                    } else {
                        paths.add(read(mode, 1, plateData.getNearSwitchSide(),
                            plateData.getNearSwitchSide()));
                    }
                    paths.add(read(mode, 2, plateData.getScaleSide().opposite(),
                            plateData.getScaleSide()));
                    break;
                case ScaleOnly:
                    paths.add(read(mode, 0, startingSide, plateData.getScaleSide()));
                    if (plan == Plan.ActuallyScaleOnly) {
                        break;
                    }
                    paths.add(read(mode, 1, plateData.getScaleSide(), plateData.getScaleSide()));
                    paths.add(
                        read(mode, 2, plateData.getScaleSide(), plateData.getNearSwitchSide()));
                    break;
                default:
                    break;
            }
        } catch (Exception andrew) {
            System.err.println("Failed reading file: " + andrew.getMessage());
            andrew.printStackTrace();
        }
        return paths;
    }

    private static AutoPath read(Mode mode, int stage, StartingSide start, Side end)
        throws IOException {
        return readFile(getFilename(mode, stage, start, end));
    }

    private static AutoPath read(Mode mode, int stage, Side start, Side end) throws IOException {
        return read(mode, stage, start.toStartingSide(), end);
    }

    private static String getFilename(Mode mode, int stage, StartingSide start, Side end) {
        return getModeString(mode) + "-" + stage + "-" + getStartString(start) + "-"
            + getEndString(end) + ".csv";
    }

    private static String getModeString(Mode mode) {
        switch (mode) {
            case SwitchScale:
                return "switch and scale";
            case ScaleOnly:
                return "scale only";
            default:
                throw new IllegalArgumentException(
                    "Attempted to read file for invalid mode: " + mode);
        }
    }

    private static String getStartString(StartingSide start) {
        switch (start) {
            case Left:
                return "left";
            case Center:
                return "center";
            case Right:
                return "right";
            default:
                return null; // this should never happen
        }
    }

    private static String getEndString(Side end) {
        return getStartString(end.toStartingSide());
    }

    private static AutoPath readFile(String filename) throws IOException {
        List<String> lines = Files.readAllLines(DIRECTORY.resolve(filename));
        Mode mode = parseMode(lines.get(0));
        int stage = Integer.parseInt(lines.get(1));
        StartingSide start = parseStartingSide(lines.get(2));
        Side end = parseSide(lines.get(3));
        boolean reverse = parseDirection(lines.get(4));
        GeoGebraEntry[] motorData = new GeoGebraEntry[lines.size() - 5];
        for (int i = 0; i < motorData.length; i++) {
            String[] row = lines.get(i + 5).split(",");
            motorData[i] =
                new GeoGebraEntry(Double.parseDouble(row[0]), Double.parseDouble(row[1]));
        }
        return new AutoPath(mode, stage, start, end, reverse, motorData);
    }

    private static Mode parseMode(String input) {
        switch (input.toLowerCase()) {
            case "switch and scale":
                return Mode.SwitchScale;
            case "scale only":
                return Mode.ScaleOnly;
            default:
                throw new IllegalArgumentException("Failed to parse mode: " + input);
        }
    }

    private static StartingSide parseStartingSide(String input) {
        switch (input.toLowerCase()) {
            case "left":
                return StartingSide.Left;
            case "center":
                return StartingSide.Center;
            case "right":
                return StartingSide.Right;
            default:
                throw new IllegalArgumentException("Failed to parse starting side: " + input);
        }
    }

    private static Side parseSide(String input) {
        switch (input.toLowerCase()) {
            case "left":
                return Side.Left;
            case "right":
                return Side.Right;
            default:
                throw new IllegalArgumentException("Failed to parse side: " + input);
        }
    }

    private static boolean parseDirection(String input) {
        switch (input.toLowerCase()) {
            case "forward":
                return false;
            case "backward":
                return true;
            default:
                throw new IllegalArgumentException("Failed to parse direction " + input);
        }
    }

}
