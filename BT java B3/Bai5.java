import javax.swing.*;

public class Bai5 {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Bài 5");
        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Nhập n:");
        label.setBounds(30, 30, 80, 30);
        frame.add(label);

        JTextField text = new JTextField();
        text.setBounds(100, 30, 120, 30);
        frame.add(text);

        JButton button = new JButton("Hiển thị");
        button.setBounds(240, 30, 80, 30);
        frame.add(button);

        JTextArea result = new JTextArea();
        result.setBounds(30, 80, 320, 120);
        frame.add(result);

        button.addActionListener(e -> {

            int n = Integer.parseInt(text.getText());

            int a = 0;
            int b = 1;

            result.setText("");

            for (int i = 0; i < n; i++) {

                result.append(a + " ");

                int c = a + b;
                a = b;
                b = c;
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}