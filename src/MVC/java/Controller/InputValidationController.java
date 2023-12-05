package Controller;

public class InputValidationController {

    public InputValidationController() {
        // 构造函数
    }

    public boolean validatePrice(Double price) {
        // 验证价格是否有效（例如，非负数）
        return price != null && price >= 0;
    }

    public boolean validateProductName(String name) {
        // 验证产品名称是否有效（例如，非空且长度合适）
        return name != null && !name.trim().isEmpty();
    }

}
