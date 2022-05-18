import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.*;
import java.util.Arrays;

public class urmInterpreter extends JFrame
{
    private static JFrame f;
    private static String[] menus = { "Opcion A","Opcion B","Opcion C", "Acerca de" };
    private static String[] itemsMenu = { "Seccion 1A","Seccion 1B", "Seccion 1C", "Ayuda" };
    private static String[] opciones = { "Opcion 1A","Opcion 1B", "Opcion 1C" };
    private static File ruta;
    private static String codigo = "";
    private static JTextArea texto = new JTextArea(10,30);
    private static int R1 = 0, R2 = 0, R3 = 0, numLineas = 0, lineaActual=1;
    private static JTextField registros = new JTextField();


    // ===================================== URM simulator =========================================
    public static void procesar()
    {
        String[] instrucciones = codigo.split("\n");
        numLineas = instrucciones.length;
        String text = registros.getText();
        String[] splitText = text.split(",");
        R1 = Integer.parseInt(splitText[0]);
        R2 = Integer.parseInt(splitText[1]);
        //System.out.println(R1);
        //System.out.println(R2);
        //System.out.println(R3);
        int i = 0;
        while (i < numLineas)
        {
            switch (instrucciones[i].charAt(0)) {
                case 'J' -> {

                    if (salta(Character.getNumericValue(instrucciones[i].charAt(2)), Character.getNumericValue(instrucciones[i].charAt(4)),
                            Character.getNumericValue(instrucciones[i].charAt(6))))
                        if (Character.getNumericValue(instrucciones[i].charAt(6)) <= 0 || Character.getNumericValue(instrucciones[i].charAt(6)) > numLineas)
                            return;
                        else
                            i=Character.getNumericValue(instrucciones[i].charAt(6))+1;
                    else
                        i++;
                }
                case 'S' -> {
                    sucessor(Character.getNumericValue(instrucciones[i].charAt(2)));
                    i++;
                }

                case 'Z' -> {
                    nUll(Character.getNumericValue(instrucciones[i].charAt(2)));
                    i++;
                }
                case 'T' -> {
                    copy(Character.getNumericValue(instrucciones[i].charAt(2)),Character.getNumericValue(instrucciones[i].charAt(4)));
                    i++;
                }
                default -> {
                }
            }

        }
    }

    public static void sucessor(int reg)
    {
        switch (reg) {
            case 1 -> {R1++; System.out.println("R1="+R1);}
            case 2 ->{R2++; System.out.println("R2="+R2);}
            case 3 -> {R3++; System.out.println("R3="+R3);}
            default -> {}
        }
    }

    public static void nUll(int reg)
    {
        switch (reg) {
            case 1 -> {R1=0; System.out.println("R1=0");}
            case 2 -> {R2=0; System.out.println("R2=0");}
            case 3 -> {R3=0; System.out.println("R3=0");}
            default -> {}
        }
    }

    public static void copy(int reg1, int reg2)
    {
        System.out.println(reg2);
        int numACopiar = 0;
        switch (reg1) {
            case 1 -> numACopiar = R1;
            case 2 -> numACopiar = R2;
            case 3 -> numACopiar = R3;
            default -> {}
        }
        switch (reg2) {
            case 1 -> R1 = numACopiar;
            case 2 -> R2 = numACopiar;
            case 3 -> R3 = numACopiar;
            default -> {}
        }
    }

    public static boolean salta(int reg1, int reg2, int lugar)
    {
        boolean salto = false;
        int numAcomparar = 0;
        switch (reg1) {
            case 1 -> numAcomparar = R1;
            case 2 -> numAcomparar = R2;
            case 3 -> numAcomparar = R3;
            default -> {}
        }
        switch (reg2) {
            case 1 -> {
                if (R1 == numAcomparar)
                    salto=true;
            }
            case 2 ->{
                if (R2 == numAcomparar)
                    salto=true;
            }
            case 3 -> {
                if (R3 == numAcomparar)
                    salto=true;
            }
            default -> {}
        }

       return salto;
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
                codigo = texto.getText();
                procesar();
                System.out.println(R1);
            }
            if (name.equals("Reset"))
            {
                //System.out.println(codigo.charAt(0));
                R1 = 0; R2 = 0; R3 = 0; numLineas = 0;
                codigo = ""; texto.setText(null);
                registros.setText("Introduce los valores de entrada separados por coma");
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
        JButton fichero = new JButton("Selecciona fichero: ");
        fichero.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(fileChooser);
            ruta = fileChooser.getSelectedFile();
            String aux = "";
            try {
                if (ruta != null) {
                    FileReader archivos = new FileReader(ruta);
                    BufferedReader leer = new BufferedReader(archivos);
                    while ((aux = leer.readLine()) != null) {
                        codigo += aux + "\n";
                        numLineas++;
                    }
                    leer.close();
                }
                } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error Importando - " + ex);
            }
            texto.setText(codigo);
        });
        panelBotones.add(fichero);
        layout.putConstraint(SpringLayout.WEST, fichero, 10, SpringLayout.WEST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, fichero, 10, SpringLayout.NORTH, panelBotones);
        panelBotones.add(texto);
        layout.putConstraint(SpringLayout.WEST, texto, -5, SpringLayout.WEST, fichero);
        layout.putConstraint(SpringLayout.NORTH, texto, 30, SpringLayout.NORTH, fichero);

        registros.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                registros.setText("");
            }
        });
        registros.addActionListener(e -> {
            String text = registros.getText();
            String[] splitText = text.split(",");
            R1 = Integer.parseInt(splitText[0]);
            R2 = Integer.parseInt(splitText[1]);
        });
        panelBotones.add(registros);
        registros.setText("Introduce los valores de entrada separados por coma");
        layout.putConstraint(SpringLayout.WEST, registros, -3, SpringLayout.WEST, texto);
        layout.putConstraint(SpringLayout.NORTH, registros, 190, SpringLayout.NORTH, texto);
        JButton b1 = new JButton("Inicio");
        b1.addActionListener(bl);
        panelBotones.add(b1);
        layout.putConstraint(SpringLayout.WEST, b1, 10, SpringLayout.WEST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b1, 30, SpringLayout.NORTH, registros);
        JButton b2 = new JButton("Reset");
        b2.addActionListener(bl);
        panelBotones.add(b2);
        layout.putConstraint(SpringLayout.WEST, b2, 90, SpringLayout.WEST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b2, 30, SpringLayout.NORTH, registros);

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


    public urmInterpreter(String nombre)
    {
        super(nombre);
        f = this;
        setJMenuBar(SimpleMenus()); //crear menu
        add(crearPanelBotones()); //panel botones

    }

    public static void main(String[] args)
    {
        urmInterpreter frame = new urmInterpreter("Microinterprete del modelo URM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }
}



