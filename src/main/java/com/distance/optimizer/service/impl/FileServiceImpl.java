package com.distance.optimizer.service.impl;

import com.distance.optimizer.dto.DisOptimizerDto;
import com.distance.optimizer.dto.request.google.GeoCodeRequestDto;
import com.distance.optimizer.service.FileService;
import com.distance.optimizer.service.GoogleService;
import com.distance.optimizer.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

@Component
public class FileServiceImpl implements FileService {

    private List<String> googleApiKeys;
    private GoogleService googleService;

    @Autowired
    public FileServiceImpl(GoogleService googleService,
                           DisOptimizerDto disOptimizerDto) {
        Objects.requireNonNull(disOptimizerDto);
        Objects.requireNonNull(googleService);
        Objects.requireNonNull(disOptimizerDto.getGoogleApiKeys());
        this.googleApiKeys = disOptimizerDto.getGoogleApiKeys();
        this.googleService = googleService;
    }

    @Override
    public void generateLocationFile(String inputFile, String outputFile) throws IOException {
        List<String> addresses = new ArrayList<>();
        Scanner reader = new Scanner(new FileReader(inputFile));
        while (reader.hasNextLine()) {
            String loc = reader.nextLine();
            addresses.add(loc);
        }
        reader.close();

        Integer size = googleApiKeys.size();
        Integer index = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (String address : addresses) {
            index++;
            if (index >= size) {
                index = 0;
            }
            GeoCodeRequestDto geoCodeRequestDto = new GeoCodeRequestDto(address, googleApiKeys.get(index));
            stringBuilder.append(googleService.getLocationFromGeoCodeResponse(geoCodeRequestDto));
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write(stringBuilder.toString());
        writer.close();
    }

    /**
     * @return List of valid cooordinates.
     * @throws FileNotFoundException if file not found.
     */
    @Override
    public List<String> getValidCoordinates(String sourceFile) throws FileNotFoundException {
        List<String> coordinates = new ArrayList<>();
        Scanner reader = new Scanner(new FileReader(sourceFile));
        while (reader.hasNextLine()) {
            String loc = reader.nextLine();
            if (ValidationUtil.isValidLatLngString(loc)) {
                coordinates.add(loc);
            }
        }
        reader.close();
        return coordinates;
    }
}
