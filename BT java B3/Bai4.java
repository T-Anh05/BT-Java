import javax.swing.*;

public class Bai4 {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Phân loại tam giác");
        frame.setSize(350, 300);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel a = new JLabel("Cạnh a:");
        a.setBounds(30, 20, 80, 30);
        frame.add(a);

        JTextField txtA = new JTextField();
        txtA.setBounds(100, 20, 150, 30);
        frame.add(txtA);

        JLabel b = new JLabel("Cạnh b:");
        b.setBounds(30, 60, 80, 30);
        frame.add(b);

        JTextField txtB = new JTextField();
        txtB.setBounds(100, 60, 150, 30);
        frame.add(txtB);

        JLabel c = new JLabel("Cạnh c:");
        c.setBounds(30, 100, 80, 30);
        frame.add(c);

        JTextField txtC = new JTextField();
        txtC.setBounds(100, 100, 150, 30);
        frame.add(txtC);

        JButton button = new JButton("Kiểm tra");
        button.setBounds(110, 150, 100, 30);
        frame.add(button);

        button.addActionListener(e -> {

            double x = Double.parseDouble(txtA.getText());
            double y = Double.parseDouble(txtB.getText());
            double z = Double.parseDouble(txtC.getText());

            if (x + y <= z ||
                x + z <= y ||
                y + z <= x) {

                JOptionPane.showMessageDialog(frame,
                        "Không phải tam giác");

            } else if (x == y && y == z) {

                JOptionPane.showMessageDialog(frame,
                        "Tam giác đều");

            } else if (x == y ||
                       x == z ||
                       y == z) {

                JOptionPane.showMessageDialog(frame,
                        "Tam giác cân");

            } else {

                JOptionPane.showMessageDialog(frame,
                        "Tam giác thường");
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}