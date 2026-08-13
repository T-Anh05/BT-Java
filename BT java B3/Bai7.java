import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bai7 extends JFrame {
    private JTextField txtDisplay;
    private JTextArea txtHistory;
    private double firstOperand = 0;
    private String operator = "";
    private boolean startNewInput = true;

    public Bai7() {
        setTitle("Máy tính mini");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        txtDisplay = new JTextField();
        txtDisplay.setEditable(false);
        txtDisplay.setFont(new Font("Arial", Font.BOLD, 24));
        txtDisplay.setHorizontalAlignment(JTextField.RIGHT);
        add(txtDisplay, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "C", "0", "=", "+"
        };

        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.BOLD, 20));
            btn.addActionListener(new ButtonClickListener());
            btnPanel.add(btn);
        }
        add(btnPanel, BorderLayout.CENTER);

        txtHistory = new JTextArea();
        txtHistory.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtHistory);
        scrollPane.setPreferredSize(new Dimension(150, 0));
        add(scrollPane, BorderLayout.EAST);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if ("0123456789".contains(command)) {
                if (startNewInput) {
                    txtDisplay.setText(command);
                    startNewInput = false;
                } else {
                    txtDisplay.setText(txtDisplay.getText() + command);
                }
            } else if ("+-*/".contains(command)) {
                try {
                    firstOperand = Double.parseDouble(txtDisplay.getText());
                    operator = command;
                    startNewInput = true;
                } catch (NumberFormatException ex) {
                    // Ignore if display is empty
                }
            } else if ("=".equals(command)) {
                try {
                    double secondOperand = Double.parseDouble(txtDisplay.getText());
                    double result = 0;
                    boolean valid = true;

                    switch (operator) {
                        case "+": result = firstOperand + secondOperand; break;
                        case "-": result = firstOperand - secondOperand; break;
                        case "*": result = firstOperand * secondOperand; break;
                        case "/": 
                            if (secondOperand == 0) {
                                JOptionPane.showMessageDialog(Bai7.this, "Không thể chia cho 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                valid = false;
                            } else {
                                result = firstOperand / secondOperand;
                            }
                            break;
                    }

                    if (valid) {
                        txtDisplay.setText(String.valueOf(result));
                        txtHistory.append(firstOperand + " " + operator + " " + secondOperand + " = " + result + "\n");
                    }
                    startNewInput = true;
                    operator = "";
                } catch (NumberFormatException ex) {
                     // Ignore
                }
            } else if ("C".equals(command)) {
                txtDisplay.setText("");
                firstOperand = 0;
                operator = "";
                startNewInput = true;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Bai7().setVisible(true));
    }
}
