import java.util.*;

public class productoEsREC{

  public static int l(int x, int y){
    if(y==0) return(0);
    else return(l(x,y-1)+x);
  }
  public static void main(String[] args){
  int x, y;
  x = 9; y = 9;
  System.out.println(l(x, y));
  x = 5; y = 6;
  System.out.println(l(x, y));
  }//main
}//class