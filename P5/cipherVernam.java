import java.util.*;
import java.io.*;
import java.util.Vector;
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

public class cipherVernam extends JFrame
{
    private static int n_celulas = 512, regla_escogida=0;
    private static int[] v = {2, 3, 5, 6, 9, 10, 11, 18, 19, 21, 22, 25, 26, 27, 34, 35, 37, 38, 
                        41, 42, 43, 50, 51, 53, 54, 57, 58, 59, 66, 67, 69, 70, 73, 74, 75, 82, 83, 
                        85, 86, 89, 90, 91, 98, 99, 101, 102, 105, 106, 107, 114, 115, 117, 118, 121, 122, 
                        123, 130, 131, 133, 134, 137, 138, 139, 146, 147, 149, 150, 153, 154, 155, 162, 
                        163, 165, 166, 169, 170, 171, 178, 179, 181, 182, 185, 186, 187, 194, 195, 197, 
                        198, 201, 202, 203, 210, 211, 213, 214, 217, 218, 219};
    private static JFrame f;
    private static String password;
    private static Vector password_en_ASCII = new Vector(), binario = new Vector();
    private static String[] menus = { "Opcion A","Opcion B","Opcion C", "Acerca de" };
    private static String[] itemsMenu = { "Sección 1A","Sección 1B", "Sección 1C", "Ayuda" };
    private static String[] opciones = { "Opción 1A","Opción 1B", "Opción 1C" };
    private static JTextField textField;
    
    public static void to_binary()
    {
        for( int i = 0; i < password.length(); i++ )
            password_en_ASCII.addElement((int)password.charAt(i));

        int decimal, j = 8;
        int[] code = new int[8];
        for (int i = 0; i < password_en_ASCII.size(); i++)
        {
            decimal = (int)password_en_ASCII.elementAt(i);
            j = 7;
            while(decimal > 0)
            {
                code[j--] = decimal % 2;
                decimal = decimal / 2;
            }
            while( j >= 0 )
                code[j--] = 0;
            j++;
            while( j < 8 )
                binario.addElement(code[j++]);
        }
    }
  

    //============================================= LISTENERS =================================================

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
    private ButtonListener bl = new ButtonListener();
    //crea nuevo JDialog por cada boton
    class ButtonListener implements ActionListener //listeners de los botones
    {
        public void actionPerformed(ActionEvent e)
        {
            String name = ((JButton)e.getSource()).getText();
            if(name.equals("Cifrar"))
            {
                password = textField.getText();
                to_binary();
                System.out.println(binario);
            }
            if(name.equals("Limpiar"))
            {
                
            }
        }
    }

    // ======================================== PANEL BOTONES ========================================

    private JPanel crearPanelBotones()
    {
        JPanel panelBotones = new JPanel();
        SpringLayout layout = new SpringLayout();
        panelBotones.setLayout(layout);
        
        JComboBox combo = new JComboBox();
        for (int estado : v) combo.addItem(estado);
        combo.addActionListener(e -> regla_escogida = combo.getSelectedIndex());
        panelBotones.add(combo);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, combo, 0, SpringLayout.HORIZONTAL_CENTER, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, combo, 100, SpringLayout.NORTH, panelBotones);
        textField = new JTextField(50);
        panelBotones.add(textField);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, textField, 0, SpringLayout.HORIZONTAL_CENTER, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, textField, 30, SpringLayout.NORTH, combo);
        JButton b1 = new JButton("Cifrar");
        b1.addActionListener(bl);
        panelBotones.add(b1);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, b1, 0, SpringLayout.HORIZONTAL_CENTER, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b1, 30, SpringLayout.NORTH, textField);
        JButton b2 = new JButton("Limpiar");
        b2.addActionListener(bl);
        panelBotones.add(b2);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, b2, 0, SpringLayout.HORIZONTAL_CENTER, panelBotones);
        layout.putConstraint(SpringLayout.NORTH, b2, 30, SpringLayout.NORTH, b1);
        
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

    public cipherVernam(String nombre)
    {
        super(nombre);
        f = this;
        setJMenuBar(SimpleMenus()); //crear menu
        add(crearPanelBotones(), BorderLayout.CENTER);
    }
    


    public static void main( String[] args ) 
    {
        cipherVernam frame = new cipherVernam("CIFRADO DE FLUJO CON AUTOMATAS CELULARES");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }
}
