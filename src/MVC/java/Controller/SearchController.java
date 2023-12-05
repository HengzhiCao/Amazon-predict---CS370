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

    public void handleSearch(double priceFrom, double priceTo, String productName, MainView view) {
        try {
            // 调用 ProductModel 获取过滤后的产品列表
            List<Product> products = productModel.getFilteredProducts(priceFrom, priceTo, productName);
            // 将产品列表传递给视图层进行显示
            view.displayProducts(products);
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常，例如显示错误消息
        }
    }
}
