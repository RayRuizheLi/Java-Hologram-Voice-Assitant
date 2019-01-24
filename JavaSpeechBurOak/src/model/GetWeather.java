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

public class GetWeather {
	public static String CurrentWeather() {
		try {
			
			Document doc = Jsoup.connect("https://www.google.com/search?q=markham+weather&num=20").get();
			Elements weathers = doc.getElementsByAttributeValue("id", "wob_dc");
			Elements degrees = doc.getElementsByAttributeValue("id", "wob_tm");
			
			String returnString = "";
			
			for (Element weather : weathers) {
				returnString += weather.toString();
			}
			
			for (Element degree : degrees) {
				returnString += " " + degree.toString();
			}
			
			return returnString;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
