import java.awt.*;
import javax.swing.*;

public class LoginUI extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblStatus;

    public LoginUI() {

        setTitle("ĐĂNG NHẬP - QUẢN LÝ GIÀY HUY TOÀN");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Panel chính
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        // Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ GIÀY HUY TOÀN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBounds(80, 25, 300, 35);
        panel.add(lblTitle);

        JLabel lblLogin = new JLabel("ĐĂNG NHẬP");
        lblLogin.setFont(new Font("Arial", Font.BOLD, 18));
        lblLogin.setBounds(165, 65, 150, 30);
        panel.add(lblLogin);

        // Tên đăng nhập
        JLabel lblUsername = new JLabel("Tài khoản:");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsername.setBounds(60, 115, 100, 25);
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(160, 115, 220, 30);
        panel.add(txtUsername);

        // Mật khẩu
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPassword.setBounds(60, 160, 100, 25);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(160, 160, 220, 30);
        panel.add(txtPassword);

        // Nút đăng nhập
        JButton btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setBounds(80, 210, 130, 35);
        btnLogin.setBackground(new Color(50, 150, 250));
        btnLogin.setForeground(Color.BLACK);
        panel.add(btnLogin);

        // Nút thoát
        JButton btnExit = new JButton("THOÁT");
        btnExit.setBounds(230, 210, 130, 35);
        btnExit.setBackground(new Color(220, 80, 80));
        btnExit.setForeground(Color.BLACK);
        panel.add(btnExit);

        // Thông báo
        lblStatus = new JLabel("");
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setBounds(50, 260, 350, 30);
        panel.add(lblStatus);

        add(panel);

        // Sự kiện đăng nhập
        btnLogin.addActionListener(e -> login());

        // Sự kiện thoát
        btnExit.addActionListener(e -> System.exit(0));

        // Nhấn Enter để đăng nhập
        txtPassword.addActionListener(e -> login());
    }

    private void login() {

        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        // Tài khoản và mật khẩu
        if (username.equals("admin") && password.equals("HuyToan123")) {

            lblStatus.setText("Đăng nhập thành công!");
            lblStatus.setForeground(new Color(0, 150, 0));

            JOptionPane.showMessageDialog(
                    this,
                    "Chào mừng " + username + "!"
            );

            // Đóng form đăng nhập
            dispose();

        } else {

            lblStatus.setText("Sai tài khoản hoặc mật khẩu!");
            lblStatus.setForeground(Color.RED);

            txtPassword.setText("");
        }
    }

    public static void main(String[] args) {

        new LoginUI().setVisible(true);
    }
}