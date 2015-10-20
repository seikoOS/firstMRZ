/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package anaglyph;

import at.imagelibrary.ImageToolException;
import at.imagelibrary.StereoImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author NafanyaVictorovna
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        JFrame frame = new JFrame("3D anaglyph");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(430, 180);

        try {        
            BufferedImage left = ImageIO.read(new File("F:\\NetBeans\\anaglyph\\src\\images\\Left.png"));        
            BufferedImage right = ImageIO.read(new File("F:\\NetBeans\\anaglyph\\src\\images\\Right.png"));           
            //фильтры для цветов
            double[][] leftFilter = {{1,0,0},{0,0,0},{0,0,0}};
            double[][] rightFilter = {{0,0,0},{0,1,0},{0,0,1}};
        StereoImage stereo = new StereoImage(left, right);
    
        ImageIcon anaglyph = new ImageIcon(stereo.getResultImage(leftFilter, rightFilter));        
        frame.add(new JLabel(anaglyph)); 
        
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ImageToolException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        frame.setVisible(true);
    }
}
