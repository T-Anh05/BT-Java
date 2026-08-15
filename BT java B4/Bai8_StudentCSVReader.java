import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

public class Bai8_StudentCSVReader extends JFrame {
    private JButton btnChooseFile;
    private JButton btnLoad;
    private JButton btnStats;
    private JLabel lblFilePath;
    private JProgressBar progressBar;
    private JLabel lblStatus;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea statsArea;
    private File selectedFile;
    private List<Student> students;
    private CSVWorker worker;
    
    public Bai8_StudentCSVReader() {
        setTitle("Đọc file CSV điểm sinh viên");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        // Panel trên cùng
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Các thành phần
        btnChooseFile = new JButton("Chọn file CSV");
        btnLoad = new JButton("Đọc dữ liệu");
        btnLoad.setEnabled(false);
        btnStats = new JButton("Thống kê");
        btnStats.setEnabled(false);
        lblFilePath = new JLabel("Chưa chọn file");
        lblFilePath.setFont(new Font("Arial", Font.PLAIN, 12));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(400, 20));
        lblStatus = new JLabel("Sẵn sàng");
        lblStatus.setFont(new Font("Arial", Font.ITALIC, 12));
        
        // Thêm vào panel trên
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        topPanel.add(btnChooseFile, gbc);
        
        gbc.gridx = 1;
        topPanel.add(btnLoad, gbc);
        
        gbc.gridx = 2;
        topPanel.add(btnStats, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(new JLabel("Đường dẫn: "), gbc);
        
        gbc.gridy = 2;
        topPanel.add(lblFilePath, gbc);
        
        gbc.gridy = 3;
        topPanel.add(progressBar, gbc);
        
        gbc.gridy = 4;
        topPanel.add(lblStatus, gbc);
        
        // Panel giữa - Table và Stats
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(400);
        
        // Bảng dữ liệu
        String[] columns = {"Mã SV", "Họ Tên", "Điểm"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Danh sách sinh viên"));
        
        // Khu vực thống kê
        statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane statsScroll = new JScrollPane(statsArea);
        statsScroll.setBorder(BorderFactory.createTitledBorder("Thống kê"));
        
        splitPane.setTopComponent(tableScroll);
        splitPane.setBottomComponent(statsScroll);
        
        // Thêm vào frame
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        
        // Xử lý sự kiện
        btnChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile();
            }
        });
        
        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCSV();
            }
        });
        
        btnStats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStatistics();
            }
        });
        
        setVisible(true);
    }
    
    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file CSV");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "File CSV (*.csv)", "csv"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            lblFilePath.setText(selectedFile.getAbsolutePath());
            lblFilePath.setForeground(Color.BLACK);
            btnLoad.setEnabled(true);
            btnStats.setEnabled(false);
            tableModel.setRowCount(0);
            statsArea.setText("");
            progressBar.setValue(0);
            lblStatus.setText("Đã chọn file: " + selectedFile.getName());
        }
    }
    
    private void loadCSV() {
        if (selectedFile == null || !selectedFile.exists()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn file hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
        }
        
        // Reset giao diện
        tableModel.setRowCount(0);
        statsArea.setText("");
        progressBar.setValue(0);
        progressBar.setForeground(Color.BLUE);
        lblStatus.setText("Đang đọc file...");
        lblStatus.setForeground(Color.BLUE);
        btnChooseFile.setEnabled(false);
        btnLoad.setEnabled(false);
        btnStats.setEnabled(false);
        
        worker = new CSVWorker(selectedFile);
        worker.execute();
    }
    
    private void showStatistics() {
        if (students == null || students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để thống kê!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        StringBuilder stats = new StringBuilder();
        stats.append("========== THỐNG KÊ ĐIỂM SINH VIÊN ==========\n\n");
        
        // Số lượng sinh viên
        stats.append("Tổng số sinh viên: ").append(students.size()).append("\n\n");
        
        // Thống kê điểm
        DoubleSummaryStatistics statsSummary = students.stream()
            .mapToDouble(Student::getScore)
            .summaryStatistics();
        
        stats.append("Điểm trung bình: ").append(String.format("%.2f", statsSummary.getAverage())).append("\n");
        stats.append("Điểm cao nhất: ").append(String.format("%.2f", statsSummary.getMax())).append("\n");
        stats.append("Điểm thấp nhất: ").append(String.format("%.2f", statsSummary.getMin())).append("\n\n");
        
        // Phân loại theo điểm
        long excellent = students.stream().filter(s -> s.getScore() >= 8.5).count();
        long good = students.stream().filter(s -> s.getScore() >= 7.0 && s.getScore() < 8.5).count();
        long average = students.stream().filter(s -> s.getScore() >= 5.0 && s.getScore() < 7.0).count();
        long poor = students.stream().filter(s -> s.getScore() < 5.0).count();
        
        stats.append("PHÂN LOẠI THEO ĐIỂM:\n");
        stats.append("  - Xuất sắc (>= 8.5): ").append(excellent).append(" sinh viên\n");
        stats.append("  - Giỏi (7.0 - 8.4): ").append(good).append(" sinh viên\n");
        stats.append("  - Trung bình (5.0 - 6.9): ").append(average).append(" sinh viên\n");
        stats.append("  - Yếu (< 5.0): ").append(poor).append(" sinh viên\n\n");
        
        // Top 5 sinh viên có điểm cao nhất
        stats.append("TOP 5 SINH VIÊN CÓ ĐIỂM CAO NHẤT:\n");
        students.stream()
            .sorted((s1, s2) -> Double.compare(s2.getScore(), s1.getScore()))
            .limit(5)
            .forEach(s -> stats.append("  - ").append(s.getStudentId())
                              .append(" | ").append(s.getFullName())
                              .append(" | ").append(String.format("%.2f", s.getScore()))
                              .append("\n"));
        
        statsArea.setText(stats.toString());
        btnStats.setEnabled(true);
    }
    
    // Lớp Student
    private static class Student {
        private String studentId;
        private String fullName;
        private double score;
        
        public Student(String studentId, String fullName, double score) {
            this.studentId = studentId;
            this.fullName = fullName;
            this.score = score;
        }
        
        public String getStudentId() { return studentId; }
        public String getFullName() { return fullName; }
        public double getScore() { return score; }
    }
    
    // SwingWorker
    private class CSVWorker extends SwingWorker<Void, Object[]> {
        private File file;
        private List<Student> loadedStudents = new ArrayList<>();
        
        public CSVWorker(File file) {
            this.file = file;
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                
                String line;
                int lineNumber = 0;
                int totalLines = 0;
                
                // Đếm tổng số dòng để tính tiến độ
                try (BufferedReader countReader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                    while (countReader.readLine() != null) {
                        totalLines++;
                    }
                }
                
                while ((line = reader.readLine()) != null && !isCancelled()) {
                    lineNumber++;
                    
                    // Bỏ qua header
                    if (lineNumber == 1) {
                        publish(new Object[]{"PROGRESS", 0});
                        continue;
                    }
                    
                    // Parse dòng CSV
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        try {
                            String studentId = parts[0].trim();
                            String fullName = parts[1].trim();
                            double score = Double.parseDouble(parts[2].trim());
                            
                            Student student = new Student(studentId, fullName, score);
                            loadedStudents.add(student);
                            
                            // Thêm vào bảng
                            publish(new Object[]{studentId, fullName, score});
                            
                        } catch (NumberFormatException e) {
                            // Bỏ qua dòng lỗi
                        }
                    }
                    
                    // Cập nhật tiến độ
                    int progress = (int) ((double) lineNumber / totalLines * 100);
                    publish(new Object[]{"PROGRESS", progress});
                }
            }
            
            return null;
        }
        
        @Override
        protected void process(List<Object[]> chunks) {
            for (Object[] chunk : chunks) {
                if (chunk[0].equals("PROGRESS")) {
                    int progress = (int) chunk[1];
                    progressBar.setValue(progress);
                    lblStatus.setText("Đang đọc dữ liệu... " + progress + "%");
                } else {
                    // Thêm dòng vào bảng
                    tableModel.addRow(chunk);
                }
            }
        }
        
        @Override
        protected void done() {
            btnChooseFile.setEnabled(true);
            btnLoad.setEnabled(true);
            
            try {
                if (isCancelled()) {
                    lblStatus.setText("Đã hủy đọc file");
                    lblStatus.setForeground(Color.RED);
                    progressBar.setForeground(Color.RED);
                    return;
                }
                
                get(); // Kiểm tra lỗi
                
                students = loadedStudents;
                progressBar.setValue(100);
                progressBar.setForeground(Color.GREEN);
                lblStatus.setText(String.format("Đọc thành công %d sinh viên", students.size()));
                lblStatus.setForeground(Color.GREEN);
                
                btnStats.setEnabled(true);
                
                JOptionPane.showMessageDialog(Bai8_StudentCSVReader.this, 
                    String.format("Đọc dữ liệu thành công!\nSố sinh viên: %d", students.size()),
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                e.printStackTrace();
                lblStatus.setText("Lỗi: " + e.getMessage());
                lblStatus.setForeground(Color.RED);
                JOptionPane.showMessageDialog(Bai8_StudentCSVReader.this, 
                    "Đã xảy ra lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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
                new Bai8_StudentCSVReader();
            }
        });
    }
}