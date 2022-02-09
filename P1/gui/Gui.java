import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Gui extends JFrame 
{
    private static String botones[] = 
    { 
        "Parámetros","Curva","Computar", "Detener"
    };
    private static String menus[] = 
    { 
        "Opcion A","Opcion B","Opcion C", "Acerca de"
    };

    

    private JPanel crearPanelBotones()
    {
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));

        JButton b1;
        for(int i = 0; i < botones.length; i++)
        {
            b1 = new JButton(botones[i]);
            b1.setAlignmentX(Component.CENTER_ALIGNMENT); //botones centrados en el panel
            b1.setMinimumSize(new Dimension(150,30)); //establezco tam de los botones para que sean iguales
            b1.setMaximumSize(new Dimension(150,30));
            b1.addActionListener(new escuchaClick()); 
            panelBotones.add(b1);
        }
        return panelBotones;
    }

    public Gui(String nombre) 
    {
        super(nombre);
        //menu superior
        JMenuBar jmb = new JMenuBar();
        setJMenuBar(jmb);
        for(int i = 0; i < botones.length; i++)
            jmb.add(new JMenu(menus[i]));


        //panel botones
        add(crearPanelBotones());

        JButton image = new JButton("", new ImageIcon(getClass().getResource("Raspberry.png")));
        add(BorderLayout.WEST, image);
    }

    public static void main(String args[]) 
    {
        Gui frame = new Gui("Mi primera interfaz gráfica");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setSize(700, 600);
        frame.setVisible(true); 

    }
        
}