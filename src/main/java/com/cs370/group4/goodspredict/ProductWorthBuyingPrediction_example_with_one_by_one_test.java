package com.cs370.group4.goodspredict;

import weka.classifiers.trees.RandomForest;
import weka.core.*;

import weka.classifiers.trees.RandomForest;
import weka.core.*;

public class ProductWorthBuyingPrediction_example_with_one_by_one_test {

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

            // 创建Instances对象
            Instances dataset = new Instances("ProductWorthBuying", attributes, 0);

            // 创建训练集实例并添加到数据集
            DenseInstance instance1 = new DenseInstance(5);
            instance1.setValue(actualPrice, 50);
            instance1.setValue(discount, 10);
            instance1.setValue(rating, 4.5);
            instance1.setValue(ratingCount, 100);
            instance1.setValue(worthBuying, 1);
            dataset.add(instance1);

            DenseInstance instance2 = new DenseInstance(5);
            instance2.setValue(actualPrice, 80);
            instance2.setValue(discount, 20);
            instance2.setValue(rating, 3.8);
            instance2.setValue(ratingCount, 50);
            instance2.setValue(worthBuying, 0);
            dataset.add(instance2);

            DenseInstance instance3 = new DenseInstance(5);
            instance3.setValue(actualPrice, 30);
            instance3.setValue(discount, 5);
            instance3.setValue(rating, 4.2);
            instance3.setValue(ratingCount, 120);
            instance3.setValue(worthBuying, 1);
            dataset.add(instance3);

            DenseInstance instance4 = new DenseInstance(5);
            instance4.setValue(actualPrice, 60);
            instance4.setValue(discount, 15);
            instance4.setValue(rating, 3.5);
            instance4.setValue(ratingCount, 80);
            instance4.setValue(worthBuying, 0);
            dataset.add(instance4);

            // 设置类别属性
            dataset.setClassIndex(dataset.numAttributes() - 1);

            // 创建随机森林分类器
            RandomForest randomForest = new RandomForest();
            randomForest.buildClassifier(dataset);

            // 创建测试集
            Instances testSet = new Instances("TestSet", attributes, 0);

            // 创建测试集实例并添加到测试集
            DenseInstance testInstance1 = new DenseInstance(4);
            testInstance1.setValue(actualPrice, 35);
            testInstance1.setValue(discount, 8);
            testInstance1.setValue(rating, 4.8);
            testInstance1.setValue(ratingCount, 150);
            testSet.add(testInstance1);

            DenseInstance testInstance2 = new DenseInstance(4);
            testInstance2.setValue(actualPrice, 70);
            testInstance2.setValue(discount, 18);
            testInstance2.setValue(rating, 3.2);
            testInstance2.setValue(ratingCount, 60);
            testSet.add(testInstance2);

            // 设置类别属性
            testSet.setClassIndex(testSet.numAttributes() - 1);

            // 进行测试集预测
            for (int i = 0; i < testSet.numInstances(); i++) {
                double prediction = randomForest.classifyInstance(testSet.instance(i));
                System.out.println("Predicted Worth Buying for Test Instance " + (i + 1) + ": " + (int) prediction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
