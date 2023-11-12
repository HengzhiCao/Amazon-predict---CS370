package com.cs370.group4.goodspredict;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;

public class CSVToArff {
    public static void main(String[] args) throws Exception {
        // 创建 CSVLoader 实例
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File("path_to_your_csv_file.csv")); // 替换为您的 CSV 文件路径
        Instances data = loader.getDataSet();

        // 创建 ArffSaver 实例
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File("output_file.arff")); // 输出文件的路径
        saver.writeBatch();

        System.out.println("CSV to ARFF conversion completed successfully!");
    }
}
