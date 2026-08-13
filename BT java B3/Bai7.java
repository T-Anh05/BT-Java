import javax.swing.*;

public class Bai7 {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Máy tính mini");
        frame.setSize(400, 280);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel a = new JLabel("Nhập số a:");
        a.setBounds(30, 30, 80, 30);
        frame.add(a);

        JTextField txtA = new JTextField();
        txtA.setBounds(100, 30, 150, 30);
        frame.add(txtA);

        JLabel b = new JLabel("Nhập số b:");
        b.setBounds(30, 70, 80, 30);
        frame.add(b);

        JTextField txtB = new JTextField();
        txtB.setBounds(100, 70, 150, 30);
        frame.add(txtB);

        JButton cong = new JButton("+");
        cong.setBounds(30, 120, 60, 30);
        frame.add(cong);

        JButton tru = new JButton("-");
        tru.setBounds(100, 120, 60, 30);
        frame.add(tru);

        JButton nhan = new JButton("*");
        nhan.setBounds(170, 120, 60, 30);
        frame.add(nhan);

        JButton chia = new JButton("/");
        chia.setBounds(240, 120, 60, 30);
        frame.add(chia);

        cong.addActionListener(e -> {

            double x = Double.parseDouble(txtA.getText());
            double y = Double.parseDouble(txtB.getText());

            JOptionPane.showMessageDialog(frame,
                    "Kết quả = " + (x + y));
        });

        tru.addActionListener(e -> {

            double x = Double.parseDouble(txtA.getText());
            double y = Double.parseDouble(txtB.getText());

            JOptionPane.showMessageDialog(frame,
                    "Kết quả = " + (x - y));
        });

        nhan.addActionListener(e -> {

            double x = Double.parseDouble(txtA.getText());
            double y = Double.parseDouble(txtB.getText());

            JOptionPane.showMessageDialog(frame,
                    "Kết quả = " + (x * y));
        });

        chia.addActionListener(e -> {

            double x = Double.parseDouble(txtA.getText());
            double y = Double.parseDouble(txtB.getText());

            if (y == 0) {

                JOptionPane.showMessageDialog(frame,
                        "Không thể chia cho 0");

            } else {

                JOptionPane.showMessageDialog(frame,
                        "Kết quả = " + (x / y));
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}