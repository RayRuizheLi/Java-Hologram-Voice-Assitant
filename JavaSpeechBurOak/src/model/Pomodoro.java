package model;

import java.util.Timer;
import java.util.TimerTask;

public class Pomodoro {

	private static Timer timer = new Timer();
	private static int minutes = 0;
	private static int time = 3;

	static TimerTask task = new TimerTask() {
	        @Override
	        public void run() { 
	            while (minutes < time) {
	                try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                minutes++;
	                System.out.println(minutes);
	            }
	            timer.cancel();
                timer.purge();
	          
	        }
	    };
	    
	public static void countDown(int minutes){
		time = minutes;
		task.run();
	}

	public static void main(String[] args){
		task.run();
	}


}
