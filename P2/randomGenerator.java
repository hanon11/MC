import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import java.util.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.math.BigInteger;


public class randomGenerator extends JFrame 
{
    private int n_puntos;
    private int generador_escogido = 0;
    private double[] vector_puntos;
    private static JPanel grafica;
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
            if(name.equals("Generar"))
            {
                grafica.setVisible(false); grafica.setVisible(true);

            }
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

    public class Puntos extends JPanel 
    {
        public void paint(Graphics g) 
        {
            Image img = createImageWithText();
            g.drawImage(img, 20,50,this);
            this.setPreferredSize( new Dimension( 400, 300 ) );
        }

        private Image createImageWithText() 
        {
            int ancho      = 700;
            int alto       = 370;
            int num_puntos = n_puntos;
            BufferedImage bufferedImage = new BufferedImage( ancho, alto, BufferedImage.TYPE_INT_RGB );
            Graphics g = bufferedImage.getGraphics();
            g.setColor(Color.BLUE);
            switch(generador_escogido)
            {
                case 0:
                    vector_puntos = veintiseis1a();
                    //System.out.println(Arrays.toString(vector_puntos));
                break;
                case 1:
                    //vector_puntos = veintiseis1b();
                break;
                case 2:
                    //vector_puntos = veintiseis2();
                break;
                case 3:
                    //vector_puntos = veintiseis3();
                break;
                case 4:
                break;
                case 5:
                break;
                case 6:
                break;
            }
            double x, y;
            int i = 0;
            while(i < num_puntos/2)
            {
                x = vector_puntos[i];
                y = vector_puntos[i+1];
                i += 2;
                g.drawOval((int)x, (int)y, 1, 1);   // o tambien...
            } 
            return bufferedImage;
        }
    };

    

    private JPanel crearPanelBotones()
    {
        JPanel panelBotones = new JPanel();
        SpringLayout layout = new SpringLayout();
        panelBotones.setLayout(layout);
        panelBotones.setPreferredSize( new Dimension( 340, 280 ) );
        Choice ch = new Choice();
        JLabel label = new JLabel("Generador de números aleatorios:");   
        panelBotones.add(label);   
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, label, 170, SpringLayout.NORTH, panelBotones);
         
        for(int i = 0; i < generadores.length; i++) 
            ch.addItem(generadores[i]);
        generador_escogido = ch.getSelectedIndex();
        panelBotones.add(ch);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, ch, 0, SpringLayout.HORIZONTAL_CENTER, label);
        layout.putConstraint(SpringLayout.NORTH, ch, 30, SpringLayout.NORTH, label);

        JLabel label2 = new JLabel("Números de dígitos aleatorios:");  
        panelBotones.add(label2);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label2, 0, SpringLayout.HORIZONTAL_CENTER, ch);
        layout.putConstraint(SpringLayout.NORTH, label2, 30, SpringLayout.NORTH, ch);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
        spinner.setSize(70, 10);
        spinner.addChangeListener(new ChangeListener() 
        {
            public void stateChanged(ChangeEvent e) 
            {
                n_puntos = (int)spinner.getValue();
            }
        
        });

        panelBotones.add(spinner);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, spinner, 0, SpringLayout.HORIZONTAL_CENTER, label2);
        layout.putConstraint(SpringLayout.NORTH, spinner, 30, SpringLayout.NORTH, label2);

        JButton b1 = new JButton("Generar");
        b1.addActionListener(bl);
        panelBotones.add(b1);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, b1, 0, SpringLayout.HORIZONTAL_CENTER, spinner);
        layout.putConstraint(SpringLayout.NORTH, b1, 30, SpringLayout.NORTH, spinner);

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
        mb.add(menu).addActionListener(al);
        return mb;
    }


    public randomGenerator(String nombre) 
    {
        super(nombre);
        f = this;

        setJMenuBar(SimpleMenus()); //crear menu
        add(crearPanelBotones(), BorderLayout.EAST); //panel botones
        grafica = new Puntos();
        add(grafica, BorderLayout.CENTER);    
    }

    public double[] veintiseis1a()
    {
        double[] aleatorios = new double[n_puntos];
        double x0 = 1;
        //System.out.println(n_puntos);
        for(int i = 0; i < n_puntos; i++)
        {
            aleatorios[i] = 5 * x0 % 2e5;
            x0 = aleatorios[i]; 
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
        frame.setSize(1000, 700);
        frame.setVisible(true); 
    }
        
}