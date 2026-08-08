package vn.edu.eaut.lab2;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== LAB 2 - MAVEN PROJECT VA DONG GOI JAR =====");
        System.out.print("Nhap ma sinh vien: ");
        String studentId = scanner.nextLine();

        System.out.print("Nhap ho ten sinh vien: ");
        String fullName = scanner.nextLine();

        double attendanceScore = inputScore(scanner, "diem chuyen can");
        double midtermScore = inputScore(scanner, "diem giua ky");
        double finalScore = inputScore(scanner, "diem cuoi ky");

        Student student = new Student(studentId, fullName,
                attendanceScore, midtermScore, finalScore);

        double totalScore = GradeCalculator.calculateFinalScore(student);
        String grade = GradeCalculator.calculateGrade(totalScore);

        System.out.printf("\n----- KET QUA HOC PHAN -----\n");
        System.out.printf("Ma SV: %-10s | Ho ten: %-20s | Diem tong ket: %-8.2f | Xep loai: %-5s%n", 
            student.getStudentId(), student.getFullName(), totalScore, grade);

        scanner.close();
    }

    private static double inputScore(Scanner scanner, String label) {
        while (true) {
            try {
                System.out.print("Nhap " + label + ": ");
                double score = Double.parseDouble(scanner.nextLine());
                GradeCalculator.validateScore(score, label);
                return score;
            } catch (IllegalArgumentException ex) {
                System.out.println("Loi: " + ex.getMessage());
            }
        }
    }
}