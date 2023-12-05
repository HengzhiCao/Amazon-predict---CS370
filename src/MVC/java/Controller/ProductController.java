package Controller;

import Controller.DataPreprocessingController;
import Controller.ErrorHandlingController;
import Controller.InputValidationController;
import Controller.PredictionController;
import Model_classifier.Product;
import Model_classifier.ProductModel;
import View.MainView;
import View.PredictionResultView;
import View.ProductDetailView;

import java.util.List;

public class ProductController {
    private ProductModel productModel;
    private MainView mainView;

    private ProductModel model;
    private PredictionController predictionController;

    private PredictionResultView predictionResultView;

    private DataPreprocessingController dataPreprocessingController;

    private ErrorHandlingController errorHandlingController;

    private InputValidationController inputValidationController;





    public ProductController(ProductModel productModel, PredictionController predictionController, MainView mainView) {
        this.productModel = productModel;
        this.predictionController = predictionController;
        this.mainView = mainView;
        this.predictionResultView = new PredictionResultView();
        this.dataPreprocessingController = new DataPreprocessingController();
        this.errorHandlingController = new ErrorHandlingController();
        this.inputValidationController = new InputValidationController();


    }

    public void handleSearch(double priceFrom, double priceTo, String productName, MainView view) {
        if (!inputValidationController.validatePrice(priceFrom) || !inputValidationController.validatePrice(priceTo)) {
            view.showError("Invalid price range");
            return;
        }
        try {
            List<Product> products = productModel.getFilteredProducts(priceFrom, priceTo, productName);
            List<Product> preprocessedProducts = dataPreprocessingController.preprocessProducts(products);
            if (products.isEmpty()) {
                // 如果产品列表为空，通知用户没有找到产品
                view.showInfo("No products found in the database matching your criteria.");
            } else {
                // 否则显示产品列表
                view.displayProducts(products);
            }
            view.displayProducts(preprocessedProducts);
        } catch (Exception e) {
            errorHandlingController.handleException(e, "Search Operation");
        }
    }

    public void handleProductSelection(Product selectedProduct) {
        // 创建 ProductDetailView 实例并传递所需的参数
        ProductDetailView detailView = new ProductDetailView(selectedProduct, predictionController, this);
        detailView.setVisible(true);

        // 可以在这里添加调用预测控制器的代码
        // 例如：String prediction = predictionController.predictProduct(selectedProduct);
    }

    public void handlePredictionResult(Product selectedProduct) {
        String prediction = predictionController.predictProduct(selectedProduct);
        predictionResultView.displayPredictionResult("Prediction: " + prediction);

        if ("No".equals(prediction)) {
            try {
                List<Product> recommendedProducts = productModel.getProductsByCategory(selectedProduct.getCategory());
                recommendedProducts.remove(selectedProduct); // 移除当前选中的产品
                mainView.displayRecommendedProducts(recommendedProducts);
                mainView.notifyUserAboutUpdate();  // 通知用户更新
            } catch (Exception e) {
                errorHandlingController.handleException(e, "Prediction Result");}
        }
    }

}
