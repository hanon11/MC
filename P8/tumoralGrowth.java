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
    private static int tam = 400, n_generaciones = 2, indice =0;
    private static double Ps = 1, Pp = 0.25, NP = 2, Pm = 0.2 ,PH = 0, P1, P2,P3,P4;
    private static int[] celulasVivas = new int[n_generaciones];
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
        t_1[tam/2][tam/2] = 1;
        for ( int i = 0; i < tam; i++ ) //una vez hechos todos los cambios, actualizamos para la proxima iteracion
        {
            System.arraycopy(t_1[i], 0, actual[i], 0, tam);
        }
    }



    public static void reset()
    {
        rellenar_matrices();
        indice = 0;
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
            int sumatorio = 0, numerador1, numerador2, numerador3, numerador4;
            if( i-1 == -1 ) //se pasa por la izq
            {
                sumatorio += t_1[tam - 1][j];
                numerador1 = tam-1;
            }
            else
            {
                sumatorio += t_1[i - 1][j];
                numerador1 = i-1;
            }

            if( i+1 == tam )//se pasa por la dcha
            {
                sumatorio += t_1[0][j];
                numerador2 = 0;
            }
            else {
                sumatorio += t_1[i + 1][j];
                numerador2 = i+1;
            }

            if( j-1 == -1 ) //se pasa por arriba
            {
                sumatorio += t_1[i][tam - 1];
                numerador3 = tam-1;
            }
            else {
                sumatorio += t_1[i][j - 1];
                numerador3 = j-1;
            }

            if( j+1 == tam )//se pasa por abajo
            {
                sumatorio += t_1[i][0];
                numerador4 = 0;
            }
            else {
                sumatorio += t_1[i][j + 1];
                numerador4 = j+1;
            }

            P1 = (double)(1-t_1[numerador1][j])/(4-sumatorio);
            P2 = (double)(1-t_1[numerador2][j])/(4-sumatorio);
            P3 = (double)(1-t_1[i][numerador3])/(4-sumatorio);
            P4 = (double)(1-t_1[i][numerador4])/(4-sumatorio);
        }

        public void funcionTransicion()
        {
            int celulas = 0;
            for ( int i = 0; i < tam; i++ )
            {
                for (int j = 0; j < tam; j++)
                {
                    double rrp = Math.random();
                    double rr = Math.random();
                    double rrm = Math.random();
                    if (actual[i][j]==1)
                    {
                        if (rr >= Ps) //la celula muere
                            actual[i][j] = 0;
                        else {
                            //celulas++;
                            proliferation(i, j); //sirve tanto para proliferar como para migrar
                            if (rrp >= Pp) //no proliferacion
                            {
                                if (rrm < Pm) {
                                    rr = Math.random();
                                    if (rr <= P1) {
                                        actual[i][j] = 0;
                                        if (i - 1 == -1)
                                            actual[tam - 1][j] = 1;
                                        else
                                            actual[i - 1][j] = 1; //hija
                                    } else if (rr <= P1 + P2) {
                                        actual[i][j] = 0;
                                        if (i + 1 == tam)
                                            actual[0][j] = 1;
                                        else
                                            actual[i + 1][j] = 1; //hija
                                    } else if (rr <= P1 + P2 + P3) {
                                        actual[i][j] = 0;
                                        if (j - 1 == -1)
                                            actual[i][tam - 1] = 1;
                                        else
                                            actual[i][j - 1] = 1; //hija
                                    } else {
                                        actual[i][j] = 0;
                                        if (j + 1 == tam)
                                            actual[i][0] = 1;
                                        else
                                            actual[i][j + 1] = 1; //hija
                                    }
                                    //celulas++;
                                }
                            } else {
                                PH++;
                                rr = Math.random();
                                if (PH < NP) //migra sin proliferar
                                {
                                    if (rrm >= Pm) {
                                        //quiescent
                                    } else {
                                        if (rr <= P1) {
                                            actual[i][j] = 0;
                                            if (i - 1 == -1)
                                                actual[tam - 1][j] = 1;
                                            else
                                                actual[i - 1][j] = 1; //hija
                                        } else if (rr <= P1 + P2) {
                                            actual[i][j] = 0;
                                            if (i + 1 == tam)
                                                actual[0][j] = 1;
                                            else
                                                actual[i + 1][j] = 1; //hija
                                        } else if (rr <= P1 + P2 + P3) {
                                            actual[i][j] = 0;
                                            if (j - 1 == -1)
                                                actual[i][tam - 1] = 1;
                                            else
                                                actual[i][j - 1] = 1; //hija
                                        } else {
                                            actual[i][j] = 0;
                                            if (j + 1 == tam)
                                                actual[i][0] = 1;
                                            else
                                                actual[i][j + 1] = 1; //hija
                                        }
                                        //celulas++;
                                    }
                                } else //proliferation
                                {
                                    rr = Math.random();
                                    if (rr <= P1) {
                                        if (i - 1 == -1)
                                            actual[tam - 1][j] = 1;
                                        else
                                            actual[i - 1][j] = 1; //hija
                                    } else if (rr <= P1 + P2) {
                                        if (i + 1 == tam)
                                            actual[0][j] = 1;
                                        else
                                            actual[i + 1][j] = 1; //hija
                                    } else if (rr <= P1 + P2 + P3) {
                                        if (j - 1 == -1)
                                            actual[i][tam - 1] = 1;
                                        else
                                            actual[i][j - 1] = 1; //hija
                                    } else {
                                        if (j + 1 == tam)
                                            actual[i][0] = 1;
                                        else
                                            actual[i][j + 1] = 1; //hija
                                    }
                                    celulas++;
                                    PH = 0;
                                }
                            }
                        }
                    }
                }
            }
            celulasVivas[indice] = celulas;
            indice++;
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
                        g.setColor(Color.WHITE);
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
                            g.drawOval(i, j, 3, 3);
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

        public grafPobCelular()
        {
            super();
            dat = celulasVivas;
            puntos = dat.length;
            Xdat = new int[puntos];
        }
        public void paint(Graphics g)
        {
            Image img = createImageWithText2();
            g.drawImage(img, 5,5,this);
        }

        public Image createImageWithText2()
        {
            Xdat = new int[puntos];
            int ancho = 5*n_generaciones;
            int alto = tam+tam;
            BufferedImage bufferedImage = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            g = bufferedImage.getGraphics();
            for (int i = 0; i < puntos; i++)
            {
                Xdat[i] = 5*i;
            }
            //System.out.println(Arrays.toString(celulasVivas));
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(3));
            for (int i = 0; i < puntos-1; i++)
            {
                int x0 = Xdat[i];
                int x1 = Xdat[i + 1];
                int y0 = alto - celulasVivas[i];
                int y1 = alto - celulasVivas[i + 1];
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
                indice=0;
                rellenar_matrices();
                for (int i = 0; i < n_generaciones; i++)
                {
                    panel.generar();
                }
                if(curvaCrecimiento)
                {
                    JDialog d = new JDialog(f, "Evolucion grafica del estado de la reticula");
                    d.add(new grafPobCelular());
                    d.setSize(1200, 1000);
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

        JComboBox<String> combo = new JComboBox<>();
        combo.addItem("A");combo.addItem("B"); combo.addItem("C");combo.addItem("D");
        combo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int escogido = combo.getSelectedIndex();
                Ps = 1; Pp = 0.25;
                switch (escogido) {
                    case 0 -> {
                        NP = 1;
                        Pm = 0.2;
                    }
                    case 1 -> {
                        NP = 1;
                        Pm = 0.8;
                    }
                    case 2 -> {
                        NP = 2;
                        Pm = 0.2;
                    }
                    case 3 -> {
                        NP = 2;
                        Pm = 0.8;
                    }
                }
            }
        });
        panelBotones.add(combo);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, combo, 0, SpringLayout.HORIZONTAL_CENTER, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, combo, 40, SpringLayout.NORTH, panelBotones);

        JLabel label3 = new JLabel("Numero de generaciones:");
        panelBotones.add(label3);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label3, 0, SpringLayout.HORIZONTAL_CENTER, combo);
        layout.putConstraint(SpringLayout.NORTH, label3, 40, SpringLayout.NORTH, combo);

        JSpinner spinner2 = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
        spinner2.setSize(70, 10);
        spinner2.addChangeListener(e ->
        {
            n_generaciones = (int) spinner2.getValue();
            celulasVivas = new int[n_generaciones];
        });

        panelBotones.add(spinner2);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, spinner2, 0, SpringLayout.HORIZONTAL_CENTER, label3);
        layout.putConstraint(SpringLayout.NORTH, spinner2, 30, SpringLayout.NORTH, label3);

        JLabel label4 = new JLabel("Probabilidad de que una celula sobreviva (Ps) [0-1]:");
        panelBotones.add(label4);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label4, 0, SpringLayout.HORIZONTAL_CENTER, spinner2);
        layout.putConstraint(SpringLayout.NORTH, label4, 40, SpringLayout.NORTH, spinner2);

        JTextField textField = new JTextField(14);
        textField.addActionListener(e ->
        {
            Ps = Double.parseDouble(textField.getText());
        });
        panelBotones.add(textField);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, textField, 0, SpringLayout.HORIZONTAL_CENTER, label4);
        layout.putConstraint(SpringLayout.NORTH, textField, 30, SpringLayout.NORTH, label4);

        JLabel label5 = new JLabel("Probabilidad de que una celula migre (Pm) [0-1]:");
        panelBotones.add(label5);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label5, 0, SpringLayout.HORIZONTAL_CENTER, textField);
        layout.putConstraint(SpringLayout.NORTH, label5, 40, SpringLayout.NORTH, textField);

        JTextField textField2 = new JTextField(14);
        textField2.addActionListener(e ->
        {
            Pm = Double.parseDouble(textField2.getText());
        });
        panelBotones.add(textField2);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, textField2, 0, SpringLayout.HORIZONTAL_CENTER, label5);
        layout.putConstraint(SpringLayout.NORTH, textField2, 30, SpringLayout.NORTH, label5);

        JLabel label6 = new JLabel("Probabilidad de que una celula prolifere (Pp) [0-1]:");
        panelBotones.add(label6);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label6, 0, SpringLayout.HORIZONTAL_CENTER, textField2);
        layout.putConstraint(SpringLayout.NORTH, label6, 40, SpringLayout.NORTH, textField2);

        JTextField textField3 = new JTextField(14);
        textField3.addActionListener(e ->
        {
            Pp = Double.parseDouble(textField3.getText());
        });
        panelBotones.add(textField3);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, textField3, 0, SpringLayout.HORIZONTAL_CENTER, label6);
        layout.putConstraint(SpringLayout.NORTH, textField3, 30, SpringLayout.NORTH, label6);

        JLabel label7 = new JLabel("Total PH que hace falta para proliferar (NP):");
        panelBotones.add(label7);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label7, 0, SpringLayout.HORIZONTAL_CENTER, textField3);
        layout.putConstraint(SpringLayout.NORTH, label7, 40, SpringLayout.NORTH, textField3);

        JTextField textField4 = new JTextField(14);
        textField4.addActionListener(e ->
        {
            NP = Double.parseDouble(textField4.getText());
        });
        panelBotones.add(textField4);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, textField4, 0, SpringLayout.HORIZONTAL_CENTER, label7);
        layout.putConstraint(SpringLayout.NORTH, textField4, 30, SpringLayout.NORTH, label7);


        JCheckBox checkPoblacion = new JCheckBox("Curva de poblacion popular");
        checkPoblacion.addChangeListener(e -> curvaCrecimiento = checkPoblacion.isSelected());
        panelBotones.add(checkPoblacion);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, checkPoblacion, 0, SpringLayout.HORIZONTAL_CENTER, textField4);
        layout.putConstraint(SpringLayout.NORTH, checkPoblacion, 30, SpringLayout.NORTH, textField4);
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
        tumoralGrowth frame = new tumoralGrowth("Simulacion de Crecimiento Tumoral con AC 2-D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }
}



