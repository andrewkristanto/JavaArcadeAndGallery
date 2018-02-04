package cs1302.arcade;

import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyValue;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.Pane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.util.ArrayList;

/**
 * This class is used to create the Arcade stage
 * 
 * @author Morgan, Andrew 
 *
 */
@SuppressWarnings("restriction")
public class ArcadeApp extends Application {


	private Scene scene = null;
	private Scene intro = null;
	private StackPane introPane = new StackPane();
	private BorderPane mainMenu = new BorderPane();
	private StackPane center = new StackPane();
	
	private final Menu file = new Menu("File");
	private final Menu help = new Menu("Help");
	private final Menu score = new Menu("High Scores");
	private MenuItem exit = new MenuItem("Exit");
	private MenuItem reversiHelp = new MenuItem("Reversi");
	private MenuItem spaceInvadersHelp = new MenuItem("Space Invaders");
	private MenuItem scoreItem = new MenuItem("High Score List");
	private MenuBar menuBar = new MenuBar();
	
	String MenuBackground = "Hi-Tech-Wallpaper-1.png";
	ImageView menuBackground = new ImageView(new Image(MenuBackground, 640, 480, false, true));
	
	String ReversiImage = "Reversi.gif";
	ImageView reversiImage = new ImageView(new Image(ReversiImage, 200, 200, false, true));
	
	String SpaceInvadersImage = "spaceinvaders300.jpg";
	ImageView spaceInvadersImage = new ImageView(new Image(SpaceInvadersImage, 300, 200, true, true));

	@Override
	/**
	 * Generates the Arcade App main menu
	 */
    public void start(Stage stage) {
	
		Image gif = new Image("blockgif.gif", 640, 480, false, true);
		ImageView opening = new ImageView(gif);
		HighScoreList.setInitialPlayer();
		
		//adds border to images so that they stand out against background
		DropShadow border = new DropShadow(2, Color.WHITESMOKE);
		spaceInvadersImage.setEffect(border);
		reversiImage.setEffect(border);

		//creates background
		center.getChildren().add(menuBackground);
		
		//create Reversi button
		Button reversi = new Button();
		reversi.setGraphic(reversiImage);
		center.getChildren().add(reversi);
		center.setAlignment(reversi, Pos.CENTER_LEFT);
		center.setMargin(reversi, new Insets(20, 20, 20, 40));
		
		//create Space Invaders button
		Button spaceInvaders = new Button();
		spaceInvaders.setGraphic(spaceInvadersImage);
		center.setAlignment(spaceInvaders, Pos.CENTER_RIGHT);
		center.setMargin(spaceInvaders, new Insets(20, 30, 20, 20));
		center.getChildren().add(spaceInvaders);
		
		scoreItem.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0){
				VBox highScore = new VBox(20);
		        Stage highScoreStage = new Stage();
		        Scene highScoreScene = new Scene(highScore);
		        String scoreList = "Space Invaders High Scores:\n" + HighScoreList.getHighScoreList();
		        Text Scores = new Text(scoreList);
		        
		        highScore.setPadding(new Insets(10, 50, 50, 50));
		        highScore.setSpacing(10);
		        highScore.setAlignment(Pos.CENTER);
		        highScore.getChildren().add(Scores);
		          
		        highScoreStage.setTitle("Reversi Help");
		        highScoreStage.setScene(highScoreScene);
		        highScoreStage.sizeToScene();
		        highScoreStage.initModality(Modality.APPLICATION_MODAL);
		        highScoreStage.show();
			}
		});
		
		
		
		//add menu options
		reversiHelp.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) { // creates the instructions for Reversi
				VBox helpVbox = new VBox(20);
		        Stage helpStage = new Stage();
		        Scene helpScene = new Scene(helpVbox);
		        
		        Text instructions = new Text("Reversi!\n "
		        		+ "The goal of the game is to fill the board with more of\n"
		        		+ "your pieces than your opponent's. Placing a tile flips\n"
		        		+ "all of your opponent's pieces in a straight line if\n"
		        		+ "bounded by your own piece. Yellow tiles represent the\n"
		        		+ "possible moves. Black and white tiles represent each\n"
		        		+ "player's pieces. Use the mouse to click a tile.");
		        instructions.setFont(new Font(20));
		        instructions.setTextAlignment(TextAlignment.CENTER);
		        
		        helpVbox.setPadding(new Insets(10, 50, 50, 50));
			    helpVbox.setSpacing(10);
			    helpVbox.setAlignment(Pos.CENTER);
		        helpVbox.getChildren().add(instructions);
		          
		        helpStage.setTitle("Reversi Help");
		        helpStage.setScene(helpScene);
		        helpStage.sizeToScene();
		        helpStage.initModality(Modality.APPLICATION_MODAL);
		        helpStage.show();
			} // handle
			
		}); // Reversi help button
		
		spaceInvadersHelp.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) { // creates the instructions for Space Invaders
				VBox helpVbox = new VBox(20);
		        Stage helpStage = new Stage();
		        Scene helpScene = new Scene(helpVbox);
		         
		        Text instructions = new Text("Space Invaders!\n "
		        		+ "The goal of the game is survive as long as you can while\n"
		        		+ "shooting as many enemies as you can. The bottom two rows of\n"
		        		+ "aliens are worth 10 points, the next two rows of aliens are\n"
		        		+ "worth 20 points, the top row of aliens are worth 30 points.\n"
		        		+ "The aliens move faster and faster as you gain more points.\n"
		        		+ "Use the left and right arrow keys to move and spacebar to\n"
		        		+ "shoot.");
		        instructions.setFont(new Font(20));
		        instructions.setTextAlignment(TextAlignment.CENTER);
		        
		        helpVbox.setPadding(new Insets(10, 50, 50, 50));
			    helpVbox.setSpacing(10);
			    helpVbox.setAlignment(Pos.CENTER);
		        helpVbox.getChildren().addAll(instructions);
		          
		        helpStage.setTitle("Space Invaders Help");
		        helpStage.setScene(helpScene);
		        helpStage.sizeToScene();
		        helpStage.initModality(Modality.APPLICATION_MODAL);
		        helpStage.show();
			} // handle
			
		}); // Space Invaders Help Button
		
		help.getItems().addAll(reversiHelp, spaceInvadersHelp);
		exit.setOnAction(e -> stage.close());
		file.getItems().add(exit);
		score.getItems().add(scoreItem);

		menuBar.getMenus().addAll(file, help, score);	
		mainMenu.setTop(menuBar);
		mainMenu.setCenter(center);
	
		
		
		reversi.setOnAction(new EventHandler<ActionEvent>(){
	
			@Override
			public void handle(ActionEvent event) { // creates Reversi game
				BorderPane border = new BorderPane();
				Scene Reversi = new Scene(border, 640, 480);
				MenuBar menuBar = new MenuBar();
				Menu file = new Menu("File");
				MenuItem exit = new MenuItem("Return to Main Menu");
				
				exit.setOnAction(e -> stage.setScene(scene));
				file.getItems().add(exit);
				menuBar.getMenus().add(file);
				
				border.setTop(menuBar);
				BorderPane r = new Reversi(stage, scene);
				border.setCenter(r);

				stage.setScene(Reversi);
				stage.sizeToScene();
			} // handle
			
		}); // Reversi Button
		

		spaceInvaders.setOnAction(new EventHandler<ActionEvent>(){
	
			@Override
			public void handle(ActionEvent event) { // creates Space Invaders game
				BorderPane border = new BorderPane();
				Scene spaceInvaders = new Scene(border, 640, 480);
				MenuBar menuBar = new MenuBar();
				Menu file = new Menu("File");
				MenuItem exit = new MenuItem("Return to Main Menu");
				
				exit.setOnAction(e -> stage.setScene(scene));
				file.getItems().add(exit);
				menuBar.getMenus().add(file);
					
				border.setTop(menuBar);
				Pane p = new SpaceInvaders(stage, scene, spaceInvaders);
				
				border.setCenter(p);
					
				stage.setScene(spaceInvaders);
				stage.sizeToScene();
			} // handle
			
		}); // Space Invaders button
	
	    scene = new Scene(mainMenu, 640, 480);
	    introPane.getChildren().add(opening);
	    Button b = new Button("PRESS TO START");
	    	b.setOnAction(e -> stage.setScene(scene));
	    Text names = new Text("cs1302.arcade\n"
	    		+ "Team Foo\n"
	    		+ "Andrew and Morgan");
	    names.setFill(Color.WHITE);
	    names.setTextAlignment(TextAlignment.CENTER);
	    names.setFont(new Font(15));
	    introPane.setMargin(b, new Insets(10, 10, 10, 10));
	    introPane.setMargin(names, new Insets(10, 10 ,10 ,10));
	    introPane.getChildren().addAll(names, b);
	    introPane.setAlignment(names, Pos.TOP_CENTER);
	    introPane.setAlignment(b, Pos.BOTTOM_CENTER);
	    intro = new Scene(introPane, 640, 480);
	    
	    stage.setTitle("cs1302-arcade!");
	    stage.setScene(intro);
	    stage.sizeToScene();
	    stage.show();

    } // start

	/**
	 * Executes the ArcadeApp
	 * @param args initial user input
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
} // ArcadeApp