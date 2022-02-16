import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class randomGenerator extends JFrame 
{
    private int n_puntos = 1000;
    private int generador_escogido;
    private static String menus[] = 
    { 
        "Opción A","Opción B","Opción C", "Acerca de"
    };

    private static String itemsMenu[] = 
    { 
        "Sección 1A","Sección 1B", "Sección 1C", "Ayuda"
    };

    private static String opciones[] = 
    { 
        "Opción 1A","Opción 1B", "Opción 1C"
    };

    private static String generadores[] = 
    { 
        "26.1a", "26.1b", "26.2", "26.3", "Combinado", "Fishman y Moore", "RANDU"
    };

    static JFrame f;
    private ButtonListener bl = new ButtonListener();
    //crea nuevo JDialog por cada boton
    class ButtonListener implements ActionListener //listeners de los botones
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

    private ActionListener al = new ActionListener() //listeners del menu
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

    private JPanel crearPanelBotones()
    {
        JPanel panelBotones = new JPanel();
        SpringLayout layout = new SpringLayout();
        panelBotones.setLayout(layout);
        Choice ch = new Choice();
        for(int i = 0; i < generadores.length; i++) 
            ch.addItem(generadores[i]);
        generador_escogido = ch.getSelectedIndex();
        panelBotones.add(ch);
        return panelBotones;
    }

    private JMenuBar SimpleMenus()
    {
        JMenuBar mb = new JMenuBar(); 
        JMenu menu;
        JMenu submenu;
        JMenuItem item;
        //creo los items que tienen mas de un subnivel
        for(int i = 0; i < itemsMenu.length-1; i++) 
        {
            menu = new JMenu(menus[i]); //creo el menu
            submenu = new JMenu(opciones[i]); //creo el submenu
            submenu.add(new JMenuItem(itemsMenu[i])).addActionListener(al); //al submenu le aniado el Item (este si es clickable)
            menu.add(submenu); //aniado el submenu al menu
            mb.add(menu); //aniado a la barra de menus todo lo anterior
        }
        //este es el panel de acerca de-ayuda y solo tiene 1 subnivel
        menu = new JMenu(menus[itemsMenu.length-1]); 
        menu.add(itemsMenu[itemsMenu.length-1]).addActionListener(al);
        mb.add(menu).addActionListener(al);;
        return mb;
    }


    public randomGenerator(String nombre) 
    {
        super(nombre);
        f = this;

        setJMenuBar(SimpleMenus()); //crear menu
        
        add(crearPanelBotones()); //panel botones

        //JButton image = new JButton(new ImageIcon(getClass().getResource("grogu.jpg"))); 
        //add(BorderLayout.WEST, image);
    }

    public double[] veintiseis1a()
    {
        double[] aleatorios = new double[n_puntos];
        double x0 = 1;
        for(int i = 0; i < n_puntos; i++)
        {
            aleatorios[i] = 5 * x0 % 2e5;
            x0 = aleatorios[i]; 
            aleatorios[i] /= 2e5; 
        }
        return aleatorios;
    }

    public double[] veintiseis1b()
    {
        double[] aleatorios = new double[n_puntos];
        double x0 = 1;
        for(int i = 0; i < n_puntos; i++)
        {
            aleatorios[i] = 7 * x0 % 2e5;
            x0 = aleatorios[i]; 
            aleatorios[i] /= 2e5; 
        }
        return aleatorios;
    }

    public double[] veintiseis2()
    {
        double[] aleatorios = new double[n_puntos];
        double x0 = 1;
        for(int i = 0; i < n_puntos; i++)
        {
            aleatorios[i] = 3 * x0 % 31;
            x0 = aleatorios[i]; 
            aleatorios[i] /= 31; 
        }
        return aleatorios;
    }

    public double[] veintiseis3()
    {
        double[] aleatorios = new double[n_puntos];
        double x0 = 1;
        for(int i = 0; i < n_puntos; i++)
        {
            aleatorios[i] = 7e5 * x0 % (2e31 -1);
            x0 = aleatorios[i]; 
            aleatorios[i] /= (2e31 -1); 
        }
        return aleatorios;
    }

    public double[] FishmanMoore1()
    {
        double[] aleatorios = new double[n_puntos];
        double x0 = 1;
        for(int i = 0; i < n_puntos; i++)
        {
            aleatorios[i] = 48271 * x0 % (2e31 -1);
            x0 = aleatorios[i]; 
            aleatorios[i] /= (2e31 -1); 
        }
        return aleatorios;
    }

    public double[] FishmanMoore2()
    {
        double[] aleatorios = new double[n_puntos];
        double x0 = 1;
        for(int i = 0; i < n_puntos; i++)
        {
            aleatorios[i] = 69621 * x0 % (2e31 -1);
            x0 = aleatorios[i]; 
            aleatorios[i] /= (2e31 -1); 
        }
        return aleatorios;
    }

    public double[] RANDU()
    {
        double[] aleatorios = new double[n_puntos];
        double x0 = 1;
        for(int i = 0; i < n_puntos; i++)
        {
            aleatorios[i] = (2e16+3)* x0 % (2e31);
            x0 = aleatorios[i]; 
            aleatorios[i] /= (2e31); 
        }
        return aleatorios;
    }

    public static void main(String args[]) 
    {
        randomGenerator frame = new randomGenerator("Generador de números aleatorios");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setSize(700, 600);
        frame.setVisible(true); 
    }
        
}