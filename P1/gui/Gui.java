import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Gui extends JFrame {
    private static String botones[] = 
    { 
        "Parámetros","Curva","Computar", 
        "Detener"
    };
    public Gui(String nombre) {
        super(nombre);
        //menu superior
        JMenuBar jmb = new JMenuBar();
        setJMenuBar(jmb);
        JMenu A = new JMenu("Opcion A");
        JMenu B = new JMenu("Opcion B");
        JMenu C = new JMenu("Opcion C");
        JMenu info = new JMenu("Acerca de");
        jmb.add(A);
        jmb.add(B);
        jmb.add(C);
        jmb.add(info);
        setLayout(new BorderLayout(5, 10));
        JPanel jp = new JPanel();
        //add(jp);
        //botones
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        for(int i = 0; i < botones.length; i++)
            panelDerecho.add(new JButton(botones[i]));
        
        JButton image = new JButton("", new ImageIcon(getClass().getResource("Raspberry.png")));
        add(BorderLayout.WEST, image);
        add(panelDerecho);
    }
    public static void main(String args[]) {
        Gui frame = new Gui("Mi primera interfaz gráfica");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setSize(700, 600);
        frame.setVisible(true); 

    }
        
}