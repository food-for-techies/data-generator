package com.nagpal.datagenerator.controller;

import au.com.anthonybruno.Gen;
import au.com.anthonybruno.definition.ResultDefinition;
import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@Log4j2
public class DataGenerator {

    @GetMapping("/data-generator/generate")
    public ResponseEntity<String> generateData(@RequestParam(defaultValue = "names") String type,
                                               @RequestParam(defaultValue = "10") int count,
                                               @RequestParam String fileName) {

        log.info("Generating data of type: {} and count: {}", type, count);
        String destinationFolder = System.getProperty("user.home");

        switch (type) {
            case "names":
                generateNamesData(count, destinationFolder, fileName);
                break;
            case "city_temp":
                generateCityTemperatureData(count, destinationFolder, fileName);
                break;
            default:
                throw new IllegalArgumentException("Unknown option");
        }

        log.info("Data generated and saved to file");
        return ResponseEntity.ok("Generated "+ count + " row of type: " + type +" and saved data to " +
                destinationFolder + File.separator + fileName);
    }

    private void generateCityTemperatureData(int count, String destination, String fileName) {
        Faker faker = new Faker();

        ResultDefinition resultDefinition = Gen.start()
                .addField("City", ()-> faker.address().city())
                .addField("Temperature", () -> faker.number().randomDouble(1, -99, 99))
                .generate(count)
                .asCsv();

        saveToFile(destination, fileName, resultDefinition);
    }

    private void generateNamesData(int count, String destination, String fileName) {

        Faker faker = new Faker();

        ResultDefinition resultDefinition = Gen.start()
                .addField("First Name", () -> faker.name().firstName())
                .addField("Last Name", () -> faker.name().lastName())
                .addField("Full Name", () -> faker.name().fullName())
                .addField("Address", () -> faker.address().fullAddress())
                .generate(count)
                .asCsv();

        saveToFile(destination, fileName, resultDefinition);
    }

    private void saveToFile(String destination, String fileName, ResultDefinition resultDefinition) {
        log.info("Saving data to file: {}", fileName);
        resultDefinition.toFile(destination + File.separator + fileName);
    }
}
