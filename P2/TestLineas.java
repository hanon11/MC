import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.*;

public class TestLineas extends JPanel {

   public void paint(Graphics g) {
      Image img = createImageWithText();
      g.drawImage(img, 20,20,this);
   }

   private Image createImageWithText() {
      Random r = new Random();
      int ancho      = 400;
      int alto       = 400;
      int num_lineas = 100;
      BufferedImage bufferedImage = new BufferedImage(ancho,alto,BufferedImage.TYPE_INT_RGB);
      Graphics g = bufferedImage.getGraphics();
      int x1, y1, x2, y2;

      for(int i = 0; i<num_lineas; i++) {
        x1=0; 
        y1=0; 
        x2=r.nextInt(ancho); 
        y2=r.nextInt(alto); 
        g.drawLine(x1, y1, x2, y2); // para pintar lineas...

      }
      
      return bufferedImage;
   }
   
   public static void main(String[] args) {
      int anchoFrame = 600;
      int altoFrame = 600;
      JFrame frame = new JFrame();
      frame.getContentPane().add(new TestLineas());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(anchoFrame, altoFrame);
      frame.setVisible(true);
   }
}