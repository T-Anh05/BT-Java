import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Bai5_FileLineCounter extends JFrame {
    private JButton btnChooseFile;
    private JButton btnCountLines;
    private JLabel lblFilePath;
    private JLabel lblLineCount;
    private JProgressBar progressBar;
    private JLabel lblStatus;
    private File selectedFile;
    private LineCounterWorker worker;
    
    public Bai5_FileLineCounter() {
        setTitle("Đếm số dòng trong file lớn");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        setSize(650, 280);
        setLocationRelativeTo(null);
        
        // Khởi tạo các thành phần
        btnChooseFile = new JButton("Chọn file");
        btnChooseFile.setPreferredSize(new Dimension(120, 30));
        
        btnCountLines = new JButton("Đếm dòng");
        btnCountLines.setPreferredSize(new Dimension(120, 30));
        btnCountLines.setEnabled(false);
        
        lblFilePath = new JLabel("Chưa chọn file");
        lblFilePath.setFont(new Font("Arial", Font.PLAIN, 12));
        lblFilePath.setPreferredSize(new Dimension(550, 25));
        lblFilePath.setForeground(Color.GRAY);
        
        lblLineCount = new JLabel("Số dòng: ");
        lblLineCount.setFont(new Font("Arial", Font.BOLD, 14));
        lblLineCount.setPreferredSize(new Dimension(550, 30));
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(550, 25));
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        
        lblStatus = new JLabel("Sẵn sàng");
        lblStatus.setFont(new Font("Arial", Font.ITALIC, 14));
        lblStatus.setPreferredSize(new Dimension(550, 25));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Thêm các thành phần vào frame
        // Dòng 1: Các nút
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(btnChooseFile, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(btnCountLines, gbc);
        
        // Dòng 2: Đường dẫn file
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Đường dẫn: "), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 8, 8, 8);
        add(lblFilePath, gbc);
        
        // Dòng 3: ProgressBar
        gbc.gridy = 3;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.CENTER;
        add(progressBar, gbc);
        
        // Dòng 4: Status
        gbc.gridy = 4;
        add(lblStatus, gbc);
        
        // Dòng 5: Kết quả
        gbc.gridy = 5;
        add(lblLineCount, gbc);
        
        // Xử lý sự kiện cho nút Chọn file
        btnChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile();
            }
        });
        
        // Xử lý sự kiện cho nút Đếm dòng
        btnCountLines.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startCounting();
            }
        });
        
        setVisible(true);
    }
    
    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file văn bản");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // Thiết lập bộ lọc file
        javax.swing.filechooser.FileNameExtensionFilter filter = 
            new javax.swing.filechooser.FileNameExtensionFilter(
                "File văn bản (*.txt, *.log, *.csv, *.java, *.xml, *.json)", 
                "txt", "log", "csv", "java", "xml", "json");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            
            // Hiển thị đường dẫn file
            lblFilePath.setText(selectedFile.getAbsolutePath());
            lblFilePath.setForeground(Color.BLACK);
            
            // Kích hoạt nút đếm dòng
            btnCountLines.setEnabled(true);
            
            // Reset kết quả cũ
            lblLineCount.setText("Số dòng: ");
            progressBar.setValue(0);
            lblStatus.setText("Đã chọn file: " + selectedFile.getName());
            lblStatus.setForeground(Color.BLUE);
            
            // Hiển thị kích thước file
            long fileSize = selectedFile.length();
            String sizeStr = formatFileSize(fileSize);
            lblStatus.setText(String.format("Đã chọn file: %s (kích thước: %s)", 
                                          selectedFile.getName(), sizeStr));
        }
    }
    
    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
        }
    }
    
    private void startCounting() {
        if (selectedFile == null || !selectedFile.exists()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn file hợp lệ!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra và hủy worker cũ nếu đang chạy
        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
        }
        
        // Reset giao diện
        progressBar.setValue(0);
        progressBar.setForeground(Color.BLUE);
        lblLineCount.setText("Đang đếm...");
        lblLineCount.setForeground(Color.BLUE);
        lblStatus.setText("Đang đọc file...");
        lblStatus.setForeground(Color.BLUE);
        btnChooseFile.setEnabled(false);
        btnCountLines.setEnabled(false);
        
        // Tạo và thực thi SwingWorker
        worker = new LineCounterWorker(selectedFile);
        worker.execute();
    }
    
    // Lớp SwingWorker để đếm số dòng
    private class LineCounterWorker extends SwingWorker<Long, Integer> {
        private File file;
        private long lineCount = 0;
        private long totalSize;
        private long processedSize = 0;
        
        public LineCounterWorker(File file) {
            this.file = file;
            this.totalSize = file.length();
        }
        
        @Override
        protected Long doInBackground() throws Exception {
            // Phương pháp 1: Sử dụng BufferedReader (hiệu quả cho file lớn)
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                
                String line;
                int progress = 0;
                long previousProgress = -1;
                
                // Đọc từng dòng
                while ((line = reader.readLine()) != null && !isCancelled()) {
                    lineCount++;
                    
                    // Cập nhật tiến độ dựa trên số byte đã đọc (ước lượng)
                    processedSize += line.getBytes(StandardCharsets.UTF_8).length + 2; // +2 cho newline
                    progress = (int) ((double) processedSize / totalSize * 100);
                    
                    // Chỉ publish khi tiến độ thay đổi đáng kể (giảm tải cho EDT)
                    if (progress != previousProgress && progress % 1 == 0) {
                        previousProgress = progress;
                        publish(progress);
                    }
                }
            } catch (IOException e) {
                // Nếu file quá lớn hoặc có lỗi, thử phương pháp khác
                try {
                    // Phương pháp 2: Sử dụng Files.lines() cho Java 8+
                    try (Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()))) {
                        lineCount = stream.count();
                    }
                } catch (Exception ex) {
                    throw new IOException("Không thể đọc file: " + ex.getMessage());
                }
            }
            
            // Đảm bảo progressBar đạt 100%
            publish(100);
            
            return lineCount;
        }
        
        @Override
        protected void process(List<Integer> chunks) {
            // Lấy tiến độ mới nhất
            int latestProgress = chunks.get(chunks.size() - 1);
            
            // Cập nhật thanh tiến trình
            progressBar.setValue(latestProgress);
            
            // Cập nhật trạng thái
            if (latestProgress < 100) {
                lblStatus.setText(String.format("Đang đọc file... %d%%", latestProgress));
                progressBar.setString(String.format("Đang xử lý... %d%%", latestProgress));
            } else {
                lblStatus.setText("Gần hoàn tất...");
                progressBar.setString("Hoàn tất đọc file!");
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
            btnChooseFile.setEnabled(true);
            btnCountLines.setEnabled(true);
            
            try {
                // Kiểm tra xem có bị hủy không
                if (isCancelled()) {
                    lblStatus.setText("Đã hủy đọc file");
                    lblStatus.setForeground(Color.RED);
                    progressBar.setForeground(Color.RED);
                    progressBar.setString("Đã hủy");
                    lblLineCount.setText("Số dòng: ");
                    return;
                }
                
                // Lấy kết quả
                Long result = get();
                
                // Hiển thị kết quả
                progressBar.setValue(100);
                progressBar.setForeground(Color.GREEN);
                progressBar.setString("Hoàn tất!");
                
                String formattedCount = String.format("%,d", result);
                lblLineCount.setText(String.format("Số dòng: %s", formattedCount));
                lblLineCount.setForeground(Color.BLACK);
                
                lblStatus.setText(String.format("Hoàn tất! Đã đếm %s dòng", formattedCount));
                lblStatus.setForeground(Color.GREEN);
                
                // Hiển thị thông báo hoàn thành
                JOptionPane.showMessageDialog(Bai5_FileLineCounter.this, 
                    String.format("Đếm dòng hoàn tất!\n\nFile: %s\nSố dòng: %s\nKích thước: %s", 
                                file.getName(), 
                                formattedCount,
                                formatFileSize(file.length())),
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                e.printStackTrace();
                lblStatus.setText("Lỗi: " + e.getMessage());
                lblStatus.setForeground(Color.RED);
                progressBar.setForeground(Color.RED);
                progressBar.setString("Lỗi!");
                JOptionPane.showMessageDialog(Bai5_FileLineCounter.this, 
                    "Đã xảy ra lỗi khi đọc file:\n" + e.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new Bai5_FileLineCounter();
            }
        });
    }
}