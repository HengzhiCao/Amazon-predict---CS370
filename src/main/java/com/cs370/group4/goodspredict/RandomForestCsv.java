package com.cs370.group4.goodspredict;

import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Evaluation;

public class RandomForestCsv {

    public static void main(String[] args) throws Exception {
        // Paths to the training and testing dataset CSV files
        String testDataPath = "C:\\Users\\cao10\\Downloads\\CS370\\TrainData.csv"; // Replace with your training data path
        String trainingDataPath = "C:\\Users\\cao10\\Downloads\\CS370\\amazon_product_only_id.csv"; // Replace with your test data path

//        String testDataPath = "C:\\Users\\cao10\\Downloads\\CS370\\amazon_product_only_id.csv"; // Replace with your training data path
//        String trainingDataPath = "C:\\Users\\cao10\\Downloads\\CS370\\amazon_product_only_product_ID.csv"; // Replace with your test data path

        // Load training data
        DataSource trainSource = new DataSource(trainingDataPath);
        Instances trainDataset = trainSource.getDataSet();
        if (trainDataset.classIndex() == -1)
            trainDataset.setClassIndex(trainDataset.numAttributes() - 1);

        // Load testing data
        DataSource testSource = new DataSource(testDataPath);
        Instances testDataset = testSource.getDataSet();
        if (testDataset.classIndex() == -1)
            testDataset.setClassIndex(testDataset.numAttributes() - 1);

        // Create and build the classifier with training data
        RandomForest forest = new RandomForest();
        forest.buildClassifier(trainDataset);

        // Evaluate classifier with test data
        Evaluation eval = new Evaluation(trainDataset);
        eval.evaluateModel(forest, testDataset);

        // Print the evaluation summary
        System.out.println(eval.toSummaryString("\nResults\n======\n", true));
    }
}
