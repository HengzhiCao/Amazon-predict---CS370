package org.example;

import java.io.BufferedReader;
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

        public TreeNode(String attribute, double threshold, TreeNode left, TreeNode right, String label) {
            this.attribute = attribute;
            this.threshold = threshold;
            this.left = left;
            this.right = right;
            this.label = label;
        }
    }

    private int maxDepth;
    private Map<String, Integer> featureImportance = new HashMap<>();
    private TreeNode root;
    private static final Random random = new Random();
    private String[] featureNames = new String[]{"Discounted_Price", "Actual_Price", "Discount_Percentage", "Rating", "Rating_Count"};

    public DecisionTree(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public TreeNode train(List<Instance> data, int currentDepth) {
        if (currentDepth == maxDepth || data.isEmpty() || isHomogeneous(data)) {
            return new TreeNode(null, Double.NaN, null, null, getMajorityLabel(data));
        }

        int numFeatures = data.get(0).features.length;
        int[] featureIndices = random.ints(0, numFeatures)
                .distinct()
                .limit(Math.max(1, (int) Math.sqrt(numFeatures)))
                .toArray();

        double bestGain = Double.NEGATIVE_INFINITY;
        int bestFeatureIndex = -1;
        double bestThreshold = Double.NaN;

        for (int featureIndex : featureIndices) {
            double[] thresholdGain = findBestThresholdAndGain(data, featureIndex);
            if (thresholdGain[1] > bestGain) {
                bestGain = thresholdGain[1];
                bestFeatureIndex = featureIndex;
                bestThreshold = thresholdGain[0];
            }
        }

        if (bestFeatureIndex == -1) {
            return new TreeNode(null, Double.NaN, null, null, getMajorityLabel(data));
        }

        String splitFeature = featureNames[bestFeatureIndex];
        featureImportance.merge(splitFeature, 1, Integer::sum);

        TreeNode node = new TreeNode(splitFeature, bestThreshold, null, null, null);
        List<Instance> leftData = new ArrayList<>();
        List<Instance> rightData = new ArrayList<>();

        for (Instance instance : data) {
            if (instance.features[bestFeatureIndex] <= bestThreshold) {
                leftData.add(instance);
            } else {
                rightData.add(instance);
            }
        }

        node.left = train(leftData, currentDepth + 1);
        node.right = train(rightData, currentDepth + 1);

        return node;
    }

    private double[] findBestThresholdAndGain(List<Instance> data, int featureIndex) {
        double bestGain = Double.NEGATIVE_INFINITY;
        double bestThreshold = Double.NaN;

        for (Instance instance : data) {
            double threshold = instance.features[featureIndex];
            double gain = calculateInformationGainForThreshold(data, featureIndex, threshold);

            if (gain > bestGain) {
                bestGain = gain;
                bestThreshold = threshold;
            }
        }

        return new double[]{bestThreshold, bestGain};
    }

    private double calculateInformationGainForThreshold(List<Instance> data, int featureIndex, double threshold) {
        List<Instance> leftData = new ArrayList<>();
        List<Instance> rightData = new ArrayList<>();

        for (Instance instance : data) {
            if (instance.features[featureIndex] <= threshold) {
                leftData.add(instance);
            } else {
                rightData.add(instance);
            }
        }

        double totalEntropy = calculateEntropy(data);
        double leftEntropy = calculateEntropy(leftData);
        double rightEntropy = calculateEntropy(rightData);

        double weightedEntropy = ((double) leftData.size() / data.size()) * leftEntropy
                + ((double) rightData.size() / data.size()) * rightEntropy;

        return totalEntropy - weightedEntropy;
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
        List<String> labels = data.stream().map(instance -> instance.label).collect(Collectors.toList());
        Collections.sort(labels);
        int middleIndex = labels.size() / 2;
        return labels.get(middleIndex);
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

    public String predict(Instance instance) {
        TreeNode currentNode = root;

        while (currentNode != null && !(currentNode.left == null && currentNode.right == null)) {
            double featureValue = instance.features[getIndexForFeature(currentNode.attribute)];

            if (featureValue <= currentNode.threshold) {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
        }

        return currentNode != null ? currentNode.label : null;
    }

    private int getIndexForFeature(String feature) {
        Map<String, Integer> featureIndexMap = new HashMap<>();
        featureIndexMap.put("discounted_price", 0);
        featureIndexMap.put("actual_price", 1);
        featureIndexMap.put("discount_percentage", 2);
        featureIndexMap.put("rating", 3);
        featureIndexMap.put("rating_count", 4);

        return featureIndexMap.getOrDefault(feature, -1); // 如果找不到特征，返回-1
    }

    public Map<String, Integer> getFeatureImportance() {
        return featureImportance;
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

    public RandomForest(int numTrees, int maxDepth) {
        trees = new ArrayList<>();
        this.maxDepth = maxDepth;
        for (int i = 0; i < numTrees; i++) {
            trees.add(new DecisionTree(maxDepth));
        }
    }

    public void train(List<Instance> data) {
        for (DecisionTree tree : trees) {
            tree.train(data, 0);
            Map<String, Integer> treeImportance = tree.getFeatureImportance();
            mergeImportance(treeImportance);
        }
    }

    private void mergeImportance(Map<String, Integer> treeImportance) {
        treeImportance.forEach((feature, count) ->
                globalFeatureImportance.merge(feature, count, Integer::sum));
    }

    public String predict(Instance instance) {
        int countYes = 0;
        int countNo = 0;

        for (DecisionTree tree : trees) {
            String prediction = tree.predict(instance);

            if ("Yes".equals(prediction)) {
                countYes++;
            } else if ("No".equals(prediction)) {
                countNo++;
            }
        }

        return (countYes > countNo) ? "Yes" : "No";
    }

    public Map<String, Integer> getGlobalFeatureImportance() {
        return globalFeatureImportance;
    }

    public static void main(String[] args) throws IOException {
        // [此处省略数据集的加载和预测逻辑，包括文件读取和CSV解析]
    }
}
