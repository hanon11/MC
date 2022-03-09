import java.awt.*;
import javax.swing.*;

public class pintaCurva2 extends JFrame {
    private int ancho = 600;
    private int alto  = 600;
    private Container hoja;
    private JPanel canvas;

    public pintaCurva2(double[] Datos) {
        super("pintaCurva");
        hoja = getContentPane();
        canvas = new LaBrocha(Datos);
        hoja.add(canvas);	
        setSize(ancho, alto);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public class LaBrocha extends JPanel {
        private double[] dat;
        private int puntos;
        private double[] Xdat;
        private double[] Ydat;

        public LaBrocha(double[] Datos) {
            super();
            dat = Datos;
            puntos = dat.length / 2;
            Xdat = new double[puntos];
            Ydat = new double[puntos];
            //separamos puntos para coordenadas x e y...
            for(int i = 0; i < puntos; i++) {
                Xdat[i] = dat[i * 2];
                Ydat[i] = dat[i * 2 + 1];
            }
        }

        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
	    g.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(3)); //aumentamos grosor de la línea...
            //pintamos la gráfica, con transformación de coordenadas de muy de andar por casa...
            for(int i = 0; i < puntos - 1; i++) {
                int x0 = (int) (Xdat[i]);
                int x1 = (int) (Xdat[i + 1]);
                int y0 = (int) (alto-Ydat[i]);
                int y1 = (int) (alto-Ydat[i + 1]);
                //para tablas de datos discretos, drawLine será más que suficiente con drawLine...
                g2.drawLine(x0, y0, x1, y1);
            }   
            //ahora, la recta f(x)=x+2, a partir de su ecuación, y cambiando color y grosor...
           int puntosrecta = 500;
           double[] polinomio = new double[puntosrecta];
           for (int i = 0; i < puntosrecta; i++)polinomio[i] = i+2; //calculamos la recta
	   g.setColor(Color.YELLOW);
	   g2.setStroke(new BasicStroke(2));
	   for (int i = 0; i < puntosrecta; i++) {
             g.drawLine(i, alto-(int)polinomio[i], i+1, alto-(int)polinomio[i+1]);
             i++;
           }
        }
   }		

    public static void main(String[] args) {
        //nube  de datos (tabla) con la que construir la curva...  
        //quizás hará falta una transformación de coordenadas con los datos de autómata celular
        //primera columna: eje x
        // segunda columna: eje y
        double[] d = { 0.0, 15.0,
                       10.0, 20.0,
                       20.0, 162.0,
                       30.0, 128.0,
                       40.0, 114.0,
                       50.0, 240.0,
                       60.0, 400.0,
                       70,   450.0,
                       80,   85.0,
                       90,   30.0,
                       100,  30,
                       110,  30,
                       120,  30,
		       130,  30,
                       300,  200,
                       500,  200,
                       510,  210,
                       520,  220,
		       530,  230,
                       540,  230,
                       550,  450,
                       560,  450,
                       570,  0,
                       580,  0,
                       590,  0,
                       600,  0,
                       
		       };
	Frame f = new pintaCurva2(d);	
        f.setVisible(true);
    }
}