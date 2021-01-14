package com.distance.optimizer.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface LocationApi {

    /**
     * saveToDatabase location file
     *
     * @param inputFilePath
     * @param outputFilePath
     * @throws IOException
     */
    void generateLocationFile(String inputFilePath, String outputFilePath) throws IOException;

    /**
     * Read the sourcefile for location in each line and save it to database.
     * <p>The data will grow as the number of addresses increases.</p>
     *
     * @param sourceFile The full path to address file.
     *                   The Address file should contain location in each line
     *                   <b>Format : latitude,longitude</b>
     *                   1.0,0.1
     *                   1.9,2.1
     * @throws FileNotFoundException if source file not found.
     */
    void locPairFromLocFileToDb(String sourceFile) throws FileNotFoundException;

    void createLocPairsWithLocStrings(List<String> addresses);
}
