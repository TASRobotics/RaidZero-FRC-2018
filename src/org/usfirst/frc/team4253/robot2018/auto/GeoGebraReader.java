package org.usfirst.frc.team4253.robot2018.auto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Reader for CSV files generated from GeoGebra data.
 */
public class GeoGebraReader {

    private static final String FILENAME = "/home/lvuser/data.csv";

    /**
     * Reads the CSV file on the robot file system and, if successful, returns the data.
     * 
     * <p>This returns an Optional object because there may be a failure when trying to read the
     * file.
     * 
     * @return the data from the CSV file
     */
    public static Optional<GeoGebraEntry[]> readFile(String filename) {
        try {
            return Optional.of(Files.lines(Paths.get(filename)).map(line -> {
                String[] nums = line.split(",");
                return new GeoGebraEntry(Double.parseDouble(nums[1]), Double.parseDouble(nums[0]));
            }).toArray(GeoGebraEntry[]::new));
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e);
            return Optional.empty();
        }
    }

}
