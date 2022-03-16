/*Programita para pasar una cadena de texto a codigo ASCII... */
import java.util.*;

public class string2Ascii{
  public static void main(String[] args){
  Scanner sc = new Scanner(System.in);
  System.out.println("cadena? ");
  String string = sc.nextLine();
  System.out.println("Tu cadena es: "+string);
  System.out.print("Y su representacion ASCII: ");
  //basta usar el método String.charAt, moldeando el resultado a int...
  for(int i=0; i<string.length(); i++)
    System.out.print((int)string.charAt(i)+" ");
  sc.close();
  }//main
}//class