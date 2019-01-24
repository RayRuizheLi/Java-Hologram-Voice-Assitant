package model;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Port;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.result.WordResult;
import marytts.modules.synthesis.Voice;
import tts.TextToSpeech;

public class SpeechCalculator {


	private boolean recogFlag = false;
	// Necessary
	TextToSpeech textToSpeech = new TextToSpeech();

	// Logger
	private Logger logger = Logger.getLogger(getClass().getName());

	// Variables
	private String result;

	// Threads
	Thread	speechThread;
	Thread	resourcesThread;

	// LiveRecognizer
	private LiveSpeechRecognizer recognizer;

	private volatile boolean recognizerStopped = true;

	private int waiting = 1;
	private boolean isBrowser = false;

	//Gif gif = new Gif();
	int googleCounter = 0;

	/**
	 * Constructor
	 */
	public SpeechCalculator() {

		// Loading Message
		logger.log(Level.INFO, "Loading../n");

		// Configuration
		Configuration configuration = new Configuration();

		// Load model from the jar
		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");

		// if you want to use LanguageModelPath disable the 3 lines after which
		// are setting a custom grammar->

		// configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin")

		// Grammar
		configuration.setGrammarPath("resource:/grammars");
		configuration.setGrammarName("grammar");
		configuration.setUseGrammar(true);

		try {
			recognizer = new LiveSpeechRecognizer(configuration);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, null, ex);
		}

		// Start recognition process pruning previously cached data.
		// recognizer.startRecognition(true);

		// that we have added on the class path
		Voice.getAvailableVoices().stream().forEach(voice -> System.out.println("Voice: " + voice));
		textToSpeech.setVoice("cmu-slt-hsmm");

		// Start the Thread
		// startSpeechThread()
		recognizer.startRecognition(true);
		startResourcesThread();
	}

	/**
	 * Starting the main Thread of speech recognition
	 */
	public void startSpeechThread() {

		System.out.println("Entering start speech thread");

		// alive?
		if (speechThread != null && speechThread.isAlive())
			return;

		// initialise
		speechThread = new Thread(() -> {

			// Allocate the resources
			recognizerStopped = false;
			logger.log(Level.INFO, "You can start to speak.../n");

			try {
				while (!recognizerStopped) {
					/*
					 * This method will return when the end of speech is
					 * reached. Note that the end pointer will determine the end
					 * of speech.
					 */
					SpeechResult speechResult = recognizer.getResult();
					if (speechResult != null) {

						result = speechResult.getHypothesis();
						System.out.println("You said: [" + result + "]/n");
						makeDecision(result);
						// logger.log(Level.INFO, "You said: " + result + "/n")
					} else
						logger.log(Level.INFO, "I can't understand what you said./n");

				}
			} catch (Exception ex) {
				logger.log(Level.WARNING, null, ex);
				recognizerStopped = true;
			}

			logger.log(Level.INFO, "SpeechThread has exited...");
		});

		// Start
		speechThread.start();

	}

	/**
	 * Stopping the main Thread of Speech Recognition
	 */
	public void stopSpeechThread() {
		// alive?
		if (speechThread != null && speechThread.isAlive()) {
			recognizerStopped = true;
			//recognizer.stopRecognition(); it will throw error ;)
		}
	}

	/**
	 * Starting a Thread that checks if the resources needed to the
	 * SpeechRecognition library are available
	 */
	public void startResourcesThread() {

		// alive?
		if (resourcesThread != null && resourcesThread.isAlive())
			return;

		resourcesThread = new Thread(() -> {
			try {

				// Detect if the microphone is available
				while (true) {
					if (AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
						// logger.log(Level.INFO, "Microphone is available./n")
					} else {
						logger.log(Level.INFO, "Microphone is not available./n");
					}

					// Sleep some period
					Thread.sleep(350);
				}

			} catch (InterruptedException ex) {
				logger.log(Level.WARNING, null, ex);
				resourcesThread.interrupt();
			}
		});

		// Start
		resourcesThread.start();
	}

	/**
	 * Takes a decision based on the given result
	 * 
	 * @param speechWords
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */

	public void makeDecision(String speech) throws MalformedURLException, IOException, URISyntaxException {

		if(speech.contains("set timer")){
			if(googleCounter == 0){
				textToSpeech.speak("ok, your 25 minutes timer is set", 1.5f, false, true);
				googleCounter = 1;
			} else{
				textToSpeech.speak("ok, a second 25 minutes timer is set", 1.5f, false, true);
			}

			return;
		} else if(speech.contains("first timer")){
			textToSpeech.speak("The frist 25 minutes timer is cancelled. Have a nice day", 1.5f, false, true);
		} else if(speech.contains("cancel")){
			textToSpeech.speak("You have 2 25 minutes timers. Which 25 minutes timer do you want to cancel? The timer set at 3 25, or the timer set at 3 26?", 1.5f, false, true);
		}

	}



	/*	public void makeDecision(String speech) throws MalformedURLException, IOException, URISyntaxException {

		// Split the sentence
		// System.out.println("SpeechWords: " +
		// Arrays.toString(speechWords.toArray()))
		// if (!speech.contains("hey"))
		// return;
		// else
		// speech = speech.replace("hey", "");

		if(recogFlag == false){

			if(speech.contains("hey bull dog")){
				if(isBrowser){
					//gif.killWindow();
					//gif = new Gif();
				}
				if(waiting == 1){
					//gif.change("dramatic stare");
					waiting = 2;
				}
				else{
					//gif.change("waiting");
					waiting = 1;


					textToSpeech.speak("yes I am listening", 1.5f, false, true);
					recogFlag = true;
				}
			} else{
				if(speech.contains("set timer")){
					textToSpeech.speak("Timer is set for 25 minutes", 1.5f, false, true);
					return;
				}else if(speech.contains("cancel")){


				} else if (speech.contains("how are you")) {
					//gif.change("nood");
					textToSpeech.speak("Fine Thanks", 1.5f, false, true);
					return;
					//			} else if (speech.contains("hey boss")) {
					//				textToSpeech.speak("can i have the pizza pliz", 1.5f, false, true);

				} else if (speech.contains("say hello")) {
					//gif.change("nose");
					textToSpeech.speak("Hello Friends", 1.5f, false, true);
					return;
					//			} else if (speech.contains("say amazing")) {
					//				textToSpeech.speak("WoW it's amazing!", 1.5f, false, true);
					//				return;
					//			} else if (speech.contains("what day is today")) {
					//				textToSpeech.speak("A good day", 1.5f, false, true);
					//				return;
				} else if (speech.contains("change to voice one")) {
					//gif.change("nood");
					textToSpeech.setVoice("cmu-slt-hsmm");
					textToSpeech.speak("Hi", 1.5f, false, true);
					return;
				} else if (speech.contains("change to voice two")) {
					//gif.change("nood");
					textToSpeech.setVoice("dfki-poppy-hsmm");
					textToSpeech.speak("At your service", 1.5f, false, true);
				} else if (speech.contains("change to voice three")) {
					//gif.change("nood");
					textToSpeech.setVoice("cmu-rms-hsmm");
					textToSpeech.speak("Greetings", 1.5f, false, true);
					//			} else if (speech.contains("search")){
					//				speech = speech.replace("search","");
					//				speech = speech.replace(" ","+");
					//				Desktop.getDesktop().browse(new URL("https://www.google.com/search?q=" + speech + "&num=20").toURI());
					//			}
					//			else if (speech.contains("define")){
					//				speech = speech.replace(" ","+");
					//				Desktop.getDesktop().browse(new URL("https://www.google.com/search?q=" + speech + "&num=20").toURI());
				}else if (speech.contains("introduce yourself")){
					//gif.change("dramatic stare");
					textToSpeech.speak("Hello, I am ray's voice assistant. Please forgive my mistakes as I am still in testing phase.", 1.5f, false, true);
					//	}
					//				else if (speech.equals("Burst Mode")){
					//				Pomodoro pomo = new Pomodoro();
					//				pomo.countDown(5);
					//			}else if(speech.contains("open reader")){
					//				File file = new File("/Program Files (x86)/Adobe/Acrobat Reader DC/Reader/AcroRd32.exe");
					//				Desktop.getDesktop().open(file);
					//				textToSpeech.speak("reader opened", 1.5f, false, true);
					//			}else if(speech.contains("open word")){
					//				File file = new File("/Users/Ray/AppData/Local/kingsoft/WPS Office/10.1.0.6749/office6/wps.exe");
					//				Desktop.getDesktop().open(file);
					//				textToSpeech.speak("word opened", 1.5f, false, true);
					//			}else if(speech.contains("open eclipse")){
					//				File file = new File("/Users/Ray/eclipse/cpp-neon/eclipse/eclipse.exe");
					//				Desktop.getDesktop().open(file);
					//				textToSpeech.speak("eclipse opened", 1.5f, false, true);
					//			}else if(speech.contains("open browser")){
					//				File file = new File("/Program Files (x86)/Google/Chrome/Application/chrome.exe");
					//				Desktop.getDesktop().open(file);
					//				textToSpeech.speak("chrome opened", 1.5f, false, true);
				}else if(speech.contains("mister me seek")){
					//gif.change("mouth");
					textToSpeech.speak("I am mister me seek, look at me!", 1.5f, false, true);
					//			}else if(speech.contains("robot")){
					//				textToSpeech.speak("what's my purpose", 1.5f, false, true);
					//			}else if(speech.contains("you pass butter")){
					//				textToSpeech.speak("oh my god", 1.5f, false, true);
				}else if(speech.contains("bus")){
					//gif.change("transition to screen");
					Desktop.getDesktop().browse(new URL(GetBus.busUrl()).toURI());
					//String busTime = GetBus.busTime();
					textToSpeech.speak("Here are the bus times", 1.5f, false, true);
					isBrowser = true;
				} else if (speech.contains("weather")) {
					//gif.change("transition to screen");
					Desktop.getDesktop().browse(new URL("https://www.google.com/search?q=markham+weather&num=20").toURI());
					//String weather = GetWeather.CurrentWeather();
					textToSpeech.speak("Here are the weather", 1.5f, false, true);
					isBrowser = true;
				} 

				//recogFlag = false;
			}

		}
	}*/

	// /**
	// * Java Main Application Method
	// *
	// * @param args
	// */
	// public static void main(String[] args) {
	//
	// // // Be sure that the user can't start this application by not giving
	// // the
	// // // correct entry string
	// // if (args.length == 1 && "SPEECH".equalsIgnoreCase(args[0]))
	// new Main();
	// // else
	// // Logger.getLogger(Main.class.getName()).log(Level.WARNING, "Give me
	// // the correct entry string..");
	//
	// }

}