package com.cs370.group4.goodspredict;

import weka.classifiers.trees.RandomForest;
import weka.core.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RandForest_with_Read_testCSV_line_one_by_one {

    public static void main(String[] args) {
        try {
            // 创建特征属性
            Attribute actualPrice = new Attribute("Actual_Price");
            Attribute discount = new Attribute("Discount");
            Attribute rating = new Attribute("Rating");
            Attribute ratingCount = new Attribute("Rating_Count");

            // 创建类别属性
            Attribute worthBuying = new Attribute("Worth_Buying");
            // 为类别属性添加可能的取值
            worthBuying.addStringValue("No");
            worthBuying.addStringValue("Yes");

            // 创建属性集
            FastVector attributes = new FastVector();
            attributes.addElement(actualPrice);
            attributes.addElement(discount);
            attributes.addElement(rating);
            attributes.addElement(ratingCount);
            attributes.addElement(worthBuying);

            // 创建训练集实例并添加到数据集
            Instances trainingData = new Instances("ProductWorthBuying", attributes, 0);

            DenseInstance instance1 = new DenseInstance(5);
            instance1.setValue(actualPrice, 50);
            instance1.setValue(discount, 10);
            instance1.setValue(rating, 4.5);
            instance1.setValue(ratingCount, 100);
            instance1.setValue(worthBuying, 1);
            trainingData.add(instance1);

            DenseInstance instance2 = new DenseInstance(5);
            instance2.setValue(actualPrice, 80);
            instance2.setValue(discount, 20);
            instance2.setValue(rating, 3.8);
            instance2.setValue(ratingCount, 50);
            instance2.setValue(worthBuying, 0);
            trainingData.add(instance2);

            DenseInstance instance3 = new DenseInstance(5);
            instance3.setValue(actualPrice, 30);
            instance3.setValue(discount, 5);
            instance3.setValue(rating, 4.2);
            instance3.setValue(ratingCount, 120);
            instance3.setValue(worthBuying, 1);
            trainingData.add(instance3);

            DenseInstance instance4 = new DenseInstance(5);
            instance4.setValue(actualPrice, 60);
            instance4.setValue(discount, 15);
            instance4.setValue(rating, 3.5);
            instance4.setValue(ratingCount, 80);
            instance4.setValue(worthBuying, 0);
            trainingData.add(instance4);

            // 设置类别属性
            trainingData.setClassIndex(trainingData.numAttributes() - 1);

            // 创建随机森林分类器
            RandomForest randomForest = new RandomForest();
            randomForest.buildClassifier(trainingData);

            // 读取待预测的商品信息
            String inputCSVPath = "C:\\Users\\cao10\\Downloads\\CS370\\amazon_product_only_id.csv";
            String outputCSVPath = "C:\\Users\\cao10\\Downloads\\CS370\\output.csv";
            String line;

            // 以追加模式打开CSV文件
            // 以追加模式打开CSV文件
            FileWriter writer = new FileWriter(outputCSVPath, true);

            try (BufferedReader br = new BufferedReader(new FileReader(inputCSVPath))) {
                // 跳过第一行（列名行）
                br.readLine();

                while ((line = br.readLine()) != null) {
                    // 解析CSV行
                    String[] values = line.split(",");

                    // 创建测试实例
                    DenseInstance testInstance = new DenseInstance(4);
                    testInstance.setValue(actualPrice, Double.parseDouble(values[0]));
                    testInstance.setValue(discount, Double.parseDouble(values[1]));
                    testInstance.setValue(rating, Double.parseDouble(values[2]));
                    testInstance.setValue(ratingCount, Double.parseDouble(values[3]));

                    // 设置类别属性
                    testInstance.setDataset(trainingData);

                    // 进行预测
                    double prediction = randomForest.classifyInstance(testInstance);

                    // 将预测结果追加到CSV文件
                    writer.write(line + "," + (int) prediction + "\n");
                }
            }

// 关闭文件写入器
            writer.close();


            // 关闭文件写入器
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
