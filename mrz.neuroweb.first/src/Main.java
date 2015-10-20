
import java.util.Locale;
import javax.swing.JFrame;
import windows.Window;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author NafanyaVictorovna
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Window window = new Window("smth");
        window.setSize(700, 700);
        window.setResizable(true);
        window.setLocation(430, 175);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        window.repaint();
    }
    
}
