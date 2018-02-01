package org.usfirst.frc.team4253.robot2018.auto;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * Reader for CSV files generated from GeoGebra data.
 */
public class GeoGebraReader {

    /**
     * Reads the CSV file on the robot file system and, if successful, returns the data.
     * 
     * <p>This returns an Optional object because there may be a failure when trying to read the
     * file.
     * 
     * @param filename the name of the file to read
     * @return the data from the CSV file
     */
    public static Optional<Path> readFile(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            int stage = Integer.parseInt(lines.get(0));
            Side start = parseSide(lines.get(1));
            Side end = parseSide(lines.get(2));
            boolean reverse = parseDirection(lines.get(3));
            GeoGebraEntry[] motorData = new GeoGebraEntry[lines.size() - 4];
            for (int i = 0; i < motorData.length; i++) {
                String[] row = lines.get(i + 4).split(",");
                motorData[i] =
                    new GeoGebraEntry(Double.parseDouble(row[0]), Double.parseDouble(row[1]));
            }
            return Optional.of(new Path(stage, start, end, reverse, motorData));
        } catch (Exception andrew) {
            System.err.println("Failed to read file: " + andrew.getMessage());
            return Optional.empty();
        }
    }

    private static Side parseSide(String input) {
        switch (input.toLowerCase()) {
            case "left":
                return Side.Left;
            case "center":
                return Side.Center;
            case "right":
                return Side.Right;
            default:
                throw new IllegalArgumentException("Failed to parse side " + input);
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
