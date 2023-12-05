package Model_classifier.classifier;

import Model_classifier.Instance;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DecisionTree{
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

    private TreeNode root;

    /**
     * Returns the root node of the tree.
     *
     * @return the root node
     */
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
//        System.out.println("Training: Current depth = " + currentDepth + ", Data size = " + data.size());


        // Check termination conditions
        if (currentDepth == maxDepth || data.isEmpty() || isHomogeneous(data)) {
            // Create a leaf node
            TreeNode leaf = new TreeNode();
            leaf.label = getMajorityLabel(data);
            // Print debug information
//            System.out.println("Creating leaf node with label: " + leaf.label);
            return leaf;
        }

        // Find the best split
        Split bestSplit = findBestSplit(data);
        if (bestSplit == null) {
            // Unable to find a split, create a leaf node
            TreeNode leaf = new TreeNode();
            leaf.label = getMajorityLabel(data);
            // Print debug information
//            System.out.println("Creating leaf node due to no split found, with label: " + leaf.label);
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
//        System.out.println("Splitting node at featureIndex: " + node.featureIndex + " with threshold: " + node.threshold);

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
    /**
     * Finds the best split for a given list of instances.
     *
     * @param data The list of instances to find the best split for.
     * @return The best split found.
     * Diversifying by Feature Projection
     */
    private Split findBestSplit(List<Instance> data) {
        Split bestSplit = new Split();
        double bestImpurity = Double.MAX_VALUE;
        int numFeatures = data.get(0).features.length;
        int numFeaturesToConsider = numFeatures / 2; // 选择一半的特征

        // 随机选择特征
        List<Integer> shuffledFeatureIndices = IntStream.range(0, numFeatures).boxed().collect(Collectors.toList());
        Collections.shuffle(shuffledFeatureIndices);
        List<Integer> selectedFeatureIndices = shuffledFeatureIndices.subList(0, numFeaturesToConsider);

        // 只在选定的特征中寻找最佳分裂点
        for (int featureIndex : selectedFeatureIndices) {
            Set<Double> featureValues = new HashSet<>();
            for (Instance instance : data) {
                featureValues.add(instance.features[featureIndex]);
            }

            for (double threshold : featureValues) {
                Split split = new Split();
                split.featureIndex = featureIndex;
                split.threshold = threshold;

                List<Instance> leftSplit = new ArrayList<>();
                List<Instance> rightSplit = new ArrayList<>();
                for (Instance instance : data) {
                    if (instance.features[featureIndex] <= threshold) {
                        leftSplit.add(instance);
                    } else {
                        rightSplit.add(instance);
                    }
                }

                double impurity = calculateImpurity(leftSplit, rightSplit);
                if (impurity < bestImpurity) {
                    bestImpurity = impurity;
                    bestSplit = split;
                }
            }
        }

        return bestSplit;
    }

    /**
     * Calculates the impurity of a split given the left and right splits.
     *
     * @param leftSplit  the list of instances in the left split
     * @param rightSplit the list of instances in the right split
     * @return the impurity of the split
     */
    private double calculateImpurity(List<Instance> leftSplit, List<Instance> rightSplit) {
        // Calculate the total number of instances in both splits
        double total = leftSplit.size() + rightSplit.size();

        // Initialize the impurity
        double impurity = 0.0;

        // Calculate the impurity of the left split and add it to the impurity
        impurity += (leftSplit.size() / total) * calculateGini(leftSplit);

        // Calculate the impurity of the right split and add it to the impurity
        impurity += (rightSplit.size() / total) * calculateGini(rightSplit);

        // Return the impurity of the split
        return impurity;
    }
    /**
     * Calculate the Gini impurity of a given dataset.
     *
     * @param data The list of instances in the dataset.
     * @return The Gini impurity.
     */
    public double calculateGini(List<Instance> data) {
        // Get the total number of instances in the dataset
        int total = data.size();
        if (total == 0) {
            return 0;
        }

        // Count the number of instances for each label
        Map<String, Integer> counts = new HashMap<>();
        for (Instance instance : data) {
            counts.merge(instance.label, 1, Integer::sum);
        }

        // Calculate the Gini impurity
        double impurity = 1.0;
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            double prob = entry.getValue() / (double) total;
            impurity -= prob * prob;
        }
        return impurity;
    }

    /**
     * Checks if the given list of instances is homogeneous.
     * A list is considered homogeneous if all instances have the same label.
     *
     * @param data The list of instances to check.
     * @return True if the list is homogeneous, false otherwise.
     */
    boolean isHomogeneous(List<Instance> data) {
        // Create a set to store unique labels
        Set<String> uniqueLabels = new HashSet<>();

        // Iterate over each instance in the data list
        for (Instance instance : data) {
            // Add the label of the instance to the set of unique labels
            uniqueLabels.add(instance.label);

            // If there are more than one unique labels, the list is not homogeneous
            if (uniqueLabels.size() > 1) {
                return false;
            }
        }

        // If all instances have the same label, the list is homogeneous
        return true;
    }

    /**
     * Returns the majority label from a list of instances.
     * If the list is empty, returns a default label.
     * @param data The list of instances.
     * @return The majority label.
     */
    String getMajorityLabel(List<Instance> data) {
        // Check if the list is empty
        if (data.isEmpty()) {
            // Handle the case of empty data, for example, return a default label or throw an exception
            return "defaultLabel"; // or throw a more specific exception
        }

        // Count the occurrences of each label in the list
        Map<String, Long> labelCounts = data.stream().collect(Collectors.groupingBy(instance -> instance.label, Collectors.counting()));

        // Find the label with the highest count
        return Collections.max(labelCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
    }


    /**
     * Predicts the label for the given instance using the decision tree.
     *
     * @param instance The instance for which to predict the label.
     * @param node The current node in the decision tree.
     * @return The predicted label for the instance.
     * @throws IllegalStateException If the node is null.
     */
    public String predict(Instance instance, TreeNode node) {
        // Check if the node is null
        if (node == null) {
            throw new IllegalStateException("Node is null. Current instance: " + Arrays.toString(instance.features));
        }

        // Check if the node is a leaf node
        if (node.left == null && node.right == null) {
            return node.label; // Reached a leaf node
        }

        // Traverse the decision tree based on the instance features
        if (instance.features[node.featureIndex] <= node.threshold) {
            return predict(instance, node.left);
        } else {
            return predict(instance, node.right);
        }
    }


}

