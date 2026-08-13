import javax.swing.*;

public class Bai6 {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Form đăng nhập cơ bản");
        frame.setSize(350, 280);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel user = new JLabel("Tài khoản:");
        user.setBounds(30, 30, 80, 30);
        frame.add(user);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(110, 30, 150, 30);
        frame.add(txtUser);

        JLabel pass = new JLabel("Mật khẩu:");
        pass.setBounds(30, 70, 80, 30);
        frame.add(pass);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(110, 70, 150, 30);
        frame.add(txtPass);

        JButton button = new JButton("Đăng nhập");
        button.setBounds(110, 120, 110, 30);
        frame.add(button);

        button.addActionListener(e -> {

            String taiKhoan = txtUser.getText();
            String matKhau =
                    new String(txtPass.getPassword());

            if (taiKhoan.equals("admin") &&
                matKhau.equals("123")) {

                JOptionPane.showMessageDialog(frame,
                        "Đăng nhập thành công");

            } else {

                JOptionPane.showMessageDialog(frame,
                        "Sai tài khoản hoặc mật khẩu");
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}