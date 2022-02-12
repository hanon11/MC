import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Gui extends JFrame 
{
    static JFrame f;
    private ButtonListener bl = new ButtonListener();

    private ActionListener al = new ActionListener() 
    {  
        public void actionPerformed(ActionEvent e) 
        {
            String name =  ((JMenuItem)e.getSource()).getText();  
            JDialog d = new JDialog(f, name);
            JLabel l = new JLabel(name);
            d.add(l);
            d.setSize(300, 150);
            d.setVisible(true);
        }
    };

    private static String botones[] = 
    { 
        "Parámetros","Curva","Computar", "Detener"
    };

    private static String menus[] = 
    { 
        "Opcion A","Opcion B","Opcion C", "Acerca de"
    };

    private static String itemsMenu[] = 
    { 
        "Sección 1A","Sección 1B", "Sección 1C", "Ayuda"
    };

    private static String opciones[] = 
    { 
        "Opcion 1A","Opcion 1B", "Opcion 1C"
    };
    //crea nuevo JDialog por cada boton
    class ButtonListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent e) 
        {
            String name = ((JButton)e.getSource()).getText();  
            JDialog d = new JDialog(f, name);
            JLabel l = new JLabel(name);
            d.add(l);
            d.setSize(300, 150);
            d.setVisible(true);
        }
    }

    private JPanel crearPanelBotones()
    {
        JPanel panelBotones = new JPanel();
        SpringLayout layout = new SpringLayout();
        panelBotones.setLayout(layout);
        
        JButton b1 = new JButton(botones[0]), b2;
        b1.addActionListener(bl);
        b1.setPreferredSize(new Dimension(150, 27));
        panelBotones.add(b1);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, b1, 0, SpringLayout.HORIZONTAL_CENTER, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b1, 30, SpringLayout.NORTH, panelBotones);
        
        for(int i = 1; i < botones.length; i++)
        {
            b2 = new JButton(botones[i]);
            b2.setPreferredSize(new Dimension(150, 27));
            b2.addActionListener(bl);
            
            panelBotones.add(b2);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, b2, 0, SpringLayout.HORIZONTAL_CENTER, b1);
            layout.putConstraint(SpringLayout.NORTH, b2, 45, SpringLayout.NORTH, b1);
            b1 = b2;
        }
        return panelBotones;
    }

    private JMenuBar SimpleMenus()
    {
        JMenuBar mb = new JMenuBar();
        JMenu menu;
        JMenu submenu;
        JMenuItem item;
        for(int i = 0; i < itemsMenu.length-1; i++) 
        {
            //itemsMenu[i].addActionListener(al);
            menu = new JMenu(menus[i]);
            submenu = new JMenu(opciones[i]);
            submenu.add(new JMenuItem(itemsMenu[i])).addActionListener(al);
            menu.add(submenu);
            mb.add(menu);
        }
        menu = new JMenu(menus[itemsMenu.length-1]);
        menu.add(itemsMenu[itemsMenu.length-1]).addActionListener(al);
        mb.add(menu).addActionListener(al);;
        return mb;
    }


    public Gui(String nombre) 
    {
        super(nombre);
        f = new JFrame(nombre);
        //menu superior
        /*JMenuBar jmb = new JMenuBar();
        setJMenuBar(jmb);
        for(int i = 0; i < botones.length; i++)
            jmb.add(new JMenu(menus[i]));*/

        setJMenuBar(SimpleMenus());
        //panel botones
        add(crearPanelBotones());

        JButton image = new JButton("", new ImageIcon(getClass().getResource("descarga.jfif")));
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