package Model_classifier;

import Model_classifier.Product;

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

    public List<Product> getProductsByCategory(String category) throws IOException {
        List<Product> allProducts = getProducts();
        List<Product> productsInCategory = new ArrayList<>();

        for (Product product : allProducts) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                productsInCategory.add(product);
            }
        }

        return productsInCategory;
    }


    public List<Product> getFilteredProducts(double priceFrom, double priceTo, String productName) throws IOException {
        List<Product> allProducts = getProducts();
        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : allProducts) {
            if (product.getDiscountedPrice() >= priceFrom && product.getDiscountedPrice() <= priceTo) {
                if (productName.isEmpty() || product.getName().toLowerCase().contains(productName.toLowerCase())) {
                    filteredProducts.add(product);
                }
            }
        }

        return filteredProducts;
    }

    public List<Product> getProducts() throws IOException {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // 跳过标题行
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
                        // 处理解析错误
                    }
                }
            }
        }
        return products;
    }
}
