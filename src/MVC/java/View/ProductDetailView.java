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

    /**
     * Initializes the user interface for the product detail view.
     */
    private void initializeUI() {
        // Set the title and size of the window
        setTitle("Product Detail");
        setSize(400, 300);
        setLayout(new BorderLayout());

        // Create labels for displaying product information
        nameLabel = new JLabel("Name: " + product.getName());
        priceLabel = new JLabel("Price: " + product.getDiscountedPrice());
        descriptionLabel = new JLabel("Description: " + product.getDescription());
        imageLabel = new JLabel();
        loadImage();

        // Create a button for predicting the product worth
        predictButton = new JButton("Predict Worth");
        predictButton.addActionListener(e -> onPredict());

        // Create a panel to hold the labels, image, and button
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(nameLabel);
        panel.add(priceLabel);
        panel.add(descriptionLabel);
        panel.add(imageLabel);
        panel.add(predictButton);

        // Add the panel to the center of the window
        add(panel, BorderLayout.CENTER);
    }

    /**
     * Loads and displays an image from a URL.
     * If the URL is invalid or the image fails to load, an error message is displayed.
     */
    private void loadImage() {
        try {
            String imageUrl = product.getImageUrl();
            if (imageUrl != null && imageUrl.startsWith("http")) {
                // Ensure the URL is valid
                URL url = new URL(imageUrl);
                // Scale and set the image icon
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(url).getImage()
                        .getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                imageLabel.setIcon(imageIcon);
            } else {
                // Display a message indicating that the image is not available
                imageLabel.setText("Image not available");
            }
        } catch (MalformedURLException e) {
            // Display an error message for an invalid URL
            imageLabel.setText("Invalid image URL");
            e.printStackTrace();
        } catch (Exception e) {
            // Display an error message for any other exception
            imageLabel.setText("Error loading image");
            e.printStackTrace();
        }
    }


    /**
     * Makes a prediction for the product and displays the result.
     * Also handles the prediction result in the controller.
     */
    private void onPredict() {
        // Make a prediction for the product
        String prediction = predictionController.predictProduct(product);

        // Create a PredictionResultView
        PredictionResultView resultView = new PredictionResultView();

        // Display the prediction result
        resultView.displayPredictionResult(prediction);

        // Set the view as visible
        resultView.setVisible(true);

        // Handle the prediction result in the controller
        controller.handlePredictionResult(product);
    }

    /**
     * Displays the details of a product.
     * @param product The product whose details need to be displayed.
     */
    public void displayProductDetails(Product product) {
        // Update the components in the window to display the product information
        nameLabel.setText("Name: " + product.getName());
        priceLabel.setText("Price: " + product.getDiscountedPrice());
        descriptionLabel.setText("Description: " + product.getDescription());
        // Additional logic to update more components such as images, ratings, etc. can be added here
    }
}
