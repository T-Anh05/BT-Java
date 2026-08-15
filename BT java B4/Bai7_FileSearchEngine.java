import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Bai7_FileSearchEngine extends JFrame {
    private JButton btnChooseFile;
    private JButton btnSearch;
    private JButton btnCancel;
    private JTextField txtKeyword;
    private JLabel lblFilePath;
    private JLabel lblResult;
    private JProgressBar progressBar;
    private JTextArea txtResult;
    private JScrollPane scrollPane;
    private File selectedFile;
    private SearchWorker worker;
    
    public Bai7_FileSearchEngine() {
        setTitle("Tìm kiếm từ khóa trong file");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Panel trên cùng
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Các thành phần
        btnChooseFile = new JButton("Chọn file");
        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setEnabled(false);
        btnCancel = new JButton("Hủy");
        btnCancel.setEnabled(false);
        txtKeyword = new JTextField(20);
        lblFilePath = new JLabel("Chưa chọn file");
        lblFilePath.setFont(new Font("Arial", Font.PLAIN, 12));
        lblResult = new JLabel("Kết quả: ");
        lblResult.setFont(new Font("Arial", Font.BOLD, 14));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(400, 20));
        
        // Thêm vào panel trên
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        topPanel.add(btnChooseFile, gbc);
        
        gbc.gridx = 1;
        topPanel.add(new JLabel("Từ khóa:"), gbc);
        
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(txtKeyword, gbc);
        
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.NONE;
        topPanel.add(btnSearch, gbc);
        
        gbc.gridx = 4;
        topPanel.add(btnCancel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(new JLabel("Đường dẫn: "), gbc);
        
        gbc.gridy = 2;
        topPanel.add(lblFilePath, gbc);
        
        gbc.gridy = 3;
        topPanel.add(progressBar, gbc);
        
        gbc.gridy = 4;
        topPanel.add(lblResult, gbc);
        
        // Panel giữa - kết quả
        txtResult = new JTextArea();
        txtResult.setEditable(false);
        txtResult.setFont(new Font("Monospaced", Font.PLAIN, 13));
        scrollPane = new JScrollPane(txtResult);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Kết quả tìm kiếm"));
        
        // Thêm vào frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Xử lý sự kiện
        btnChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile();
            }
        });
        
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSearch();
            }
        });
        
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelSearch();
            }
        });
        
        txtKeyword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnSearch.isEnabled()) {
                    startSearch();
                }
            }
        });
        
        setVisible(true);
    }
    
    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file văn bản");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "File văn bản (*.txt)", "txt"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            lblFilePath.setText(selectedFile.getAbsolutePath());
            lblFilePath.setForeground(Color.BLACK);
            btnSearch.setEnabled(true);
            txtResult.setText("");
            lblResult.setText("Kết quả: ");
            progressBar.setValue(0);
        }
    }
    
    private void startSearch() {
        if (selectedFile == null || !selectedFile.exists()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn file hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String keyword = txtKeyword.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa cần tìm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
        }
        
        // Reset giao diện
        txtResult.setText("");
        progressBar.setValue(0);
        progressBar.setForeground(Color.BLUE);
        lblResult.setText("Đang tìm kiếm...");
        lblResult.setForeground(Color.BLUE);
        btnChooseFile.setEnabled(false);
        btnSearch.setEnabled(false);
        btnCancel.setEnabled(true);
        txtKeyword.setEnabled(false);
        
        worker = new SearchWorker(selectedFile, keyword);
        worker.execute();
    }
    
    private void cancelSearch() {
        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
            btnCancel.setEnabled(false);
            lblResult.setText("Đang hủy...");
            lblResult.setForeground(Color.ORANGE);
        }
    }
    
    private class SearchWorker extends SwingWorker<Void, String> {
        private File file;
        private String keyword;
        private int lineCount = 0;
        private int foundCount = 0;
        private long totalSize;
        private long processedSize = 0;
        private List<String> foundLines = new ArrayList<>();
        
        public SearchWorker(File file, String keyword) {
            this.file = file;
            this.keyword = keyword.toLowerCase();
            this.totalSize = file.length();
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                
                String line;
                int progress = 0;
                
                while ((line = reader.readLine()) != null && !isCancelled()) {
                    lineCount++;
                    processedSize += line.getBytes(StandardCharsets.UTF_8).length + 2;
                    
                    // Tìm kiếm không phân biệt hoa/thường
                    if (line.toLowerCase().contains(keyword)) {
                        foundCount++;
                        String resultLine = String.format("Dòng %d: %s", lineCount, line);
                        foundLines.add(resultLine);
                        
                        // Publish để hiển thị ngay
                        publish(resultLine);
                    }
                    
                    // Cập nhật tiến độ
                    progress = (int) ((double) processedSize / totalSize * 100);
                    if (progress % 5 == 0 || progress == 100) {
                        publish("PROGRESS:" + progress);
                    }
                }
            }
            
            return null;
        }
        
        @Override
        protected void process(List<String> chunks) {
            for (String chunk : chunks) {
                if (chunk.startsWith("PROGRESS:")) {
                    int progress = Integer.parseInt(chunk.substring(9));
                    progressBar.setValue(Math.min(progress, 100));
                    lblResult.setText(String.format("Đang tìm kiếm... Đã tìm thấy %d kết quả", foundCount));
                } else {
                    // Thêm dòng kết quả vào JTextArea
                    txtResult.append(chunk + "\n");
                }
            }
        }
        
        @Override
        protected void done() {
            btnChooseFile.setEnabled(true);
            btnSearch.setEnabled(true);
            btnCancel.setEnabled(false);
            txtKeyword.setEnabled(true);
            
            try {
                if (isCancelled()) {
                    lblResult.setText("Đã hủy tác vụ tìm kiếm");
                    lblResult.setForeground(Color.RED);
                    progressBar.setForeground(Color.RED);
                    return;
                }
                
                get(); // Kiểm tra lỗi
                
                progressBar.setValue(100);
                progressBar.setForeground(Color.GREEN);
                lblResult.setText(String.format("Tìm thấy %d dòng chứa từ khóa '%s'", foundCount, txtKeyword.getText()));
                lblResult.setForeground(Color.BLACK);
                
                if (foundCount == 0) {
                    txtResult.setText("Không tìm thấy dòng nào chứa từ khóa '" + txtKeyword.getText() + "'");
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                lblResult.setText("Lỗi: " + e.getMessage());
                lblResult.setForeground(Color.RED);
                JOptionPane.showMessageDialog(Bai7_FileSearchEngine.this, 
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
                new Bai7_FileSearchEngine();
            }
        });
    }
}