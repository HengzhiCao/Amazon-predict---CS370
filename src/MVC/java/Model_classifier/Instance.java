package Model_classifier;

public class Instance {
    public double[] features;
    public String label;
    public String splitFeature;

    public Instance(double[] features, String label, String splitFeature) {
        this.features = features;
        this.label = label;
        this.splitFeature = splitFeature;
    }

}