package com.cs370.group4.goodspredict;

import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Evaluation;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class RandomForestWriter {

    public static void main(String[] args) throws Exception {
        // 路径到训练和测试数据集的CSV文件
        String testDataPath = "C:\\Users\\cao10\\Downloads\\CS370\\Air Conditioners (1).csv"; // 替换为你的训练数据路径
        String trainingDataPath = "C:\\Users\\cao10\\Downloads\\CS370\\amazon_product.csv"; // 替换为你的测试数据路径
        String outputPath = "C:\\Users\\cao10\\Downloads\\CS370\\PredictionResults.csv"; // 替换为你的输出文件路径


        // 加载训练数据
        DataSource trainSource = new DataSource(trainingDataPath);
        Instances trainDataset = trainSource.getDataSet();
        if (trainDataset.classIndex() == -1)
            trainDataset.setClassIndex(trainDataset.numAttributes() - 1);

        // 加载测试数据
        DataSource testSource = new DataSource(testDataPath);
        Instances testDataset = testSource.getDataSet();
        if (testDataset.classIndex() == -1)
            testDataset.setClassIndex(testDataset.numAttributes() - 1);

        // 创建并用训练数据构建分类器
        RandomForest forest = new RandomForest();
        forest.buildClassifier(trainDataset);

        // 使用测试数据评估分类器
        Evaluation eval = new Evaluation(trainDataset);
        eval.evaluateModel(forest, testDataset);

        // 打印评估摘要
        System.out.println(eval.toSummaryString("\nResults\n======\n", true));

        // 预测结果并写入新文件
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
        writer.write("Actual, Predicted\n"); // 写入表头
        for (int i = 0; i < testDataset.numInstances(); i++) {
            double actual = testDataset.instance(i).classValue();
            double predicted = forest.classifyInstance(testDataset.instance(i));
            writer.write(actual + ", " + predicted + "\n");
        }
        writer.close();
    }
}
