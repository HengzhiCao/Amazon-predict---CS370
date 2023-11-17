package com.cs370.group4.goodspredict;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class DecisionTree {
    static class TreeNode {
        int featureIndex;
        double threshold;
        TreeNode left;
        TreeNode right;
        String label;
    }

    private int maxDepth;

    public DecisionTree(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public TreeNode train(List<Instance> data, int currentDepth) {
        if (currentDepth == maxDepth || data.isEmpty() || isHomogeneous(data)) {
            TreeNode leaf = new TreeNode();
            leaf.label = getMajorityLabel(data);
            return leaf;
        }

        int bestFeature = new Random().nextInt(data.get(0).features.length);
        double bestThreshold = data.get(new Random().nextInt(data.size())).features[bestFeature];

        List<Instance> leftSplit = new ArrayList<>();
        List<Instance> rightSplit = new ArrayList<>();
        for (Instance instance : data) {
            if (instance.features[bestFeature] < bestThreshold) {
                leftSplit.add(instance);
            } else {
                rightSplit.add(instance);
            }
        }

        TreeNode node = new TreeNode();
        node.featureIndex = bestFeature;
        node.threshold = bestThreshold;
        node.left = train(leftSplit, currentDepth + 1);
        node.right = train(rightSplit, currentDepth + 1);

        return node;
    }

    public String predict(TreeNode tree, Instance instance) {
        if (tree.left == null && tree.right == null) {
            return tree.label;
        }
        if (instance.features[tree.featureIndex] < tree.threshold) {
            return predict(tree.left, instance);
        } else {
            return predict(tree.right, instance);
        }
    }

    private boolean isHomogeneous(List<Instance> data) {
        String firstLabel = data.get(0).label;
        for (Instance instance : data) {
            if (!instance.label.equals(firstLabel)) {
                return false;
            }
        }
        return true;
    }

    private String getMajorityLabel(List<Instance> data) {
        int yesCount = 0;
        int noCount = 0;
        for (Instance instance : data) {
            if ("Yes".equals(instance.label)) {
                yesCount++;
            } else {
                noCount++;
            }
        }
        return yesCount > noCount ? "Yes" : "No";
    }
}

class Instance {
    double[] features;
    String label;

    public Instance(double[] features, String label) {
        this.features = features;
        this.label = label;
    }
}

public class RandomForest {
    private List<DecisionTree> trees;
    private int maxDepth;

    public RandomForest(int numTrees, int maxDepth) {
        trees = new ArrayList<>();
        this.maxDepth = maxDepth;
        for (int i = 0; i < numTrees; i++) {
            trees.add(new DecisionTree(maxDepth));
        }
    }

    public void train(List<Instance> data) {
        for (DecisionTree tree : trees) {
            List<Instance> bootstrapSample = bootstrapSample(data);
            tree.train(bootstrapSample, 0);
        }
    }

    public String predict(Instance instance) {
        int yesCount = 0;
        int noCount = 0;
        for (DecisionTree tree : trees) {
            String prediction = tree.predict(tree.train(new ArrayList<>(), 0), instance);
            if ("Yes".equals(prediction)) {
                yesCount++;
            } else {
                noCount++;
            }
        }
        return yesCount > noCount ? "Yes" : "No";
    }

    private List<Instance> bootstrapSample(List<Instance> data) {
        List<Instance> sample = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < data.size(); i++) {
            sample.add(data.get(random.nextInt(data.size())));
        }
        return sample;
    }

    public static void main(String[] args) {
        RandomForest randomForest = new RandomForest(10, 5); // 增加树的数量和深度

        // 扩展的训练数据集，包含五个特征
        List<Instance> trainingData = new ArrayList<>();
        // 添加 "Yes" 实例
        trainingData.add(new Instance(new double[]{100, 20, 80, 4.5, 200}, "Yes")); // 假设的特征值
        trainingData.add(new Instance(new double[]{120, 15, 102, 4.6, 180}, "Yes"));
        trainingData.add(new Instance(new double[]{130, 10, 117, 4.4, 250}, "Yes"));
        trainingData.add(new Instance(new double[]{90, 25, 67.5, 4.3, 300}, "Yes"));
        trainingData.add(new Instance(new double[]{110, 18, 90.2, 4.7, 150}, "Yes"));
        // 添加 "No" 实例
        trainingData.add(new Instance(new double[]{200, 5, 190, 3.8, 50}, "No"));
        trainingData.add(new Instance(new double[]{180, 8, 165.6, 3.7, 60}, "No"));
        trainingData.add(new Instance(new double[]{170, 12, 149.6, 3.9, 40}, "No"));

        // 训练随机森林模型
        randomForest.train(trainingData);

        // 扩展的测试数据集
        List<Instance> testData = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            // 随机生成测试数据，这里也可以根据实际情况调整
            double actualPrice = 80 + random.nextDouble() * 140; // 假设的特征值范围
            double discountPercent = 5 + random.nextDouble() * 25;
            double discountPrice = actualPrice * (1 - discountPercent / 100);
            double rating = 3 + random.nextDouble() * 2;
            double ratingCount = 50 + random.nextDouble() * 300;
            testData.add(new Instance(new double[]{actualPrice, discountPercent, discountPrice, rating, ratingCount}, ""));
        }

        // 对测试数据集进行预测
        for (Instance instance : testData) {
            String prediction = randomForest.predict(instance);
            System.out.print("Features: [");
            for (double feature : instance.features) {
                System.out.printf("%.2f ", feature);
            }
            System.out.println("] - Predicted: " + prediction);
        }
    }

}

