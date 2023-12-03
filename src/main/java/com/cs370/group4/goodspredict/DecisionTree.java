package com.cs370.group4.goodspredict;

import java.util.*;
import java.util.stream.Collectors;

public class DecisionTree {
    static class TreeNode {
        double threshold;
        TreeNode left;
        TreeNode right;
        String label;

        int featureIndex;
    }

    static class Split {
        int featureIndex;
        double threshold;
    }

    private int maxDepth;
    private Map<String, Integer> featureImportance = new HashMap<>();

    private TreeNode root;

    public TreeNode getRoot() {
        return root;
    }

    public DecisionTree(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    /**
     * Trains a decision tree using the given data and current depth.
     *
     * @param data          The list of instances to train the tree on.
     * @param currentDepth  The current depth of the tree.
     * @return              The root node of the trained decision tree.
     */
    public TreeNode train(List<Instance> data, int currentDepth) {
        // Print training information
        System.out.println("Training: Current depth = " + currentDepth + ", Data size = " + data.size());


        // Check termination conditions
        if (currentDepth == maxDepth || data.isEmpty() || isHomogeneous(data)) {
            // Create a leaf node
            TreeNode leaf = new TreeNode();
            leaf.label = getMajorityLabel(data);
            // Print debug information
            System.out.println("Creating leaf node with label: " + leaf.label);
            return leaf;
        }

        // Find the best split
        Split bestSplit = findBestSplit(data);
        if (bestSplit == null) {
            // Unable to find a split, create a leaf node
            TreeNode leaf = new TreeNode();
            leaf.label = getMajorityLabel(data);
            // Print debug information
            System.out.println("Creating leaf node due to no split found, with label: " + leaf.label);
            return leaf;
        }

        // Create a new node
        TreeNode node = new TreeNode();
        node.featureIndex = bestSplit.featureIndex;
        node.threshold = bestSplit.threshold;

        // Split the data
        List<Instance> leftData = new ArrayList<>();
        List<Instance> rightData = new ArrayList<>();
        for (Instance instance : data) {
            if (instance.features[node.featureIndex] <= node.threshold) {
                leftData.add(instance);
            } else {
                rightData.add(instance);
            }
        }

        // Print debug information
        System.out.println("Splitting node at featureIndex: " + node.featureIndex + " with threshold: " + node.threshold);

        // Recursively train the left and right subtrees
        node.left = train(leftData, currentDepth + 1);
        node.right = train(rightData, currentDepth + 1);

        // Set the root node if at the top level
        if (currentDepth == 0) {
            root = node;
        }

        return node;
    }


    /**
     * Finds the best split for a given list of instances.
     *
     * @param data The list of instances to find the best split for.
     * @return The best split found.
     */
    private Split findBestSplit(List<Instance> data) {
        // Initialize the best split and impurity
        Split bestSplit = new Split();
        double bestImpurity = Double.MAX_VALUE;
        int numFeatures = data.get(0).features.length;

        // Iterate over each feature
        for (int featureIndex = 0; featureIndex < numFeatures; featureIndex++) {
            // Get the unique values for the current feature
            Set<Double> featureValues = new HashSet<>();
            for (Instance instance : data) {
                featureValues.add(instance.features[featureIndex]);
            }

            // Iterate over each unique value as a potential threshold
            for (double threshold : featureValues) {
                // Create a new split with the current feature index and threshold
                Split split = new Split();
                split.featureIndex = featureIndex;
                split.threshold = threshold;

                // Split the data into left and right splits based on the threshold
                List<Instance> leftSplit = new ArrayList<>();
                List<Instance> rightSplit = new ArrayList<>();
                for (Instance instance : data) {
                    if (instance.features[featureIndex] <= threshold) {
                        leftSplit.add(instance);
                    } else {
                        rightSplit.add(instance);
                    }
                }

                // Calculate the impurity of the split
                double impurity = calculateImpurity(leftSplit, rightSplit);

                // Update the best split if the current impurity is lower
                if (impurity < bestImpurity) {
                    bestImpurity = impurity;
                    bestSplit = split;
                }
            }
        }

        // Return the best split found
        return bestSplit;
    }

    private double calculateImpurity(List<Instance> leftSplit, List<Instance> rightSplit) {
        double total = leftSplit.size() + rightSplit.size();
        double impurity = 0.0;
        impurity += (leftSplit.size() / total) * calculateGini(leftSplit);
        impurity += (rightSplit.size() / total) * calculateGini(rightSplit);
        return impurity;
    }

    double calculateGini(List<Instance> data) {
        int total = data.size();
        if (total == 0) {
            return 0;
        }

        Map<String, Integer> counts = new HashMap<>();
        for (Instance instance : data) {
            counts.merge(instance.label, 1, Integer::sum);
        }

        double impurity = 1.0;
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            double prob = entry.getValue() / (double) total;
            impurity -= prob * prob;
        }
        return impurity;
    }

    boolean isHomogeneous(List<Instance> data) {
        Set<String> uniqueLabels = new HashSet<>();
        for (Instance instance : data) {
            uniqueLabels.add(instance.label);
            if (uniqueLabels.size() > 1) {
                return false;
            }
        }
        return true;
    }

    String getMajorityLabel(List<Instance> data) {
        Map<String, Long> labelCounts = data.stream().collect(Collectors.groupingBy(instance -> instance.label, Collectors.counting()));
        return Collections.max(labelCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
    }


    public Map<String, Integer> getFeatureImportance() {
        return featureImportance;
    }

    public String predict(Instance instance, TreeNode node) {
        if (node == null) {
            throw new IllegalStateException("Node is null. Current instance: " + Arrays.toString(instance.features));
        }

        if (node.left == null && node.right == null) {
            return node.label; // 到达叶子节点
        }

        if (instance.features[node.featureIndex] <= node.threshold) {
            return predict(instance, node.left);
        } else {
            return predict(instance, node.right);
        }
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

