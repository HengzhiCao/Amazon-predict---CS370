package Controller;

public class ErrorHandlingController {

    public ErrorHandlingController() {
        // 构造函数
    }

    public void handleException(Exception e, String context) {
        // 根据异常类型和上下文显示错误消息或执行其他错误处理逻辑
        // 例如，记录错误、通知用户等
        System.err.println("Error occurred in " + context + ": " + e.getMessage());
        e.printStackTrace();
    }

    // 可以添加更多的方法来处理特定类型的异常或错误情况
}
