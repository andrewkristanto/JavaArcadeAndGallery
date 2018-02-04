package cs1302.gallery;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.net.URL;
import java.net.URLEncoder;
import java.io.InputStreamReader;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * A class representing a Gallery App
 *
 * @author Andrew Kristanto
 */
public class GalleryApp extends Application {

    // Creation of objects
    String searchTerm = "rock";
    String encodedString = null;
    URL url;
    ArrayList<ImageView> galleryShown = new ArrayList<ImageView>();
    ArrayList<ImageView> galleryHidden = new ArrayList<ImageView>();
    GridPane grid = new GridPane();
    ProgressBar pBar = new ProgressBar(0);
    Timeline timeline = new Timeline();
    KeyFrame keyFrame;
    TextField textField = new TextField("rock");
    Button update = new Button("Update Images");
    Button pause = new Button("Pause");
    Label search = new Label("Search Query: ");
    ToolBar toolbar = new ToolBar(pause, new Separator(), search, textField, update);
    BorderPane innerPane = new BorderPane();    
    HBox bottomBar = new HBox();
    Label courtesy = new Label(" Images provided courtesy of iTunes");
    final Menu file = new Menu("File");
    final Menu theme = new Menu("Theme");
    final Menu help = new Menu("Help");
    MenuBar menuBar = new MenuBar();
    MenuItem exitItem = new MenuItem("Exit");
    MenuItem lightTheme = new MenuItem("Light Theme");
    MenuItem darkTheme = new MenuItem("Dark Theme");
    MenuItem about = new MenuItem("About");
    BorderPane outerPane = new BorderPane();
    Scene scene = new Scene(outerPane);

    /**
     * Begins execution of the gallery
     *
     * @param stage root of the GUI
     */
    @Override
    public void start(Stage stage) {

	// default query of 'rock'
	retrieveImages(stage);
	updateImages();

	// random replacement of image every 2 seconds
	keyFrame = new KeyFrame(Duration.seconds(2), event -> replaceImage());
	timeline.setCycleCount(Timeline.INDEFINITE);
	timeline.getKeyFrames().add(keyFrame);
	timeline.play();

	// allows users to search new keywords with the update button
	update.setOnAction(event -> {
	    Thread t = new Thread(() -> {
		    Platform.runLater(() -> {
		    searchTerm = textField.getText();
		    retrieveImages(stage);
		    updateImages();       
			});
		});
	    t.setDaemon(true);
	    t.start();	
	});

	// pauses/plays the random replacement of images
	pause.setOnAction(new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent e){
		    if(pause.getText().equals("Pause")){
			pause.setText("Play");
			timeline.pause();
		    }
		    else{
			pause.setText("Pause");
			timeline.play();
		    }
		}
	    });

	// creates the exit button
	exitItem.setOnAction(e -> {
		Platform.exit();
		System.exit(0);
	    });

	// creates the theme buttons
	darkTheme.setOnAction(e -> {
		toolbar.setStyle("-fx-background-color: black;");
		pause.setStyle("-fx-background-color: black; -fx-text-fill: white;");
		textField.setStyle("-fx-background-color: black; -fx-text-fill: white;");
		update.setStyle("-fx-background-color: black; -fx-text-fill: white;");
		bottomBar.setStyle("-fx-background-color: black; -fx-text-fill: white;");
		menuBar.setStyle("-fx-background-color: black; -fx-text-fill: white;");
		search.setStyle("-fx-text-fill: white;");
		courtesy.setStyle("-fx-text-fill: white;");
		file.setStyle("-fx-background-color: black; -fx-text-fill: white;");
		theme.setStyle("-fx-background-color: black; -fx-text-fill: white;");
		help.setStyle("-fx-background-color: black; -fx-text-fill: white;");
	    });

	lightTheme.setOnAction(e -> {
		toolbar.setStyle("-fx-background-color: white;");
		pause.setStyle("-fx-background-color: white; -fx-text-fill: black;");
		textField.setStyle("-fx-background-color: white; -fx-text-fill: black;");
		update.setStyle("-fx-background-color: white; -fx-text-fill: black;");
		bottomBar.setStyle("-fx-background-color: white;");
		menuBar.setStyle("-fx-background-color: white;");
		search.setStyle("-fx-text-fill: black;");
		courtesy.setStyle("-fx-text-fill: black;");
		file.setStyle("-fx-background-color: white; -fx-text-fill: black;");
		theme.setStyle("-fx-background-color: white; -fx-text-fill: black;");
		help.setStyle("-fx-background-color: white; -fx-text-fill: black;");
	    });

	// creates the about button
	about.setOnAction(e -> {
		final Stage aboutStage = new Stage();
		aboutStage.initModality(Modality.APPLICATION_MODAL);
		aboutStage.initOwner(stage);
		BorderPane aboutPane = new BorderPane();
		aboutPane.setBottom(new ImageView("https://i.imgur.com/iZRYF9x.jpg"));
		Text aboutText = new Text("Andrew Kristanto\nandrewkristanto555@gmail.com\nVersion 8.1.5");
		aboutText.setTextAlignment(TextAlignment.CENTER);
		aboutPane.setCenter(aboutText);
		Scene aboutScene = new Scene(aboutPane);
		aboutStage.setScene(aboutScene);
		aboutStage.setTitle("About Andrew Kristanto");
		aboutStage.show();
	    });

	// adds menuItems to menus
	file.getItems().add(exitItem);
	theme.getItems().addAll(darkTheme, lightTheme);
	help.getItems().add(about);

	// sets up the inside of the GUI
	innerPane.setTop(toolbar);
        innerPane.setCenter(grid);
        bottomBar.getChildren().addAll(pBar, courtesy);
        menuBar.getMenus().addAll(file, theme, help);

	// sets up the layout of the GUI
	outerPane.setTop(menuBar);
	outerPane.setCenter(innerPane);
	outerPane.setBottom(bottomBar);

	// sets the stage
	stage.setMaxWidth(640);
	stage.setMaxHeight(480);
        stage.setTitle("Gallery!");
        stage.setScene(scene);
	stage.sizeToScene();
        stage.show();
    } // start

    /**
     * Executes the start method
     *
     * @param args initial conditions
     */
    public static void main(String[] args) {
	try {
	    Application.launch(args);
	} catch (UnsupportedOperationException e) {
	    System.out.println(e);
	    System.err.println("If this is a DISPLAY problem, then your X server connection");
	    System.err.println("has likely timed out. This can generally be fixed by logging");
	    System.err.println("out and logging back in.");
	    System.exit(1);
	} // try
    } // main

    /**
     * Searches itunes with user input and stores the images in ArrayLists,
     * if less than 20 results are generated, an error dialogue box appears.
     *
     * @param stage root of the GUI
     */
    private void retrieveImages(Stage stage){
	if(searchTerm != null){
	    try{
		encodedString = URLEncoder.encode(searchTerm,"UTF-8");
	    }catch(java.io.UnsupportedEncodingException e){
		// stuff
	    } // try
	} // if
	if(encodedString != null){
	    try{
		url = new URL("http://itunes.apple.com/search?term="+encodedString);
		InputStreamReader reader = new InputStreamReader(url.openStream());
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(reader);
		JsonObject root = je.getAsJsonObject();
		JsonArray results = root.getAsJsonArray("results");
		int numResults = results.size();
		// retrieves images if results are >= 20
		if(numResults >= 20){
		    galleryShown.clear();
		    galleryHidden.clear();
		    // loops through array and adds objects into ArrayLists
		    for(int i = 0; i < numResults; i++){
			JsonObject result = results.get(i).getAsJsonObject();
			JsonElement artworkUrl100 = result.get("artworkUrl100");
			if(artworkUrl100 != null){
			    String imageUrl = artworkUrl100.getAsString();
			    // adds first 20 images to an ArrayList for show, adds additional images to another ArrayList for image replacements
			    if(galleryShown.size() == 20){
				galleryHidden.add(new ImageView(imageUrl));
			    }else{
				galleryShown.add(new ImageView(imageUrl));
			    } // if
			} // if
		    } // for
		}else{ // creates error dialogue box otherwise
		    final Stage errorStage = new Stage();
		    errorStage.initModality(Modality.APPLICATION_MODAL);
		    errorStage.initOwner(stage);
		    VBox errorVbox = new VBox(20);
		    errorVbox.getChildren().add(new Text("Search Results: inadequate"));
		    Scene errorScene = new Scene(errorVbox);
		    errorStage.setScene(errorScene);
		    errorStage.setTitle("Error");
		    errorStage.show();
		} // if   
	    }catch(java.io.IOException e){
		
	    } // try
	} // if
    } // retrieveImages

    /**
     * Adds images to the grid for display while updating progress bar
     *
     */
    private void updateImages(){
	int index = 0;
	pBar.setProgress(0.0);

	// stacked for loops in order to accomodate columns and rows of the grid
	for(int r = 0; r < 4; r++){
	    for(int c = 0; c < 5; c++){
		grid.add(galleryShown.get(index),c,r);
		index++;
		pBar.setProgress(1.0*(index+1)/20);
		try{
		    Thread.sleep(10);
		}catch(java.lang.InterruptedException e){

		}
	    } // for
	} // for
    } // updateImages

    /**
     * Replaces a random image on the grid with an image not from the grid, adjusts the Arraylists accordingly
     *
     */
    private void replaceImage(){
	int randomInt = new Random().nextInt(20);
	if(galleryHidden != null){
	    grid.add(galleryHidden.get(0), GridPane.getColumnIndex(galleryShown.get(randomInt)), GridPane.getRowIndex(galleryShown.get(randomInt)));
	    galleryHidden.add(galleryShown.set(randomInt, galleryHidden.remove(0)));
	}
    } // replaceImage

} // GalleryApp

