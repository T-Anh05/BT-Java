import javax.swing.*;

public class Bai1 {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Bài 1");
        frame.setSize(350, 200);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Nhập tên:");
        label.setBounds(30, 30, 80, 30);
        frame.add(label);

        JTextField text = new JTextField();
        text.setBounds(100, 30, 150, 30);
        frame.add(text);

        JButton button = new JButton("Chào");
        button.setBounds(120, 80, 80, 30);
        frame.add(button);

        button.addActionListener(e -> {
            String ten = text.getText();

            JOptionPane.showMessageDialog(frame,
                    "Xin chào " + ten);
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}