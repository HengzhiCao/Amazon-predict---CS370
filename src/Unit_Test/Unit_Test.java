package Unit_Test;

import Controller.DataPreprocessingController;
import Controller.InputValidationController;
import Controller.PredictionController;
import Controller.ProductController;
import Model_classifier.*;
import View.MainView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import Model_classifier.classifier.RandomForest;
import Model_classifier.classifier.DecisionTree;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class Unit_Test {

    private DecisionTree decisionTree;
    private RandomForest randomForest;
    private List<Instance> trainingData;
    private List<Instance> testingData;
    private DataPreprocessingController controller;
    private ProductModel productModel;
    private PredictionController predictionController;
    private MainView mainView;
    private ProductController productControllerMock;




    @Before
    public void setUp() throws FileNotFoundException {
        decisionTree = new DecisionTree(3); // maxDepth = 3 for simplicity
        randomForest = new RandomForest(5, 3); // 5 trees, maxDepth = 3
        controller = new DataPreprocessingController();



        // Initialize your training data here...
        trainingData = createSampleTrainingData();
        testingData = createTestingData();


    }

    @Test
    public void testValidatePrice() {
        InputValidationController controller = new InputValidationController();

        assertTrue(controller.validatePrice(10.0)); // 有效价格
        assertFalse(controller.validatePrice(-1.0)); // 无效价格
    }

    @Test
    public void testValidateProductName() {
        InputValidationController controller = new InputValidationController();

        assertTrue(controller.validateProductName("Product Name")); // 有效名称
        assertFalse(controller.validateProductName("")); // 无效名称
        assertFalse(controller.validateProductName(null)); // null 名称
    }
    @Test
    public void testPreprocessProducts() {
        // 创建包含不同初始状态的 Product 实例的列表
        List<Product> products = Arrays.asList(
                new Product("1", null, -1.0, 100.0, 50.0, -1, -1, null, null, null), // 缺失名称、描述、图片 URL 和类别，无效价格和评分
                new Product("2", "Valid Product", 20.0, 100.0, 20.0, 4.0, 20, "validUrl", "description", "category"), // 有效的产品
                new Product("3", "Unknown", 0.0, 100.0, 30.0, 3.5, 30, "defaultImageUrl", "No description available", "Unknown") // 名称、描述、图片 URL 和类别为空，价格为0
        );

        // 使用 DataPreprocessingController 处理这些产品
        DataPreprocessingController controller = new DataPreprocessingController();
        List<Product> preprocessed = controller.preprocessProducts(products);

        // 断言预处理后的结果
        assertEquals("Unknown", preprocessed.get(0).getName());
        assertEquals(50.0, preprocessed.get(0).getPrice(), 0.01); // 根据折扣计算的价格
        assertEquals("No description available", preprocessed.get(0).getDescription());
        assertEquals("defaultImageUrl", preprocessed.get(0).getImageUrl());
        assertEquals("Unknown", preprocessed.get(0).getCategory());
        assertEquals("1", preprocessed.get(0).getId());
        assertEquals(0, preprocessed.get(0).getRating(), 0.01); // 设置评分为0
        assertEquals(0, preprocessed.get(0).getRatingCount()); // 设置评分数量为0

        assertEquals("Valid Product", preprocessed.get(1).getName());
        assertEquals(20.0, preprocessed.get(1).getPrice(), 0.01); // 根据折扣计算的价格
        assertEquals("description", preprocessed.get(1).getDescription());
        assertEquals("validUrl", preprocessed.get(1).getImageUrl());
        assertEquals("category", preprocessed.get(1).getCategory());
        assertEquals("2", preprocessed.get(1).getId());

        assertEquals("Unknown", preprocessed.get(2).getName());
        assertEquals(70.0, preprocessed.get(2).getPrice(), 0.01); // 根据折扣计算的价格
        assertEquals("No description available", preprocessed.get(2).getDescription());
        assertEquals("defaultImageUrl", preprocessed.get(2).getImageUrl());
        assertEquals("Unknown", preprocessed.get(2).getCategory());
        assertEquals("3", preprocessed.get(2).getId());

    }


    @Test
    public void testCalculateMissingDiscountedPrice() {
        // Input
        Product product = new Product("", "", -1.0, 100.0, 20.0, 0.0, 0, "", "", "");

        // Action
        controller.calculateMissingIncorrectValue(product);

        // Expected Output
        assertEquals("Discounted price should be calculated based on actual price and discount percentage",
                80.0,
                product.getDiscountedPrice(),
                0.01);
    }

    @Test
    public void testCalculateMissingRatingScore() {
        // Input
        Product product = new Product("", "", 0.0, 0.0, 0.0, -1.0, 0, "", "", "");

        // Action
        controller.calculateMissingIncorrectValue(product);

        // Expected Output
        assertEquals("Missing rating score should be given a 0",
                0.0,
                product.getRating(),
                0.01);
    }

    @Test
    public void testCalculateMissingRatingCount() {
        // Input
        Product product = new Product("", "", 0.0, 0.0, 0.0, 0.0, -1, "", "", "");

        // Action
        controller.calculateMissingIncorrectValue(product);

        // Expected Output
        assertEquals("Missing rating count should be considered as null (0 for int representation)",
                0,
                product.getRatingCount());
    }





    @Test
    public void testTreeTraining() {
        decisionTree.train(trainingData, 0);
        Assert.assertNotNull("Root node should not be null after training", decisionTree.getRoot());
    }


    @Test
    public void testCalculateGini() {
        List<Instance> data = createSampleTrainingData();
        double expectedGini = 0.5;
        assertEquals("Gini calculation should match expected value",
                expectedGini, decisionTree.calculateGini(data), 0.01);
    }

    @Test
    public void testBootstrapSample() {
        List<Instance> sample = randomForest.bootstrapSample(trainingData);
        Assert.assertNotNull("Bootstrap sample should not be null", sample);
        assertEquals("Bootstrap sample size should match training data size",
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
        data.add(new Instance(new double[]{199, 349, 0.43, 4, 43994}, ""));
        data.add(new Instance(new double[]{299, 449, 0.32, 3.5, 43994}, ""));
        data.add(new Instance(new double[]{199, 1899, 0.90, 3.9, 7928}, ""));
        data.add(new Instance(new double[]{329, 699, 0.53, 4.2, 94363}, ""));
        data.add(new Instance(new double[]{154, 399, 0.61, 4.2, 16905}, ""));


        return data;
    }

    private List<Instance> createSampleTrainingData() {
        List<Instance> data = new ArrayList<>();


        data.add(new Instance(new double[]{20, 40, 0.50, 4.5, 100}, "Yes"));
        data.add(new Instance(new double[]{35, 50, 0.30, 3.8, 50}, "No"));
        data.add(new Instance(new double[]{45, 60, 0.25, 4.0, 90}, "Yes"));
        data.add(new Instance(new double[]{30, 45, 0.33, 4.2, 70}, "No"));
        data.add(new Instance(new double[]{55, 70, 0.21, 3.9, 60}, "Yes"));
        data.add(new Instance(new double[]{60, 80, 0.15, 4.1, 120}, "No"));
        data.add(new Instance(new double[]{70, 90, 0.10, 4.0, 150}, "Yes"));
        data.add(new Instance(new double[]{130, 170, 0.09, 3.8, 260}, "No"));
        data.add(new Instance(new double[]{140, 180, 0.07, 3.9, 280}, "Yes"));
        data.add(new Instance(new double[]{150, 190, 0.05, 3.7, 300}, "No"));
        data.add(new Instance(new double[]{160, 200, 0.03, 3.8, 320}, "Yes"));
        data.add(new Instance(new double[]{170, 210, 0.01, 3.9, 340}, "No"));
        data.add(new Instance(new double[]{180, 220, 0.02, 2.8, 360}, "No"));
        data.add(new Instance(new double[]{190, 230, 0.00, 2.9, 380}, "Yes"));
        data.add(new Instance(new double[]{200, 240, 0.00, 2.7, 400}, "No"));
        data.add(new Instance(new double[]{210, 250, 0.00, 2.8, 420}, "Yes"));
        data.add(new Instance(new double[]{220, 260, 0.00, 2.9, 440}, "No"));
        data.add(new Instance(new double[]{200, 1200, 0.1667, 4.7, 200}, "Yes"));
        data.add(new Instance(new double[]{250, 1500, 0.1667, 4.9, 300}, "Yes"));
        data.add(new Instance(new double[]{300, 1800, 0.1667, 4.6, 250}, "No"));
        data.add(new Instance(new double[]{350, 2000, 0.175, 4.8, 400}, "Yes"));
        data.add(new Instance(new double[]{450, 2250, 0.20, 4.4, 150}, "No"));
        data.add(new Instance(new double[]{400, 2500, 0.6, 4.5, 200}, "Yes"));
        data.add(new Instance(new double[]{500, 3000, 0.65, 4.8, 300}, "Yes"));
        data.add(new Instance(new double[]{600, 3500, 0.7, 4.6, 250}, "No"));
        data.add(new Instance(new double[]{700, 4000, 0.75, 4.7, 200}, "Yes"));
        data.add(new Instance(new double[]{800, 4500, 0.8, 4.8, 300}, "Yes"));
        data.add(new Instance(new double[]{900, 5000, 0.85, 4.6, 250}, "No"));
        data.add(new Instance(new double[]{230, 270, 0.02, 3.7, 360}, "Yes"));
        data.add(new Instance(new double[]{240, 280, 0.01, 3.8, 380}, "No"));
        data.add(new Instance(new double[]{250, 290, 0.00, 3.9, 400}, "Yes"));
        data.add(new Instance(new double[]{260, 300, 0.00, 3.7, 420}, "No"));
        data.add(new Instance(new double[]{270, 310, 0.00, 3.8, 440}, "Yes"));



        return data;
    }


}
