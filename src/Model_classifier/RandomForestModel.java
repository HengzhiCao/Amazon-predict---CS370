package Model_classifier;

import Model_classifier.classifier.RandomForest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RandomForestModel {
    private RandomForest randomForest;

    public RandomForestModel() {
        try {
            randomForest = new RandomForest(100, 10);
            loadTrainingData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadTrainingData() {
        List<Instance> trainingData = new ArrayList<>();
        String csvFile = "Amazon_training_data (2).csv"; // Update with your actual path to the CSV file

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFile));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                double[] features = new double[]{
                        Double.parseDouble(csvRecord.get("Discounted_Price")),
                        Double.parseDouble(csvRecord.get("Actual_Price")),
                        Double.parseDouble(csvRecord.get("Discount_Percentage")),
                        Double.parseDouble(csvRecord.get("Rating")),
                        Double.parseDouble(csvRecord.get("Rating_Count"))
                };
                String label = csvRecord.get("Label");
                trainingData.add(new Instance(features, label));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!trainingData.isEmpty()) {
            randomForest.train(trainingData);
        } else {
            System.out.println("Training data is empty.");
        }
    }

//    private void loadTrainingData() {
//        String csvFile = "Amazon_training_data (2).csv"; // Update with your actual path to the CSV file
//
//        try (Reader reader = Files.newBufferedReader(Paths.get(csvFile));
//             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
//
//            List<Instance> trainingData = csvParser.getRecords().parallelStream().map(csvRecord -> {
//                double[] features = new double[]{
//                        Double.parseDouble(csvRecord.get("Discounted_Price")),
//                        Double.parseDouble(csvRecord.get("Actual_Price")),
//                        Double.parseDouble(csvRecord.get("Discount_Percentage")),
//                        Double.parseDouble(csvRecord.get("Rating")),
//                        Double.parseDouble(csvRecord.get("Rating_Count"))
//                };
//                String label = csvRecord.get("Label");
//                return new Instance(features, label);
//            }).collect(Collectors.toList());
//
//            if (!trainingData.isEmpty()) {
//                randomForest.train(trainingData);
//            } else {
//                System.out.println("Training data is empty.");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public RandomForest getRandomForest() {
        return randomForest;
    }
}
