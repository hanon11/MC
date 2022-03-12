/*Programa chorra que mide la entropia del generador aleatorio soportado con Random para una muestra de 10000000 bits...*/

import java.util.Random;

public class entropyCalc{
  
  //conversion logaritmo de base 10 a logaritmo de base 2, para entropia en base dos (bits)...
  public static double logConversion(double x){
  return(Math.log(x)/Math.log(2));
  }

  public static void main(String[] args){
  int item      = 10000000;
  int vactual;
  int nceros    = 0;
  int nunos     = 0;
  double p, q;
  double entrop;
  double H;
  Random fuente = new Random();

  for(int i=0; i<item; i++){ 
    //generamos ceros y unos aleatoriamente...
    vactual = fuente.nextInt(2);
    //contamos total de ceros/unos...
    switch(vactual){
      case 0: nceros++; break;
      case 1: nunos++; break;
    }//switch
  }//for
  //aproximamos probabilidades por frecuencias...
  p   = (double)nceros/item;
  q   = 1-p;
  //calculamos la entropia con la ecuacion de Shannon...
  H   = -((p*logConversion(p))+(q*logConversion(q))); 
  //mostramos resultados...
  System.out.println("P(0)="+p);
  System.out.println("P(1)="+q);
  System.out.println("H="+H);
 }//main
}//class