package Flappy_Bird_Clone;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Bird {
    int x;
    float y;
    float ysp;
    int jtimr;
    int radius;

    public Bird(int Rad){
        this.x=200;
        this.y=400;
        this.ysp=0;
        this.jtimr=25;
        this.radius=Rad;
    }

    public void draw(Graphics2D G){
        G.setColor(new Color(255,255,0));
        G.fillOval(x-radius,(int)y-radius,2*radius,2*radius);
    }

    public void move(List<Integer> keys){
        if ((keys.contains(KeyEvent.VK_SPACE) || keys.contains(KeyEvent.VK_W)) && jtimr>=20){
            ysp=-8;
            jtimr=0;
        } else if (ysp<7){
            ysp+=0.35f;
        }
        if (jtimr<20){
            jtimr++;
        }
        y+=ysp;        
    }

    public boolean collide(ArrayList<Pipe> pipes){
        boolean ret = false;
        if (y>830){
            ret=true;
        } else {
            for (Pipe pipe : pipes){
                if (pipe.x-pipe.width/2<x && x<pipe.x+pipe.width/2){
                    if (pipe.y-pipe.inter+radius>y || y>pipe.y+pipe.inter-radius){
                        ret=true;
                    }
                } else if(pipe.y-pipe.inter>y || pipe.y+pipe.inter<y){
                    if (pipe.x-pipe.width/2-radius<x && x<pipe.x+pipe.width/2+radius){
                        ret=true;
                    }
                } else if(Math.sqrt((double)(Math.pow(pipe.x-pipe.width/2-x,2)+Math.pow(pipe.y-pipe.inter-y,2)))<radius){
                    ret=true;
                } else if(Math.sqrt((double)(Math.pow(pipe.x+pipe.width/2-x,2)+Math.pow(pipe.y-pipe.inter-y,2)))<radius){
                    ret=true;
                } else if(Math.sqrt((double)(Math.pow(pipe.x-pipe.width/2-x,2)+Math.pow(pipe.y+pipe.inter-y,2)))<radius){
                    ret=true;
                } else if(Math.sqrt((double)(Math.pow(pipe.x+pipe.width/2-x,2)+Math.pow(pipe.y+pipe.inter-y,2)))<radius){
                    ret=true;
                }
            }
        }
        return ret;
    }

    public static boolean collide(ArrayList<Pipe> pipes, int x, int y, int radius){
        boolean ret = false;
        if (y>830){
            ret=true;
        } else {
            for (Pipe pipe : pipes){
                if (pipe.x-pipe.width/2<x && x<pipe.x+pipe.width/2){
                    if (pipe.y-pipe.inter+radius>y || y>pipe.y+pipe.inter-radius){
                        ret=true;
                    }
                } else if(pipe.y-pipe.inter>y || pipe.y+pipe.inter<y){
                    if (pipe.x-pipe.width/2-radius<x && x<pipe.x+pipe.width/2+radius){
                        ret=true;
                    }
                } else if(Math.sqrt((double)(Math.pow(pipe.x-pipe.width/2-x,2)+Math.pow(pipe.y-pipe.inter-y,2)))<radius){
                    ret=true;
                } else if(Math.sqrt((double)(Math.pow(pipe.x+pipe.width/2-x,2)+Math.pow(pipe.y-pipe.inter-y,2)))<radius){
                    ret=true;
                } else if(Math.sqrt((double)(Math.pow(pipe.x-pipe.width/2-x,2)+Math.pow(pipe.y+pipe.inter-y,2)))<radius){
                    ret=true;
                } else if(Math.sqrt((double)(Math.pow(pipe.x+pipe.width/2-x,2)+Math.pow(pipe.y+pipe.inter-y,2)))<radius){
                    ret=true;
                }
            }
        }
        return ret;
    }

    public void reset(){
        y=400;
        ysp=0;
        jtimr=25;
    }
}
