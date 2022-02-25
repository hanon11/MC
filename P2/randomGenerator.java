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
    private static int n_puntos;
    private static int generador_escogido;
    private static BigInteger[] vector_puntos;
    private static BigInteger[] vector_puntos2;
    private static JPanel panel;
    private static Graphics g;
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
        "26.1a", "26.1b", "26.2", "26.3", "Combinado", "Fishman", "Moore", "RANDU"
    };

    public class Puntos extends JPanel 
    {
        public void paint(Graphics g) 
        {
            Image img = createImageWithText();
            g.drawImage(img, 20,50,this);
        }

        public static Image createImageWithText() 
        {
            int ancho      = 700;
            int alto       = 370;
            BufferedImage bufferedImage = new BufferedImage( ancho, alto, BufferedImage.TYPE_INT_RGB );
            g = bufferedImage.getGraphics();
            g.setColor(Color.BLUE);
            int x, y;
            int i = 0;
            switch(generador_escogido)
            {
                case 0:
                    vector_puntos = veintiseis1a();
                    //System.out.println(Arrays.toString(vector_puntos));
                    break;
                case 1:
                    vector_puntos = veintiseis1b();
                    //System.out.println(Arrays.toString(vector_puntos));
                    break;
                case 2:
                    vector_puntos = veintiseis2();
                    //System.out.println(Arrays.toString(vector_puntos));
                    break;
                case 3:
                    vector_puntos = veintiseis3();
                    //System.out.println(Arrays.toString(vector_puntos));
                    break;
                case 4:
                    vector_puntos = combinado();
                    //System.out.println(Arrays.toString(vector_puntos));
                    break;
                case 5:
                    vector_puntos = Fishman();
                    //System.out.println(Arrays.toString(vector_puntos));
                    break;
                case 6:
                    vector_puntos = Moore();
                    //System.out.println(Arrays.toString(vector_puntos));
                    break;
                case 7:
                    vector_puntos = RANDU();
                    //System.out.println(Arrays.toString(vector_puntos));
                    break;
                default:
                    break;
            }
            
            while(i < n_puntos)
            {
                x = vector_puntos[i].intValue() % 700;
                y = vector_puntos[i+1].intValue() % 370;
                i += 2;
                //System.out.println(i);
                //g.drawLine(x, y, x, y);  // o tambien...
                g.drawOval(x, alto-y, 3, 3);
                g.fillOval(x, alto-y, 3, 3);
            } 
            return bufferedImage;
        }
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
                Image img = Puntos.createImageWithText();
                g.drawImage(img, 0, 0, null);
                panel.setVisible(false);
                panel.setVisible(true);
                g.setColor(Color.white);
               //generamos y pintamos puntos...
                int i = 0, x, y;
                while(i < n_puntos)
                {
                    x = vector_puntos[i].intValue() % 700;
                    y = vector_puntos[i+1].intValue() % 370;
                    i += 2;
                    //System.out.println(i);
                    //g.drawLine(x, y, x, y);  // o tambien...
                    g.drawOval(x, 370-y, 3, 3);
                    g.fillOval(x, 370-y, 3, 3);
                } 
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
        ch.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                generador_escogido = ch.getSelectedIndex();
            }
        });

        panelBotones.add(ch);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, ch, 0, SpringLayout.HORIZONTAL_CENTER, label);
        layout.putConstraint(SpringLayout.NORTH, ch, 30, SpringLayout.NORTH, label);

        JLabel label2 = new JLabel("Números de dígitos aleatorios:");  
        panelBotones.add(label2);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label2, 0, SpringLayout.HORIZONTAL_CENTER, ch);
        layout.putConstraint(SpringLayout.NORTH, label2, 30, SpringLayout.NORTH, ch);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 9999999, 1));
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
        panel = new Puntos();
        add(panel, BorderLayout.CENTER);    
    }

    public static BigInteger[] veintiseis1a()
    {
        BigInteger[] aleatorios = new BigInteger[n_puntos];
        BigInteger x0 = BigInteger.ONE;
        //System.out.println(n_puntos);
        for(int i = 0; i < n_puntos; i++)
        {
            aleatorios[i] = x0.multiply(BigInteger.valueOf(5));
            aleatorios[i] = aleatorios[i].mod(BigInteger.valueOf((int)2e5));
            x0 = aleatorios[i]; 
        }
        return aleatorios;
    }

    public static BigInteger[] veintiseis1b()
    {
        BigInteger[] aleatorios = new BigInteger[n_puntos];
        BigInteger x0 = BigInteger.ONE;
        for(int i = 0; i < n_puntos; i++)
        {
            aleatorios[i] = x0.multiply(BigInteger.valueOf(7));
            aleatorios[i] = aleatorios[i].mod(BigInteger.valueOf((int)2e5));
            x0 = aleatorios[i]; 
        }
        return aleatorios;
    }

    public static BigInteger[] veintiseis2()
    {
        BigInteger[] aleatorios = new BigInteger[n_puntos];
        BigInteger x0 = BigInteger.ONE;
        for(int i = 0; i < n_puntos; i++)
        {
            aleatorios[i] = x0.multiply(BigInteger.valueOf(3));
            aleatorios[i] = aleatorios[i].mod(BigInteger.valueOf(31));
            x0 = aleatorios[i]; 
            //aleatorios[i] /= 31; 
        }
        return aleatorios;
    }

    public static BigInteger[] veintiseis3()
    {
        BigInteger[] aleatorios = new BigInteger[n_puntos];
        BigInteger x0 = BigInteger.ONE;
        for(int i = 0; i < n_puntos; i++)
        {
            aleatorios[i] = x0.multiply(BigInteger.valueOf((long)7e5));
            aleatorios[i] = aleatorios[i].mod(BigInteger.valueOf((long)(2e31 -1)));
            x0 = aleatorios[i]; 
            //aleatorios[i] /= (2e31 -1); 
        }
        return aleatorios;
    }

    public static BigInteger[] combinado()
    {
        BigInteger[] aleatorios = new BigInteger[n_puntos];
        BigInteger wn = BigInteger.ONE, xn = BigInteger.ONE, yn = BigInteger.ONE;
        for(int i = 0; i < n_puntos; i++)
        {
            wn = wn.multiply(BigInteger.valueOf(157));
            wn = wn.mod(BigInteger.valueOf(32363));
            xn = xn.multiply(BigInteger.valueOf(146));
            xn = xn.mod(BigInteger.valueOf(31727));
            yn = yn.multiply(BigInteger.valueOf(142));
            yn = yn.mod(BigInteger.valueOf(31657));
            aleatorios[i] = (wn.subtract(xn).add(yn)); 
            aleatorios[i] = aleatorios[i].mod(BigInteger.valueOf(32362));
        }
        return aleatorios;
    }

    public static BigInteger[] Fishman()
    {
        BigInteger[] aleatorios = new BigInteger[n_puntos];
        BigInteger x0 = BigInteger.ONE;
        for(int i = 0; i < n_puntos; i++)
        {
            aleatorios[i] = x0.multiply(BigInteger.valueOf(48271));
            aleatorios[i] = aleatorios[i].mod(BigInteger.valueOf((long)(2e31 -1)));
            x0 = aleatorios[i]; 
        }
        return aleatorios;
    }

    public static BigInteger[] Moore()
    {
        BigInteger[] aleatorios = new BigInteger[n_puntos];
        BigInteger x0 = BigInteger.ONE;
        for(int i = 0; i < n_puntos; i++)
        {
            aleatorios[i] = x0.multiply(BigInteger.valueOf(69621));
            aleatorios[i] = aleatorios[i].mod(BigInteger.valueOf((long)(2e31 -1)));
            x0 = aleatorios[i]; 
        }
        return aleatorios;
    }


    public static BigInteger[] RANDU()
    {
        BigInteger[] aleatorios = new BigInteger[n_puntos];
        BigInteger x0 = BigInteger.ONE;
        for(int i = 0; i < n_puntos; i++)
        {
            aleatorios[i] = x0.multiply(BigInteger.valueOf((long)(2e16+3)));
            aleatorios[i] = aleatorios[i].mod(BigInteger.valueOf((long)2e31));
            x0 = aleatorios[i]; 
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