import java.util.*;
import java.math.BigInteger;

public class cipherVernam
{
    private static int n_vecinos = 1, n_estados = 2, n_celulas = 1024, generaciones = 1000, regla, celula_entropia = 499, 
        indice_entropia = 0, indice_hamming = 0, indice_temporal = 0;
    private static int[] t_1 = new int[n_celulas], actual = new int[n_celulas], code, hamming = new int[generaciones-1];
    private static double[] entropia = new double[generaciones-1];
    private static double[] entropia_celular = new double[generaciones-1];
    
    public static void calcular_estado_actual()
    {
        int sumatorio = 0;
        for( int i = 0; i < actual.length; i++ )
        {
            sumatorio = 0;
            for( int j = i-n_vecinos; j <= i+n_vecinos; j++ )
            {
                if( j < 0 || j > t_1.length-1 )
                    sumatorio += 0;
                else
                    sumatorio += t_1[j];
            }
            
            actual[i] = code[sumatorio % 8]; //para que no se salga del vector
        }
        calcular_distancia_hamming();
        calcular_entropia();
        System.arraycopy(actual, 0, t_1, 0, actual.length);
    }

    public static void decimal_a_binario_2()
    {
        code = new int[8];
        int i = 0, decimal = regla;

        while(decimal > 0 && i < 8)
        {
            code[i++] = decimal % 2;
            decimal = decimal / 2;
        }
    }

    public static void rellenar_vectores()
    {
        BigInteger[] aleatorios = new BigInteger[n_celulas];
        BigInteger x0 = BigInteger.ONE;
        for(int i = 0; i < n_celulas; i++)
        {
            aleatorios[i] = x0.multiply(BigInteger.valueOf(69621));
            aleatorios[i] = aleatorios[i].mod(BigInteger.valueOf((long)(2e31 -1)));
            x0 = aleatorios[i];
            aleatorios[i] = aleatorios[i].mod( BigInteger.valueOf( n_estados ));
        }
        for(int i = 0; i < n_celulas; i++)
            t_1[i] = aleatorios[i].intValue();
    }

    public static void calcular_distancia_hamming()
    {
        int sumatorio = 0;
        for( int i = 0; i < n_celulas; i++ )
            if( t_1[i] != actual[i] )
                sumatorio++;
        hamming[indice_hamming] = sumatorio;
        if( indice_hamming < hamming.length-1 )
            indice_hamming++;
    }

    public static double logConversion( double x ) { return ( Math.log(x) / Math.log(2) ); }

    public static void calcular_entropia()
    {
        int[] sumatorio = new int[n_estados];
        for( int i = 0; i < n_celulas; i++ )
            sumatorio[actual[i]]++;
        entropia_celular[indice_temporal] = actual[celula_entropia];
        //aproximamos probabilidades por frecuencias...
        double H = 0;
        for( int i = 0; i < n_estados; i++ )
        {
            if( (double)sumatorio[i]/n_celulas != 0 )
                H += (double)sumatorio[i]/n_celulas*logConversion( (double)sumatorio[i]/n_celulas );
        }
        H = -H;

        entropia[indice_entropia] = H*300;
        if( indice_entropia < entropia.length-1 )
            indice_entropia++;
        if( indice_temporal < entropia_celular.length-1 )
            indice_temporal++;
    }

    public static void main( String[] args ) 
    {
        for( int i = 0; i < 255; i++ ) //reglas
        {
            regla = i;
            decimal_a_binario_2();
            rellenar_vectores();
            indice_entropia = 0; indice_temporal = 0; indice_hamming = 0;
            int sumatorio_hamming = 0;
            double sumatorio_entropia = 0, H = 0;
            int ceros = 0, unos = 0;
            for( int j = 0; j < generaciones; j++ )
                calcular_estado_actual();
            for( int j = 0; j < hamming.length; j++ )
                sumatorio_hamming += hamming[j];
            
            for( int j = 0; j < entropia.length; j++ )
                sumatorio_entropia += entropia[j];
            
            for( int j = 0; j < entropia_celular.length; j++ )
            {
                if( entropia_celular[j] == 0)
                    ceros++;
                else
                    unos++;
            }
            if( (double)ceros != 0 )
                H += (double)ceros/generaciones*logConversion( (double)ceros/generaciones );
            if( (double)unos/generaciones != 0 )
                H += (double)unos/generaciones*logConversion( (double)unos/generaciones );
            H = -H;
            //System.out.println(Arrays.toString(entropia_celular));
            //System.out.println("H " + H);
            //System.out.println("Ceros " + ceros);
            //System.out.println("Unos " + unos);
            if( sumatorio_hamming/generaciones > 300 && sumatorio_entropia/generaciones > 0.8 && H > 0.8 )
                System.out.println("Regla " + i);
        }
    }
}
