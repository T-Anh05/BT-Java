import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Bai2_DataLoaderSimulation extends JFrame {
    private JButton btnLoad;
    private JProgressBar progressBar;
    private JLabel lblStatus;
    private LoadWorker worker;
    
    public Bai2_DataLoaderSimulation() {
        setTitle("Mô phỏng tải dữ liệu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(450, 180);
        setLocationRelativeTo(null);
        
        // Tạo các thành phần giao diện
        btnLoad = new JButton("Tải dữ liệu");
        btnLoad.setPreferredSize(new Dimension(120, 30));
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(300, 25));
        progressBar.setStringPainted(true); // Hiển thị phần trăm trên thanh
        progressBar.setValue(0);
        
        lblStatus = new JLabel("Sẵn sàng");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 14));
        lblStatus.setPreferredSize(new Dimension(300, 30));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Thêm các thành phần vào frame
        add(btnLoad);
        add(progressBar);
        add(lblStatus);
        
        // Xử lý sự kiện cho nút Tải dữ liệu
        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startLoading();
            }
        });
        
        setVisible(true);
    }
    
    private void startLoading() {
        // Kiểm tra và hủy worker cũ nếu đang chạy
        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
        }
        
        // Reset giao diện
        progressBar.setValue(0);
        progressBar.setForeground(Color.BLUE);
        btnLoad.setEnabled(false);
        lblStatus.setText("Đang tải...");
        lblStatus.setForeground(Color.BLUE);
        
        // Tạo và thực thi SwingWorker
        worker = new LoadWorker();
        worker.execute();
    }
    
    // Lớp SwingWorker để mô phỏng tải dữ liệu
    private class LoadWorker extends SwingWorker<Void, Integer> {
        private static final int TOTAL_STEPS = 100;
        private int currentProgress = 0;
        
        @Override
        protected Void doInBackground() throws Exception {
            // Mô phỏng tải dữ liệu trong 10 giây
            while (currentProgress < TOTAL_STEPS && !isCancelled()) {
                // Tăng tiến độ lên 1%
                currentProgress++;
                
                // Publish tiến độ hiện tại
                publish(currentProgress);
                
                // Ngủ 100ms để mô phỏng tải dữ liệu (10 giây cho 100%)
                Thread.sleep(100);
            }
            return null;
        }
        
        @Override
        protected void process(List<Integer> chunks) {
            // Lấy giá trị tiến độ mới nhất
            int latestProgress = chunks.get(chunks.size() - 1);
            
            // Cập nhật JProgressBar
            progressBar.setValue(latestProgress);
            
            // Cập nhật trạng thái dựa trên tiến độ
            if (latestProgress < 30) {
                lblStatus.setText("Đang kết nối đến máy chủ...");
                lblStatus.setForeground(Color.ORANGE);
            } else if (latestProgress < 60) {
                lblStatus.setText("Đang tải dữ liệu...");
                lblStatus.setForeground(Color.BLUE);
            } else if (latestProgress < 90) {
                lblStatus.setText("Đang xử lý dữ liệu...");
                lblStatus.setForeground(new Color(0, 100, 200));
            } else if (latestProgress < 100) {
                lblStatus.setText("Gần hoàn tất...");
                lblStatus.setForeground(new Color(0, 150, 0));
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
            // Kích hoạt lại nút
            btnLoad.setEnabled(true);
            
            try {
                // Kiểm tra xem có bị hủy không
                if (isCancelled()) {
                    lblStatus.setText("Đã hủy tải");
                    lblStatus.setForeground(Color.RED);
                    progressBar.setForeground(Color.RED);
                    return;
                }
                
                // Kiểm tra xem có hoàn thành thành công không
                get(); // Sẽ throw exception nếu có lỗi
                
                // Hoàn thành thành công
                progressBar.setValue(100);
                progressBar.setForeground(Color.GREEN);
                lblStatus.setText("Tải dữ liệu hoàn tất!");
                lblStatus.setForeground(Color.GREEN);
                
                // Hiển thị thông báo hoàn thành
                JOptionPane.showMessageDialog(Bai2_DataLoaderSimulation.this, 
                    "Tải dữ liệu thành công!", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                e.printStackTrace();
                lblStatus.setText("Lỗi: " + e.getMessage());
                lblStatus.setForeground(Color.RED);
                progressBar.setForeground(Color.RED);
                JOptionPane.showMessageDialog(Bai2_DataLoaderSimulation.this, 
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
                new Bai2_DataLoaderSimulation();
            }
        });
    }
}