package Flappy_Bird_Clone;

import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Main {
    public static void main(String[] args){
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("Flappy Bird");

        Dimension scrsize = Toolkit.getDefaultToolkit().getScreenSize();
        int scr_w=(int) scrsize.getWidth();
        int scr_h=(int) scrsize.getHeight();
        window.setLocation(scr_w/2-640/2,scr_h/2-940/2);
        
        Window contents = new Window();
        window.add(contents);

        window.pack();
        window.setVisible(true);

        contents.startthread();
        contents.run();
    }
}

