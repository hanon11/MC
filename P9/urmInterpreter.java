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
    private static String[] opciones = { "Opcion 1A","Opcion 1B", "Opcion 1C" }, instruccionesPasoaPaso;
    private static File ruta;
    private static String codigo = "", traza = "";
    private static JTextArea texto = new JTextArea(10,30), output = new JTextArea(25,30);
    private static int R1 = 0, R2 = 0, R3 = 0,  R4 = 0, R5 = 0,numLineas = 0, lineaActual=0;
    private static JTextField registros = new JTextField();
    private static boolean centinela = true;


    // ===================================== URM simulator =========================================
    public static boolean procesarPorPasos()
    {
        //System.out.println(lineaActual+1);
        String salto = ";"; int num = 0;
    switch (instruccionesPasoaPaso[lineaActual].charAt(0)) {
            case 'J' -> {
                if(Character.isDigit(instruccionesPasoaPaso[lineaActual].charAt(7))) {
                    salto = instruccionesPasoaPaso[lineaActual].substring(6, 8);
                    num =  Integer.parseInt(salto);
                }
                else num = Character.getNumericValue(instruccionesPasoaPaso[lineaActual].charAt(6));
                if (salta(Character.getNumericValue(instruccionesPasoaPaso[lineaActual].charAt(2)), Character.getNumericValue(instruccionesPasoaPaso[lineaActual].charAt(4))))
                    if (num <= 0 || num > numLineas)
                        return true;
                    else{
                        lineaActual=num-1;
                    }
                else
                    lineaActual++;
            }
            case 'S' -> {
                sucessor(Character.getNumericValue(instruccionesPasoaPaso[lineaActual].charAt(2)));
                lineaActual++;
            }

            case 'Z' -> {
                nUll(Character.getNumericValue(instruccionesPasoaPaso[lineaActual].charAt(2)));
                lineaActual++;
            }
            case 'T' -> {
                copy(Character.getNumericValue(instruccionesPasoaPaso[lineaActual].charAt(2)),Character.getNumericValue(instruccionesPasoaPaso[lineaActual].charAt(4)));
                lineaActual++;
            }
            default -> {
            }
        }

        output.append("<R1="+R1+" ,R2="+R2+" , R3="+R3+" ,R4="+R4+" , R5="+R5+">\n");
        if(lineaActual>=numLineas)
            return true;
        return false;
    }



    public static void sucessor(int reg)
    {
        switch (reg) {
            case 1 -> {R1++; }//output.append("R1="+R1+"\n");}
            case 2 -> {R2++; }//output.append("R2="+R2+"\n");}
            case 3 -> {R3++; }//output.append("R3="+R3+"\n");}
            case 4 -> {R4++; }//output.append("R2="+R2+"\n");}
            case 5 -> {R5++; }//output.append("R3="+R3+"\n");}
            default -> {}
        }
    }

    public static void nUll(int reg)
    {
        switch (reg) {
            case 1 -> {R1=0; }//output.append("R1=0\n");}
            case 2 -> {R2=0; }//output.append("R2=0\n");}
            case 3 -> {R3=0; }//output.append("R3=0\n");}
            case 4 -> {R4=0; }//output.append("R2=0\n");}
            case 5 -> {R5=0; }//output.append("R3=0\n");}
            default -> {}
        }
    }

    public static void copy(int reg1, int reg2)
    {
        String s = "Copiar el valor de ";
        int numACopiar = 0;
        switch (reg1) {
            case 1 -> { numACopiar = R1; s += "R1";}
            case 2 -> { numACopiar = R2; s += "R2";}
            case 3 -> { numACopiar = R3; s += "R3";}
            case 4 -> { numACopiar = R4; s += "R2";}
            case 5 -> { numACopiar = R5; s += "R3";}
            default -> {}
        }
        switch (reg2) {
            case 1 -> {R1 = numACopiar; s += " en el registro R1";}
            case 2 -> {R2 = numACopiar; s += " en el registro R2";}
            case 3 -> {R3 = numACopiar; s += " en el registro R3";}
            case 4 -> {R4 = numACopiar; s += " en el registro R3";}
            case 5 -> {R5 = numACopiar; s += " en el registro R3";}
            default -> {}
        }
        //output.append(s+"\n");
    }

    public static boolean salta(int reg1, int reg2)
    {
        boolean salto = false;
        int numAcomparar = 0;
        switch (reg1) {
            case 1 -> numAcomparar = R1;
            case 2 -> numAcomparar = R2;
            case 3 -> numAcomparar = R3;
            case 4 -> numAcomparar = R4;
            case 5 -> numAcomparar = R5;
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
            case 4 -> {
                if (R4 == numAcomparar)
                    salto=true;
            }
            case 5 -> {
                if (R5 == numAcomparar)
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
                if (centinela){
                    codigo = texto.getText();
                    output.setText("");
                    instruccionesPasoaPaso = codigo.split("\n");
                    numLineas = instruccionesPasoaPaso.length;
                    output.append("INICIO: <R1="+R1+" ,R2="+R2+" , R3="+R3+" ,R4="+R4+" , R5="+R5+">\n");
                    centinela=false;
                }
                while (!procesarPorPasos()) {}
                output.append("El resultado de la computacion es: "+R1);
            }
            if (name.equals("Step"))
            {
                if (centinela){
                    codigo = texto.getText();
                    output.setText("");
                    instruccionesPasoaPaso = codigo.split("\n");
                    numLineas = instruccionesPasoaPaso.length;
                    output.append("INICIO: <R1="+R1+" ,R2="+R2+" , R3="+R3+" ,R4="+R4+" , R5="+R5+">\n");
                    centinela=false;
                }
                if (procesarPorPasos())
                    output.append("El resultado de la computacion es: "+R1);
            }
            if (name.equals("Reset"))
            {
                R1 = 0; R2 = 0; R3 = 0; numLineas = 0;R4 = 0; R5 = 0;
                codigo = ""; texto.setText(null); traza = ""; output.setText(null);
                registros.setText("Introduce los valores de entrada separados por coma");
                lineaActual=0; centinela = true;
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
        JButton b3 = new JButton("Step");
        b3.addActionListener(bl);
        panelBotones.add(b3);
        layout.putConstraint(SpringLayout.WEST, b3, 180, SpringLayout.WEST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b3, 30, SpringLayout.NORTH, registros);

        JScrollPane scroll = new JScrollPane(output);
        output.setEditable(false);
        output.setText("Output de la simulacion");
        panelBotones.add(scroll);
        layout.putConstraint(SpringLayout.WEST, scroll, 5, SpringLayout.WEST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, scroll, 40, SpringLayout.NORTH, b2);

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



