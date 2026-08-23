import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableCellRenderer;

// ============ CLASS SẢN PHẨM ============
class SanPham {
    String maHang;
    String tenHang;
    int soLuong;
    int donGia;
    
    // Constructor
    SanPham(String ma, String ten, int sl) {
        maHang = ma;
        tenHang = ten;
        soLuong = sl;
        
        // Tính đơn giá theo quy tắc
        if (soLuong < 10) {
            donGia = 10000;
        } else {
            donGia = 5000;
        }
    }
    
    // Tính tiền hàng
    int tinhTien() {
        return soLuong * donGia;
    }
}

// ============ CLASS QUẢN LÝ KHO ============
class QuanLyKho {
    ArrayList<SanPham> danhSach = new ArrayList<SanPham>();
    
    // Thêm sản phẩm
    boolean them(SanPham sp) {
        // Kiểm tra mã trùng
        for (SanPham s : danhSach) {
            if (s.maHang.equalsIgnoreCase(sp.maHang)) {
                return false; // Trùng mã
            }
        }
        danhSach.add(sp);
        return true;
    }
    
    // Sửa sản phẩm
    boolean sua(String maCu, SanPham spMoi) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).maHang.equalsIgnoreCase(maCu)) {
                // Kiểm tra mã mới trùng với sản phẩm khác không
                for (int j = 0; j < danhSach.size(); j++) {
                    if (j != i && danhSach.get(j).maHang.equalsIgnoreCase(spMoi.maHang)) {
                        return false; // Trùng mã với sản phẩm khác
                    }
                }
                danhSach.set(i, spMoi);
                return true;
            }
        }
        return false; // Không tìm thấy mã cũ
    }
    
    // Xóa sản phẩm
    boolean xoa(String ma) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).maHang.equalsIgnoreCase(ma)) {
                danhSach.remove(i);
                return true;
            }
        }
        return false;
    }
}

// ============ GIAO DIỆN CHÍNH ============
public class QuanLyKhoHang extends JFrame {
    // Khai báo các thành phần
    QuanLyKho kho = new QuanLyKho();
    JTextField txtMa, txtTen, txtSL;
    JTable table;
    DefaultTableModel model;
    JLabel lblStatus;
    
    public QuanLyKhoHang() {
        // Cài đặt cửa sổ
        setTitle("QUẢN LÝ KHO HÀNG");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout(10, 10));
        
        // === Tạo panel nhập liệu đẹp ===
        JPanel panelNhap = new JPanel(new GridBagLayout());
        panelNhap.setBackground(Color.WHITE);
        panelNhap.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
            "NHẬP THÔNG TIN SẢN PHẨM",
            TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(0, 102, 204)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Mã hàng
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblMa = new JLabel("Mã hàng:");
        lblMa.setFont(new Font("Arial", Font.BOLD, 13));
        panelNhap.add(lblMa, gbc);
        
        gbc.gridx = 1;
        txtMa = new JTextField(10);
        txtMa.setFont(new Font("Arial", Font.PLAIN, 14));
        txtMa.setPreferredSize(new Dimension(150, 30));
        txtMa.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) 
                    throws javax.swing.text.BadLocationException {
                if (getLength() + str.length() <= 5) {
                    super.insertString(offs, str.toUpperCase(), a);
                }
            }
        });
        panelNhap.add(txtMa, gbc);
        
        // Tên hàng
        gbc.gridx = 2;
        JLabel lblTen = new JLabel("Tên hàng:");
        lblTen.setFont(new Font("Arial", Font.BOLD, 13));
        panelNhap.add(lblTen, gbc);
        
        gbc.gridx = 3;
        txtTen = new JTextField(15);
        txtTen.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTen.setPreferredSize(new Dimension(200, 30));
        panelNhap.add(txtTen, gbc);
        
        // Số lượng
        gbc.gridx = 4;
        JLabel lblSL = new JLabel("Số lượng:");
        lblSL.setFont(new Font("Arial", Font.BOLD, 13));
        panelNhap.add(lblSL, gbc);
        
        gbc.gridx = 5;
        txtSL = new JTextField(8);
        txtSL.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSL.setPreferredSize(new Dimension(120, 30));
        panelNhap.add(txtSL, gbc);
        
        // === Panel nút chức năng đẹp ===
        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelNut.setBackground(Color.WHITE);
        panelNut.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        
        JButton btnThem = taoNut("Thêm", new Color(46, 204, 113));
        JButton btnSua = taoNut("Sửa", new Color(52, 152, 219));
        JButton btnXoa = taoNut("Xóa", new Color(231, 76, 60));
        JButton btnClear = taoNut("Làm mới", new Color(149, 165, 166));
        
        panelNut.add(btnThem);
        panelNut.add(btnSua);
        panelNut.add(btnXoa);
        panelNut.add(btnClear);
        
        // Gộp panel nhập và panel nút
        JPanel panelTren = new JPanel(new BorderLayout());
        panelTren.setBackground(new Color(240, 248, 255));
        panelTren.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        panelTren.add(panelNhap, BorderLayout.CENTER);
        panelTren.add(panelNut, BorderLayout.SOUTH);
        add(panelTren, BorderLayout.NORTH);
        
        // === Tạo bảng đẹp ===
        String[] cot = {"Mã hàng", "Tên hàng", "Số lượng", "Đơn giá", "Tiền hàng"};
        model = new DefaultTableModel(cot, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setGridColor(new Color(200, 200, 200));
        
        // Căn giữa các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);
        
        // === Status bar ===
        lblStatus = new JLabel("Sẵn sàng");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(0, 102, 204));
        lblStatus.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(lblStatus, BorderLayout.SOUTH);
        
        // === Sự kiện click vào bảng ===
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMa.setText(model.getValueAt(row, 0).toString());
                    txtTen.setText(model.getValueAt(row, 1).toString());
                    txtSL.setText(model.getValueAt(row, 2).toString());
                    lblStatus.setText("Đang chọn: " + model.getValueAt(row, 0).toString());
                }
            }
        });
        
        // === Sự kiện cho các nút ===
        btnThem.addActionListener(e -> themSanPham());
        btnSua.addActionListener(e -> suaSanPham());
        btnXoa.addActionListener(e -> xoaSanPham());
        btnClear.addActionListener(e -> clearFields());
        
        // Khởi tạo bảng trống
        capNhatBang();
    }
    
    // Hàm tạo nút đẹp
    private JButton taoNut(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 40));
        return btn;
    }
    
    // === Cập nhật bảng ===
    void capNhatBang() {
        model.setRowCount(0);
        for (SanPham sp : kho.danhSach) {
            Object[] dong = {sp.maHang, sp.tenHang, sp.soLuong, sp.donGia, sp.tinhTien()};
            model.addRow(dong);
        }
        lblStatus.setText("Tổng số sản phẩm: " + kho.danhSach.size());
    }
    
    // === Xóa trắng các trường nhập ===
    void clearFields() {
        txtMa.setText("");
        txtTen.setText("");
        txtSL.setText("");
        table.clearSelection();
        txtMa.requestFocus();
        lblStatus.setText("Đã làm mới form nhập");
    }
    
    // === Kiểm tra mã hàng (phải đúng 5 ký tự) ===
    boolean kiemTraMaHang(String ma) {
        if (ma.length() != 5) {
            JOptionPane.showMessageDialog(this, 
                "Mã hàng phải có đúng 5 ký tự!\nVí dụ: SP001, P001A, ...", 
                "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    // === Kiểm tra dữ liệu nhập ===
    boolean kiemTraDuLieu() {
        String ma = txtMa.getText().trim();
        if (ma.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã hàng!", 
                "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!kiemTraMaHang(ma)) {
            return false;
        }
        
        if (txtTen.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên hàng!", 
                "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            int sl = Integer.parseInt(txtSL.getText().trim());
            if (sl < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải >= 0!", 
                    "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!", 
                "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    // === Chức năng THÊM ===
    void themSanPham() {
        if (!kiemTraDuLieu()) return;
        
        String ma = txtMa.getText().trim().toUpperCase();
        String ten = txtTen.getText().trim();
        int sl = Integer.parseInt(txtSL.getText().trim());
        
        SanPham sp = new SanPham(ma, ten, sl);
        if (kho.them(sp)) {
            capNhatBang();
            clearFields();
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!", 
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
            lblStatus.setText("Đã thêm sản phẩm: " + ma);
        } else {
            JOptionPane.showMessageDialog(this, "Mã hàng '" + ma + "' đã tồn tại!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // === Chức năng SỬA ===
    void suaSanPham() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!kiemTraDuLieu()) return;
        
        String maCu = model.getValueAt(row, 0).toString();
        String maMoi = txtMa.getText().trim().toUpperCase();
        String ten = txtTen.getText().trim();
        int sl = Integer.parseInt(txtSL.getText().trim());
        
        SanPham spMoi = new SanPham(maMoi, ten, sl);
        if (kho.sua(maCu, spMoi)) {
            capNhatBang();
            clearFields();
            JOptionPane.showMessageDialog(this, "Sửa sản phẩm thành công!", 
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
            lblStatus.setText("Đã sửa sản phẩm: " + maMoi);
        } else {
            JOptionPane.showMessageDialog(this, "Mã hàng mới '" + maMoi + "' đã tồn tại!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // === Chức năng XÓA ===
    void xoaSanPham() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String ma = model.getValueAt(row, 0).toString();
        int chon = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa sản phẩm '" + ma + "'?", 
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
            
        if (chon == JOptionPane.YES_OPTION) {
            kho.xoa(ma);
            capNhatBang();
            clearFields();
            JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!", 
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
            lblStatus.setText("Đã xóa sản phẩm: " + ma);
        }
    }
    
    // === HÀM MAIN ===
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuanLyKhoHang().setVisible(true);
        });
    }
}