import java.util.Scanner;

public class BT2Buoi1 {
  public static void main(String[] arg) {
  Scanner scanner = new Scanner(System.in);
  System.out.print("nhap n:");
  int n = scanner.nextInt();
  // kiem tra n la so duong
  if(n<=0){
      System.out.print("n nhap phai la so nguyen duong");
     scanner.close();
     return;
  }
  Double s =0.0;
  for (int i =1; i<=n; i++){
      s +=1.0/i;
  }
  System.out.println("S="+s);
  scanner.close();
  }
}