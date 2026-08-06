import java.util.Scanner;

public class XepLoaiSinhVien {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Nhap ma sinh vien: ");
        String maSV = sc.nextLine();  // Sửa thành String
        
        System.out.println("Nhap ho ten:");
        String hoTen = sc.nextLine();
        
        System.out.println("Nhap diem chuyen can:");
        double diemChuyenCan = sc.nextDouble();
        
        System.out.println("Nhap diem giua ky:");
        double diemGiuaKy = sc.nextDouble();
        
        System.out.println("Nhap diem cuoi ky:");
        double diemCuoiKy = sc.nextDouble();
        
        // Kiểm tra dữ liệu
        if (diemChuyenCan < 0 || diemChuyenCan > 10 || 
            diemGiuaKy < 0 || diemGiuaKy > 10 || 
            diemCuoiKy < 0 || diemCuoiKy > 10) {
            System.out.println("Diem khong hop le. Vui long nhap lai.");
        } else {
            // Tính điểm tổng kết theo công thức: CC*10% + GK*30% + CK*60%
            double diemTongKet = diemChuyenCan * 0.1 + diemGiuaKy * 0.3 + diemCuoiKy * 0.6;
            
            // Xếp loại theo yêu cầu
            String xepLoai;
            if (diemTongKet >= 8.5) {
                xepLoai = "A";
            } else if (diemTongKet >= 7.0) {
                xepLoai = "B";
            } else if (diemTongKet >= 5.5) {
                xepLoai = "C";
            } else if (diemTongKet >= 4.0) {
                xepLoai = "D";
            } else {
                xepLoai = "F";
            }
            
            // Hiển thị kết quả theo định dạng yêu cầu
            System.out.println(maSV + " - " + hoTen + " - " + diemTongKet + " - " + xepLoai);
        }
        
        sc.close();
    }
}