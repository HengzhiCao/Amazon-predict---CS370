package Model_classifier;

import Model_classifier.classifier.RandomForest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
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
        String csvFile = "Amazon_training_data.csv"; // Update with the actual path to the CSV file

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Assuming that the CSV file is comma-separated
                double[] features = new double[]{
                        Double.parseDouble(values[0]),
                        Double.parseDouble(values[1]),
                        Double.parseDouble(values[2]),
                        Double.parseDouble(values[3]),
                        Double.parseDouble(values[4])
                };
                String label = values[5];
                String splitFeature = values[6];

                trainingData.add(new Instance(features, label, splitFeature));
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

    public String predict(Instance instance) {
        return randomForest.predict(instance);
    }

    public RandomForest getRandomForest() {
        return randomForest;
    }
}
