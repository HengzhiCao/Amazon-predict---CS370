package Model_classifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductModel {
    private String csvFilePath;

    public ProductModel(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    /**
     * Retrieves a list of products that belong to a specific category.
     *
     * @param category The category of products to retrieve.
     * @return A list of products that belong to the specified category.
     * @throws IOException If there is an error retrieving the products.
     */
    public List<Product> getProductsByCategory(String category) throws IOException {
        // Retrieve all products
        List<Product> allProducts = getProducts();

        // Create a new list to store products in the specified category
        List<Product> productsInCategory = new ArrayList<>();

        // Loop through each product and check if it belongs to the specified category
        for (Product product : allProducts) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                // Add the product to the list if it belongs to the specified category
                productsInCategory.add(product);
            }
        }

        // Return the list of products in the specified category
        return productsInCategory;
    }


    /**
     * Returns a list of filtered products based on the given price range and product name.
     *
     * @param priceFrom   the minimum discounted price of the products
     * @param priceTo     the maximum discounted price of the products
     * @param productName the name of the products to filter
     * @return a list of filtered products
     * @throws IOException if there is an error in retrieving the products
     */
    public List<Product> getFilteredProducts(double priceFrom, double priceTo, String productName) throws IOException {
        List<Product> allProducts = getProducts();
        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : allProducts) {
            double discountedPrice = product.getDiscountedPrice();
            String name = product.getName();

            if (discountedPrice >= priceFrom && discountedPrice <= priceTo) {
                if (productName.isEmpty() || name.toLowerCase().contains(productName.toLowerCase())) {
                    filteredProducts.add(product);
                }
            }
        }

        return filteredProducts;
    }

    /**
     * Retrieves a list of products from a CSV file.
     *
     * @return The list of products.
     * @throws IOException If there is an error reading the file.
     */
    public List<Product> getProducts() throws IOException {
        List<Product> products = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }

                String[] values = line.split(",");

                if (values.length >= 10) {
                    try {
                        String id = values[0];
                        double discountedPrice = Double.parseDouble(values[1]);
                        double actualPrice = Double.parseDouble(values[2]);
                        double discountPercentage = Double.parseDouble(values[3]);
                        double rating = Double.parseDouble(values[4]);
                        int ratingCount = Integer.parseInt(values[5]);
                        String name = values[6];
                        String category = values[7];
                        String imageUrl = values[8];
                        String description = values[9];

                        Product product = new Product(id, name, discountedPrice, actualPrice, discountPercentage, rating, ratingCount, imageUrl, description, category);
                        products.add(product);
                    } catch (NumberFormatException e) {
                        // Handle parsing errors
                    }
                }
            }
        }

        return products;
    }
}
