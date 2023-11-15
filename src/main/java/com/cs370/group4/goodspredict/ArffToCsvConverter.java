package com.cs370.group4.goodspredict;

import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVSaver;

import java.io.File;

public class ArffToCsvConverter {
    public static void main(String[] args) throws Exception {
        // Specify the path to the ARFF file
        String arffFilePath = "C:\\Users\\cao10\\Downloads\\CS370\\amazon_product_only_Product_ID.arff";
        // Specify the path for the output CSV file
        String csvFilePath = "Product_ID.csv";

        // Load the ARFF file
        ArffLoader loader = new ArffLoader();
        loader.setSource(new File(arffFilePath));
        Instances data = loader.getDataSet();

        // Save as CSV
        CSVSaver saver = new CSVSaver();
        saver.setInstances(data);
        // Set the destination file (CSV)
        File outputFile = new File(csvFilePath);
        // Check if file doesn't exist, then create a new file
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        saver.setFile(outputFile);
        saver.writeBatch();
    }
}
