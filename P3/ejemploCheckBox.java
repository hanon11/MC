//ejemplo sencillo de uso de una checbox; puede utilizarse adaptandolo para escoger el estado de la configuracion inicial de un 
//AC 1-D para célula centra activa (resto no) o estados aleatorios

import javax.swing.*;  
import java.awt.event.*;  

public class ejemploCheckBox extends JFrame implements ActionListener{  
    JLabel l;  
    JCheckBox cb1,cb2,cb3;  
    JButton b;  

    ejemploCheckBox(){  
        l=new JLabel("Compra On-Line");  
        l.setBounds(50,50,300,20);  
        //las opciones a escoger...
        cb1=new JCheckBox("Teclado 100 EUR");  
        cb1.setBounds(100,100,150,20);  
        cb2=new JCheckBox("Raton 30 EUR");  
        cb2.setBounds(100,150,150,20);  
        cb3=new JCheckBox("Monitor 423 EUR");  
        cb3.setBounds(100,200,150,20);  
        //un boton...
        b=new JButton("Hacer Pedido...");  
        b.setBounds(100,250,200,30);  
        b.addActionListener(this);  
        add(l);add(cb1);add(cb2);add(cb3);add(b);  
        setSize(400,400);  
        setLayout(null);  
        setVisible(true);  
        setDefaultCloseOperation(EXIT_ON_CLOSE);  
    }  
    //procesador de eventos...
    public void actionPerformed(ActionEvent e){  
        float importe=0;  
        String msg="";  
        if(cb1.isSelected()){  
            importe+=100;  
            msg="Teclado: 100\n";  
        }  
        if(cb2.isSelected()){  
            importe+=30;  
            msg+="Raton: 30\n";  
        }  
        if(cb3.isSelected()){  
            importe+=423;  
            msg+="Monitor: 423\n";  
        }  
        msg+="-----------------\n";  
        JOptionPane.showMessageDialog(this,msg+"Total: "+importe);  
    }  
    public static void main(String[] args) {  
        new ejemploCheckBox();  
    }  
}  