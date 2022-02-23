/*
Modelo básico de interfaz gráfica para computación de autómatas celulares unidimensionales binarios incluyendo:

a) Dos modos de carga de configuración inicial (por linea de comandos): aleatoria, o célula central activa, resto no;
b) Núcleo de computación básico para una función de transición que suma el valor de un células y sus vecinas;
c) Condición de frontera nula;
d) Elementos básicos para pintar la evoluación espacio-temporal del modelo con un output gráfico.

Este código puede utilizarse como base para desarrollar los productos pedidos en la práctica número 3.

Ejecución: java automataCelularChorra modo (modo=true para carga aleatoria, false para célula central activa, resto no)

*/


import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.util.*;

public class automataCelularChorra extends JPanel {  

  boolean modo;  //elegir configuración inicial aleatoria (true) o célula inicial a 1, resto a 0 (false)...

  public automataCelularChorra(boolean modo){
      this.modo = modo;
  }          

   public void paint(Graphics g) {
      Image img = createImageWithText();
      g.drawImage(img, 20,20,this);
   }

   private Image createImageWithText() {
      Random r       = new Random();
      int ancho      = 800;
      int alto       = 400;	
      int [] data    = new int[ancho]; //configuración actual...
      int [] data2   = new int[ancho]; //configuración sucesora...
      int [] temp;                     //para intercambios...

      //estableciendo configuracion inicial
      if(modo==true)
        for(int i=0; i<data.length; i++) data[i]=r.nextInt(2);
        else {for(int i=0; i<data.length; i++) data[i]=0;
              data[399]=1;
             }
      int nGeneraciones = alto; //tiempo de evolucion...

      BufferedImage bufferedImage = new BufferedImage(ancho,alto, BufferedImage.TYPE_INT_RGB);
      Graphics g = bufferedImage.getGraphics();
      int i, j;

      //núcleo de computación del autómata celular...
      for(i = 0; i<nGeneraciones; i++){
        for(j=0; j<data.length; j++){
          //ahora, la funcion de transicion (regla) del automata celular, utilizando un condición de frontera nula...
          if(j==0)data2[j]=(data[j]+data[j+1])%2;
             else if(j==data.length-1)data2[j]=(data[j-1]+data[j])%2;
                   else data2[j]=(data[j-1]+data[j]+data[j+1])%2;
        }

        //se pinta la actual configuración
        for(int k=0; k<data.length; k++)
          if(data2[k]==0){g.setColor(Color.BLUE);g.drawOval(k, i, 1, 1);}
            else{g.setColor(Color.YELLOW);g.drawOval(k, i, 1, 1);}

        //se intercambian los arrays...
        temp=data;
        data=data2;
        data2=temp;	
      }
      return bufferedImage;
   }
   
   public static void main(String[] args) {
      boolean  mode  = Boolean.valueOf(args[0]);    
      int anchoFrame = 1000;
      int altoFrame  = 600;
      JFrame frame   = new JFrame();
      frame.setSize(anchoFrame, altoFrame);
      frame.getContentPane().add(new automataCelularChorra(mode));
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(anchoFrame, altoFrame);
      frame.setVisible(true);
   }
}