package com.distance.optimizer.api.impl;

import com.distance.optimizer.api.LocationApi;
import com.distance.optimizer.model.entity.LocationString;
import com.distance.optimizer.service.FileService;
import com.distance.optimizer.service.LocationsService;
import com.distance.optimizer.service.PairGeneratorService;
import com.distance.optimizer.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class LocationApiImpl implements LocationApi {

    private FileService fileService;
    private LocationsService locationsService;
    private PairGeneratorService pairGeneratorService;

    @Autowired
    public LocationApiImpl(FileService fileService,
                           LocationsService locationsService,
                           PairGeneratorService pairGeneratorService) {
        ValidationUtil.requireNonNull(fileService, locationsService, pairGeneratorService);
        this.fileService = fileService;
        this.locationsService = locationsService;
        this.pairGeneratorService = pairGeneratorService;
    }

    @Override
    public void generateLocationFile(String inputFilePath, String outputFilePath) throws IOException {
        ValidationUtil.requireNonNull(inputFilePath, outputFilePath);
        fileService.generateLocationFile(inputFilePath, outputFilePath);
    }

    @Override
    public void locPairFromLocFileToDb(String sourceFile) throws FileNotFoundException {
        ValidationUtil.requireNonNull(sourceFile);
        List<String> coordinates = fileService.getValidCoordinates(sourceFile);
        locationsService.saveToDatabase(coordinates);
        pairGeneratorService.generate(coordinates);
    }

    @Override
    public void createLocPairsWithLocStrings(List<String> addresses) {
        Objects.requireNonNull(addresses);
        List<LocationString> locationStrings = locationsService.getAll();
        for (String destAddress : addresses) {
            for (LocationString locationString : locationStrings) {
                pairGeneratorService.save(locationString.getLoc(), destAddress);
            }
        }
    }
}
