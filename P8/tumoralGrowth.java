import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JSpinner;


public class tumoralGrowth extends JFrame
{
    private static int tam = 500, n_generaciones = 2,i=0;
    private static double Ps = 1, Pp = 0.25, NP = 1, Pm = 0.2 ,PH =0, P1, P2,P3,P4;
    private static int[] celulasVivas = new int[tam];
    private static JFrame f;
    private static String[] menus = { "Opcion A","Opcion B","Opcion C", "Acerca de" };
    private static String[] itemsMenu = { "Seccion 1A","Seccion 1B", "Seccion 1C", "Ayuda" };
    private static String[] opciones = { "Opcion 1A","Opcion 1B", "Opcion 1C" };
    private static Graphics g;
    private static Puntos panel;
    private static byte[][] t_1 = new byte[tam][tam], actual = new byte[tam][tam];
    private static boolean centinela = true, curvaCrecimiento;


    public static void rellenar_matrices()
    {
        for ( int i = 0; i < tam; i++ )
        {
            for ( int j = 0; j < tam; j++ )
                t_1[i][j] = 0;
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
                try{ Thread.sleep(30); }catch (Exception exc) {};
                paintImmediately(panel.getBounds());
            }).run();

        }

        public void proliferation(int i, int j)
        {
            P1 = (double)(1-t_1[i-1][j])/(4-(t_1[i+1][j]+t_1[i-1][j]+t_1[i][j+1]+t_1[i][j-1]));
            P2 = (double)(1-t_1[i+1][j])/(4-(t_1[i+1][j]+t_1[i-1][j]+t_1[i][j+1]+t_1[i][j-1]));
            P3 = (double)(1-t_1[i][j-1])/(4-(t_1[i+1][j]+t_1[i-1][j]+t_1[i][j+1]+t_1[i][j-1]));
            P4 = (double)(1-t_1[i][j+1])/(4-(t_1[i+1][j]+t_1[i-1][j]+t_1[i][j+1]+t_1[i][j-1]));
        }

        public void funcionTransicion()
        {
            for ( int i = 0; i < tam; i++ )
            {
                for (int j = 0; j < tam; j++)
                {
                    double rrp = Math.random();
                    double rr = Math.random();
                    double rrm = Math.random();
                    if( rr >= Ps )
                        actual[i][j] = 0;
                    else
                    {
                        if( rrp >= Pp )
                        {
                            if( rrm >= Pm )
                            {
                                //quiescent
                            }
                            else
                            {
                                //migration
                            }
                        }
                        else
                        {
                            PH++;
                            if( PH < NP )
                            {
                                if( rrp >= Pp )
                                {
                                    if( rrm >= Pm )
                                    {
                                        //quiescent
                                    }
                                    else
                                    {
                                        //migration
                                    }
                                }
                            }
                            else
                            {
                                //proliferation
                            }
                        }
                    }
                }
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
                            g.setColor(Color.WHITE);
                            g.drawOval(i, j, 2, 2);
                        } else {
                            g.setColor(Color.BLUE);
                            g.drawOval(i, j, 2, 2);
                        }
                    }
                }
            }
        }
    }


    public static class grafPobCelular extends JPanel
    {
        private int[] dat;
        private int puntos;
        private int[] Xdat;
        private int[] Ydat;

        public grafPobCelular()
        {
            super();
            dat = celulasVivas;
            puntos = dat.length;
            Xdat = new int[puntos];
            Ydat = new int[puntos];
            //separamos puntos para coordenadas x e y...
            for (int i = 0; i < puntos; i++)
            {
                Xdat[i] = i;
                Ydat[i] = dat[i];
            }
        }
        public void paint(Graphics g)
        {
            Image img = createImageWithText2();
            g.drawImage(img, 5,90,this);
        }

        public Image createImageWithText2()
        {
            dat = celulasVivas;
            puntos = dat.length;
            Xdat = new int[puntos];
            Ydat = new int[puntos];
            int ancho = tam;
            int alto = tam;
            BufferedImage bufferedImage = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            g = bufferedImage.getGraphics();
            for (int i = 0; i < puntos; i++)
            {
                Xdat[i] = i;
                Ydat[i] = dat[i];
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(3));

            for (int i = 0; i < puntos-1; i++)
            {
                int x0 = Xdat[i];
                int x1 = Xdat[i + 1];
                int y0 = tam - Ydat[i];
                int y1 = tam - Ydat[i + 1];
                g2.drawLine( x0, y0, x1, y1 );
            }
            return bufferedImage;
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
                if(curvaCrecimiento)
                {
                    JDialog d = new JDialog(f, "Evolucion grafica del estado de la reticula");
                    d.add(new grafPobCelular());
                    d.setSize(800, 850);
                    d.setVisible(true);
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


        JLabel label3 = new JLabel("Numero de generaciones:");
        panelBotones.add(label3);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label3, 0, SpringLayout.HORIZONTAL_CENTER, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, label3, 40, SpringLayout.NORTH, panelBotones);

        JSpinner spinner2 = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
        spinner2.setSize(70, 10);
        spinner2.addChangeListener(e -> n_generaciones = (int) spinner2.getValue());

        panelBotones.add(spinner2);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, spinner2, 0, SpringLayout.HORIZONTAL_CENTER, label3);
        layout.putConstraint(SpringLayout.NORTH, spinner2, 30, SpringLayout.NORTH, label3);


        JCheckBox checkPoblacion = new JCheckBox("Curva de poblacion popular");
        checkPoblacion.addChangeListener(e -> curvaCrecimiento = checkPoblacion.isSelected());
        panelBotones.add(checkPoblacion);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, checkPoblacion, 0, SpringLayout.HORIZONTAL_CENTER, spinner2);
        layout.putConstraint(SpringLayout.NORTH, checkPoblacion, 30, SpringLayout.NORTH, spinner2);
        JButton b1 = new JButton("Inicio");
        b1.addActionListener(bl);
        panelBotones.add(b1);
        layout.putConstraint(SpringLayout.EAST, b1, -180, SpringLayout.EAST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b1, 60, SpringLayout.NORTH, checkPoblacion);
        JButton b2 = new JButton("Reset");
        b2.addActionListener(bl);
        panelBotones.add(b2);
        layout.putConstraint(SpringLayout.EAST, b2, -100, SpringLayout.EAST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b2, 60, SpringLayout.NORTH, checkPoblacion);

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


    public tumoralGrowth(String nombre)
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
        tumoralGrowth frame = new tumoralGrowth("Conway's Game of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setVisible(true);
    }
}



