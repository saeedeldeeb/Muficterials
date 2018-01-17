/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package OR1_AHP;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author power
 */
public class SolutionResult extends JFrame {
    
    double [][] PV_values ;
     double [] solve;
     double [] hmax ;
     double [] CI ;
     
     int IDname;
     double number ;
     
     double max;
   
     public SolutionResult(){
     
     }
     
     
    
    public SolutionResult(String Cnames [] , String Anames [] , double [][] PV_Array , double[] hMax, double[]C_I){
        
        ImageIcon icon = new ImageIcon("src/Images/ImageIcon.png");
        this.setIconImage(icon.getImage());
        
        getContentPane().setBackground(new Color(153,153,153));
        
        setLayout(new GridLayout(Anames.length+2, 1));
        
        
    this.PV_values = PV_Array ;
    this.hmax = hMax ;
    this.CI = C_I ;
          String [] text = new String[Anames.length+2];

//    PV_values = new double[Anames.length+1][Cnames.length];
    
    
     solve = new double[Anames.length] ;
    
     number = 0 ;
     max = 0 ;
     IDname = 0 ;
    
     
                 text[0] = "\n \t\t - Criteria Table - \n"
                         +"\n  hmax  =  "+hmax[0]
                         +"\n  C.I   =  "+CI[0];

                 int k ;
        for ( k = 1; k < C_I.length; k++) {

            System.out.println(hmax[k]+" - "+CI[k]); 
            
            text[k] = "\n \t\t - Alternative Table "+k+"("+Anames[k-1]+")- \n"
                         +"\n  hmax  =  "+hmax[k]
                         +"\n  C.I   =  "+CI[k];

        }
     
     
     
    
        for (int i = 0; i < PV_values.length-1; i++) {
            
            for (int j = 0; j < PV_values[i].length; j++) {

                number += PV_values[i*0][j] * PV_values[i+1][j];
                
                
                
            }
            
            solve[i] = number ;
            
            if(number > max){
            
            max = number ;
            IDname = i ;
            
            }
            
            number = 0 ;
        }
        
        text[k] = "\n \t\t - Finish Solution - \n"
                         +"\n* The Best is ( "+Anames[IDname]+" )"
                         +"\n* the value : "+max;
               

        for (int i = 0; i < text.length; i++) {
            
             JTextArea textarea = new JTextArea(text[i], 3*text.length, 20);
             JScrollPane scroll = new JScrollPane(textarea);
                                  scroll.getVerticalScrollBar().setUnitIncrement(14);
             textarea.setEditable(false);
             textarea.setFont(new Font("Serif", Font.ITALIC, 18));
textarea.setLineWrap(true);
textarea.setWrapStyleWord(true);

add(scroll);
        }
       
    }
}
