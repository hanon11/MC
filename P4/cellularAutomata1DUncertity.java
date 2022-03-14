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
import java.math.BigInteger;


public class cellularAutomata1DUncertity extends JFrame
{
    private static int tam = 800;
    private static int conf_escogida = 0;  //0 -> aleatoria || 1 -> central
    private static int cond_frontera = 0;  //0 -> nula || 1 -> cilindrica
    private static int n_generaciones = 400;
    private static int n_vecinos = 1, n_estados = 2, regla, indice_hamming = 0, indice_entropia = 0, celula_entropia;
    private static int posibles_estados = 3 * n_estados - 2;
    private static int[] code;
    private static int[] t_1 = new int[tam], actual = new int[tam];
    private static JPanel panel;
    private static Graphics g;
    private static boolean centinela = true;
    private static int[] hamming;
    private static double[] entropia;
    private static boolean pintarHamming = false, pintarEntropia = false, pintaCelula = false;
    private static String[] menus = { "Opción A","Opción B","Opción C", "Acerca de" };
    private static String[] itemsMenu = { "Sección 1A","Sección 1B", "Sección 1C", "Ayuda" };
    private static String[] opciones = { "Opción 1A","Opción 1B", "Opción 1C" };
    private static String[] configuracion = { "Aleatoria", "Celula central activa" };
    private static String[] estados = { "2 estados", "3 estados", "4 estados", "5 estados" };


    public static void rellenar_vectores()
    {
        //aleatoria
        if( conf_escogida == 0 )
        {
            BigInteger[] aleatorios = new BigInteger[tam];
            BigInteger x0 = BigInteger.ONE;
            for(int i = 0; i < tam; i++)
            {
                aleatorios[i] = x0.multiply(BigInteger.valueOf(69621));
                aleatorios[i] = aleatorios[i].mod(BigInteger.valueOf((long)(2e31 -1)));
                x0 = aleatorios[i];
                aleatorios[i] = aleatorios[i].mod( BigInteger.valueOf( n_estados ));
            }
            for(int i = 0; i < tam; i++)
            {
                t_1[i] = aleatorios[i].intValue();
            }
        }
        //central
        if( conf_escogida == 1 )
        {
            for( int i = 0; i < tam; i++ )
            {
                if( i == tam/2 )
                    t_1[i] = actual[i] = 1;
                else
                    t_1[i] = actual[i] = 0;
            }
        }
    }

    public static void calcular_estado_actual()
    {
        int sumatorio = 0;
        if(cond_frontera == 0)
        {
            for( int i = 0; i < actual.length; i++ )
            {
                sumatorio = 0;
                for( int j = i-n_vecinos; j <= i+n_vecinos; j++ )
                {
                    if( j < 0 || j > t_1.length-1 )
                        sumatorio += 0;
                    else
                        sumatorio += t_1[j];
                }

                if(code.length == 8)
                    actual[i] = code[sumatorio % 8]; //para que no se salga del vector
                else
                    actual[i] = code[sumatorio % posibles_estados]; //para que no se salga del vector
            }
        }
        if(cond_frontera == 1)
        {
            for( int i = 0; i < actual.length; i++ )
            {
                sumatorio = 0;
                for( int j = i-n_vecinos; j <= i+n_vecinos; j++ )
                {
                    if( j < 0 )
                        sumatorio += t_1[t_1.length+j];
                    else if( j > t_1.length-1 )
                        sumatorio += t_1[j-t_1.length];
                    else
                        sumatorio += t_1[j];
                }
                if(code.length == 8)
                    actual[i] = code[sumatorio % 8]; //para que no se salga del vector
                else
                    actual[i] = code[sumatorio % posibles_estados]; //para que no se salga del vector
            }
        }
        calcular_distancia_hamming();
        calcular_entropia();
        System.arraycopy(actual, 0, t_1, 0, actual.length);
    }


    public static void reset()
    {
        hamming = new int[0];
        indice_hamming = 0;
        hamming = new int[n_generaciones-1];
        entropia = new double[0];
        indice_entropia = 0;
        entropia = new double[n_generaciones-1];
        rellenar_vectores();
    }

    // ======================================== DIBUJO AUTOMATA ====================================================================

    public class Puntos extends JPanel
    {
        public void paint(Graphics g)
        {
            Image img = createImageWithText();
            g.drawImage(img, 5,90,this);
        }

        public Image createImageWithText()
        {
            int ancho      = tam;
            int alto       = n_generaciones;
            BufferedImage bufferedImage = new BufferedImage( ancho, alto, BufferedImage.TYPE_INT_RGB );
            g = bufferedImage.getGraphics();
            g.setColor(Color.BLACK);
            if(centinela)
            {
                centinela = false;
                for( int i = 0; i < n_generaciones; i++)
                {
                    for( int k = 0; k < actual.length; k++)
                    {
                        g.setColor(Color.BLACK);g.drawOval(k, i, 1, 1);
                    }
                }
            }
            else
            {
                rellenar_vectores();
                for( int i = 0; i < n_generaciones; i++)
                {
                    for( int k = 0; k < actual.length; k++)
                    {
                        switch (actual[k])
                        {
                            case 1 -> {
                                g.setColor(Color.BLUE);
                                g.drawOval(k, i, 1, 1);
                            }
                            case 2 -> {
                                g.setColor(Color.RED);
                                g.drawOval(k, i, 1, 1);
                            }
                            case 3 -> {
                                g.setColor(Color.GREEN);
                                g.drawOval(k, i, 1, 1);
                            }
                            case 4 -> {
                                g.setColor(Color.GRAY);
                                g.drawOval(k, i, 1, 1);
                            }
                            default -> {
                                g.setColor(Color.WHITE);
                                g.drawOval(k, i, 1, 1);
                            }
                        }
                    }
                    calcular_estado_actual();
                }
            }
            return bufferedImage;
        }
    }

    //================================================ Dibujo Hamming ====================================
    public static void calcular_distancia_hamming()
    {
        int sumatorio = 0;
        for( int i = 0; i < tam; i++)
            if( t_1[i] != actual[i] )
                sumatorio++;
        hamming[indice_hamming] = sumatorio;
        if( indice_hamming < hamming.length-1 )
            indice_hamming++;
    }

    public static class grafHamming extends JPanel
    {
        private int[] dat;
        private int puntos;
        private int[] Xdat;
        private int[] Ydat;

        public grafHamming()
        {
            super();
            dat = hamming;
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
            dat = hamming;
            puntos = dat.length;
            Xdat = new int[puntos];
            Ydat = new int[puntos];
            int ancho = n_generaciones;
            int alto = n_generaciones;
            BufferedImage bufferedImage = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            g = bufferedImage.getGraphics();
            //separamos puntos para coordenadas x e y...
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
                int y0 = n_generaciones - Ydat[i];
                int y1 = n_generaciones - Ydat[i + 1];
                //para tablas de datos discretos, drawLine será más que suficiente con drawLine...
                g2.drawLine( x0, y0, x1, y1 );
            }
            return bufferedImage;
        }
    }


    //================================================ Dibujo Entropia ====================================


    public static double calcular_entropia_temporal()
    {
        int[] sumatorio = new int[n_estados];
        for( int i = 0; i < n_generaciones; i++)
            sumatorio[actual[celula_entropia]]++;

        //aproximamos probabilidades por frecuencias...
        double H = 0;
        for( int i = 0; i < n_estados; i++ )
        {
            if( (double)sumatorio[i]/tam != 0 )
                H += (double)sumatorio[i]/tam*logConversion( (double)sumatorio[i]/tam );
        }
        H = -H;
        return H;
    }

    public static double logConversion( double x ){
        return ( Math.log(x) / Math.log(2) );
    }

    public static void calcular_entropia()
    {
        int[] sumatorio = new int[n_estados];
        for( int i = 0; i < tam; i++)
            sumatorio[actual[i]]++;

        //aproximamos probabilidades por frecuencias...
        double H = 0;
        for( int i = 0; i < n_estados; i++ )
        {
            if( (double)sumatorio[i]/tam != 0 )
                H += (double)sumatorio[i]/tam*logConversion( (double)sumatorio[i]/tam );
        }
        H = -H;

        entropia[indice_entropia] = H*300;
        if( indice_entropia < entropia.length-1 )
            indice_entropia++;
    }

    public static class grafEntropia extends JPanel
    {
        private double[] dat;
        private int puntos;
        private int[] Xdat;
        private double[] Ydat;

        public grafEntropia()
        {
            super();
            dat = entropia;
            puntos = dat.length;
            Xdat = new int[puntos];
            Ydat = new double[puntos];
            //separamos puntos para coordenadas x e y...
            for (int i = 0; i < puntos; i++)
            {
                Xdat[i] = i;
                Ydat[i] = dat[i];
            }

        }
        public void paint(Graphics g)
        {
            Image img = createImageWithText3();
            g.drawImage(img, 5,90,this);
        }

        public Image createImageWithText3()
        {
            dat = entropia;
            puntos = dat.length;
            Xdat = new int[puntos];
            Ydat = new double[puntos];
            int ancho = n_generaciones;
            int alto = n_generaciones;
            BufferedImage bufferedImage = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            g = bufferedImage.getGraphics();
            //separamos puntos para coordenadas x e y...
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
                double y0 = n_generaciones - Ydat[i];
                double y1 = n_generaciones - Ydat[i + 1];
                //para tablas de datos discretos, drawLine será más que suficiente con drawLine...
                g2.drawLine( x0, (int)y0, x1, (int)y1 );
            }
            return bufferedImage;
        }
    }

    static JFrame f;

    //================================================== LISTENERS BOTONES ==========================================
    private ButtonListener bl = new ButtonListener();
    //crea nuevo JDialog por cada boton
    class ButtonListener implements ActionListener //listeners de los botones
    {
        public void actionPerformed(ActionEvent e)
        {
            String name = ((JButton)e.getSource()).getText();
            if(name.equals("Start"))
            {
                Puntos p = new Puntos();
                Image img = p.createImageWithText();
                g.drawImage(img, 0, 0, null);
                panel.setVisible(false);
                panel.setVisible(true);

                if(pintarHamming)
                {
                    JDialog d = new JDialog(f, "Hamming");
                    JLabel l = new JLabel("Max: " + Arrays.stream(hamming).max().getAsInt() + " Min: " + Arrays.stream(hamming).min().getAsInt() );
                    d.add(l,BorderLayout.NORTH);
                    d.add(new grafHamming());
                    d.setSize(800, 850);
                    d.setVisible(true);
                }
                if(pintarEntropia)
                {
                    JDialog d = new JDialog(f, "Entropia espacial");
                    JLabel l = new JLabel("Max: " + Arrays.stream(entropia).max().getAsDouble()/300 + " Min: " + Arrays.stream(entropia).min().getAsDouble()/300 );
                    d.add( l,BorderLayout.NORTH );
                    d.add( new grafEntropia() );
                    d.setSize(800, 850);
                    d.setVisible(true);
                }
                if(pintaCelula)
                {
                    JDialog d = new JDialog(f, "Entropia temporal");
                    JLabel l = new JLabel("" + calcular_entropia_temporal());
                    d.add( l, BorderLayout.CENTER );
                    d.setSize(200, 250);
                    d.setVisible(true);
                }

            }
            if(name.equals("Reset"))
            {
                Puntos p = new Puntos();
                Image img = p.createImageWithText();
                g.drawImage(img, 0, 0, null);
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
        ButtonGroup grupoBotones = new ButtonGroup();
        JRadioButton aleatoria = new JRadioButton(configuracion[0]), central = new JRadioButton(configuracion[1]);
        JLabel label = new JLabel("Configuracion:");
        panelBotones.add(label);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, label, 100, SpringLayout.NORTH, panelBotones);

        grupoBotones.add(central); grupoBotones.add(aleatoria);
        aleatoria.addChangeListener(e -> conf_escogida = 0);
        central.addChangeListener(e -> conf_escogida = 1);
        panelBotones.add(central); panelBotones.add(aleatoria);
        layout.putConstraint(SpringLayout.NORTH, aleatoria, 30, SpringLayout.NORTH, label);
        layout.putConstraint(SpringLayout.WEST, aleatoria, 60, SpringLayout.WEST, panelBotones);


        layout.putConstraint(SpringLayout.NORTH, central, 30, SpringLayout.NORTH, label);
        layout.putConstraint(SpringLayout.WEST, central, 80, SpringLayout.WEST, aleatoria);



        ButtonGroup grupoBotones2 = new ButtonGroup();
        JRadioButton nula = new JRadioButton("Nula"), cilindrica = new JRadioButton("Cilindrica");
        JLabel labelConFront = new JLabel("Condicion de frontera:");
        panelBotones.add(labelConFront);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelConFront, 0, SpringLayout.HORIZONTAL_CENTER, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, labelConFront, 40, SpringLayout.NORTH, central);

        grupoBotones2.add(nula); grupoBotones2.add(cilindrica);
        nula.addChangeListener(e -> cond_frontera = 0);
        cilindrica.addChangeListener(e -> cond_frontera = 1);
        panelBotones.add(nula); panelBotones.add(cilindrica);
        layout.putConstraint(SpringLayout.NORTH, nula, 30, SpringLayout.NORTH, labelConFront);
        layout.putConstraint(SpringLayout.WEST, nula, 90, SpringLayout.WEST, panelBotones);


        layout.putConstraint(SpringLayout.NORTH, cilindrica, 30, SpringLayout.NORTH, labelConFront);
        layout.putConstraint(SpringLayout.WEST, cilindrica, 93, SpringLayout.WEST, nula);


        JLabel label2 = new JLabel("Numero de estados por celula:");
        panelBotones.add(label2);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label2, 0, SpringLayout.HORIZONTAL_CENTER, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, label2, 40, SpringLayout.NORTH, cilindrica);


        JComboBox<String> combo = new JComboBox<String>();
        for (String estado : estados) combo.addItem(estado);
        combo.addActionListener(e -> {
            n_estados = combo.getSelectedIndex() + 2; // 0 -> 2 estados ... 3 -> 5 estados
            posibles_estados = 3 * n_estados - 2;
        });

        panelBotones.add(combo);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, combo, 0, SpringLayout.HORIZONTAL_CENTER, label2);
        layout.putConstraint(SpringLayout.NORTH, combo, 30, SpringLayout.NORTH, label2);


        JLabel label3 = new JLabel("Numero de generaciones:");
        panelBotones.add(label3);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label3, 0, SpringLayout.HORIZONTAL_CENTER, combo);
        layout.putConstraint(SpringLayout.NORTH, label3, 30, SpringLayout.NORTH, combo);

        JSpinner spinner2 = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
        spinner2.setSize(70, 10);
        spinner2.addChangeListener(e -> {
            n_generaciones = (int)spinner2.getValue();
            hamming = new int[n_generaciones-1];
            entropia = new double[n_generaciones-1];
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
        spinner3.addChangeListener(e -> n_vecinos = (int)spinner3.getValue());

        panelBotones.add(spinner3);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, spinner3, 0, SpringLayout.HORIZONTAL_CENTER, label4);
        layout.putConstraint(SpringLayout.NORTH, spinner3, 30, SpringLayout.NORTH, label4);

        JLabel label5 = new JLabel("Regla:");
        panelBotones.add(label5);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label5, 0, SpringLayout.HORIZONTAL_CENTER, spinner3);
        layout.putConstraint(SpringLayout.NORTH, label5, 30, SpringLayout.NORTH, spinner3);
        JSpinner spinner4 = new JSpinner(new SpinnerNumberModel(0, 0, 1000000, 1));
        spinner4.setSize(70, 10);
        spinner4.addChangeListener(e -> {
            regla = (int)spinner4.getValue();
            if(n_estados == 2)
                decimal_a_binario_2();
            else
                decimal_a_baseK();
        });

        panelBotones.add(spinner4);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, spinner4, 0, SpringLayout.HORIZONTAL_CENTER, label5);
        layout.putConstraint(SpringLayout.NORTH, spinner4, 30, SpringLayout.NORTH, label5);


        JCheckBox checkHamming = new JCheckBox("Calcular distancia de Hamming");
        JCheckBox checkEntropia = new JCheckBox("Calcular entropia espacial");
        JCheckBox checkCelula = new JCheckBox("Calcular entropia temporal");
        checkHamming.addChangeListener(e -> pintarHamming = checkHamming.isSelected());
        checkEntropia.addChangeListener(e -> pintarEntropia = checkEntropia.isSelected());
        checkCelula.addChangeListener(e -> pintaCelula = checkCelula.isSelected());
        panelBotones.add(checkCelula);
        panelBotones.add(checkHamming);
        panelBotones.add(checkEntropia);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, checkHamming, 0, SpringLayout.HORIZONTAL_CENTER, spinner4);
        layout.putConstraint(SpringLayout.NORTH, checkHamming, 30, SpringLayout.NORTH, spinner4);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, checkEntropia, 0, SpringLayout.HORIZONTAL_CENTER, checkHamming);
        layout.putConstraint(SpringLayout.NORTH, checkEntropia, 30, SpringLayout.NORTH, checkHamming);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, checkCelula, 0, SpringLayout.HORIZONTAL_CENTER, checkEntropia);
        layout.putConstraint(SpringLayout.NORTH, checkCelula, 30, SpringLayout.NORTH, checkEntropia);
        JSpinner spinner5 = new JSpinner(new SpinnerNumberModel(0, 0, tam, 1));
        spinner4.setSize(70, 10);
        spinner4.addChangeListener(e -> celula_entropia = (int)spinner5.getValue());
        panelBotones.add(spinner5);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, spinner5, 0, SpringLayout.HORIZONTAL_CENTER, checkCelula);
        layout.putConstraint(SpringLayout.NORTH, spinner5, 30, SpringLayout.NORTH, checkCelula);


        JButton b1 = new JButton("Start");
        b1.addActionListener(bl);
        panelBotones.add(b1);
        layout.putConstraint(SpringLayout.EAST, b1, -180, SpringLayout.EAST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b1, 60, SpringLayout.NORTH, spinner5);
        JButton b2 = new JButton("Reset");
        b2.addActionListener(bl);
        panelBotones.add(b2);
        layout.putConstraint(SpringLayout.EAST, b2, -100, SpringLayout.EAST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b2, 60, SpringLayout.NORTH, spinner5);

        return panelBotones;
    }

    private JMenuBar SimpleMenus()
    {
        JMenuBar mb = new JMenuBar();
        JMenu menu;
        JMenu submenu;
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


    public cellularAutomata1DUncertity(String nombre)
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
        cellularAutomata1DUncertity frame = new cellularAutomata1DUncertity("Automatas celulares 1-D.");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 1000);
        frame.setVisible(true);
    }


    //automata de solo 2 estados
    public static void decimal_a_binario_2()
    {
        code = new int[8];
        int i = 0, decimal = regla;

        while(decimal > 0 && i < 8)
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
    }
}