package View;

import Model_classifier.Product;
import Controller.ProductController;
import Controller.PredictionController;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class ProductDetailView extends JFrame {
    private JLabel nameLabel;
    private JLabel priceLabel;
    private JLabel descriptionLabel;
    private JLabel imageLabel;
    private JButton predictButton;
    private Product product;
    private PredictionController predictionController;

    private ProductController controller; // 添加对 ProductController 的引用


    public ProductDetailView(Product product, PredictionController predictionController, ProductController controller) {
        this.product = product;
        this.predictionController = predictionController;
        this.controller = controller;


        initializeUI();
        loadImage();
    }

    private void initializeUI() {
        setTitle("Product Detail");
        setSize(400, 300);
        setLayout(new BorderLayout());

        nameLabel = new JLabel("Name: " + product.getName());
        priceLabel = new JLabel("Price: " + product.getDiscountedPrice());
        descriptionLabel = new JLabel("Description: " + product.getDescription());
        imageLabel = new JLabel();
        loadImage();

        predictButton = new JButton("Predict Worth");
        predictButton.addActionListener(e -> onPredict());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(nameLabel);
        panel.add(priceLabel);
        panel.add(descriptionLabel);
        panel.add(imageLabel);
        panel.add(predictButton);

        add(panel, BorderLayout.CENTER);
    }

    private void loadImage() {
        try {
            String imageUrl = product.getImageUrl();
            if (imageUrl != null && imageUrl.startsWith("http")) {  // 确保 URL 有效
                URL url = new URL(imageUrl);
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                imageLabel.setIcon(imageIcon);
            } else {
                imageLabel.setText("Image not available");
            }
        } catch (MalformedURLException e) {
            imageLabel.setText("Invalid image URL");
            e.printStackTrace();
        } catch (Exception e) {
            imageLabel.setText("Error loading image");
            e.printStackTrace();
        }
    }


    private void onPredict() {
        String prediction = predictionController.predictProduct(product);
        PredictionResultView resultView = new PredictionResultView();
        resultView.displayPredictionResult(prediction);
        resultView.setVisible(true);

        // 调用 ProductController 的 handlePredictionResult 方法
        controller.handlePredictionResult(product);
    }


    public void displayProductDetails(Product product) {
        // 更新窗口中的各个组件以显示产品信息
        nameLabel.setText("Name: " + product.getName());
        priceLabel.setText("Price: " + product.getDiscountedPrice());
        descriptionLabel.setText("Description: " + product.getDescription());
        // 可以添加更多的组件更新逻辑，例如图片、评分等
    }
}
