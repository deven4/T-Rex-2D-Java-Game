package Utils;

import java.util.Timer;
import java.util.TimerTask;

public class Utils {

    public static void setTimeout(Runnable runnable, int delay) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
                timer.cancel();
            }
        }, delay);
    }
}
