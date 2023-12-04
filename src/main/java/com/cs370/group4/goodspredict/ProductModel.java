package com.cs370.group4.goodspredict;

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

    public List<Product> getProducts() throws IOException {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                // 跳过CSV文件的标题行
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                try {
                    // 尝试解析数字字段
                    double discountedPrice = Double.parseDouble(values[1]);
                    Product product = new Product(
                            values[6], // product_name
                            values[8], // img_link
                            discountedPrice, // discounted_price
                            values[7] // category
                    );
                    products.add(product);
                } catch (NumberFormatException e) {
                    // 处理解析错误，例如跳过这行或记录错误
                    System.err.println("Error parsing line: " + line);
                }
            }
        }
        return products;
    }
}
