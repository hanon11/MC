import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.*;

public class TestPuntos extends JPanel {

   public void paint(Graphics g) {
      Image img = createImageWithText();
      g.drawImage(img, 20,20,this);
   }

   private Image createImageWithText() {
      Random r = new Random();
      int ancho      = 400;
      int alto       = 400;
      int num_puntos = 100000;
      BufferedImage bufferedImage = new BufferedImage(ancho,alto,BufferedImage.TYPE_INT_RGB);
      Graphics g = bufferedImage.getGraphics();
      g.setColor(Color.BLUE);
      int x, y;
      for(int i = 0; i<num_puntos; i++) {
        x=r.nextInt(ancho); 
        y=r.nextInt(alto); 
        g.drawLine(x, y, x, y); // para pintar puntos simples...
	     //g.drawOval(x, y, 1, 1);   // o tambien...
      }
      
      return bufferedImage;
   }
   
   public static void main(String[] args) {
      int anchoFrame = 600;
      int altoFrame = 600;
      JFrame frame = new JFrame();
      frame.getContentPane().add(new TestPuntos());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(anchoFrame, altoFrame);
      frame.setVisible(true);
   }
}