import java.util.*;

public class potenciaEsREC{

  public static int m(int x, int y){
    if(y==0) return(1);
    else return(m(x,y-1)*x);
  }
  public static void main(String[] args){
  int x, y;
  x = 9; y = 2;
  System.out.println(m(x, y));
  x = 10; y = 4;
  System.out.println(m(x, y));
  }//main
}//class