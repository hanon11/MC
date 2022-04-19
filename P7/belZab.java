import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JSpinner;

public class belZab extends JFrame
{
    // celula viva ==> 1 ||||||||| celula muerta ==> 0
    private static int tam = 600, n_generaciones = 2, p = 0, q = 1, indiceA = 0, indiceB = 0, indiceC = 0;
    private static int[] celulasVivas = new int[tam];
    private static JFrame f;
    private static String[] menus = { "Opcion A","Opcion B","Opcion C", "Acerca de" };
    private static String[] colores = { "Blanco y negro","Azul y rojo","Verde y amarillo" };
    private static String[] itemsMenu = { "Sección 1A","Sección 1B", "Sección 1C", "Ayuda" };
    private static String[] opciones = { "Opción 1A","Opción 1B", "Opción 1C" };
    private static Graphics g;
    private static Puntos panel;
    private static boolean centinela = true, pintarEstado, pintarPoblacion;
    private static float[][][] a = new float [tam][tam][2];
    private static float[][][] b = new float [tam][tam][2];
    private static float[][][] c = new float [tam][tam][2];
    private static float[] evolA = new float [n_generaciones], evolB = new float [n_generaciones],evolC = new float [n_generaciones];
    private static Color color1, color2;

    public static void rellenar_matrices()
    {
        for ( int i = 0; i < tam; i++ )
        {
            for (int j = 0; j < tam; j++)
            {
                a[i][j][p] = (float)Math.random() * (1);
                b[i][j][p] = (float) Math.random() * (1);
                c[i][j][p] = (float) Math.random() * (1);
            }
        }
    }

    public static void reset()
    {
        rellenar_matrices();
        indiceC = 0;
        indiceB = 0;
        indiceA = 0;
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
                try{ Thread.sleep(1); }catch (Exception exc) {};
                paintImmediately(panel.getBounds());
            }).run();
        }

        public void funcionTransicion()
        {
            float concentracionA = 0, concentracionB = 0, concentracionC = 0;
            for ( int x = 0; x < tam ; x++ )
            {
                for ( int y = 0; y < tam ; y++ )
                {
                    float c_a = (float)0.0;
                    float c_b = (float)0.0;
                    float c_c = (float)0.0;
                    for ( int i = x - 1; i <= x + 1; i++ )
                    {
                        for ( int j = y - 1; j <= y + 1; j++ )
                        {
                            c_a += a [( i + tam )% tam ][( j + tam ) % tam ][ p ];
                            c_b += b [( i + tam )% tam ][( j + tam ) % tam ][ p ];
                            c_c += c [( i + tam )% tam ][( j + tam ) % tam ][ p ];
                        }
                    }
                    c_a /= 9.0;
                    c_b /= 9.0;
                    c_c /= 9.0;
                    a[ x ][ y ][ q ] = c_a + c_a * ( c_b - c_c ) % 1;
                    b[ x ][ y ][ q ] = c_b + c_b * ( c_c - c_a ) % 1;
                    c[ x ][ y ][ q ] = c_c + c_c * ( c_a - c_b ) % 1;
                    concentracionA += a[ x ][ y ][ q ];
                    concentracionB += b[ x ][ y ][ q ];
                    concentracionC += c[ x ][ y ][ q ];
                }
            }
            evolA[indiceA] = concentracionA;
            evolB[indiceB] = concentracionB;
            evolC[indiceC] = concentracionC;
            indiceC++;
            indiceB++;
            indiceA++;
        }

        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if ( centinela )
            {
                for( int i = 0; i < ancho; i++ )
                {
                    for ( int j = 0; j < alto; j++ )
                    {
                        g.setColor( Color.BLACK );
                        g.drawOval( i, j, 1, 1 );
                    }
                }
                centinela = false;
            } else {
                for (int x = 0; x < tam ; x ++)
                {
                    for (int y = 0; y < tam ; y ++)
                    {
                        Color color;
                        if ( a[ x ][ y ][ q ] < 0.5 )
                            g.setColor(color1);
                        else g.setColor(color2);
                        g.drawOval(x, y, 1, 1);
                    }
                }
                if (p == 0) {
                    p = 1; q = 0;
                }
                else {
                    p = 0; q = 1;
                }
            }
        }
    }


    public static class grafCineticaTemporal extends JPanel
    {
        private int[] dat;
        private int puntos;
        private int[] Xdat;
        private int[] Ydat;

        public grafCineticaTemporal()
        {
            super();
            Xdat = new int[n_generaciones];
        }

        public void paint(Graphics g)
        {
            Image img = createImageWithText2();
            g.drawImage(img, 5,90,this);
        }

        public Image createImageWithText2()
        {
            int ancho = tam;
            int alto = tam;
            BufferedImage bufferedImage = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            g = bufferedImage.getGraphics();
            for (int i = 0; i < n_generaciones; i++)
            {
                Xdat[i] = 10*i;
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(3));

            for (int i = 0; i < n_generaciones-1; i++)
            {
                int x0 = Xdat[i];
                int x1 = Xdat[i + 1];
                int y0 = (int)evolA[i] % tam;
                int y1 = (int)evolA[i+1] % tam;
                g2.drawLine( x0, y0, x1, y1 );
            }
            g2.setColor(Color.RED);
            for (int i = 0; i < n_generaciones-1; i++)
            {
                int x0 = Xdat[i];
                int x1 = Xdat[i + 1];
                int y0 = (int)evolB[i] % tam;
                int y1 = (int)evolB[i+1] % tam;
                g2.drawLine( x0, y0, x1, y1 );
            }
            g2.setColor(Color.GREEN);
            for (int i = 0; i < n_generaciones-1; i++)
            {
                int x0 = Xdat[i];
                int x1 = Xdat[i + 1];
                int y0 = (int)evolC[i] % tam;
                int y1 = (int)evolC[i+1] % tam;
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
                indiceC = 0;
                indiceB = 0;
                indiceA = 0;
                rellenar_matrices();
                for (int i = 0; i < n_generaciones; i++ )
                    panel.generar();

                if(pintarPoblacion)
                {
                    JDialog d = new JDialog(f, "Cinetica Temporal");
                    d.add(new grafCineticaTemporal());
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
        spinner2.addChangeListener(e -> {
            n_generaciones = (int) spinner2.getValue();
            evolA = new float[n_generaciones];
            evolB = new float[n_generaciones];
            evolC = new float[n_generaciones];
        });

        panelBotones.add(spinner2);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, spinner2, 0, SpringLayout.HORIZONTAL_CENTER, label3);
        layout.putConstraint(SpringLayout.NORTH, spinner2, 30, SpringLayout.NORTH, label3);


        JCheckBox checkPoblacion = new JCheckBox("Cinetica Temporal");
        checkPoblacion.addChangeListener(e -> pintarPoblacion = checkPoblacion.isSelected());
        panelBotones.add(checkPoblacion);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, checkPoblacion, 0, SpringLayout.HORIZONTAL_CENTER, spinner2);
        layout.putConstraint(SpringLayout.NORTH, checkPoblacion, 30, SpringLayout.NORTH, spinner2);

        JComboBox<String> combo = new JComboBox<>();
        for (String col : colores) combo.addItem(col);
        combo.addActionListener(e -> {
            switch (combo.getSelectedIndex())
            {
                case 0:
                    color1 = Color.BLACK;
                    color2 = Color.WHITE;
                    break;
                case 1:
                    color1 = Color.BLUE;
                    color2 = Color.RED;
                    break;
                case 2:
                    color1 = Color.GREEN;
                    color2 = Color.YELLOW;
                    break;
                default:
                    break;
            }
        });
        panelBotones.add(combo);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, combo, 0, SpringLayout.HORIZONTAL_CENTER, checkPoblacion);
        layout.putConstraint(SpringLayout.NORTH, combo, 30, SpringLayout.NORTH, checkPoblacion);

        JButton b1 = new JButton("Inicio");
        b1.addActionListener(bl);
        panelBotones.add(b1);
        layout.putConstraint(SpringLayout.EAST, b1, -180, SpringLayout.EAST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b1, 60, SpringLayout.NORTH, combo);
        JButton b2 = new JButton("Reset");
        b2.addActionListener(bl);
        panelBotones.add(b2);
        layout.putConstraint(SpringLayout.EAST, b2, -100, SpringLayout.EAST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b2, 60, SpringLayout.NORTH, combo);

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


    public belZab(String nombre)
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
        belZab frame = new belZab("Modelo de la reaccion quimica oscilante de Belousov-Zhabotinsky");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setVisible(true);
    }
}
