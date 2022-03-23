/*
  Va todo sin tildes. Uso del metodo repaint() para pintar sucesivos arrays 2-D de numeros aleatorios en respuesta a un click de raton (que podría haber sido la 
  pulsación de un botón). Esto puede aplicar con pocos cambios para simular un AC 2-D como Life, donde cada generacion calcula un nuevo estado de un array 2-D 
  segun la funcion de transicion.

  Compilacion y ejecucion: estandar, con javac y java
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

       private int ancho;
       private int alto;
       private Random s = new Random();
       private int[][] cells;             //espacio de celulas


      //se construye el espacio de celulas, se llena aleatoriamente, y se pinta la primera vez...

       public usoRepaint(int ancho, int alto) {
           super();
           this.ancho = ancho;           
           this.alto  = alto;
           cells = new int[ancho][alto]; 
           for(int i=0; i<ancho; i++)
             for(int j=0; j<alto; j++)
               cells[i][j] = s.nextInt(2);
           repaint();
           this.addMouseListener(this);	
       }

      //cada click de raton recomputa el espacio de celulas cells con esta funcion y lo vuelve a pintar...
      //aqui la transicion es volver a rellenar aleatoriamente el espacio celular...
      //para implementar LIFE, esta funcion puede sustituirse por la dinamica bacteriana de LIFE...

      public void funcionTransicion(){
          for(int i=0; i<ancho; i++)
            for(int j=0; j<alto; j++)
            cells[i][j] = s.nextInt(2);
            repaint();
      }


       //el click de raton construye un nuevo estado aleatorio del espacio cells y lo pinta...
       //al programar LIFE, la funcion de transicion debe cambiarse...
       //el evento de raton no sera necesario mas de una vez (para arrancar), y bastara computar la siguiente genereracion y volverla a pintar con repaint...

       public void mouseClicked(MouseEvent me) {
            funcionTransicion();
            repaint();
       }

       public void mouseEntered(MouseEvent e) {}
       public void mouseReleased(MouseEvent e) {}
       public void mousePressed(MouseEvent e) {}
       public void mouseExited(MouseEvent e) {}

      //se pinta el estado actual del espacio celular
       public void paintComponent(Graphics g) {
           super.paintComponent(g);
           for(int i=0; i<ancho; i++)
             for(int j=0; j<alto; j++)
             if(cells[i][j]==0){
               g.setColor(Color.BLUE);
               g.drawOval(i, j, 1, 1);
             }else{
                 g.setColor(Color.YELLOW);
                 g.drawOval(i, j, 1, 1);
               }//else
       }//paintComponent

	public static void main(String args[]) {

           int anc = 600;
           int alt = 400;
           JFrame unJFrame = new JFrame("Hacer clicks sucesivos con el raton en cualquier punto de la imagen...");
           unJFrame.setSize(anc, alt);
           unJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           unJFrame.getContentPane().add(new usoRepaint(anc, alt));
           unJFrame.setVisible(true);
              
       }
}

