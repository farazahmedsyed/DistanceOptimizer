package v1;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by VenD on 1/31/2018.
 */
public class SingaporeExtractor {
    public static void extract(String sourceFile, String destinationFile){
        try {
            Integer postCodeLength = 3;
            List<String> postCodes = new ArrayList<>();
            Set<String> processedPostCodes = new HashSet<>();
            List<String> postCodesOfRange;

            Scanner file = new Scanner(new FileReader(sourceFile));
            while(file.hasNext())
                postCodes.add(file.next());
            file.close();
            Collections.sort(postCodes);

            String processedPostCode = null;
            for(String postCode : postCodes){
                if(!processedPostCodes.contains(processedPostCode)) {
                    processedPostCode = postCode.substring(0, postCodeLength);
                    final String finalProcessedPostCode = processedPostCode;
                    postCodesOfRange = postCodes.stream().filter(code -> code.substring(0, postCodeLength).equals(finalProcessedPostCode)).collect(Collectors.toList());
                    processedPostCodes.add(postCodesOfRange.get(0));
                    processedPostCodes.add(postCodesOfRange.get(postCodesOfRange.size() - 1));
                }
            }
            System.out.println();

            BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFile));
            for(String postCode : processedPostCodes){
                writer.write(postCode);
                writer.newLine();
            }
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
