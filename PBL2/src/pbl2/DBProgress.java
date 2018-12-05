package pbl2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Label;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class DBProgress extends JPanel { 
        JProgressBar p; 
        Label status; 

        public DBProgress() { 
                setLayout(new BorderLayout()); 
                p = new JProgressBar(); 
                p.setMinimum(0); 
                p.setMaximum(100); 
                p.setValue(0); 
                p.setStringPainted(true);
                status = new Label(""); 
                
                add(p,"Center"); 
                add(status, "South"); 
        } 

        public void go() { 
                try        { 
                        for(int i=0;i<=100;i++) { 
                                p.setValue(i); 
                                Thread.sleep(1000); 
                                status.setText(""+i+"% 진행중..."); 
                        } 
                } 
                catch (InterruptedException e) {} 
        } 

        public Dimension getPreferredSize() { 
                return new Dimension(300, 80); 
        } 
}
