import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Bai8 {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Quản lý sinh viên");
        frame.setSize(550, 400);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel ma = new JLabel("Mã SV:");
        ma.setBounds(30, 20, 70, 30);
        frame.add(ma);

        JTextField txtMa = new JTextField();
        txtMa.setBounds(90, 20, 120, 30);
        frame.add(txtMa);

        JLabel ten = new JLabel("Họ tên:");
        ten.setBounds(30, 60, 70, 30);
        frame.add(ten);

        JTextField txtTen = new JTextField();
        txtTen.setBounds(90, 60, 120, 30);
        frame.add(txtTen);

        JLabel lop = new JLabel("Lớp:");
        lop.setBounds(30, 100, 70, 30);
        frame.add(lop);

        JTextField txtLop = new JTextField();
        txtLop.setBounds(90, 100, 120, 30);
        frame.add(txtLop);

        JButton them = new JButton("Thêm");
        them.setBounds(250, 20, 80, 30);
        frame.add(them);

        JButton xoa = new JButton("Xóa");
        xoa.setBounds(350, 20, 80, 30);
        frame.add(xoa);

        String[] cot = {"Mã SV", "Họ tên", "Lớp"};

        DefaultTableModel model =
                new DefaultTableModel(cot, 0);

        JTable table = new JTable(model);

        JScrollPane scroll =
                new JScrollPane(table);

        scroll.setBounds(30, 150, 470, 150);
        frame.add(scroll);

        them.addActionListener(e -> {

            String maSV = txtMa.getText();
            String hoTen = txtTen.getText();
            String lopSV = txtLop.getText();

            model.addRow(new Object[]{
                    maSV, hoTen, lopSV
            });

            txtMa.setText("");
            txtTen.setText("");
            txtLop.setText("");
        });

        xoa.addActionListener(e -> {

            int dong = table.getSelectedRow();

            if (dong >= 0) {
                model.removeRow(dong);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}