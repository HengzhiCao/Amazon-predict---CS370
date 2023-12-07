package Model_classifier;

public class Instance {
    public double[] features;
    public String label;

    public Instance(double[] features, String label) {
        this.features = features;
        this.label = label;

    }

}