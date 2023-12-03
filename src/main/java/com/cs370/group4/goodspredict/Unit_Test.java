package com.cs370.group4.goodspredict;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Unit_Test {

    private DecisionTree decisionTree;
    private RandomForest randomForest;
    private List<Instance> trainingData;
    private List<Instance> testingData;

    @Before
    public void setUp() throws FileNotFoundException {
        decisionTree = new DecisionTree(3); // maxDepth = 3 for simplicity
        randomForest = new RandomForest(5, 3); // 5 trees, maxDepth = 3

        // Initialize your training data here...
        trainingData = createSampleTrainingData();
        testingData = createTestingData();
    }



    @Test
    public void testTreeTraining() {
        decisionTree.train(trainingData, 0);
        Assert.assertNotNull("Root node should not be null after training", decisionTree.getRoot());
    }


    @Test
    public void testCalculateGini() {
        List<Instance> data = createSampleTrainingData();
        double expectedGini = 0.5; // Replace with the actual expected Gini value
        Assert.assertEquals("Gini calculation should match expected value",
                expectedGini, decisionTree.calculateGini(data), 0.01);
    }

    @Test
    public void testBootstrapSample() {
        List<Instance> sample = randomForest.bootstrapSample(trainingData);
        Assert.assertNotNull("Bootstrap sample should not be null", sample);
        Assert.assertEquals("Bootstrap sample size should match training data size",
                trainingData.size(), sample.size());
    }


    @Test
    public void testDecisionTreeTrainingAndPrediction() {
        // Train the decision tree
        decisionTree.train(trainingData, 0);

        // Test the predictions
        for (Instance instance : testingData) {
            String predictedLabel = decisionTree.predict(instance, decisionTree.getRoot());
            System.out.println("Predicted: " + predictedLabel);
            Assert.assertNotNull("Predicted label should not be null", predictedLabel);
        }
    }



    private List<Instance> createTestingData() {
        List<Instance> data = new ArrayList<>();
        // Add more test data with actual labels
        data.add(new Instance(new double[]{199, 349, 0.43, 4, 43994}, "", ""));
        data.add(new Instance(new double[]{299, 449, 0.32, 3.5, 43994}, "", ""));
        data.add(new Instance(new double[]{199, 1899, 0.90, 3.9, 7928}, "", ""));
        data.add(new Instance(new double[]{329, 699, 0.53, 4.2, 94363}, "", ""));
        data.add(new Instance(new double[]{154, 399, 0.61, 4.2, 16905}, "", ""));


        return data;
    }

    private List<Instance> createSampleTrainingData() {
        List<Instance> data = new ArrayList<>();
        data.add(new Instance(new double[]{20, 40, 0.50, 4.5, 100}, "Yes", "Discounted_Price")); // 折扣价格20, 实际价格40, 折扣率0.50
        data.add(new Instance(new double[]{35, 50, 0.30, 3.8, 50}, "No", "Actual_Price"));        // 折扣价格35, 实际价格50, 折扣率0.30
        data.add(new Instance(new double[]{45, 60, 0.25, 4.0, 90}, "Yes", "Discount_Percentage")); // 折扣价格45, 实际价格60, 折扣率0.25
        data.add(new Instance(new double[]{30, 45, 0.33, 4.2, 70}, "No", "Rating"));               // 折扣价格30, 实际价格45, 折扣率0.33
        data.add(new Instance(new double[]{55, 70, 0.21, 3.9, 60}, "Yes", "Rating_Count"));       // 折扣价格55, 实际价格70, 折扣率0.21
        data.add(new Instance(new double[]{60, 80, 0.15, 4.1, 120}, "No", "Discounted_Price"));   // 折扣价格60, 实际价格80, 折扣率0.15
        data.add(new Instance(new double[]{70, 90, 0.10, 4.0, 150}, "Yes", "Actual_Price"));      // 添加更多trainingData price more than 500 and rating less than 4.0
        data.add(new Instance(new double[]{130, 170, 0.09, 3.8, 260}, "No", "Discounted_Price"));   // 折扣价格130, 实际价格170, 折扣率0.09
        data.add(new Instance(new double[]{140, 180, 0.07, 3.9, 280}, "Yes", "Actual_Price"));      // 折扣价格140, 实际价格180, 折扣率0.07
        data.add(new Instance(new double[]{150, 190, 0.05, 3.7, 300}, "No", "Discount_Percentage")); // 折扣价格150, 实际价格190, 折扣率0.05
        data.add(new Instance(new double[]{160, 200, 0.03, 3.8, 320}, "Yes", "Rating_Count"));    // 折扣价格160, 实际价格200, 折扣率0.03
        data.add(new Instance(new double[]{170, 210, 0.01, 3.9, 340}, "No", "Rating"));           // 折扣价格170, 实际价格210, 折扣率0.01
        // 添加更多trainingData Actual_Price more than 500 and rating less than 3.0
        data.add(new Instance(new double[]{180, 220, 0.02, 2.8, 360}, "No", "Discounted_Price"));   // 折扣价格180, 实际价格220, 折扣率0.02
        data.add(new Instance(new double[]{190, 230, 0.00, 2.9, 380}, "Yes", "Actual_Price"));      // 折扣价格190, 实际价格230, 折扣率0.00
        data.add(new Instance(new double[]{200, 240, 0.00, 2.7, 400}, "No", "Discount_Percentage")); // 折扣价格200, 实际价格240, 折扣率0.00
        data.add(new Instance(new double[]{210, 250, 0.00, 2.8, 420}, "Yes", "Rating_Count"));    // 折扣价格210, 实际价格250, 折扣率0.00
        data.add(new Instance(new double[]{220, 260, 0.00, 2.9, 440}, "No", "Rating"));           // 折扣价格220, 实际价格260, 折扣率0.00
        // 添加更多trainingData Actual_Price more than 1000
        data.add(new Instance(new double[]{200, 1200, 0.1667, 4.7, 200}, "Yes", "Discounted_Price")); // 折扣价格200, 实际价格1200, 折扣率0.1667
        data.add(new Instance(new double[]{250, 1500, 0.1667, 4.9, 300}, "Yes", "Actual_Price"));     // 折扣价格250, 实际价格1500, 折扣率0.1667
        data.add(new Instance(new double[]{300, 1800, 0.1667, 4.6, 250}, "No", "Discount_Percentage")); // 折扣价格300, 实际价格1800, 折扣率0.1667
        data.add(new Instance(new double[]{350, 2000, 0.175, 4.8, 400}, "Yes", "Rating"));             // 折扣价格350, 实际价格2000, 折扣率0.175
        data.add(new Instance(new double[]{450, 2250, 0.20, 4.4, 150}, "No", "Rating_Count"));         // 折扣价格450, 实际价格2250, 折扣率0.20
        // 添加更多data Discounted_percentage between 0.5 and 0.7
        data.add(new Instance(new double[]{400, 2500, 0.6, 4.5, 200}, "Yes", "Discounted_Price")); // 折扣价格400, 实际价格2500, 折扣率0.6
        data.add(new Instance(new double[]{500, 3000, 0.65, 4.8, 300}, "Yes", "Actual_Price"));     // 折扣价格500, 实际价格3000, 折扣率0.65
        data.add(new Instance(new double[]{600, 3500, 0.7, 4.6, 250}, "No", "Discount_Percentage")); // 折扣价格600, 实际价格3500, 折扣率0.7
        data.add(new Instance(new double[]{700, 4000, 0.75, 4.7, 200}, "Yes", "Actual_Price"));     // 折扣价格700, 实际价格4000, 折扣率0.75
        data.add(new Instance(new double[]{800, 4500, 0.8, 4.8, 300}, "Yes", "Actual_Price"));      // 折扣价格800, 实际价格4500, 折扣率0.8
        data.add(new Instance(new double[]{900, 5000, 0.85, 4.6, 250}, "No", "Actual_Price"));         // 折扣价格900, 实际价格5000, 折扣率0.85
        // 添加更多data about the spilt Feature about rating and rating_count
        data.add(new Instance(new double[]{230, 270, 0.02, 3.7, 360}, "Yes", "Rating_Count"));    // 折扣价格230, 实际价格270, 折扣率0.02
        data.add(new Instance(new double[]{240, 280, 0.01, 3.8, 380}, "No", "Rating"));           // 折扣价格240, 实际价格280, 折扣率0.01
        data.add(new Instance(new double[]{250, 290, 0.00, 3.9, 400}, "Yes", "Rating_Count"));    // 折扣价格250, 实际价格290, 折扣率0.00
        data.add(new Instance(new double[]{260, 300, 0.00, 3.7, 420}, "No", "Rating"));           // 折扣价格260, 实际价格300, 折扣率0.00
        data.add(new Instance(new double[]{270, 310, 0.00, 3.8, 440}, "Yes", "Rating_Count"));    // 折扣价格270, 实际价格310, 折扣率0.00

        return data;
    }


}
