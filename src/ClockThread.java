
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class ClockThread extends Thread{
    
    Text clock;
    public ClockThread(){
        
    }
    public ClockThread(Text text){
        clock = text;
        
    }
    @Override
    public void run(){
        while(true){
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            Calendar cal = Calendar.getInstance();

            int second = cal.get(Calendar.SECOND);
            int minute = cal.get(Calendar.MINUTE);
            int hour = cal.get(Calendar.HOUR);
            String time = "";
            if (hour >= 10)
                time = time + hour + ":";
            else
                time = time + "0" + hour + ":";
            if (minute >= 10)
                time = time + minute + ":";
            else
                time = time + "0" + minute + ":";
            if (second >= 10)
                time = time + second;
            else
                time = time + "0" + second;
            clock.setText(time);
            
            try {
                    sleep(1000);
            } catch (InterruptedException ex) {
                 //...
            }
        }        
    }
}
