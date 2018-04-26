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
     * @param startingSide the starting position of the robot
     * @param plateData the plate assignment data
     * @return the paths in a list ordered by stage
     */
    public static List<Movement> getPaths(Plan plan, StartingSide startingSide,
        PlateData plateData) {
        ArrayList<Movement> paths = new ArrayList<>();
        try {
            switch (plan) {
                case SwitchOnly:
                    if (startingSide == StartingSide.Center) {
                        paths.add(
                            read(Mode.SwitchScale, 0, startingSide, plateData.getNearSwitchSide()));
                    } else {
                        paths.add(
                            read(Mode.SideSwitch, 0, startingSide, plateData.getNearSwitchSide()));
                    }
                    break;
                case SwitchThenScale:
                    if (startingSide == StartingSide.Center) {
                        paths.add(
                            read(Mode.SwitchScale, 0, startingSide, plateData.getNearSwitchSide()));
                        if (plateData.getNearSwitchSide() == plateData.getScaleSide()) {
                            paths.add(read(Mode.SwitchScale, 1, plateData.getNearSwitchSide(),
                                plateData.getNearSwitchSide().opposite()));
                        } else {
                            paths.add(read(Mode.SwitchScale, 1, plateData.getNearSwitchSide(),
                                plateData.getNearSwitchSide()));
                        }
                        paths.add(read(Mode.SwitchScale, 2, plateData.getScaleSide().opposite(),
                            plateData.getScaleSide()));
                    } else {
                        paths.add(
                            read(Mode.SideSwitch, 0, startingSide, plateData.getNearSwitchSide()));
                        paths.add(read(Mode.SideSwitch, 1, plateData.getNearSwitchSide(),
                            plateData.getNearSwitchSide()));
                        paths.add(read(Mode.SideSwitch, 2, plateData.getNearSwitchSide(),
                            plateData.getScaleSide()));
                    }
                    break;
                case ScaleOnly:
                    paths.add(read(Mode.ScaleFirst, 0, startingSide, plateData.getScaleSide()));
                    paths.add(read(Mode.ScaleFirst, 1, plateData.getScaleSide(),
                        plateData.getScaleSide()));
                    break;
                case ScaleThenSwitch:
                    if (startingSide != StartingSide.Center
                        && startingSide == plateData.getNearSwitchSide().toStartingSide()
                        && plateData.getScaleSide() != plateData.getNearSwitchSide()) {
                        paths.add(
                            read(Mode.SideSwitch, 0, startingSide, plateData.getNearSwitchSide()));
                        paths.add(read(Mode.SideSwitch, 1, plateData.getNearSwitchSide(),
                            plateData.getNearSwitchSide()));
                        paths.add(read(Mode.SideSwitch, 2, plateData.getNearSwitchSide(),
                            plateData.getScaleSide()));
                    } else {
                        paths.add(read(Mode.ScaleFirst, 0, startingSide, plateData.getScaleSide()));
                        paths.add(read(Mode.ScaleFirst, 1, plateData.getScaleSide(),
                            plateData.getScaleSide()));
                        paths.add(read(Mode.ScaleFirst, 2, plateData.getScaleSide(),
                            plateData.getNearSwitchSide()));
                    }
                    break;
                case DoubleScale: {
                    AutoPath stage0 =
                        read(Mode.ScaleFirst, 0, startingSide, plateData.getScaleSide());
                    paths.add(stage0);
                    if (plateData.getScaleSide() == Side.Right) {
                        Turn stage1 = new Turn(Mode.ScaleFirst, TurnType.PivotOnRight, 145);
                        paths.add(stage1);
                        Straight stage2 = new Straight(Mode.ScaleFirst, 65, 145);
                        paths.add(stage2);
                        // Straight stage3 = new Straight(Mode.ScaleFirst, -80, 145);
                        // paths.add(stage3);
                        Turn stage4 = new Turn(Mode.ScaleFirst, TurnType.PivotOnLeft, 30);
                        paths.add(stage4);
                        paths.add(new Straight(Mode.ScaleFirst, 45, 30));
                    } else {
                        Turn stage1 = new Turn(Mode.ScaleFirst, TurnType.PivotOnLeft, -145);
                        paths.add(stage1);
                        Straight stage2 = new Straight(Mode.ScaleFirst, 65, -145);
                        paths.add(stage2);
                        // Straight stage3 = new Straight(Mode.ScaleFirst, -80, 145);
                        // paths.add(stage3);
                        Turn stage4 = new Turn(Mode.ScaleFirst, TurnType.PivotOnRight, -30);
                        paths.add(stage4);
                        paths.add(new Straight(Mode.ScaleFirst, 45, -30));

                        // Turn stage1 =
                        // new Turn(Mode.ScaleFirst, TurnType.PivotOnLeft, -145, 300, 300);
                        // paths.add(stage1);
                        // Straight stage2 = new Straight(Mode.ScaleFirst, 80, -145);
                        // paths.add(stage2);
                        // Straight stage3 = new Straight(Mode.ScaleFirst, -80, -145);
                        // paths.add(stage3);
                        // Turn stage4 = new Turn(Mode.ScaleFirst, TurnType.PivotOnLeft, -45);
                        // paths.add(stage4);
                        // paths.add(new Straight(Mode.ScaleFirst, 10, -45));
                    }
                    break;
                }
                case DoubleSwitch: {
                    AutoPath stage0 =
                        read(Mode.SwitchScale, 0, startingSide, plateData.getNearSwitchSide());
                    paths.add(stage0);
                    double angle =
                        stage0.getMotorData()[stage0.getMotorData().length - 1].getAngle();
                    if (plateData.getScaleSide() == Side.Right) {
                        Turn stage1 = new Turn(Mode.DoubleSwitch, TurnType.PivotOnRight, 70);
                        paths.add(stage1);
                        Straight stage2 = new Straight(Mode.DoubleSwitch, 30, 70);
                        paths.add(stage2);
                        Straight stage3 = new Straight(Mode.DoubleSwitch, -30, 70);
                        paths.add(stage3);
                        Turn stage4 = new Turn(Mode.DoubleSwitch, TurnType.PivotOnRight, 0);
                        paths.add(stage4);
                        Turn stage5 = new Turn(Mode.DoubleSwitch, TurnType.PivotOnRight, 70);
                        paths.add(stage5);
                        Straight stage6 = new Straight(Mode.DoubleSwitch, 35, 70);
                        paths.add(stage6);
                    } else {
                        Turn stage1 = new Turn(Mode.DoubleSwitch, TurnType.PivotOnLeft, -70);
                        paths.add(stage1);
                        Straight stage2 = new Straight(Mode.DoubleSwitch, 30, -70);
                        paths.add(stage2);
                        Straight stage3 = new Straight(Mode.DoubleSwitch, -30, -70);
                        paths.add(stage3);
                        Turn stage4 = new Turn(Mode.DoubleSwitch, TurnType.PivotOnLeft, 0);
                        paths.add(stage4);
                        Turn stage5 = new Turn(Mode.DoubleSwitch, TurnType.PivotOnLeft, -70);
                        paths.add(stage5);
                        Straight stage6 = new Straight(Mode.DoubleSwitch, 35, -70);
                        paths.add(stage6);
                    }
                    break;
                }
                case Elims:
                    if (plateData.getScaleSide() == Side.Right) {
                        paths.add(read(Mode.ScaleFirst, 0, startingSide, plateData.getScaleSide()));
                        paths.add(read(Mode.ScaleFirst, 1, plateData.getScaleSide(),
                            plateData.getScaleSide()));
                    } else if (plateData.getNearSwitchSide() == Side.Right) {
                        paths.add(
                            read(Mode.SideSwitch, 0, startingSide, plateData.getNearSwitchSide()));
                    }
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
            case SideSwitch:
                return "side switch";
            case ScaleFirst:
                return "scale first";
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
            case "side switch":
                return Mode.SideSwitch;
            case "scale first":
                return Mode.ScaleFirst;
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
