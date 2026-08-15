import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Bai3_PrimeSumCalculator extends JFrame {
    private JTextField txtN;
    private JButton btnCalculate;
    private JLabel lblResult;
    private JProgressBar progressBar;
    private JLabel lblStatus;
    private PrimeSumWorker worker;
    
    public Bai3_PrimeSumCalculator() {
        setTitle("Tính tổng số nguyên tố nhỏ hơn N");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        setSize(500, 280);
        setLocationRelativeTo(null);
        
        // Khởi tạo các thành phần
        txtN = new JTextField(15);
        btnCalculate = new JButton("Tính");
        btnCalculate.setPreferredSize(new Dimension(100, 30));
        
        lblResult = new JLabel("Kết quả: ");
        lblResult.setFont(new Font("Arial", Font.BOLD, 16));
        lblResult.setPreferredSize(new Dimension(400, 30));
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(350, 25));
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        
        lblStatus = new JLabel("Sẵn sàng");
        lblStatus.setFont(new Font("Arial", Font.ITALIC, 14));
        lblStatus.setPreferredSize(new Dimension(350, 25));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Thêm các thành phần vào frame
        // Dòng 1: Nhập N
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Nhập N:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(txtN, gbc);
        
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(btnCalculate, gbc);
        
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
        
        // Xử lý sự kiện cho nút Tính
        btnCalculate.addActionListener(new ActionListener() {
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
            if (n <= 2) {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng nhập N > 2!", 
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
            btnCalculate.setEnabled(false);
            txtN.setEnabled(false);
            
            // Tạo và thực thi SwingWorker
            worker = new PrimeSumWorker(n);
            worker.execute();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập một số nguyên hợp lệ!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Hàm kiểm tra số nguyên tố
    private boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
    
    // Lớp SwingWorker để tính tổng số nguyên tố
    private class PrimeSumWorker extends SwingWorker<Long, Integer> {
        private int n;
        private long sum = 0;
        private int count = 0;
        private int totalNumbers;
        
        public PrimeSumWorker(int n) {
            this.n = n;
            this.totalNumbers = n - 2; // Số lượng số cần kiểm tra từ 2 đến n-1
        }
        
        @Override
        protected Long doInBackground() throws Exception {
            // Kiểm tra từ 2 đến n-1
            for (int i = 2; i < n && !isCancelled(); i++) {
                if (isPrime(i)) {
                    sum += i;
                    count++;
                }
                
                // Cập nhật tiến độ
                int progress = (int) ((double) (i - 1) / totalNumbers * 100);
                publish(progress, i, count);
                
                // Ngủ một chút để hiển thị tiến trình
                Thread.sleep(5);
            }
            return sum;
        }
        
        @Override
        protected void process(List<Integer> chunks) {
            // Lấy dữ liệu mới nhất
            int latestProgress = chunks.get(chunks.size() - 3);
            int currentNumber = chunks.get(chunks.size() - 2);
            int primeCount = chunks.get(chunks.size() - 1);
            
            // Cập nhật thanh tiến trình
            progressBar.setValue(latestProgress);
            
            // Cập nhật trạng thái
            String status = String.format("Đang kiểm tra số %d, đã tìm thấy %d số nguyên tố", 
                                        currentNumber, primeCount);
            lblStatus.setText(status);
            
            // Cập nhật kết quả tạm thời
            if (primeCount > 0) {
                lblResult.setText(String.format("Đã tìm thấy %d số nguyên tố, tổng tạm thời: %,d", 
                                              primeCount, sum));
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
            btnCalculate.setEnabled(true);
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
                Long result = get();
                
                // Hiển thị kết quả
                progressBar.setValue(100);
                progressBar.setForeground(Color.GREEN);
                lblResult.setText(String.format("Kết quả: Tổng = %,d (tìm thấy %d số nguyên tố)", 
                                              result, count));
                lblResult.setForeground(Color.BLACK);
                lblStatus.setText("Hoàn tất!");
                lblStatus.setForeground(Color.GREEN);
                
                // Hiển thị thông báo hoàn thành
                JOptionPane.showMessageDialog(Bai3_PrimeSumCalculator.this, 
                    String.format("Tính toán hoàn tất!\nTổng các số nguyên tố < %d là: %,d\nTìm thấy %d số nguyên tố", 
                                n, result, count),
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                e.printStackTrace();
                lblStatus.setText("Lỗi: " + e.getMessage());
                lblStatus.setForeground(Color.RED);
                progressBar.setForeground(Color.RED);
                JOptionPane.showMessageDialog(Bai3_PrimeSumCalculator.this, 
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
                new Bai3_PrimeSumCalculator();
            }
        });
    }
}