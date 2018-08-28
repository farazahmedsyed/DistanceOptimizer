package v1;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by VenD on 2/7/2018.
 */
public class PairGenerator {

    public static void generate(String sourceFile, String destinationFile) throws Exception {
        List<String> coordinates = new ArrayList<>();
        StringBuilder locationPairs = new StringBuilder();
        String locationPair;
        Scanner reader = new Scanner(new FileReader(sourceFile));
        while (reader.hasNextLine()) {
            coordinates.add(reader.nextLine());
        }
        reader.close();

        locationPairs.append("[");
        for (String source : coordinates) {
            for (String destination : coordinates) {
                if (!source.trim().equalsIgnoreCase(destination.trim())) {
                    locationPair = "{ " +
                            "\"srcLocStr\" : \"" + source + "\"," +
                            "\"destLocStr\" : \"" + destination + "\"," +
                            "\"sent\" : false" +
                            " },";
                    locationPairs.append(locationPair);
                }
            }
        }
        locationPairs.append("]");
        BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFile));
        writer.write(locationPairs.toString());
        writer.close();

    }
}
