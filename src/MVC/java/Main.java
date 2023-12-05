import Controller.ProductController;
import Controller.PredictionController;
import Model_classifier.ProductModel;
import Model_classifier.RandomForestModel;
import View.MainView;
import javax.swing.*;
import java.io.IOException;

import java.io.IOException;
import javax.swing.SwingUtilities;

public class Main {

    /**
     * Main method to execute the program.
     *
     * @param args The command line arguments.
     * @throws IOException If an I/O error occurs.
     */
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> {
            try {
                // Initialize the random forest model
                RandomForestModel randomForestModel = new RandomForestModel();

                // Create the prediction controller
                PredictionController predictionController = new PredictionController(randomForestModel.getRandomForest());

                // CSV file path
                String csvFilePath = "amazon_product_TestData.csv";

                // Create the product model
                ProductModel model = new ProductModel(csvFilePath);

                // Create an instance of MainView without passing a ProductController
                MainView mainView = new MainView(null);

                // Create the ProductController instance and pass all the necessary parameters
                ProductController productController = new ProductController(model, predictionController, mainView);

                // Set the ProductController to the MainView
                mainView.setController(productController);

                // Display the main view
                mainView.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
