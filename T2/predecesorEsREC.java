import java.util.*;

public class predecesorEsREC{

  public static int p(int x){
    if(x==0) return(0);
    else return(x-1);
  }
  public static void main(String[] args){
  int x;
  x = 9; 
  System.out.println(p(x));
  x = 220;
  System.out.println(p(x));
  }//main
}//class