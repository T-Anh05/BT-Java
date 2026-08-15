import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Bai4_FibonacciCalculator extends JFrame {
    private JTextField txtN;
    private JButton btnFind;
    private JLabel lblResult;
    private JProgressBar progressBar;
    private JLabel lblStatus;
    private FibonacciWorker worker;
    private Map<Integer, BigInteger> memoizationMap;
    
    public Bai4_FibonacciCalculator() {
        setTitle("Tìm số Fibonacci thứ N (Memoization)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        setSize(600, 300);
        setLocationRelativeTo(null);
        
        // Khởi tạo memoization map
        memoizationMap = new HashMap<>();
        memoizationMap.put(0, BigInteger.ZERO);
        memoizationMap.put(1, BigInteger.ONE);
        
        // Khởi tạo các thành phần
        txtN = new JTextField(15);
        btnFind = new JButton("Tìm");
        btnFind.setPreferredSize(new Dimension(100, 30));
        
        lblResult = new JLabel("Kết quả: ");
        lblResult.setFont(new Font("Arial", Font.BOLD, 14));
        lblResult.setPreferredSize(new Dimension(550, 40));
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(450, 25));
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        
        lblStatus = new JLabel("Sẵn sàng");
        lblStatus.setFont(new Font("Arial", Font.ITALIC, 14));
        lblStatus.setPreferredSize(new Dimension(450, 25));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Thêm các thành phần vào frame
        // Dòng 1: Nhập N
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Nhập N (>=0):"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(txtN, gbc);
        
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(btnFind, gbc);
        
        // Dòng 2: ProgressBar
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(progressBar, gbc);
        
        // Dòng 3: Status
        gbc.gridy = 2;
        add(lblStatus, gbc);
        
        // Dòng 4: Kết quả
        gbc.gridy = 3;
        add(lblResult, gbc);
        
        // Xử lý sự kiện cho nút Tìm
        btnFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startCalculation();
            }
        });
        
        // Xử lý phím Enter trên JTextField
        txtN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startCalculation();
            }
        });
        
        setVisible(true);
    }
    
    private void startCalculation() {
        // Kiểm tra và hủy worker cũ nếu đang chạy
        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
        }
        
        try {
            int n = Integer.parseInt(txtN.getText().trim());
            if (n < 0) {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng nhập N >= 0!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Reset giao diện
            progressBar.setValue(0);
            progressBar.setForeground(Color.BLUE);
            lblResult.setText("Đang tính toán...");
            lblResult.setForeground(Color.BLUE);
            lblStatus.setText("Đang xử lý...");
            lblStatus.setForeground(Color.BLUE);
            btnFind.setEnabled(false);
            txtN.setEnabled(false);
            
            // Tạo và thực thi SwingWorker
            worker = new FibonacciWorker(n);
            worker.execute();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập một số nguyên hợp lệ!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Hàm tính Fibonacci với memoization (sử dụng trong doInBackground)
    private BigInteger fibonacciWithMemo(int n) {
        // Kiểm tra cache
        if (memoizationMap.containsKey(n)) {
            return memoizationMap.get(n);
        }
        
        // Tính toán đệ quy với memoization
        BigInteger fibNMinus1 = fibonacciWithMemo(n - 1);
        BigInteger fibNMinus2 = fibonacciWithMemo(n - 2);
        BigInteger result = fibNMinus1.add(fibNMinus2);
        
        // Lưu vào cache
        memoizationMap.put(n, result);
        
        return result;
    }
    
    // Lớp SwingWorker để tính Fibonacci
    private class FibonacciWorker extends SwingWorker<BigInteger, Integer> {
        private int n;
        private int currentProgress = 0;
        
        public FibonacciWorker(int n) {
            this.n = n;
        }
        
        @Override
        protected BigInteger doInBackground() throws Exception {
            // Nếu N <= 1, trả về kết quả ngay
            if (n <= 1) {
                publish(100);
                return memoizationMap.get(n);
            }
            
            // Tính toán Fibonacci với memoization
            // Chúng ta cần tính từ 2 đến n
            for (int i = 2; i <= n && !isCancelled(); i++) {
                // Tính Fibonacci thứ i (có thể đã được tính từ trước)
                if (!memoizationMap.containsKey(i)) {
                    // Tính đệ quy có memoization
                    fibonacciWithMemo(i);
                }
                
                // Cập nhật tiến độ
                int progress = (int) ((double) i / n * 100);
                currentProgress = progress;
                publish(progress, i);
                
                // Ngủ một chút để hiển thị tiến trình
                Thread.sleep(50);
            }
            
            // Trả về kết quả
            return memoizationMap.get(n);
        }
        
        @Override
        protected void process(List<Integer> chunks) {
            // Lấy dữ liệu mới nhất
            int latestProgress = chunks.get(chunks.size() - 2);
            int currentN = chunks.get(chunks.size() - 1);
            
            // Cập nhật thanh tiến trình
            progressBar.setValue(latestProgress);
            
            // Cập nhật trạng thái
            String status = String.format("Đang tính Fibonacci(%d)...", currentN);
            lblStatus.setText(status);
            
            // Cập nhật kết quả tạm thời (hiển thị giá trị đã tính)
            if (memoizationMap.containsKey(currentN)) {
                BigInteger tempResult = memoizationMap.get(currentN);
                String displayResult = tempResult.toString();
                if (displayResult.length() > 40) {
                    displayResult = displayResult.substring(0, 37) + "...";
                }
                lblResult.setText(String.format("Fibonacci(%d) = %s", currentN, displayResult));
            }
            
            // Đổi màu thanh tiến trình theo tiến độ
            if (latestProgress < 30) {
                progressBar.setForeground(Color.ORANGE);
            } else if (latestProgress < 70) {
                progressBar.setForeground(Color.BLUE);
            } else {
                progressBar.setForeground(Color.GREEN);
            }
        }
        
        @Override
        protected void done() {
            // Kích hoạt lại các thành phần
            btnFind.setEnabled(true);
            txtN.setEnabled(true);
            
            try {
                // Kiểm tra xem có bị hủy không
                if (isCancelled()) {
                    lblStatus.setText("Đã hủy tính toán");
                    lblStatus.setForeground(Color.RED);
                    progressBar.setForeground(Color.RED);
                    lblResult.setText("Kết quả: ");
                    return;
                }
                
                // Lấy kết quả
                BigInteger result = get();
                
                // Hiển thị kết quả
                progressBar.setValue(100);
                progressBar.setForeground(Color.GREEN);
                
                // Định dạng kết quả để hiển thị
                String resultString = result.toString();
                String displayString = resultString;
                if (resultString.length() > 100) {
                    displayString = resultString.substring(0, 97) + "...";
                }
                
                lblResult.setText(String.format("Fibonacci(%d) = %s", n, displayString));
                lblResult.setForeground(Color.BLACK);
                lblStatus.setText(String.format("Hoàn tất! Kết quả có %d chữ số", resultString.length()));
                lblStatus.setForeground(Color.GREEN);
                
                // Hiển thị thông báo hoàn thành với chi tiết
                JOptionPane.showMessageDialog(Bai4_FibonacciCalculator.this, 
                    String.format("Tính toán hoàn tất!\nFibonacci(%d) = \n%s\n\nSố chữ số: %d", 
                                n, resultString, resultString.length()),
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                e.printStackTrace();
                lblStatus.setText("Lỗi: " + e.getMessage());
                lblStatus.setForeground(Color.RED);
                progressBar.setForeground(Color.RED);
                JOptionPane.showMessageDialog(Bai4_FibonacciCalculator.this, 
                    "Đã xảy ra lỗi: " + e.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Bai4_FibonacciCalculator();
            }
        });
    }
}