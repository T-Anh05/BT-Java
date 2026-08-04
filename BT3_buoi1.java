import java.util.Scanner;

public class BT3_buoi1 {
    public static void main(String[] arg){
    try (Scanner scanner = new Scanner(System.in)) {
        System.out.print("nhap n:");
        int n =scanner.nextInt();
        if(songuyento(n)){
        System.out.println(n +"la so nguyen to");
        }else{
        System.out.println(n + "khong phai so nguyen to");
        }
    }
    }
    // kt so nguyen to
    public static  boolean songuyento(int n){
    if(n<=1){
    return false;
    }
    //2 la so nguyen chan duy nhat
    if(n==2){
    return true;
    }
    if (n %2==0){
    return false;
    }
     for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i == 0) {
                return false;
            }
            }
     return true;
    }
     
}