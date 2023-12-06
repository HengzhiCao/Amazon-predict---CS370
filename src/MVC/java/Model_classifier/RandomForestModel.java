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

    /**
     * Load training data from a CSV file and train the random forest model.
     */
    private void loadTrainingData() {
        // Create a list to store the training data instances
        List<Instance> trainingData = new ArrayList<>();

        // Specify the path to the CSV file
        String csvFile = "Amazon_training_data.csv"; // Update with  you actual path to the CSV file(if you need to change the path)

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Skip header row

            // Read each line of the CSV file
            while ((line = br.readLine()) != null) {
                // Split the line into values assuming it is comma-separated
                String[] values = line.split(",");

                // Convert the feature values to doubles
                double[] features = new double[]{
                        Double.parseDouble(values[0]),
                        Double.parseDouble(values[1]),
                        Double.parseDouble(values[2]),
                        Double.parseDouble(values[3]),
                        Double.parseDouble(values[4])
                };

                // Extract the label and split feature values
                String label = values[5];
                String splitFeature = values[6];

                // Create a new instance and add it to the training data list
                trainingData.add(new Instance(features, label, splitFeature));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Train the random forest model with the training data
        if (!trainingData.isEmpty()) {
            randomForest.train(trainingData);
        } else {
            System.out.println("Training data is empty.");
        }
    }

    public RandomForest getRandomForest() {
        return randomForest;
    }
}
