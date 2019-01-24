package model;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.*;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sphinx.api.*;
import edu.cmu.sphinx.result.WordResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class GetBus {

	public static String busUrl() {
		LocalDateTime currentTime = LocalDateTime.now();
		String isPM = "a";
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("MM-dd-yyyy");		
		String dateString = format.format(currentTime);				
		format = DateTimeFormatter.ofPattern("H");
		String hourString = format.format(currentTime);
		if (Integer.parseInt(hourString) > 12) {
			hourString = Integer.toString((Integer.parseInt(hourString)-12));
			isPM = "p";
		}
		format = DateTimeFormatter.ofPattern("mm");
		String minuteString = format.format(currentTime);
		
		//System.out.println(dateString); //DEBUG
		//System.out.println(hourString); //DEBUG
		//System.out.println(minuteString); //DEBUG
		String site = "https://tripplanner.yrt.ca/hiwire?Date="+dateString+"&TimeHour="+hourString+"&TimeMinute="+minuteString+"&Meridiem="+isPM+"&.a=iNextBusFind&.s=8ab4226f&ShowTimes=1&NumStopTimes=5&GetSchedules=1&EndGeo=&StopAbbr=1368&.a=iNextBusFind";
		
		return site;
	}
	
	public static String busTime() {
		try {		
			boolean printedSomething = false;
			
			Document doc = Jsoup.connect(busUrl()).get();
			Elements content = doc.getElementsByAttributeValue("width","90px");
			Elements routes = doc.getElementsByAttributeValue("width","393px");		
			
			ArrayList<String> routeNames = new ArrayList<String>();
			for (Element route : routes) {
				routeNames.add(route.text());
			}			
			
			ArrayList<String> times = new ArrayList<String>();
			for (Element time : content) {
				times.add(time.text());
			}
			int routeNum = 0;
			boolean estimated = false;
			
			String returnString = "";
			
			for (int i = 0; i < 2; i++) {
				String nextTime = times.get(i);
				if (estimated == false) {
					returnString += "Route: " + routeNames.get(routeNum);
					//System.out.println("Route: " + routeNames.get(routeNum));
					
					returnString += ". Scheduled Time: ";
					//System.out.print("Scheduled Time: ");
					routeNum++;
				} else {
					returnString += " Estimated Time: ";
					//System.out.print("Estimated Time: ");
				}
				returnString += nextTime;
				//System.out.println(nextTime);
				
				printedSomething = true;
				if (estimated == false)
					estimated = true;
				else	
					estimated = false;
			}
			
			if (printedSomething == false) {
				returnString += "No buses available";
				System.out.println("No buses available.");
			}
			
			//System.out.println(returnString); //debug
			return returnString; //returns next bus time
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; //default		
	}
	
	public static void main (String[] args) {		
		
	}
}
