package com.cs370.group4.goodspredict;

public class Instance {
    double[] features;
    String label;
    String splitFeature;

    public Instance(double[] features, String label, String splitFeature) {
        this.features = features;
        this.label = label;
        this.splitFeature = splitFeature;
    }

}