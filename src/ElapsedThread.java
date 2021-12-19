
import javafx.scene.text.Text;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author monta
 */
public class ElapsedThread extends Thread{
    
    public Text elapseTime = null;
    public int time = 0;
    public int state = 0;
    public ElapsedThread(){
        
    }
    public ElapsedThread(Text text){
        time = 0;
        elapseTime = text;
    }
    @Override 
    public void run(){
        while(true){                        
            time++;
            String text = "";
            int hour = time / 3600;
            int minute = (time - hour * 3600) / 60;
            int second = time % 60;
            if (hour >= 10)
                text = text + hour + ":";
            else
                text = text + "0" + hour + ":";
            if (minute >= 10)
                text = text + minute + ":";
            else
                text = text + "0" + minute + ":";
            if (second >= 10)
                text = text + second;
            else
                text = text + "0" + second;
            elapseTime.setText(text);
            try {
                    sleep(1000);
            } catch (InterruptedException ex) {
                 //...
            }
        }        
    }
   
}
