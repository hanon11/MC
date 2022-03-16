import java.util.*;

public class sumaEsREC{

  public static int f(int x, int y){
    if(y==0) return(x);
    else return(f(x,y-1)+1);
  }
  public static void main(String[] args){
  int x, y;
  x = 20; y = 25;
  System.out.println(f(x, y));
  }//main
}//class