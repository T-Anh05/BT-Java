package vn.edu.eaut.lab2;

public class GradeCalculator {
    
    // Phương thức tính điểm tổng kết
    public static double calculateFinalScore(Student student) {
        double attendance = student.getAttendanceScore() * 0.1;
        double midterm = student.getMidtermScore() * 0.3;
        double finalExam = student.getFinalScore() * 0.6;
        return attendance + midterm + finalExam;
    }
    
    // Phương thức kiểm tra điểm hợp lệ
    public static void validateScore(double score, String label) {
        if (score < 0 || score > 10) {
            throw new IllegalArgumentException(label + " phai tu 0 den 10");
        }
    }
    
    // Phương thức tính xếp loại
    public static String calculateGrade(double totalScore) {
        if (totalScore >= 9.0) {
            return "Xuat sac";
        } else if (totalScore >= 8.0) {
            return "Gioi";
        } else if (totalScore >= 7.0) {
            return "Khá";
        } else if (totalScore >= 5.0) {
            return "Trung binh";
        } else if (totalScore >= 4.0) {
            return "Yeu";
        } else {
            return "Kem";
        }
    }
}