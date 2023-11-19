package com.cs370.group4.goodspredict;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

class DecisionTree {
    static class TreeNode {
        String attribute;
        double threshold;
        TreeNode left;
        TreeNode right;
        String label;
    }

    private int maxDepth;
    private Map<String, Integer> featureImportance = new HashMap<>();

    private TreeNode root;

    public DecisionTree(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    private double calculateEntropy(List<Instance> data) {
        Map<String, Integer> labelCounts = new HashMap<>();
        for (Instance instance : data) {
            labelCounts.merge(instance.label, 1, Integer::sum);
        }

        double entropy = 0.0;
        for (Integer count : labelCounts.values()) {
            double probability = count / (double) data.size();
            entropy -= probability * Math.log(probability) / Math.log(2);
        }
        return entropy;
    }

    private double calculateConditionalEntropy(List<Instance> data, int featureIndex) {
        Map<Double, List<Instance>> subsets = new HashMap<>();
        for (Instance instance : data) {
            subsets.computeIfAbsent(instance.features[featureIndex], k -> new ArrayList<>()).add(instance);
        }

        double conditionalEntropy = 0.0;
        for (List<Instance> subset : subsets.values()) {
            double subsetProbability = subset.size() / (double) data.size();
            conditionalEntropy += subsetProbability * calculateEntropy(subset);
        }
        return conditionalEntropy;
    }




    public TreeNode train(List<Instance> data, int currentDepth) {
        if (currentDepth == maxDepth || data.isEmpty() || isHomogeneous(data)) {
            TreeNode leaf = new TreeNode();
            leaf.label = getMajorityLabel(data);
            return leaf;
        }

        Random random = new Random();
        root = new TreeNode();
        root.label = data.get(random.nextInt(data.size())).label;

        // 在进行特征分裂时更新特征重要性
        String splitFeature = data.get(random.nextInt(data.size())).splitFeature;
        featureImportance.merge(splitFeature, 1, Integer::sum);

        return root;
    }

    public Map<String, Integer> getFeatureImportance() {
        return featureImportance;
    }

    public String predict(Instance instance) {
        TreeNode currentNode = root;

        while (currentNode.left != null && currentNode.right != null) {
            double featureValue = instance.features[getIndexForFeature(currentNode.attribute)];

            if (featureValue <= currentNode.threshold) {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
        }

        // 返回叶子节点的标签作为预测结果
        return currentNode.label;
    }

    private boolean isHomogeneous(List<Instance> data) {
        Set<String> uniqueLabels = new HashSet<>();
        for (Instance instance : data) {
            uniqueLabels.add(instance.label);
            if (uniqueLabels.size() > 1) {
                return false; // 数据集包含不同的标签，不是同质的
            }
        }
        return true; // 数据集是同质的
    }


    private String getMajorityLabel(List<Instance> data) {
        // 获取标签集合
        List<String> labels = data.stream().map(instance -> instance.label).collect(Collectors.toList());

        // 对标签进行排序
        Collections.sort(labels);

        // 找到中位数
        int middleIndex = labels.size() / 2;
        String majorityLabel = labels.get(middleIndex);

        return majorityLabel;
    }


    private int getIndexForFeature(String feature) {
        Map<String, Integer> featureIndexMap = new HashMap<>();
        featureIndexMap.put("discounted_price", 1);
        featureIndexMap.put("actual_price", 2);
        featureIndexMap.put("discount_percentage", 3);
        featureIndexMap.put("rating", 4);
        featureIndexMap.put("rating_count", 5);
        // 其他特征的映射...

        return featureIndexMap.getOrDefault(feature, -1); // 如果找不到特征，返回-1
    }

}

class Instance {
    double[] features;
    String label;
    String splitFeature;

    public Instance(double[] features, String label, String splitFeature) {
        this.features = features;
        this.label = label;
        this.splitFeature = splitFeature;
    }
}

public class RandomForest {
    private List<DecisionTree> trees;
    private Map<String, Integer> globalFeatureImportance = new HashMap<>();
    private int maxDepth;

    public RandomForest(int numTrees, int maxDepth) throws FileNotFoundException {
        trees = new ArrayList<>();
        this.maxDepth = maxDepth;
        for (int i = 0; i < numTrees; i++) {
            trees.add(new DecisionTree(maxDepth));
        }
    }

    public void train(List<Instance> data) {
        for (DecisionTree tree : trees) {
            tree.train(data, 0);
            // 获取每个决策树的特征重要性
            Map<String, Integer> treeImportance = tree.getFeatureImportance();
            // 合并到全局特征重要性中
            mergeImportance(treeImportance);
        }
    }

    private void mergeImportance(Map<String, Integer> treeImportance) {
        // 合并每个决策树的特征重要性到全局中
        treeImportance.forEach((feature, count) ->
                globalFeatureImportance.merge(feature, count, Integer::sum));
    }

    public Map<String, Integer> getGlobalFeatureImportance() {
        return globalFeatureImportance;
    }

    public String predict(Instance instance) {
        int countYes = 0;
        int countNo = 0;

        for (DecisionTree tree : trees) {
            // DecisionTree 类有一个 predict 方法，它返回字符串标签
            String prediction = tree.predict(instance);

            if ("Yes".equals(prediction)) {
                countYes++;
            } else {
                countNo++;
            }
        }

        // 如果 "Yes" 的预测次数多于 "No"，则返回 "Yes"，否则返回 "No"
        return (countYes > countNo) ? "Yes" : "No";
    }

    public static void main(String[] args) throws IOException {
        RandomForest randomForest = new RandomForest(50,10);

        List<Instance> trainingData = new ArrayList<>();
        trainingData.add(new Instance(new double[]{20, 40, 0.50, 4.5, 100}, "Yes", "Discounted_Price")); // 折扣价格20, 实际价格40, 折扣率0.50
        trainingData.add(new Instance(new double[]{35, 50, 0.30, 3.8, 50}, "No", "Actual_Price"));        // 折扣价格35, 实际价格50, 折扣率0.30
        trainingData.add(new Instance(new double[]{45, 60, 0.25, 4.0, 90}, "Yes", "Discount_Percentage")); // 折扣价格45, 实际价格60, 折扣率0.25
        trainingData.add(new Instance(new double[]{30, 45, 0.33, 4.2, 70}, "No", "Rating"));               // 折扣价格30, 实际价格45, 折扣率0.33
        trainingData.add(new Instance(new double[]{55, 70, 0.21, 3.9, 60}, "Yes", "Rating_Count"));       // 折扣价格55, 实际价格70, 折扣率0.21
        trainingData.add(new Instance(new double[]{60, 80, 0.15, 4.1, 120}, "No", "Discounted_Price"));   // 折扣价格60, 实际价格80, 折扣率0.15
        trainingData.add(new Instance(new double[]{70, 90, 0.10, 4.0, 150}, "Yes", "Actual_Price"));      // 添加更多trainingData price more than 500 and rating less than 4.0
        trainingData.add(new Instance(new double[]{130, 170, 0.09, 3.8, 260}, "No", "Discounted_Price"));   // 折扣价格130, 实际价格170, 折扣率0.09
        trainingData.add(new Instance(new double[]{140, 180, 0.07, 3.9, 280}, "Yes", "Actual_Price"));      // 折扣价格140, 实际价格180, 折扣率0.07
        trainingData.add(new Instance(new double[]{150, 190, 0.05, 3.7, 300}, "No", "Discount_Percentage")); // 折扣价格150, 实际价格190, 折扣率0.05
        trainingData.add(new Instance(new double[]{160, 200, 0.03, 3.8, 320}, "Yes", "Rating_Count"));    // 折扣价格160, 实际价格200, 折扣率0.03
        trainingData.add(new Instance(new double[]{170, 210, 0.01, 3.9, 340}, "No", "Rating"));           // 折扣价格170, 实际价格210, 折扣率0.01
        // 添加更多trainingData Actual_Price more than 500 and rating less than 3.0
        trainingData.add(new Instance(new double[]{180, 220, 0.02, 2.8, 360}, "No", "Discounted_Price"));   // 折扣价格180, 实际价格220, 折扣率0.02
        trainingData.add(new Instance(new double[]{190, 230, 0.00, 2.9, 380}, "Yes", "Actual_Price"));      // 折扣价格190, 实际价格230, 折扣率0.00
        trainingData.add(new Instance(new double[]{200, 240, 0.00, 2.7, 400}, "No", "Discount_Percentage")); // 折扣价格200, 实际价格240, 折扣率0.00
        trainingData.add(new Instance(new double[]{210, 250, 0.00, 2.8, 420}, "Yes", "Rating_Count"));    // 折扣价格210, 实际价格250, 折扣率0.00
        trainingData.add(new Instance(new double[]{220, 260, 0.00, 2.9, 440}, "No", "Rating"));           // 折扣价格220, 实际价格260, 折扣率0.00
        // 添加更多trainingData Actual_Price more than 1000
        trainingData.add(new Instance(new double[]{200, 1200, 0.1667, 4.7, 200}, "Yes", "Discounted_Price")); // 折扣价格200, 实际价格1200, 折扣率0.1667
        trainingData.add(new Instance(new double[]{250, 1500, 0.1667, 4.9, 300}, "Yes", "Actual_Price"));     // 折扣价格250, 实际价格1500, 折扣率0.1667
        trainingData.add(new Instance(new double[]{300, 1800, 0.1667, 4.6, 250}, "No", "Discount_Percentage")); // 折扣价格300, 实际价格1800, 折扣率0.1667
        trainingData.add(new Instance(new double[]{350, 2000, 0.175, 4.8, 400}, "Yes", "Rating"));             // 折扣价格350, 实际价格2000, 折扣率0.175
        trainingData.add(new Instance(new double[]{450, 2250, 0.20, 4.4, 150}, "No", "Rating_Count"));         // 折扣价格450, 实际价格2250, 折扣率0.20
        // 添加更多trainingData Discounted_percentage between 0.5 and 0.7
        trainingData.add(new Instance(new double[]{400, 2500, 0.6, 4.5, 200}, "Yes", "Discounted_Price")); // 折扣价格400, 实际价格2500, 折扣率0.6
        trainingData.add(new Instance(new double[]{500, 3000, 0.65, 4.8, 300}, "Yes", "Actual_Price"));     // 折扣价格500, 实际价格3000, 折扣率0.65
        trainingData.add(new Instance(new double[]{600, 3500, 0.7, 4.6, 250}, "No", "Discount_Percentage")); // 折扣价格600, 实际价格3500, 折扣率0.7
        trainingData.add(new Instance(new double[]{700, 4000, 0.75, 4.7, 200}, "Yes", "Actual_Price"));     // 折扣价格700, 实际价格4000, 折扣率0.75
        trainingData.add(new Instance(new double[]{800, 4500, 0.8, 4.8, 300}, "Yes", "Actual_Price"));      // 折扣价格800, 实际价格4500, 折扣率0.8
        trainingData.add(new Instance(new double[]{900, 5000, 0.85, 4.6, 250}, "No", "Actual_Price"));         // 折扣价格900, 实际价格5000, 折扣率0.85
        // 添加更多trainingData about the spilt Feature about rating and rating_count
        trainingData.add(new Instance(new double[]{230, 270, 0.02, 3.7, 360}, "Yes", "Rating_Count"));    // 折扣价格230, 实际价格270, 折扣率0.02
        trainingData.add(new Instance(new double[]{240, 280, 0.01, 3.8, 380}, "No", "Rating"));           // 折扣价格240, 实际价格280, 折扣率0.01
        trainingData.add(new Instance(new double[]{250, 290, 0.00, 3.9, 400}, "Yes", "Rating_Count"));    // 折扣价格250, 实际价格290, 折扣率0.00
        trainingData.add(new Instance(new double[]{260, 300, 0.00, 3.7, 420}, "No", "Rating"));           // 折扣价格260, 实际价格300, 折扣率0.00
        trainingData.add(new Instance(new double[]{270, 310, 0.00, 3.8, 440}, "Yes", "Rating_Count"));    // 折扣价格270, 实际价格310, 折扣率0.00



        // 使用训练数据训练随机森林
        randomForest.train(trainingData);

        // 获取全局特征重要性
        Map<String, Integer> globalImportance = randomForest.getGlobalFeatureImportance();

        // 示例数据 - 测试集
//        List<Instance> testData = new ArrayList<>();
//        testData.add(new Instance(new double[]{40, 15, 4.0, 80}, "", ""));
//        testData.add(new Instance(new double[]{60, 10, 4.2, 120}, "", "Rating"));
//        testData.add(new Instance(new double[]{55, 12, 4.1, 95}, "", "Discount"));
//        testData.add(new Instance(new double[]{70, 18, 3.8, 55}, "", "Rating_Count"));
//        testData.add(new Instance(new double[]{45, 8, 4.3, 110}, "", "Actual_Price"));
        // 添加更多测试实例...

        // 进行预测并打印结果
//        System.out.println("\nPredictions:");
//        for (Instance testInstance : testData) {
//            String prediction = randomForest.predict(testInstance);
//            System.out.println("Instance Label: " + testInstance.label + ", Predicted Label: " + prediction);
//        }

        String csvFilePath = "C:\\Users\\cao10\\IdeaProjects\\untitled\\amazon_product.csv"; // 替换为你的CSV文件路径
        List<String[]> csvData = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
        String line;

        while ((line = br.readLine()) != null) {
            String[] values = line.split(","); // 假设CSV使用逗号分隔
            csvData.add(values); // 添加整行数据到列表
        }
        br.close();

        // 显示选项给用户
        System.out.println("Select an item to predict:");
        for (int i = 0; i < csvData.size(); i++) {
            System.out.println(i + ": " + csvData.get(i)[0]); // 假设每行的第一个元素是描述
        }

        // 用户输入选择
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        String[] selectedRow = csvData.get(choice);

        // 提取特征
        double[] features = new double[5];
        System.out.println("Extracted Features:");
        for (int i = 0; i < 5; i++) {
            try {
                features[i] = Double.parseDouble(selectedRow[1 + i]);
                System.out.println("Feature " + (i + 1) + ": " + features[i]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid data format for parsing to double: " + selectedRow[1 + i]);
                return; // 或者处理错误
            }
        }

        // 打印特征重要性
        System.out.println("Global Feature Importance:");
        globalImportance.forEach((feature, importance) ->
                System.out.println(feature + ": " + importance));
        System.out.println();


        Instance instance = new Instance(features, "", ""); // 创建实例

        String prediction = randomForest.predict(instance);
        System.out.println("Predicted Label: " + prediction);
    }

}
