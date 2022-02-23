/*
  Uso del método repaint() combinado con un histórico para pintar cuadrados en posiciones aleatorias en respuesta a un click de ratón (que podría haber sido la 
  pulsación de un botón). Obsérvese que se requiere mantener un histórico de las posiciones donde se dibujan los cuadrados, de manera que repaint dibuje el cuadrado 
  nuevo y también los  antiguos, tirando de la historia almacenada... para lograr mantener en el dibujo elementos antiguos.
*/

import java.awt.Graphics;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Random;

public class usoRepaint extends JPanel implements MouseListener {

       private int x;
       private int y;
       private int ancho;
       private int alto;
       private int lado = 4;
       private Random r = new Random();

       //el array que sigue contiene el histórico de posiciones aleatorias, para que repaint funcione bien...
       //esto es una chapuza, y resulta muy mejorable utilizando cualquier clase contenedora y un iterador...
       //así, funcionará con los primeros 500 cuadrados... hasta que se llene el array.

       private int[] historia = new int[1000];

       //puntero para construir la historia... de nuevo muy mejorable :-(
       private int cont = 0;

       public usoRepaint(int ancho, int alto) {
           super();
           this.ancho = ancho;           
           this.alto  = alto;
           this.addMouseListener(this);
       }

       public void mouseClicked(MouseEvent me) {
          //genera un punto aleatorio en respuesta a un click de ratón, y lo guarda en la historia... para repintar...
           x = r.nextInt(ancho);
	   y = r.nextInt(alto);
           historia[cont] = x;
           cont++;
           historia[cont] = y;
	   cont++;
           repaint();
       }

       public void mouseEntered(MouseEvent e) {}
       public void mouseReleased(MouseEvent e) {}
       public void mousePressed(MouseEvent e) {}
       public void mouseExited(MouseEvent e) {}

       public void paintComponent(Graphics g) {
          //el repintado implicará pintar el nuevo cuadrado... y los antiguos rescatados de la historia...
           super.paintComponent(g);
	   g.setColor(Color.BLUE);
           g.drawString("Click para dibujar un cuadrado...", 200, 200);
           for(int i=0; i<=cont; i++){
	     g.drawRect(historia[i], historia[i+1],lado,lado);
	     g.fillRect(historia[i], historia[i+1],lado,lado);
             i++;
          }
       }

	public static void main(String args[]) {

           int anc = 600;
           int alt = 400;
           JFrame unJFrame = new JFrame("Marcar con el puntero...");
           unJFrame.setSize(anc, alt);
           unJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           unJFrame.getContentPane().add(new usoRepaint(anc, alt));
           unJFrame.setVisible(true);
              
       }
}

