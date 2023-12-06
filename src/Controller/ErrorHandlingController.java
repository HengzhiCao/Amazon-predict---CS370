package Controller;

public class ErrorHandlingController {

    public ErrorHandlingController() {

    }

    /**
     * Handles an exception by displaying an error message and printing the stack trace.
     *
     * @param e       The exception to handle.
     * @param context The context in which the exception occurred.
     */
    public void handleException(Exception e, String context) {
        // Display error message
        System.err.println("Error occurred in " + context + ": " + e.getMessage());

        // Print stack trace
        e.printStackTrace();
    }

    // 可以添加更多的方法来处理特定类型的异常或错误情况
}
