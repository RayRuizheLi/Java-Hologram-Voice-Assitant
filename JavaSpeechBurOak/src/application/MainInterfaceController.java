package application;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import java.util.logging.Level;
import java.util.logging.Logger;




import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import model.SpeechCalculator;

/**
 * The main interface of the application
 * 
 * @author GOXR3PLUS
 *
 */
public class MainInterfaceController extends BorderPane {

	@FXML
	private Button start;

	@FXML
	private Button stop;

	@FXML
	private Button restart;

	@FXML
	private Label statusLabel;

	@FXML
	private TextArea infoArea;

	@FXML
	private ImageView mainImage;

	private boolean isChange = false;

	// -----------------------------------------

	private SpeechCalculator speechCalculator = new SpeechCalculator();


	/**
	 * Constructor
	 */
	public MainInterfaceController() {

		// FXMLLoader
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainInterfaceController.fxml"));
		loader.setController(this);
		loader.setRoot(this);

		try {
			loader.load();
		} catch (IOException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, " FXML can't be loaded!", ex);
		}

	}

	/**
	 * As soon as fxml has been loaded then this method will be called
	 * 1)-constructor,2)-FXMLLOADER,3)-initialize();
	 */
	@FXML
	private void initialize() {



		infoArea.setEditable(true);
		// start
		start.setOnAction(a -> {
			statusLabel.setText("Status : [Running]");
			infoArea.appendText("Starting Speech Recognizer\n");
			speechCalculator.startSpeechThread();
		});

		// stop
		stop.setOnAction(a -> {
			statusLabel.setText("Status : [Stopped]");
			infoArea.appendText("Stopping Speech Recognizer\n");
			speechCalculator.stopSpeechThread();

		});

		infoArea.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				String[] tokens = infoArea.getText().split("\\n");
				try {
					speechCalculator.makeDecision(tokens[tokens.length-1]);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		//clear
		restart.setOnAction(a -> {
			infoArea.clear();
		});

		mainImage.setOnMouseClicked(e ->{

			if(isChange == false){
				
//				setStyle("-fx-background-colour: #ffffff;");
				statusLabel.setTextFill(Color.web("#000000"));
				Image image = new Image("Colour.gif");
				mainImage.setImage(image);
				isChange = true;
			}
			else{
//				setStyle("-fx-background-colour: #000000;");
				statusLabel.setTextFill(Color.web("#ffffff"));
				Image image = new Image("CircleWhite.gif");
				mainImage.setImage(image);
				isChange = false;
			}
		});



	}
}
