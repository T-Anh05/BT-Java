import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Bai1_CountdownTimer extends JFrame {
    private JTextField txtSeconds;
    private JButton btnStart;
    private JLabel lblTimeRemaining;
    private CountdownWorker worker;
    
    public Bai1_CountdownTimer() {
        setTitle("Đồng hồ đếm ngược");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(400, 150);
        setLocationRelativeTo(null);
        
        // Khởi tạo các thành phần
        txtSeconds = new JTextField(10);
        btnStart = new JButton("Bắt đầu");
        lblTimeRemaining = new JLabel("00:00:00");
        lblTimeRemaining.setFont(new Font("Arial", Font.BOLD, 24));
        lblTimeRemaining.setPreferredSize(new Dimension(150, 40));
        lblTimeRemaining.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Thêm các thành phần vào frame
        add(new JLabel("Nhập số giây:"));
        add(txtSeconds);
        add(btnStart);
        add(lblTimeRemaining);
        
        // Xử lý sự kiện cho nút Bắt đầu
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startCountdown();
            }
        });
        
        setVisible(true);
    }
    
    private void startCountdown() {
        // Kiểm tra và hủy worker cũ nếu đang chạy
        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
        }
        
        try {
            int seconds = Integer.parseInt(txtSeconds.getText().trim());
            if (seconds <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng nhập số giây lớn hơn 0!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Vô hiệu hóa nút và textfield trong khi đếm
            btnStart.setEnabled(false);
            txtSeconds.setEnabled(false);
            
            // Tạo và thực thi SwingWorker
            worker = new CountdownWorker(seconds);
            worker.execute();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập một số nguyên hợp lệ!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Lớp SwingWorker để thực hiện đếm ngược trong nền
    private class CountdownWorker extends SwingWorker<Void, String> {
        private int remainingSeconds;
        private int totalSeconds;
        
        public CountdownWorker(int seconds) {
            this.remainingSeconds = seconds;
            this.totalSeconds = seconds;
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            while (remainingSeconds >= 0 && !isCancelled()) {
                // Tạo chuỗi thời gian định dạng HH:MM:SS
                int hours = remainingSeconds / 3600;
                int minutes = (remainingSeconds % 3600) / 60;
                int secs = remainingSeconds % 60;
                String timeString = String.format("%02d:%02d:%02d", hours, minutes, secs);
                
                // Cập nhật giao diện qua publish()
                publish(timeString);
                
                if (remainingSeconds == 0) {
                    break;
                }
                
                // Ngủ 1 giây
                Thread.sleep(1000);
                remainingSeconds--;
            }
            return null;
        }
        
        @Override
        protected void process(List<String> chunks) {
            // Cập nhật JLabel với giá trị mới nhất
            String latestTime = chunks.get(chunks.size() - 1);
            lblTimeRemaining.setText(latestTime);
            
            // Đổi màu khi còn ít thời gian
            if (remainingSeconds <= 10 && remainingSeconds > 0) {
                lblTimeRemaining.setForeground(Color.RED);
            } else {
                lblTimeRemaining.setForeground(Color.BLACK);
            }
        }
        
        @Override
        protected void done() {
            // Kích hoạt lại các thành phần giao diện
            btnStart.setEnabled(true);
            txtSeconds.setEnabled(true);
            
            try {
                // Kiểm tra xem có bị hủy không
                if (isCancelled()) {
                    lblTimeRemaining.setText("Đã hủy");
                    return;
                }
                
                // Kiểm tra xem có hoàn thành thành công không
                get(); // Sẽ throw exception nếu có lỗi
                
                if (remainingSeconds == 0) {
                    lblTimeRemaining.setText("00:00:00");
                    lblTimeRemaining.setForeground(Color.GREEN);
                    JOptionPane.showMessageDialog(Bai1_CountdownTimer.this, 
                        "Hết giờ!", 
                        "Thông báo", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(Bai1_CountdownTimer.this, 
                    "Đã xảy ra lỗi: " + e.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void main(String[] args) {
        // Chạy giao diện trên Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Bai1_CountdownTimer();
            }
        });
    }
}