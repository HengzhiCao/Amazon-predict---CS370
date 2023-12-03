package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow {
    public void createWindows() {
        // 创建 JFrame 实例
        JFrame frame = new JFrame("Product predict");

        // 设置关闭操作
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        // 禁用布局管理器，使用绝对定位
        frame.setLayout(null);

        // 创建标签和文本框
        JLabel label1 = new JLabel(" Budget from?");
        label1.setBounds(50, 50, 100, 30);
        JTextField textField1 = new JTextField();
        textField1.setBounds(160, 50, 150, 30);

        JLabel label2 = new JLabel(" Budget to?");
        label2.setBounds(50, 100, 100, 30);
        JTextField textField2 = new JTextField();
        textField2.setBounds(160, 100, 150, 30);

        JLabel label3 = new JLabel(" Product name?");
        label3.setBounds(50, 150, 100, 30);
        JTextField textField3 = new JTextField();
        textField3.setBounds(160, 150, 150, 30);

        JButton button = new JButton("search");
        button.setBounds(50, 200, 100, 30);

        // 将标签和文本框添加到 JFrame
        frame.add(label1);
        frame.add(textField1);
        frame.add(label2);
        frame.add(textField2);
        frame.add(label3);
        frame.add(textField3);
        frame.add(button);

        // 设置窗口的位置
        frame.setLocationRelativeTo(null);

        // 设置窗口可见
        frame.setVisible(true);


    }
}
