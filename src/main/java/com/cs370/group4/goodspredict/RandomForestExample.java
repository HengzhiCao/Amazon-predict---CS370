package com.cs370.group4.goodspredict;

import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Evaluation;
import java.io.IOException;

public class RandomForestExample {

    public static final String TRAINING_DATA_SET_FILENAME = "C:\\Users\\cao10\\Downloads\\CS370\\TrainData.arff";
    public static final String TESTING_DATA_SET_FILENAME = "C:\\Users\\cao10\\Downloads\\CS370\\TestData.arff";

    public static Instances getDataSet(String fileName) throws Exception {
        DataSource source = new DataSource(fileName);
        Instances dataSet = source.getDataSet();
        if (dataSet.classIndex() == -1) {
            dataSet.setClassIndex(dataSet.numAttributes() - 1);
        }
        return dataSet;
    }

    public static void process() throws Exception {
        Instances trainingDataSet = getDataSet(TRAINING_DATA_SET_FILENAME);
        Instances testingDataSet = getDataSet(TESTING_DATA_SET_FILENAME);

        // 打印训练数据集和测试数据集的结构
        System.out.println("训练数据集结构：\n" + trainingDataSet);
        System.out.println("测试数据集结构：\n" + testingDataSet);

        RandomForest rf = new RandomForest();
        rf.setNumIterations(10); // 设置随机森林的迭代次数

        rf.buildClassifier(trainingDataSet);

        Evaluation eval = new Evaluation(trainingDataSet);
        eval.evaluateModel(rf, testingDataSet);

        System.out.println("** 随机森林模型评估 **");
        System.out.println(eval.toSummaryString());
        System.out.println("混淆矩阵：\n" + eval.toMatrixString());
        System.out.println("详细的分类结果：\n" + eval.toClassDetailsString());
    }

    public static void main(String[] args) throws Exception {
        process();
    }
}
