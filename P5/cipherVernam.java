import java.util.*;
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class cipherVernam extends JFrame
{
    private static int n_celulas = 512, regla_escogida = 2, celula_central = 256, n_vecinos = 1;
    private static Vector password_en_ASCII = new Vector(), password_binario = new Vector(), mensaje_en_binario = new Vector(),
            cifrado = new Vector();
    private static int[] v = {2, 3, 5, 6, 9, 10, 11, 18, 19, 21, 22, 25, 26, 27, 34, 35, 37, 38, 
                        41, 42, 43, 50, 51, 53, 54, 57, 58, 59, 66, 67, 69, 70, 73, 74, 75, 82, 83, 
                        85, 86, 89, 90, 91, 98, 99, 101, 102, 105, 106, 107, 114, 115, 117, 118, 121, 122, 
                        123, 130, 131, 133, 134, 137, 138, 139, 146, 147, 149, 150, 153, 154, 155, 162, 
                        163, 165, 166, 169, 170, 171, 178, 179, 181, 182, 185, 186, 187, 194, 195, 197, 
                        198, 201, 202, 203, 210, 211, 213, 214, 217, 218, 219}, t_1 = new int[n_celulas], actual = new int[n_celulas],
                        evolucion_celula_central = new int[mensaje_en_binario.size()], code;
    private static JFrame f;
    private static String password, mensaje;
    private static String[] menus = { "Opcion A","Opcion B","Opcion C", "Acerca de" };
    private static String[] itemsMenu = { "Sección 1A","Sección 1B", "Sección 1C", "Ayuda" };
    private static String[] opciones = { "Opción 1A","Opción 1B", "Opción 1C" };
    private static JTextField textField, text_mensaje, mensaje_cifrado;
    
    public static int miXOR(int a, int b) { if(a==b)return(0);else return(1);}

    //====================================================== AUTOMATA =======================================
    public static void decimal_a_binario_2()
    {
        code = new int[8];
        int i = 0, decimal = regla_escogida;

        while(decimal > 0 && i < 8)
        {
            code[i++] = decimal % 2;
            decimal = decimal / 2;
        }
    }

    public static void rellenar_vectores()
    {
        for( int i = 0; i < password_binario.size(); i++ )
            t_1[i] = (int)password_binario.elementAt(i);

        for( int i = password_binario.size(); i < n_celulas; i++ )
            t_1[i] = 0;
        System.arraycopy(t_1, 0, actual, 0, t_1.length);
    }

    public static void calcular_estado_actual()
    {
        int sumatorio = 0;
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
            actual[i] = code[sumatorio % 8]; //para que no se salga del vector
        }
        System.arraycopy(actual, 0, t_1, 0, actual.length);
    }

    public static void automata()
    {
        rellenar_vectores();
        for( int i = 0; i < mensaje_en_binario.size()-1; i++ )
        {
            evolucion_celula_central[i] = actual[256];
            calcular_estado_actual();
        }
        //System.out.println(Arrays.toString(evolucion_celula_central));
    }
    //====================================================== PASAR A BINARIO =======================================
    public static void to_binary()
    {
        decimal_a_binario_2();
        password_en_ASCII.clear();
        password_binario.clear();
        mensaje_en_binario.clear();
        for( int i = 0; i < password.length(); i++ )
            password_en_ASCII.add((int)password.charAt(i));

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
                password_binario.addElement(code[j++]);
        }
        Vector mensaje_en_ASCII = new Vector();
        for( int i = 0; i < mensaje.length(); i++ )
            mensaje_en_ASCII.addElement((int)mensaje.charAt(i));

        j = 8;
        code = new int[8];
        for (int i = 0; i < mensaje_en_ASCII.size(); i++)
        {
            decimal = (int)mensaje_en_ASCII.elementAt(i);
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
                mensaje_en_binario.addElement(code[j++]);
        }
        evolucion_celula_central = new int[mensaje_en_binario.size()];
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
    class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String name = ((JButton)e.getSource()).getText();
            if(name.equals("Cifrar"))
            {
                password = textField.getText();
                mensaje = text_mensaje.getText();
                to_binary();
                automata();
                //System.out.println(Arrays.toString(evolucion_celula_central));
                //System.out.println(mensaje_en_binario);
                int[] criptograma = new int[mensaje_en_binario.size()];
                for( int i = 0; i < evolucion_celula_central.length; i++ )
                    criptograma[i] = miXOR((int)mensaje_en_binario.elementAt(i), evolucion_celula_central[i]);
                char[] mensaje_final = new char[criptograma.length/8];
                int numero = 0;
                for ( int i = 0; i < criptograma.length; i = i+8 )
                {
                    int j = 0;
                    numero = 0;
                    while ( j < 8 )
                    {
                        if( criptograma[i+j] == 1)
                            numero += Math.pow( 2, 7-j );
                        j++;
                    }
                    mensaje_final[i/8] = (char)numero;
                }
                mensaje_cifrado.setText(null);
                mensaje_cifrado.setText(new String(mensaje_final));
            }
            if(name.equals("Limpiar"))
            {
                textField.setText(null);
                text_mensaje.setText(null);
                mensaje_cifrado.setText(null);
            }
        }
    }

    // ======================================== PANEL BOTONES ========================================

    private JPanel crearPanelBotones()
    {
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT, 150,10) );
        JLabel label = new JLabel("Regla a escoger:");
        panelBotones.add(label);
        JComboBox combo = new JComboBox();
        for (int estado : v) combo.addItem(estado);
        combo.addActionListener(e -> regla_escogida = v[combo.getSelectedIndex()]);
        panelBotones.add(combo);
        JLabel label2 = new JLabel("Password:");
        panelBotones.add(label2);
        textField = new JTextField(30);
        panelBotones.add(textField);
        JLabel label3 = new JLabel("Mensaje a cifrar:");
        panelBotones.add(label3);
        text_mensaje = new JTextField(30);
        panelBotones.add(text_mensaje);
        JLabel label4 = new JLabel("Mensaje cifrado:");
        panelBotones.add(label4);
        mensaje_cifrado = new JTextField(30);
        panelBotones.add(mensaje_cifrado);
        JButton b1 = new JButton("Cifrar");
        b1.addActionListener(bl);
        panelBotones.add(b1);
        JButton b2 = new JButton("Limpiar");
        b2.addActionListener(bl);
        panelBotones.add(b2);
        
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
        frame.setSize(650, 500);
        frame.setVisible(true);
    }
}
