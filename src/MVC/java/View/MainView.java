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

    public void displayRecommendedProducts(List<Product> products) {
        productModel.clear();
        for (Product product : products) {
            productModel.addElement(product);
        }

        // 可选：更新界面以反映推荐产品的显示，例如更改窗口标题
        setTitle("Recommended Products");
    }

    public void notifyUserAboutUpdate() {
        JOptionPane.showMessageDialog(this, "We've updated the product list with recommendations with same category, This is another choices for same category products. Because the product is not worth.", "Second choice Update Notification", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }


    private void setupInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        inputPanel.add(new JLabel("Price From:"));
        priceFromField = new JTextField(10);
        inputPanel.add(priceFromField);

        inputPanel.add(new JLabel("Price To:"));
        priceToField = new JTextField(10);
        inputPanel.add(priceToField);

        inputPanel.add(new JLabel("Product Name:"));
        productNameField = new JTextField(10);
        inputPanel.add(productNameField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> onSearch());
        inputPanel.add(searchButton);

        add(inputPanel, BorderLayout.NORTH);
    }

    private void setupProductList() {
        productModel = new DefaultListModel<>();
        productList = new JList<>(productModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(productList), BorderLayout.CENTER);
    }

    private void setupSelectButton() {
        JButton selectButton = new JButton("Select Product");
        selectButton.addActionListener(e -> onSelectProduct());
        add(selectButton, BorderLayout.SOUTH);
    }

    private void onSearch() {
        try {
            double priceFrom = priceFromField.getText().isEmpty() ? Double.MIN_VALUE : Double.parseDouble(priceFromField.getText());
            double priceTo = priceToField.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(priceToField.getText());
            String productName = productNameField.getText();
            controller.handleSearch(priceFrom, priceTo, productName, this);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for price.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSelectProduct() {
        Product selectedProduct = productList.getSelectedValue();
        if (selectedProduct != null) {
            controller.handleProductSelection(selectedProduct);
        }
    }

    public void displayProducts(List<Product> products) {
        productModel.clear();
        for (Product product : products) {
            productModel.addElement(product);
        }
    }

    public void setController(ProductController controller) {
        this.controller = controller;
    }


    // ... 其他方法 ...
}