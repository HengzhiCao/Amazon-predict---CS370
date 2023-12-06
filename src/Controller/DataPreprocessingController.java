package Controller;

import Model_classifier.Product;

import java.util.List;

public class DataPreprocessingController {

    public DataPreprocessingController() {

    }

    /**
     * Preprocesses a list of products by setting default values for price, name, and description if necessary.
     *
     * @param products The list of products to preprocess.
     * @return The preprocessed list of products.
     */
    public List<Product> preprocessProducts(List<Product> products) {
        for (Product product : products) {
            if (product.getPrice() <= 0.0) {
                product.setPrice(0.0);  // Set price to 0 for products with empty price
            }
            if (product.getName() == null) {
                product.setName("Unknown");  // Set name to "Unknown" for products with empty name
            }
            if (product.getDescription() == null) {
                product.setDescription("No description available");  // Set description to default if it is empty
            }
            if (product.getImageUrl() == null) {
                product.setImageUrl("defaultImageUrl");  // Set imageUrl to default if it is empty
            }
            if (product.getCategory() == null) {
                product.setCategory("Unknown");  // Set category to "Unknown" if it is empty
            }
            if (product.getId() == null) {
                product.setProductId("Unknown");  // Set product ID to "Unknown" if it is empty
            }

            calculateMissingIncorrectValue(product);
        }
        return products;
    }

    /**
     * Calculates missing and incorrect values for a Product.
     *
     * @param product The Product object to calculate the values for.
     */
    public void calculateMissingIncorrectValue(Product product) {
        // Fill in missing discounted price based on actual price and discount percentage
        if (product.getDiscountedPrice() <= 0) {
            double calculatedDiscountedPrice = product.getActualPrice() * (1 - product.getDiscountPercentage() / 100);
            product.setPrice(calculatedDiscountedPrice);
        }

        // Set rating to 0 if it's missing
        if (product.getRating() < 0) {
            product.setRating(0);
        }

        // Set rating count to 0 if it's missing
        if (product.getRatingCount() < 0) {
            product.setRatingCount(0);
        }

        //Fill in missing Discounted percentage based on actual price and discounted price
        if (product.getDiscountPercentage() <= 0) {
            double calculatedDiscountPercentage = (product.getActualPrice() - product.getDiscountedPrice()) / product.getActualPrice() * 100;
            product.setDiscountPercentage(calculatedDiscountPercentage);
        }

        //Fill in missing actual price based on discounted price and discount percentage
        if (product.getActualPrice() <= 0) {
            double calculatedActualPrice = product.getDiscountedPrice() / (1 - product.getDiscountPercentage() / 100);
            product.setActualPrice(calculatedActualPrice);
        }

    }
}
