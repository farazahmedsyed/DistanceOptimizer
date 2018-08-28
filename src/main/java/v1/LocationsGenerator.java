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
public class LocationsGenerator {

    public static void generate(String sourceFile, String destinationFile) throws Exception{
        List<String> coordinates = new ArrayList<>();
        StringBuilder locationPairs = new StringBuilder();
        String locationPair;
        Scanner reader = new Scanner(new FileReader(sourceFile));
        while (reader.hasNextLine()) {
            coordinates.add(reader.nextLine());
        }
        reader.close();

        locationPairs.append("[");
        for (String location : coordinates) {
            locationPair = "{ " +
                    "\"loc\" : \"" + location + "\"," +
                    "\"completed\" : false" +
                    " },";
            locationPairs.append(locationPair);

        }
        locationPairs.append("]");
        BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFile));
        writer.write(locationPairs.toString());
        writer.close();

    }
}
