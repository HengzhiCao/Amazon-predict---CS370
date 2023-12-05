package View;

import javax.swing.*;
import java.awt.*;

public class PredictionResultView extends JFrame {
    private JLabel predictionLabel;

    public PredictionResultView() {
        // 初始化视图组件
        predictionLabel = new JLabel();
        add(predictionLabel);
        // 设置窗口属性
        setTitle("Prediction Result");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public void displayPredictionResult(String prediction) {
        predictionLabel.setText("Prediction: " + prediction);
    }

    // 其他方法...
}

