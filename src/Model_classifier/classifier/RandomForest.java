package Model_classifier.classifier;

import Model_classifier.Instance;

import java.io.FileNotFoundException;
import java.util.*;

public class RandomForest {
    private List<DecisionTree> trees;
    private int maxDepth;

    public RandomForest(int numTrees, int maxDepth) throws FileNotFoundException {
        trees = new ArrayList<>();
        this.maxDepth = maxDepth;
        for (int i = 0; i < numTrees; i++) {
            trees.add(new DecisionTree(maxDepth));
        }
    }

    /**
     * Creates a bootstrap sample from the given data.
     *
     * @param data The list of instances to sample from.
     * @return The bootstrap sample.
     */
    public List<Instance> bootstrapSample(List<Instance> data) {
        Random random = new Random();
        List<Instance> sample = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            int index = random.nextInt(data.size());
            sample.add(data.get(index));
        }

        return sample;
    }

    public void train(List<Instance> data) {
        for (DecisionTree tree : trees) {
            List<Instance> sample = bootstrapSample(data);
            tree.train(sample, 0);
        }
    }


    public String predict(Instance instance) {
        Map<String, Integer> voteCounts = new HashMap<>();
        for (DecisionTree tree : trees) {
            String prediction = tree.predict(instance, tree.getRoot());
            voteCounts.merge(prediction, 1, Integer::sum);
        }

        return Collections.max(voteCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
    }


}
