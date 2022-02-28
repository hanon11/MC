import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


public class ca1DSim extends JFrame 
{
    private static int conf_escogida;
    private static int n_generaciones;
    private static int n_vecinos = 1;
    private static int n_estados;
    private static int regla;
    private static int posibles_estados = 3 * n_estados - 2;
    private static int[] code;
    private static int[] t_1 = {0,0,0,1,0,0,0}, actual = {0,0,0,1,0,0,0};
    private static JPanel panel;
    private static Graphics g;

    public static void calcular_estado_actual()
    {
        int sumatorio;
        for(int k = 0; k < n_generaciones; k++)
        {
            for(int i = 0; i < actual.length; i++)
            {
                sumatorio = 0;
                for(int j = i-n_vecinos; j <= i+n_vecinos; j++)
                {
                    if(j < 0)
                        sumatorio += t_1[t_1.length+j];
                    else if(j > t_1.length-1) 
                        sumatorio += t_1[j-t_1.length];
                    else 
                        sumatorio += t_1[j];
                }
                //System.out.println(sumatorio);
                actual[i] = code[sumatorio % posibles_estados]; //para que no se salga del vector
            }
            for(int i = 0; i < actual.length; i++)
                t_1[i] = actual[i];
            System.out.println(Arrays.toString(actual));
        }
    }
    public static void reset()
    {
        int[] aux = {0,0,0,1,0,0,0};
        for(int i = 0; i < aux.length; i++)
                t_1[i] = aux[i];
        for(int i = 0; i < aux.length; i++)
                actual[i] = aux[i];
    }

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

    private static String configuracion[] = 
    { 
        "Aleatoria", "Celula central activa"
    };
    private static String estados[] = 
    { 
        "2 estados", "3 estados", "4 estados", "5 estados"
    };

    private void pasar_a_baseK()
    {
        code = new int[posibles_estados];
    }

    public class Puntos extends JPanel 
    {
        public void paint(Graphics g) 
        {
            Image img = createImageWithText();
            g.drawImage(img, 20,90,this);
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
            if(name.equals("Start"))
            {
                Image img = Puntos.createImageWithText();
                g.drawImage(img, 0, 0, null);
                panel.setVisible(false);
                panel.setVisible(true);
                g.setColor(Color.white);
               //generamos y pintamos puntos...
                int i = 0, x, y;
                calcular_estado_actual();
                
            }
            if(name.equals("Reset"))
            {
                Image img = Puntos.createImageWithText();
                g.drawImage(img, 0, 0, null);
                panel.setVisible(false);
                panel.setVisible(true);
                g.setColor(Color.white);
               //generamos y pintamos puntos...
                int i = 0, x, y;
                reset();
                
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
        JLabel label = new JLabel("Configuracion:");   
        panelBotones.add(label);   
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, label, 100, SpringLayout.NORTH, panelBotones);
         
        for(int i = 0; i < configuracion.length; i++) 
            ch.addItem(configuracion[i]);
        
        ch.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                conf_escogida = ch.getSelectedIndex();
            }
        });

        panelBotones.add(ch);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, ch, 0, SpringLayout.HORIZONTAL_CENTER, label);
        layout.putConstraint(SpringLayout.NORTH, ch, 30, SpringLayout.NORTH, label);

        JLabel label2 = new JLabel("Numero de estados por celula:");  
        panelBotones.add(label2);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label2, 0, SpringLayout.HORIZONTAL_CENTER, ch);
        layout.putConstraint(SpringLayout.NORTH, label2, 30, SpringLayout.NORTH, ch);


        JComboBox<String> combo = new JComboBox<String>();
        for(int i = 0; i < estados.length; i++) 
            combo.addItem(estados[i]);
        combo.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                n_estados = (int)combo.getSelectedIndex() + 2; // 0 -> 2 estados ... 3 -> 5 estados
                posibles_estados = 3 * n_estados - 2;
            }
        });

        panelBotones.add(combo);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, combo, 0, SpringLayout.HORIZONTAL_CENTER, label2);
        layout.putConstraint(SpringLayout.NORTH, combo, 30, SpringLayout.NORTH, label2);


        JLabel label3 = new JLabel("Numero de generaciones:");  
        panelBotones.add(label3);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label3, 0, SpringLayout.HORIZONTAL_CENTER, combo);
        layout.putConstraint(SpringLayout.NORTH, label3, 30, SpringLayout.NORTH, combo);

        JSpinner spinner2 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinner2.setSize(70, 10);
        spinner2.addChangeListener(new ChangeListener() 
        {
            public void stateChanged(ChangeEvent e) 
            {
                n_generaciones = (int)spinner2.getValue();
            }
        });

        panelBotones.add(spinner2);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, spinner2, 0, SpringLayout.HORIZONTAL_CENTER, label3);
        layout.putConstraint(SpringLayout.NORTH, spinner2, 30, SpringLayout.NORTH, label3);

        JLabel label4 = new JLabel("Numero de vecinos:");  
        panelBotones.add(label4);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label4, 0, SpringLayout.HORIZONTAL_CENTER, spinner2);
        layout.putConstraint(SpringLayout.NORTH, label4, 30, SpringLayout.NORTH, spinner2);

        JSpinner spinner3 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinner3.setSize(70, 10);
        spinner3.addChangeListener(new ChangeListener() 
        {
            public void stateChanged(ChangeEvent e) 
            {
                n_vecinos = (int)spinner3.getValue();
            }
        });

        panelBotones.add(spinner3);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, spinner3, 0, SpringLayout.HORIZONTAL_CENTER, label4);
        layout.putConstraint(SpringLayout.NORTH, spinner3, 30, SpringLayout.NORTH, label4);

        JLabel label5 = new JLabel("Regla:");  
        panelBotones.add(label5);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label5, 0, SpringLayout.HORIZONTAL_CENTER, spinner3);
        layout.putConstraint(SpringLayout.NORTH, label5, 30, SpringLayout.NORTH, spinner3);
        JSpinner spinner4 = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        spinner4.setSize(70, 10);
        spinner4.addChangeListener(new ChangeListener() 
        {
            public void stateChanged(ChangeEvent e) 
            {
                regla = (int)spinner4.getValue();
                if(n_estados + 2 == 2)
                    decimal_a_binario_2();
                else 
                    decimal_a_baseK();
            }
        });
        
        panelBotones.add(spinner4);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, spinner4, 0, SpringLayout.HORIZONTAL_CENTER, label5);
        layout.putConstraint(SpringLayout.NORTH, spinner4, 30, SpringLayout.NORTH, label5);

        JButton b1 = new JButton("Start");
        b1.addActionListener(bl);
        panelBotones.add(b1);
        layout.putConstraint(SpringLayout.EAST, b1, -180, SpringLayout.EAST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b1, 60, SpringLayout.NORTH, spinner4);
        JButton b2 = new JButton("Reset");
        b2.addActionListener(bl);
        panelBotones.add(b2);
        layout.putConstraint(SpringLayout.EAST, b2, -100, SpringLayout.EAST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b2, 60, SpringLayout.NORTH, spinner4);

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


    public ca1DSim(String nombre) 
    {
        super(nombre);
        f = this;
        setJMenuBar(SimpleMenus()); //crear menu
        add(crearPanelBotones(), BorderLayout.EAST); //panel botones
        panel = new Puntos();
        add(panel, BorderLayout.CENTER);    
        
    }

    public static void main(String args[]) 
    {
        ca1DSim frame = new ca1DSim("Automatas celulares 1-D.");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setSize(1000, 700);
        frame.setVisible(true); 
    }
        

    //automata de solo 2 estados 
    public static void decimal_a_binario_2()
    {
        code = new int[8];
        int i = 0, decimal = regla;

        while(decimal > 0)
        {
            code[i++] = decimal % 2;
            decimal = decimal / 2;
        }
    }

    //automata cualquier otro caso
    public static void decimal_a_baseK()
    {
        code = new int[posibles_estados];
        int i = 0, decimal = regla;

        while(decimal > 0)
        {
            code[i++] = decimal % n_estados;
            decimal = decimal / n_estados;
        }
        System.out.println("Regla: " +Arrays.toString(code));
    }
}