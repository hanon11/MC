import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;

 public class geometriaElemental extends JPanel {
    private static final long serialVersionUID = 7148504528835036003L;

   
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        var centro = new Point(getWidth() / 2, getHeight() / 2);
        var radio = Math.min(getWidth() / 2, getHeight() / 2) - 5;
        var diametro = radio * 2;
        var radio2 = (int)(radio * 0.9);
        var diametro2 = radio2 * 2;
        var ancho = (int)(radio2 * 1.4);
        var alto = (int)(radio2 * 0.35);

        g.setColor(Color.BLUE);
        g.fillOval(centro.x - radio, centro.y - radio, diametro, diametro);
        g.setColor(Color.YELLOW);
        g.fillOval(centro.x - radio2, centro.y - radio2, diametro2, diametro2);
        g.setColor(Color.GREEN);
        g.fillRect(centro.x - ancho/2, centro.y - alto/2, ancho, alto);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var panel = new geometriaElemental();
            panel.setBackground(Color.WHITE.darker());
            var frame = new JFrame("Geometria Elemental");
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(panel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}