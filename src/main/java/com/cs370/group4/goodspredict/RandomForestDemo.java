package com.cs370.group4.goodspredict;

import java.io.File;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import java.io.IOException;
import weka.classifiers.trees.RandomForest;
/**
 * @author Gowtham Girithar Srirangasamy
 *
 */
/**
 * @author Gowtham Girithar Srirangasamy
 *
 */
public class RandomForestDemo {

    /** file names are defined*/
    public static final String TRAINING_DATA_SET_FILENAME="C:\\Users\\cao10\\Downloads\\CS370\\TrainData.arff";
    public static final String TESTING_DATA_SET_FILENAME="C:\\Users\\cao10\\Downloads\\CS370\\TestData.arff";


    /**
     * This method is to load the data set.
     * @param fileName
     * @return
     * @throws IOException
     */
    public static Instances getDataSet(String fileName) throws IOException {
        int classIdx = 0;
        ArffLoader loader = new ArffLoader();
        loader.setFile(new File(fileName));  // 直接使用文件对象
        Instances dataSet = loader.getDataSet();
        dataSet.setClassIndex(classIdx);
        return dataSet;
    }


    /**
     * This method is used to process the input and return the statistics.
     *
     * @throws Exception
     */
    public static void process() throws Exception {

        Instances trainingDataSet = getDataSet(TRAINING_DATA_SET_FILENAME);
        Instances testingDataSet = getDataSet(TESTING_DATA_SET_FILENAME);



        RandomForest forest=new RandomForest();
        forest.setNumIterations(10);


        /** */
        forest.buildClassifier(trainingDataSet);
        /**
         * train the alogorithm with the training data and evaluate the
         * algorithm with testing data
         */

        // 在训练和测试数据集加载后，检查数据集的状态
        System.out.println("Training Dataset:\n" + trainingDataSet.toSummaryString());
        System.out.println("Testing Dataset:\n" + testingDataSet.toSummaryString());

        Evaluation eval = new Evaluation(trainingDataSet);
        eval.evaluateModel(forest, testingDataSet);


        /** Print the algorithm summary */
        System.out.println("** Decision Tress Evaluation with Datasets **");
        System.out.println(eval.toSummaryString());
        System.out.print(" the expression for the input data as per alogorithm is ");
        System.out.println(forest);
        System.out.println(eval.toMatrixString());
        System.out.println(eval.toClassDetailsString());

    }

    public static void main(String[] args) throws Exception {
        process();
    }
}
