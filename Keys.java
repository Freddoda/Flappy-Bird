package Flappy_Bird_Clone;

import java.awt.event.KeyListener;
import java.util.*;
import java.awt.event.KeyEvent;

public class Keys implements KeyListener{
    public List<Integer> keys = new ArrayList<>();

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (!keys.contains(code)){
            keys.add(code);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        keys.remove(keys.indexOf(code));
    }

    @Override
    public void keyTyped(KeyEvent e) {}

}
