package Controller;

import Model_classifier.Instance;
import Model_classifier.Product;
import Model_classifier.classifier.RandomForest;

public class PredictionController {
    private RandomForest randomForest;

    public PredictionController(RandomForest randomForest) {
        this.randomForest = randomForest;
    }


    /**
     * Predicts the product based on the given features.
     *
     * @param product The product to predict.
     * @return The predicted product.
     */
    public String predictProduct(Product product) {
        // Extract features from the product
        double[] features = extractFeatures(product);

        // Create an instance with the extracted features
        Instance instance = new Instance(features, "", "");

        // Predict the product using the random forest model
        return randomForest.predict(instance);
    }

    /**
     * Extracts features from a Product object and returns them as an array of doubles.
     * The array contains the following features in order:
     * - Discounted price
     * - Actual price
     * - Discount percentage
     * - Rating
     * - Rating count
     *
     * @param product The Product object to extract features from
     * @return An array of doubles representing the extracted features
     */
    private double[] extractFeatures(Product product) {
        double[] features = new double[5];
        features[0] = product.getDiscountedPrice();
        features[1] = product.getActualPrice();
        features[2] = product.getDiscountPercentage();
        features[3] = product.getRating();
        features[4] = product.getRatingCount();
        return features;
    }

}
