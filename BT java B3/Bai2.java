import javax.swing.*;
import java.awt.event.*;

public class Bai2 {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Bài 2 - Tính tổng");
        frame.setSize(350, 250);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lb1 = new JLabel("Số thứ nhất:");
        lb1.setBounds(20, 20, 100, 30);
        frame.add(lb1);

        JTextField txt1 = new JTextField();
        txt1.setBounds(120, 20, 150, 30);
        frame.add(txt1);

        JLabel lb2 = new JLabel("Số thứ hai:");
        lb2.setBounds(20, 60, 100, 30);
        frame.add(lb2);

        JTextField txt2 = new JTextField();
        txt2.setBounds(120, 60, 150, 30);
        frame.add(txt2);

        JButton btn = new JButton("Tính tổng");
        btn.setBounds(100, 110, 120, 30);
        frame.add(btn);

        JLabel kq = new JLabel();
        kq.setBounds(20, 160, 250, 30);
        frame.add(kq);

        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int a = Integer.parseInt(txt1.getText());
                int b = Integer.parseInt(txt2.getText());

                int tong = a + b;

                kq.setText("Tổng = " + tong);
            }
        });

        frame.setVisible(true);
    }
}