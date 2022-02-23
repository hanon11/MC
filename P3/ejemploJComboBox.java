//ejemplo de uso de Combo; puede utilizarse para escoger el numero de estados/celula en la implementacion de una AC-1D.

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class ejemploJComboBox {

	private JTextField tf;
	private JComboBox combo;
	private JFrame v;

	public static void main(String[] args) {new ejemploJComboBox();}
	
	public ejemploJComboBox(){
		// Un campillo de texto con JTextField..
		tf = new JTextField(20);
		// Creacion del JComboBox y opciones...
		combo = new JComboBox();
		combo.addItem("2 estados");
		combo.addItem("3 estados");
		combo.addItem("4 estados");
                combo.addItem("5 estados");
		
		// Procesador de eventos cuando cambia la opcion escogida...
		combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tf.setText("Ha escogido "+combo.getSelectedItem().toString()+" por celula");
			}
		});

		// el marco...
		v = new JFrame();
		v.getContentPane().setLayout(new FlowLayout());
		v.getContentPane().add(combo);
		v.getContentPane().add(tf);
		v.pack();
		v.setVisible(true);
		v.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}