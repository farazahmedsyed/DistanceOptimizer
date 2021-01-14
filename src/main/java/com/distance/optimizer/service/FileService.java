package com.distance.optimizer.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface FileService {
    void generateLocationFile(String inputFile, String outputFile) throws IOException;

    List<String> getValidCoordinates(String sourceFile) throws FileNotFoundException;
}
