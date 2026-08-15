import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Bai6_DataLoaderWithCancel extends JFrame {
    private JButton btnLoad;
    private JButton btnCancel;
    private JProgressBar progressBar;
    private JLabel lblStatus;
    private LoadWorker worker;
    
    public Bai6_DataLoaderWithCancel() {
        setTitle("Mô phỏng tải dữ liệu (Có hủy)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(500, 200);
        setLocationRelativeTo(null);
        
        // Tạo các thành phần giao diện
        btnLoad = new JButton("Tải dữ liệu");
        btnLoad.setPreferredSize(new Dimension(130, 30));
        
        btnCancel = new JButton("Hủy");
        btnCancel.setPreferredSize(new Dimension(100, 30));
        btnCancel.setEnabled(false);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(350, 25));
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        
        lblStatus = new JLabel("Sẵn sàng");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 14));
        lblStatus.setPreferredSize(new Dimension(350, 30));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Thêm các thành phần vào frame
        add(btnLoad);
        add(btnCancel);
        add(progressBar);
        add(lblStatus);
        
        // Xử lý sự kiện
        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startLoading();
            }
        });
        
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelLoading();
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
        btnCancel.setEnabled(true);
        lblStatus.setText("Đang tải...");
        lblStatus.setForeground(Color.BLUE);
        
        // Tạo và thực thi SwingWorker
        worker = new LoadWorker();
        worker.execute();
    }
    
    private void cancelLoading() {
        if (worker != null && !worker.isDone()) {
            // Hủy tác vụ đang chạy
            worker.cancel(true);
            btnCancel.setEnabled(false);
            lblStatus.setText("Đang hủy...");
            lblStatus.setForeground(Color.ORANGE);
        }
    }
    
    private class LoadWorker extends SwingWorker<Void, Integer> {
        private static final int TOTAL_STEPS = 100;
        private int currentProgress = 0;
        
        @Override
        protected Void doInBackground() throws Exception {
            while (currentProgress < TOTAL_STEPS && !isCancelled()) {
                currentProgress++;
                publish(currentProgress);
                Thread.sleep(100);
            }
            return null;
        }
        
        @Override
        protected void process(List<Integer> chunks) {
            int latestProgress = chunks.get(chunks.size() - 1);
            progressBar.setValue(latestProgress);
            
            if (latestProgress < 30) {
                lblStatus.setText("Đang kết nối đến máy chủ...");
                lblStatus.setForeground(Color.ORANGE);
            } else if (latestProgress < 60) {
                lblStatus.setText("Đang tải dữ liệu...");
                lblStatus.setForeground(Color.BLUE);
            } else if (latestProgress < 90) {
                lblStatus.setText("Đang xử lý dữ liệu...");
                lblStatus.setForeground(new Color(0, 100, 200));
            } else {
                lblStatus.setText("Gần hoàn tất...");
                lblStatus.setForeground(new Color(0, 150, 0));
            }
        }
        
        @Override
        protected void done() {
            btnLoad.setEnabled(true);
            btnCancel.setEnabled(false);
            
            try {
                if (isCancelled()) {
                    lblStatus.setText("Đã hủy tác vụ");
                    lblStatus.setForeground(Color.RED);
                    progressBar.setForeground(Color.RED);
                    progressBar.setValue(progressBar.getValue());
                    return;
                }
                
                get(); // Kiểm tra lỗi
                progressBar.setValue(100);
                progressBar.setForeground(Color.GREEN);
                lblStatus.setText("Tải dữ liệu hoàn tất!");
                lblStatus.setForeground(Color.GREEN);
                
                JOptionPane.showMessageDialog(Bai6_DataLoaderWithCancel.this, 
                    "Tải dữ liệu thành công!", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                e.printStackTrace();
                lblStatus.setText("Lỗi: " + e.getMessage());
                lblStatus.setForeground(Color.RED);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Bai6_DataLoaderWithCancel();
            }
        });
    }
}