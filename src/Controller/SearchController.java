package Controller;

import Model_classifier.Product;
import Model_classifier.ProductModel;
import View.MainView;

import java.util.List;

public class SearchController {
    private ProductModel productModel;

    public SearchController(ProductModel productModel) {
        this.productModel = productModel;
    }

    /**
     * Handles the search functionality by filtering products based on price range and product name.
     * Displays the filtered products in the main view.
     *
     * @param priceFrom The minimum price of the products to be included in the search.
     * @param priceTo The maximum price of the products to be included in the search.
     * @param productName The name of the products to be included in the search.
     * @param view The main view where the filtered products will be displayed.
     */
    public void handleSearch(double priceFrom, double priceTo, String productName, MainView view) {
        try {
            // Get the filtered products using the ProductModel
            List<Product> products = productModel.getFilteredProducts(priceFrom, priceTo, productName);
            // Display the products in the main view
            view.displayProducts(products);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception, e.g., display an error message
        }
    }
}
