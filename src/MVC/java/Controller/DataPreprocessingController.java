package Controller;

import Model_classifier.Product;

import java.util.List;

public class DataPreprocessingController {

    public DataPreprocessingController() {

    }

    public List<Product> preprocessProducts(List<Product> products) {
        for (Product product : products) {
            if (product.getPrice() <= 0.0) {
                product.setPrice(0.0);  // 将价格为空的产品价格设置为 0
            }
            if (product.getName() == null) {
                product.setName("Unknown");  // 将名称为空的产品名称设置为 "Unknown"
            }
            if (product.getDescription() == null) {
                product.setDescription("No description available");
            }
        }
        return products;
    }

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

        // Add any additional preprocessing logic needed for other fields
    }
}
