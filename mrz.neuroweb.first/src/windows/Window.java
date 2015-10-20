/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import neuro.Neuron;
/**
 *
 * @author NafanyaVictorovna
 */
public class Window extends JFrame{
    
    private BufferedImage src;
    private BufferedImage result;
    
    private JButton rip;
    
    private JTextArea resultText;
    
    private JLabel srcLabel;
    private JLabel resLabel;
    
    private JPanel commonPanel;
    private JPanel srcPanel;
    private JPanel resPanel;
    private JPanel textPanel;
    
    private GridLayout lay;

    public Window(String name){
        super(name);
        init();
        listeners();    
    }
    
    private void init(){
        srcPanel = new JPanel();
        resPanel = new JPanel();
        textPanel = new JPanel();
        commonPanel = new JPanel();
        
        openImage("src/images/NewTurtle.png");
        srcLabel = new JLabel(new ImageIcon(src));
        resLabel = new JLabel();
        
        resultText = new JTextArea();
        resultText.setSize(30,15);
        
        lay = new GridLayout(1,3);
        
        rip = new JButton("r.i.p");
        rip.setPreferredSize(new Dimension("r.i.p".length() + 20, "r.i.p".length() + 6));
        
        commonPanel.setLayout(lay);
        srcPanel.setLayout(new GridLayout(0,1));
        resPanel.setLayout(new GridLayout(0,1));
        textPanel.setLayout(new GridLayout(0,1));
        
        lay.addLayoutComponent("srcPanel", srcPanel);
        lay.addLayoutComponent("resPanel", resPanel);
        lay.addLayoutComponent("textPanel", textPanel);
               
        srcPanel.add(srcLabel);
        textPanel.add(resultText);
        textPanel.add(rip);
        textPanel.setMaximumSize(new Dimension(60, 30));
        
        resPanel.add(resLabel);
        
        commonPanel.add(srcPanel);
        commonPanel.add(textPanel);
        
        setContentPane(commonPanel);     
    }
    
    private void openImage(String fileName){
        try{
            src = ImageIO.read(new File(fileName));
            if(src.getHeight()<256 || src.getWidth()<256){
                JOptionPane.showMessageDialog(null, "You should choose other image");
                this.setDefaultCloseOperation(super.EXIT_ON_CLOSE);
            }
        }catch(IOException e){
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void listeners(){
        rip.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
              String n_value = JOptionPane.showInputDialog(null, "Input n - height  of rectangle", JOptionPane.QUESTION_MESSAGE);  
              String m_value = JOptionPane.showInputDialog(null, "Input m - width of rectangle", JOptionPane.QUESTION_MESSAGE);            
              int h = src.getHeight();
              int w = src.getWidth();
              int n = Integer.parseInt(n_value);
              int m = Integer.parseInt(m_value);
              if(n>h || m>w){
                  JOptionPane.showMessageDialog(null, "n and m value must be less than size of image");
              } else{
                  int l_height,l_width;
                  if(h%n == 0){l_height = h/n;} else {l_height = h/n +1;}
                  if(w%m == 0){l_width = w/m;} else {l_width = w/m +1;}
                  int l = l_height*l_width;             //number of areas
                  int area = 0;
                  Neuron neuron = new Neuron(n, m, l); 
                  int ni = 0;
                  for(int l_h=0; l_h<l_height; l_h++){
                     int mj = 0;
                     for(int l_w=0; l_w<l_width; l_w++){ 
                        if((w%m != 0) && (mj+m>=w)){mj = w - m;}
                        if((h%n != 0) && (ni+n>=h)){ni = h - n;}
                        neuron.createX();
                        int k = 0;
                        for(int i=ni; i<ni+n; i++){
                            for(int j=mj; j<mj+m; j++){
                                Color color = new Color(src.getRGB(i,j));
                                int red = color.getRed();
                                int green = color.getGreen();
                                int blue = color.getBlue();  
                                neuron.addToX((2.*red/255-1),k);   
                                neuron.addToX((2.*green/255 -1),k+1);
                                neuron.addToX((2.*blue/255 -1),k+2);
                                k += 3;
                            }
                        }
                        mj += m;                    //next rectangle in line
                        neuron.addToXList(area);  //Xi -> list
                        if(area<l){area++;}
                     }
                     ni += n;                       //next line
                  } 
                  String neuronNumber = JOptionPane.showInputDialog(null, "input count of neuron in the second layer of web less or equal "+6*m*n, JOptionPane.QUESTION_MESSAGE);
                  int p = Integer.parseInt(neuronNumber);         
                  if(p>6*n*m){
                    JOptionPane.showMessageDialog(null, "count of neurons must be less than "+(6*n*m)+"!");
                  }else{
                      String error = JOptionPane.showInputDialog(null,"input maximal error", JOptionPane.QUESTION_MESSAGE);
                      double er = Double.parseDouble(error);
                      double E = 100000000.0;
                      int iter = 0;
                      neuron.createMatrix(p);
                      while(E > er){
                          E = 0;
                          for(int s=0; s<l; s++){
                              E += neuron.error(p, s);
                          }
                          iter++;
                          System.out.println("error" +E);
                      }
                      System.out.println("user error: "+er);
                     er = E;
                     result = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
                     int[] rgb_list = new int[h*w];
                     int rgb;
                     int pos = 0;
                     int tempP = 0;
                     for(int s=0; s<l; s++){ 
                        int k = 0;
                        double[] NX = neuron.getNewX(s);
                        for(int i=0; i<n; i++){
                            for(int j=0; j<m; j++){
                                double red = NX[k];
                                double green = NX[k+1];
                                double blue = NX[k+2]; 
                                double r = (255*(red+1)/2); 
                                if(r>255){r=255;} else if(r<0){r=0;} 
                                double g = (255*(green+1)/2);
                                if(g>255){g=255;} else if(g<0){g=0;}
                                double b = (255*(blue+1)/2);
                                if(b>255){b=255;} else if(b<0){b=0;}
                                k += 3;
                                Color color = new Color((int)r, (int)g, (int)b);
                                rgb = color.getRGB();
                                rgb_list[pos] = rgb;
                                pos++;
                            }
                        }
                     }
                    ni = 0;
                    for(int l_h=0; l_h<l_height; l_h++){
                        int mj = 0;
                        for(int l_w=0; l_w<l_width; l_w++){                        
                            for(int i=ni; i<ni+n; i++){
                                for(int j=mj; j<mj+m; j++){
                                    result.setRGB(i, j, rgb_list[tempP]);
                                    if(tempP<pos){tempP++;}
                                }
                            }
                        mj += m;                    //next rectangle in line
                        }
                    ni += n;                       //next line
                   }
                  resLabel.setIcon(new ImageIcon(result));
                  System.out.println("count of iteration: "+iter);  
                  System.out.println("size of area: "+n+"*"+m); 
                  System.out.println("error "+er);
                  commonPanel.add(resPanel.add(resLabel));
                  commonPanel.updateUI();
                  System.out.println("Z "+neuron.calcZ(p));
              }
            }
         }
        });
    }   
}
