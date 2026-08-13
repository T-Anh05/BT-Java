import javax.swing.*;

public class Bai3 {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Phương trình bậc nhất ax + b = 0");
        frame.setSize(400, 250);
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

        JButton button = new JButton("Giải");
        button.setBounds(120, 120, 80, 30);
        frame.add(button);

        button.addActionListener(e -> {

            double a1 = Double.parseDouble(txtA.getText());
            double b1 = Double.parseDouble(txtB.getText());

            if (a1 == 0 && b1 == 0) {

                JOptionPane.showMessageDialog(frame,
                        "Vô số nghiệm");

            } else if (a1 == 0) {

                JOptionPane.showMessageDialog(frame,
                        "Vô nghiệm");

            } else {

                double x = -b1 / a1;

                JOptionPane.showMessageDialog(frame,
                        "x = " + x);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}