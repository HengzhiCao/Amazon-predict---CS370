package Model_classifier;

/**
 * Represents a single instance or data point in a machine learning dataset.
 */
public class Instance {
    // An array to store the features (attributes) of the instance.
    public double[] features;

    // A string label to indicate the class or category of the instance.
    public String label;

    /**
     * Initializes a new instance with the given features and label.
     *
     * @param features An array of feature values for this instance.
     * @param label    The label or class associated with this instance.
     */
    public Instance(double[] features, String label) {
        this.features = features;
        this.label = label;
    }
}
