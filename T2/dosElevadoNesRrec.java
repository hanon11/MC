public class dosElevadoNesRrec{

  public static int u_2_2(int x1, int x2){return(x2);}           //proyeccion
  public static int g(int x1, int x2){return(2*u_2_2(x1, x2));}  //funcion g para montar rec. primitiva simple

                                                                 //la funcion 2^n expresada recursivamente...
  public static int f(int x){
    if(x==0) return(1);
    else return(g(x-1, f(x-1)));
  }

  public static void main(String[] args){                        //algunos ejemplos de calculo...
  int n = 4;
  System.out.println(f(n));
  n = 10;
  System.out.println(f(n));
  n = 12;
  System.out.println(f(n));
  }//main
}//class