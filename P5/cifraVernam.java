/*Programita tonto que ilustra las aspectos basicos de un cifrado en flujo de bits tipo Vernam */
/*compilacion y ejecucion: no requieren nada especial, aplicar secuencia estandar              */

import java.util.*;

public class cifraVernam{

  //un metodo para hacer XOR con enteros 0, 1
  public static int miXOR(int a, int b){
    if(a==b)return(0);else return(1);
  }//miXOR

  public static void main(String[] args){
    //generadores aleatorios para obtener la cadena aleatoria de bits de cifrado, uno para cifrar y otro para descifrar; deben reproducir exactamente la misma secuencia...
    //para ello, monto dos generadores cebados con la misma semilla inicial...
    Random bitDeCifrado     = new Random(10);
    Random bitDeDescifrado  = new Random(10);
    //un array con el texto llano (sin cifrar) ya puesto en binario... en vuestro caso, hay paso previo: pasar a binario el texto llano original
    int[] mensaje           = {1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1};
    //otro array para guardar el criptograma...
    int[] criptograma       = new int[mensaje.length];
    //y otro mas, para recuperar el mensaje original...
    int[] mensaje2          = new int[mensaje.length];
   //ciframos con un Vernam haciendo XOR del mensaje y la cadena de cifrado, bit a bit...
    for(int i=0; i<mensaje.length; i++)
      criptograma[i] = miXOR(mensaje[i], bitDeCifrado.nextInt(2));
   //pintamos texto llano y texto cifrado (criptograma)...
    System.out.println("MENSAJE:     "+Arrays.toString(mensaje));
    System.out.println("CRIPTOGRAMA: "+Arrays.toString(criptograma));
  //desciframos con otro Vernam, haciendo XOR del criptograma con LA MISMA cadena de cifrado, bit a bit...
   for(int i=0; i<mensaje.length; i++)
      mensaje2[i] = miXOR(criptograma[i], bitDeDescifrado.nextInt(2));
  //pintamos el mensaje recuperado...
    System.out.println("MENSAJE:     "+Arrays.toString(mensaje2));
  }//main
}//class