import java.util.*;

public class sumaEsRECIniciales{

  public static int s(int x){return(x+1);}                     //funcion sucesor...

  public static int u_1_1(int x){return(x);}                   //funcion proyeccion 1 de 1

  public static int u_3_2(int x1, int x2, int x3){return(x2);} //funcion proyeccion 2 de 3

  //funcion f(x, y)=x+y, construida con funciones iniciales, recursion y composicion...

  public static int f(int x, int y){
    if(y==0) return(u_1_1(x));
    else return(s(u_3_2(y-1, f(x,y-1), x)));
  }

  public static void main(String[] args){
    int x, y;
    x = 20; y = 25;
    System.out.println(f(x, y));
    x = 250; y = 1000;
    System.out.println(f(x, y));
    x = 250; y = 0;
    System.out.println(f(x, y));
    x = 0; y = 1328	;
    System.out.println(f(x, y));
  }//main
}//class