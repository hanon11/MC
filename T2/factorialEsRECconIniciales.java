import java.util.*;
public class factorialEsRECconIniciales{

  //funciones iniciales necesarias...
  public static int n(int x){return(0);}
  public static int s(int x){return(x+1);}
  public static int u_2_1(int x1, int x2){return(x1);}
  public static int u_2_2(int x1, int x2){return(x2);}

  //funcion factorial soportada con funciones iniciales, composisicion y recursion primitiva...
  public static int h(int x){
    if(x==0)return(s(n(x)));
    else return(s(u_2_1(x-1, h(x-1)))* u_2_2(x-1,h(x-1)));
  }

  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    System.out.println("Numero? ");
    int x = sc.nextInt();
    System.out.println("Factorial: "+h(x));
    sc.close();
  }//main
}//class