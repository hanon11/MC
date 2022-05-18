import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JSpinner;

public class urmInterpreter extends JFrame
{
    private static JFrame f;
    private static String[] menus = { "Opcion A","Opcion B","Opcion C", "Acerca de" };
    private static String[] itemsMenu = { "Seccion 1A","Seccion 1B", "Seccion 1C", "Ayuda" };
    private static String[] opciones = { "Opcion 1A","Opcion 1B", "Opcion 1C" };
    private static Graphics g;
    private static File ruta;

    //================================================== LISTENERS BOTONES ==========================================
    private ButtonListener bl = new ButtonListener();

    static class ButtonListener implements ActionListener //listeners de los botones
    {
        public void actionPerformed(ActionEvent e)
        {}
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
        JTextArea texto = new JTextArea(10,30);
        JButton fichero = new JButton("Selecciona fichero: ");
        fichero.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(fileChooser);
            ruta = fileChooser.getSelectedFile();
            String codigo = "";
        String aux = "";
        try {
            if (ruta != null) {
            FileReader archivos = new FileReader(ruta);
            BufferedReader leer = new BufferedReader(archivos);
            while ((aux = leer.readLine()) != null) {
                codigo += aux + "\n";
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
        layout.putConstraint(SpringLayout.NORTH, fichero, 30, SpringLayout.NORTH, panelBotones);
        panelBotones.add(texto);
        layout.putConstraint(SpringLayout.WEST, texto, 10, SpringLayout.WEST, fichero);
        layout.putConstraint(SpringLayout.NORTH, texto, 30, SpringLayout.NORTH, fichero);
        
        
        JButton b1 = new JButton("Inicio");
        b1.addActionListener(bl);
        panelBotones.add(b1);
        layout.putConstraint(SpringLayout.WEST, b1, 10, SpringLayout.WEST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b1, 210, SpringLayout.NORTH, fichero);
        JButton b2 = new JButton("Reset");
        b2.addActionListener(bl);
        panelBotones.add(b2);
        layout.putConstraint(SpringLayout.WEST, b2, 90, SpringLayout.WEST, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b2, 210, SpringLayout.NORTH, fichero);

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



