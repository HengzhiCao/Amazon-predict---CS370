package View;

import Model_classifier.Product;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import Controller.ProductController;

public class MainView extends JFrame {
    private JList<Product> productList;
    private DefaultListModel<Product> productModel;
    private JTextField priceFromField;
    private JTextField priceToField;
    private JTextField productNameField;
    private ProductController controller;

    public MainView(ProductController controller) {
        this.controller = controller;

        setTitle("Product Selection");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setupInputPanel();
        setupProductList();
        setupSelectButton();
    }

    /**
     * Displays the recommended products in the UI.
     *
     * @param products The list of recommended products.
     */
    public void displayRecommendedProducts(List<Product> products) {
        // Clear the product model
        productModel.clear();

        // Add each product to the product model
        for (Product product : products) {
            productModel.addElement(product);
        }

        // Set the title of the UI window
        setTitle("Recommended Products");
    }

    /**
     * Notifies the user about the product list update.
     */
    public void notifyUserAboutUpdate() {
        String message = "Since the product you selected is not recommended,\n" +
                " we have updated the product list with items that our model deems worthy.\n" +
                "You can still click the button to check if the products are worth it.";

        JOptionPane.showMessageDialog(this, message, "Product List Update", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays an error message in a dialog box.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays an information message dialog with the given message.
     *
     * @param message the message to display
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * Sets up the input panel for the search functionality.
     */
    private void setupInputPanel() {
        // Create the input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        // Add the "Price From" label and text field
        inputPanel.add(new JLabel("Price From:"));
        priceFromField = new JTextField(10);
        inputPanel.add(priceFromField);

        // Add the "Price To" label and text field
        inputPanel.add(new JLabel("Price To:"));
        priceToField = new JTextField(10);
        inputPanel.add(priceToField);

        // Add the "Product Name" label and text field
        inputPanel.add(new JLabel("Product Name:"));
        productNameField = new JTextField(10);
        inputPanel.add(productNameField);

        // Add the search button and its action listener
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> onSearch());
        inputPanel.add(searchButton);

        // Add the input panel to the north of the container
        add(inputPanel, BorderLayout.NORTH);
    }

    /**
     * Initializes the product list.
     */
    private void setupProductList() {
        // Create a new default list model for the product list
        productModel = new DefaultListModel<>();

        // Create a new JList using the product model
        productList = new JList<>(productModel);

        // Set the selection mode of the product list to single selection
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add the product list to the center of the layout using a scroll pane
        add(new JScrollPane(productList), BorderLayout.CENTER);
    }

    /**
     * Sets up the select button and its action listener.
     */
    private void setupSelectButton() {
        // Create a new JButton with the label "Select Product"
        JButton selectButton = new JButton("Select Product");

        // Add an action listener to the selectButton
        selectButton.addActionListener(e -> onSelectProduct());

        // Add the selectButton to the SOUTH position of the layout
        add(selectButton, BorderLayout.SOUTH);
    }

    /**
     * Performs a search based on the specified criteria.
     */
    private void onSearch() {
        try {
            // Get the price range from the input fields
            double priceFrom = priceFromField.getText().isEmpty() ? Double.MIN_VALUE : Double.parseDouble(priceFromField.getText());
            double priceTo = priceToField.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(priceToField.getText());

            // Get the product name from the input field
            String productName = productNameField.getText();

            // Call the controller to handle the search with the specified criteria
            controller.handleSearch(priceFrom, priceTo, productName, this);
        } catch (NumberFormatException e) {
            // Display an error message if the input values are invalid
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for price.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called when a product is selected from the product list.
     * It handles the selection by passing the selected product to the controller.
     */
    private void onSelectProduct() {
        // Get the selected product from the product list
        Product selectedProduct = productList.getSelectedValue();

        // If a product is selected, pass it to the controller
        if (selectedProduct != null) {
            controller.handleProductSelection(selectedProduct);
        }
    }

    /**
     * Display the list of products in the productModel.
     *
     * @param products The list of products to display.
     */
    public void displayProducts(List<Product> products) {
        // Clear the productModel
        productModel.clear();

        // Add each product to the productModel
        for (Product product : products) {
            productModel.addElement(product);
        }
    }

    /**
     * Set the product controller.
     *
     * @param controller The product controller to set.
     */
    public void setController(ProductController controller) {
        this.controller = controller;
    }
}