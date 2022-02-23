import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ejemploChorra {
    //dimensiones del marco y la imagen
    static int ancho = 500;
    static int alto  = 500;
    static int anchoimg = 500;
    static int altoimg  = 400;

    //aqui irian los generadores congruenciales lineales a implementar en metodo indidividualizados, ahora pongo una chorra para pruebas...

    public static void elgenerador(int[] datX, int[] datY){
      Random r   = new Random();
      for(int i=0; i<datX.length;i++)datX[i]=r.nextInt(anchoimg);
      for(int i=0; i<datY.length;i++)datY[i]=r.nextInt(altoimg);
    }
      
    public static void main(String args[]) {
        
        //el marco...
        
        JFrame frame = new JFrame("Plotter de Puntos Aleatorios");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(ancho, alto);

        //la imagen...
        
        Image image = new BufferedImage(anchoimg, altoimg, BufferedImage.TYPE_INT_RGB);
        JLabel imgLab = new JLabel(new ImageIcon(image));
        imgLab.setVisible(true);
        Graphics g = image.getGraphics();
        g.drawImage(image, 0, 0, null);

        Box contImg = Box.createVerticalBox();
        contImg.add(imgLab);
        frame.getContentPane().add(contImg);

        //un panel para el boton...
        JPanel verticalPanel = new JPanel();

        //por esta zona del codigo, añadir resto de componentes (menu desplegable de generadores, entrada del numero de puntos, etc.)...
        //utilizar clase JComboBox y JTextField, por ejemplo...

        //ahora, un boton...   
        JButton generarPuntos = new JButton("                                CLICK PARA GENERAR PUNTOS ALEATORIOS                               ");
        Box caja = Box.createVerticalBox();
        caja.add(generarPuntos);
        verticalPanel.add(caja);

        //el procesadorcillo de eventos del boton... por aqui, poner resto de procesadores de eventos...
        generarPuntos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
               imgLab.setIcon(new ImageIcon(image));
               g.drawImage(image, 0, 0, null);
               imgLab.setVisible(true);
               g.setColor(Color.white);
               //generamos y pintamos puntos...
               int puntos = 50000;
               int[] cx = new int[puntos];
               int[] cy = new int[puntos];
               elgenerador(cx, cy);
               for (int i = 0; i < cx.length; i++) g.drawOval(cx[i], cy[i], 1, 1);
            }
        });

        JScrollPane j = new JScrollPane();
        j.setViewportView(caja);
        frame.add(BorderLayout.SOUTH, j);
        frame.setVisible(true);
    }
}