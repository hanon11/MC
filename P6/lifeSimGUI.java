import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;
import javax.swing.JSpinner;


public class lifeSimGUI extends JFrame
{
    // celula viva ==> 1 ||||||||| celula muerta ==> 0
    private static int tam = 600, n_generaciones = 2, i = 0;;
    private static String[] v = {"Aleatorio","Isla de bacterias","Cañones de planeadores"};
    private static JFrame f;
    private static String[] menus = { "Opcion A","Opcion B","Opcion C", "Acerca de" };
    private static String[] itemsMenu = { "Sección 1A","Sección 1B", "Sección 1C", "Ayuda" };
    private static String[] opciones = { "Opción 1A","Opción 1B", "Opción 1C" };
    private static Graphics g;
    private static Puntos panel;
    private static int conf_escogida = 0;  //0 -> aleatoria || 1 -> islas de bacterias || 2 -> cannones de planeadores
    private static int[][] t_1 = new int[tam][tam], actual = new int[tam][tam];
    private static boolean centinela = true;
    private static JLabel imgLab;

    public static void rellenar_matrices()
    {
        switch (conf_escogida)
        {
            case 0 -> {
                for ( int i = 0; i < tam; i++ )
                {
                    for ( int j = 0; j < tam; j++ )
                        t_1[i][j] = (int) Math.floor(Math.random()*(2));
                }
            }
            case 1 -> {
                   //todo islas de bacterias
            }
            case 2 -> {
                //todo cannones de planeadores
            }
            default -> {

            }

        }
        for ( int i = 0; i < tam; i++ ) //una vez hechos todos los cambios, actualizamos para la proxima iteracion
        {
            System.arraycopy(t_1[i], 0, actual[i], 0, tam);
        }
    }



    public static void reset()
    {
        rellenar_matrices();
    }

    // ======================================== DIBUJO AUTOMATA ====================================================================

    public static class Puntos extends JPanel
    {
        private int ancho = tam;
        private int alto = tam;


        public Puntos()
        {
            super();
            this.repaint();
        }

        public void generar()
        {
            new Thread(() -> {
                funcionTransicion();
                try{ Thread.sleep(300); }catch (Exception exc) {};
                paintImmediately(panel.getBounds());
            }).run();

        }
        public void funcionTransicion()
        {
            int sumatorio;
            for ( int i = 0; i < tam; i++ )
            {
                for ( int j = 0; j < tam; j++ )
                {
                    sumatorio = 0;
                    if( j > 0 )
                    {
                        if( t_1[i][j-1] == 1 )
                            sumatorio++;
                    }
                    if( j < tam-1 )
                    {
                        if( t_1[i][j+1] == 1 )
                            sumatorio++;
                    }
                    if( i > 0 )
                    {
                        if( t_1[i-1][j] == 1)
                            sumatorio++;

                    }
                    if( i < tam-1 )
                    {
                        if( t_1[i+1][j] == 1 )
                            sumatorio++;
                    }
                    if( t_1[i][j] == 1 )
                    {
                        if( sumatorio < 2 ) //menos de dos vecinas vivas
                            actual[i][j] = 0;
                        if( sumatorio == 2 || sumatorio == 3 ) //dos o tres vecinas vivas
                            actual[i][j] = 1;
                        if( sumatorio > 3 ) //sobrepoblacion
                            actual[i][j] = 0;
                    }
                    else
                    {
                        if( sumatorio == 3 ) //colonizacion
                            actual[i][j] = 1;
                    }
                }
            }
            for ( int i = 0; i < tam; i++ ) //una vez hechos todos los cambios, actualizamos para la proxima iteracion
            {
                System.arraycopy(actual[i], 0, t_1[i], 0, tam);
            }
        }

        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if ( centinela )
            {
                for( int i = 0; i< ancho; i++ )
                {
                    for (int j = 0; j < alto; j++)
                    {
                        g.setColor(Color.BLACK);
                        g.drawOval(i, j, 1, 1);
                    }
                }
                centinela = false;
            } else {
                for (int i = 0; i < ancho; i++) {
                    for (int j = 0; j < alto; j++) {
                        if (actual[i][j] == 0) {
                            g.setColor(Color.BLUE);
                            g.drawOval(i, j, 2, 2);
                        } else {
                            g.setColor(Color.RED);
                            g.drawOval(i, j, 2, 2);
                        }
                    }
                }
            }
        }
    }



    //================================================== LISTENERS BOTONES ==========================================
    private ButtonListener bl = new ButtonListener();

    static class ButtonListener implements ActionListener //listeners de los botones
    {
        public void actionPerformed(ActionEvent e)
        {
            String name = ((JButton) e.getSource()).getText();
            if (name.equals("Inicio"))
            {
                rellenar_matrices();
                for (int i = 0; i < n_generaciones; i++)
                {
                    panel.generar();
                }
            }
            if (name.equals("Reset"))
            {
                panel.repaint();
                panel.setVisible(false);
                panel.setVisible(true);
                centinela = true;
                reset();
            }
        }
    }

    private ActionListener al = new ActionListener() //listeners del menu
    {
        public void actionPerformed(ActionEvent e)
        {
            String name = ((JMenuItem) e.getSource()).getText();
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
        panelBotones.setPreferredSize(new Dimension(340, 280));

        JLabel label2 = new JLabel("Configuracion inicial:");
        panelBotones.add(label2);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label2, 0, SpringLayout.HORIZONTAL_CENTER, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, label2, 100, SpringLayout.NORTH, panelBotones);


        JComboBox<String> combo = new JComboBox<>();
        for (String estado : v) combo.addItem(estado);
        combo.addActionListener(e -> conf_escogida = combo.getSelectedIndex());

        panelBotones.add(combo);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, combo, 0, SpringLayout.HORIZONTAL_CENTER, label2);
        layout.putConstraint(SpringLayout.NORTH, combo, 30, SpringLayout.NORTH, label2);


        JLabel label3 = new JLabel("Numero de generaciones:");
        panelBotones.add(label3);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label3, 0, SpringLayout.HORIZONTAL_CENTER, combo);
        layout.putConstraint(SpringLayout.NORTH, label3, 40, SpringLayout.NORTH, combo);

        JSpinner spinner2 = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
        spinner2.setSize(70, 10);
        spinner2.addChangeListener(e -> n_generaciones = (int) spinner2.getValue());

        panelBotones.add(spinner2);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, spinner2, 0, SpringLayout.HORIZONTAL_CENTER, label3);
        layout.putConstraint(SpringLayout.NORTH, spinner2, 30, SpringLayout.NORTH, label3);



        JButton b1 = new JButton("Inicio");
        b1.addActionListener(bl);
        panelBotones.add(b1);
        layout.putConstraint(SpringLayout.EAST, b1, -180, SpringLayout.EAST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b1, 50, SpringLayout.NORTH, spinner2);
        JButton b2 = new JButton("Reset");
        b2.addActionListener(bl);
        panelBotones.add(b2);
        layout.putConstraint(SpringLayout.EAST, b2, -100, SpringLayout.EAST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b2, 50, SpringLayout.NORTH, spinner2);

        return panelBotones;
    }

    private JMenuBar SimpleMenus() {
        JMenuBar mb = new JMenuBar();
        JMenu menu;
        JMenu submenu;
        //creo los items que tienen mas de un subnivel
        for (int i = 0; i < itemsMenu.length - 1; i++) {
            menu = new JMenu(menus[i]); //creo el menu
            submenu = new JMenu(opciones[i]); //creo el submenu
            submenu.add(new JMenuItem(itemsMenu[i])).addActionListener(al); //al submenu le aniado el Item (este si es clickable)
            menu.add(submenu); //aniado el submenu al menu
            mb.add(menu); //aniado a la barra de menus todo lo anterior
        }
        //este es el panel de acerca de-ayuda y solo tiene 1 subnivel
        menu = new JMenu(menus[itemsMenu.length - 1]);
        menu.add(itemsMenu[itemsMenu.length - 1]).addActionListener(al);
        mb.add(menu).addActionListener(al);
        return mb;
    }


    public lifeSimGUI(String nombre)
    {
        super(nombre);
        f = this;
        setJMenuBar(SimpleMenus()); //crear menu
        add(crearPanelBotones(), BorderLayout.EAST); //panel botones
        panel = new Puntos();
        add(panel, BorderLayout.CENTER);

    }

    public static void main(String[] args)
    {
        lifeSimGUI frame = new lifeSimGUI("Conway's Game of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setVisible(true);
    }
}



