package com.nagpal.datagenerator.controller;

import au.com.anthonybruno.Gen;
import au.com.anthonybruno.definition.ResultDefinition;
import com.github.javafaker.Faker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class DataGenerator {

    @GetMapping("/data-generator/generate")
    public ResponseEntity<String> generateData(@RequestParam(defaultValue = "names") String type,
                                               @RequestParam(defaultValue = "10") int count,
                                               @RequestParam(defaultValue = "csv") String format) {



        switch (type) {
            case "names":
                generateNamesData(count, format);
                break;
            case "city_temp":
                generateCityTempratureData(count, format);
                break;
            default:
                throw new IllegalArgumentException("Unknown option");
        }

        return ResponseEntity.ok("Data generated");
    }

    private void generateCityTempratureData(int count, String format) {
        Faker faker = new Faker();

        ResultDefinition resultDefinition = Gen.start()
                .addField("City", ()-> faker.address().city())
                .addField("Temprature", () -> faker.number().randomDouble(2, -99, 99))
                .generate(10)
                .asCsv();

        System.out.println(resultDefinition.toStringForm());
    }

    private void generateNamesData(int count, String format) {

        Faker faker = new Faker();

        ResultDefinition resultDefinition = Gen.start()
                .addField("First Name", () -> faker.name().firstName())
                .addField("Last Name", () -> faker.name().lastName())
                .addField("Full Name", () -> faker.name().fullName())
                .addField("Address", () -> faker.address().fullAddress())
                .generate(count)
                .asCsv();

        resultDefinition.toFile("D:\\tmp\\fake-names.csv");
    }
}
