package cs1302.arcade;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.io.InputStream;

import javafx.event.ActionEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * This class is used to create a new scene that will set up the Reversi game
 * @author Morgan, Andrew
 *
 */
@SuppressWarnings("restriction")
public class Reversi extends BorderPane {
				
	private Button[][] grid = new Button[8][8];
	private String turn = "-fx-background-color: BLACK; -fx-background-insets: 2;";
	private String opp = "-fx-background-color: WHITE; -fx-background-insets: 2;";
	private String winner = "";
	private Stage stage = null;
	private Scene scene = null;
	private GridPane reversiGrid = new GridPane();
	private VBox turnPane = new VBox();
	private Text turnText;
	private Text scoreText;
	
	/**
	 * Creates a Reversi BorderPane with initial game board and receives the main menu stage and scene to switch back
	 * @param stage main menu stage
	 * @param scene main menu scene
	 */
	public Reversi(Stage stage, Scene scene){
	
		this.stage = stage;
		this.scene = scene;
		
		//sets up intital game board on the GridPane
		for (int r = 0; r < 8; r++){
			for (int c = 0; c< 8; c++){
				grid[r][c] = new Button();
				grid[r][c].setOnAction(new myEventHandler());		
				
				grid[r][c].setMinSize(70, 57);
				reversiGrid.add(grid[r][c], c, r);
				if ((r == 3 && c == 3) || (r == 4 && c == 4)){
					grid[r][c].setStyle(turn);
				} // if
				else if ((r == 3 && c == 4) || (r == 4 && c == 3)){
					grid[r][c].setStyle(opp);
				} // if
				else
					grid[r][c].setStyle("-fx-background-color: GREEN; -fx-background-insets: 2;");
			} // c
		} // r
		possibleMoves();
		
		// sets the Vbox with text
		turnPane.setPrefSize(80, 456);
		turnPane.setStyle("-fx-background-color: BLACK;");
		turnPane.setAlignment(Pos.CENTER);
		
		turnText = new Text(turn.substring(22,27));
		turnText.setStyle("-fx-fill: WHITE;");
		turnText.setFont(new Font(25));
		
		scoreText = new Text(displayScore());
		scoreText.setStyle("-fx-fill: WHITE;");
		scoreText.setFont(new Font(25));
		
		turnPane.getChildren().addAll(turnText,scoreText);
		setCenter(reversiGrid);
		setLeft(turnPane);
	} // Reversi
	
	/**
	 * This class handles each player's clicks
	 * @author Morgan, Andrew 
	 */
	class myEventHandler implements EventHandler<ActionEvent>{
		/**
		 * Executes the the player's move after a click
		 */
		public void handle(ActionEvent event) {
			for (int r = 0; r < 8; r++){
				for (int c = 0; c< 8; c++){
					if (event.getSource() == grid[r][c]){ // Index of where the player clicked
						if (grid[r][c].getStyle().equals("-fx-background-color: YELLOW; -fx-background-insets: 2;")){ // Makes sure it is a possible move then executes the move
							clearGrid();
							changeGrid(r, c);
							switchPlayer();
							possibleMoves();
							
							boolean gameOver = checkOver();
							if(!canMove()) { // Checks if both players can't move then ends the game
								switchPlayer();
								possibleMoves();
								if(!canMove()) {
									gameOver = true;
								} // if
							} // if
							if (gameOver){  // Creates pop-up end screen
						         VBox winVbox = new VBox(20);
						         Stage winStage = new Stage();
						         Scene winScene = new Scene(winVbox);
						         
						         Button end = new Button("Return to Main Menu?");
						         end.setOnAction(e -> {
						        	 stage.setScene(scene);
						        	 winStage.close();	
						         });
						         if (calculateScore(turn) > calculateScore(opp)){
						        	 winner = turn.substring(22, 27) + " Player wins!";
						         } // if
						         else if (calculateScore(turn) < calculateScore(opp)){
						        	 winner = opp.substring(22, 27) + " Player wins!";
						         } // if
						         else if (calculateScore(turn) == calculateScore(opp)){
						        	 winner = "Tie Game!";
						         } // if
						         Text win = new Text(winner);
						         win.setFont(new Font(30));
						         
						         winVbox.setPadding(new Insets(10, 50, 50, 50));
							     winVbox.setSpacing(10);
							     winVbox.setAlignment(Pos.CENTER);
						         winVbox.getChildren().addAll(win, end);
						          
						         winStage.setTitle("Game Over!");
						         winStage.setScene(winScene);
						         winStage.sizeToScene();
						         winStage.initModality(Modality.APPLICATION_MODAL);
						         winStage.show();
							} // if
						} // style
					} // grid position
				} // c
			} // r
		} // handle
	} // actionListener
	
	/**
	 * Changes the grid to reveal the spaces the current player can move
	 */
	private void possibleMoves(){
		for (int r = 0; r < 8; r++){
			for (int c = 0; c < 8; c++){
				if (grid[r][c].getStyle().equals(turn)){
					boolean checkOpposite = false;
					for(int i=1; i<8; i++){ // check down
						if(r+i>7){ // out of bounds check
							break;
						} // if
						if(grid[r+i][c].getStyle().equals(opp)){ // checks if spot is the opposite piece
							checkOpposite = true;
						} // if
						else if(grid[r+i][c].getStyle().equals("-fx-background-color: GREEN; -fx-background-insets: 2;") && checkOpposite){ // checks if spot is empty and everything before were the other piece
							grid[r+i][c].setStyle("-fx-background-color: YELLOW; -fx-background-insets: 2;");
							break;
						} // if
						else break;
					} // for
					
					checkOpposite=false;
					for(int i=1; i<8; i++){ // check up
						if(r-i<0){
							break;
						} // if
						if(grid[r-i][c].getStyle().equals(opp)){
							checkOpposite = true;
						} // if
						else if(grid[r-i][c].getStyle().equals("-fx-background-color: GREEN; -fx-background-insets: 2;") && checkOpposite){
							grid[r-i][c].setStyle("-fx-background-color: YELLOW; -fx-background-insets: 2;");
							break;
						} // if
						else break;
					} // for
					
					checkOpposite=false;
					for(int i=1; i<8; i++){ // check right
						if(c+i>7){
							break;
						} // if
						if(grid[r][c+i].getStyle().equals(opp)){
							checkOpposite = true;
						} // if
						else if(grid[r][c+i].getStyle().equals("-fx-background-color: GREEN; -fx-background-insets: 2;") && checkOpposite){
							grid[r][c+i].setStyle("-fx-background-color: YELLOW; -fx-background-insets: 2;");
							break;
						} // if
						else break;
					} // for
					
					checkOpposite=false;
					for(int i=1; i<8; i++){ // check left
						if(c-i<0){
							break;
						} // if
						if(grid[r][c-i].getStyle().equals(opp)){
							checkOpposite = true;
						} // if
						else if(grid[r][c-i].getStyle().equals("-fx-background-color: GREEN; -fx-background-insets: 2;") && checkOpposite){
							grid[r][c-i].setStyle("-fx-background-color: YELLOW; -fx-background-insets: 2;");
							break;
						} // if
						else break;
					} // for
					
					checkOpposite=false;
					for(int i=1; i<8; i++){ // check down right
						if((r+i>7) || (c+i>7)){
							break;
						} // if
						if(grid[r+i][c+i].getStyle().equals(opp)){
							checkOpposite = true;
						} // if
						else if(grid[r+i][c+i].getStyle().equals("-fx-background-color: GREEN; -fx-background-insets: 2;") && checkOpposite){
							grid[r+i][c+i].setStyle("-fx-background-color: YELLOW; -fx-background-insets: 2;");
							break;
						} // if
						else break;
					} // for
					
					checkOpposite=false;
					for(int i=1; i<8; i++){ // check down left
						if((r+i>7) || (c-i<0)){
							break;
						} // if
						if(grid[r+i][c-i].getStyle().equals(opp)){
							checkOpposite = true;
						} // if
						else if(grid[r+i][c-i].getStyle().equals("-fx-background-color: GREEN; -fx-background-insets: 2;") && checkOpposite){
							grid[r+i][c-i].setStyle("-fx-background-color: YELLOW; -fx-background-insets: 2;");
						
							break;
						} // if
						else break;
					} // for
					
					checkOpposite=false;
					for(int i=1; i<8; i++){ // check up right
						if((r-i<0) || (c+i>7)){
							break;
						} // if
						if(grid[r-i][c+i].getStyle().equals(opp)){
							checkOpposite = true;
						} // if
						else if(grid[r-i][c+i].getStyle().equals("-fx-background-color: GREEN; -fx-background-insets: 2;") && checkOpposite){
							grid[r-i][c+i].setStyle("-fx-background-color: YELLOW; -fx-background-insets: 2;");
							break;
						} // if
						else break;
					} // for

					checkOpposite=false;
					for(int i=1; i<8; i++){ // check up left
						if((r-i<0) || (c-i<0)){
							break;
						} // if
						if(grid[r-i][c-i].getStyle().equals(opp)){
							checkOpposite = true;
						} // if
						else if(grid[r-i][c-i].getStyle().equals("-fx-background-color: GREEN; -fx-background-insets: 2;") && checkOpposite){
							grid[r-i][c-i].setStyle("-fx-background-color: YELLOW; -fx-background-insets: 2;");
							break;
						} // if
						else break;
					} // for
				} // if
			} // c
		} // r
	} // possibleMoves

	/**
	 * Changes the grid with the user input
	 * @param r
	 * @param c
	 */
	private void changeGrid(int r, int c){
		grid[r][c].setStyle(turn);
		boolean checkOpposite = false;
		for(int i=1; i<8; i++){ // check down
			if(r+i>7){ // out of bounds check
				break;
			} // if
			if(grid[r+ i][c].getStyle().equals(opp)){ // checks for other piece
				checkOpposite = true;
			} // if
			else if(grid[r+i][c].getStyle().equals(turn) && checkOpposite){ // checks for current piece and all pieces previously were the other's piece
				int count = i;
					while(count>=0){ // changes all the previous spots into the current piece
						grid[r+count][c].setStyle(turn);
						count--;
					} // while
				break;
			} // if
			else break;
		} // for
		
		checkOpposite=false;
		for(int i=1; i<8; i++){ // check up
			if(r-i<0){
				break;
			} // if
			if(grid[r-i][c].getStyle().equals(opp)){
				checkOpposite = true;
			} // if
			else if(grid[r-i][c].getStyle().equals(turn) && checkOpposite){
				int count = i;
					while(count>=0){
						grid[r-count][c].setStyle(turn);
						count--;
					} // while
				break;
			} // if
			else break;
		} // for
		
		checkOpposite=false;
		for(int i=1; i<8; i++){ // check right
			if(c+i>7){
				break;
			} // if
			if(grid[r][c+i].getStyle().equals(opp)){
				checkOpposite = true;
			} // if
			else if(grid[r][c+i].getStyle().equals(turn) && checkOpposite){
				int count = i;
					while(count>=0){
						grid[r][c+count].setStyle(turn);
						count--;
					} // while
				break;
			} // if
			else break;
		} // for
		
		checkOpposite=false;
		for(int i=1; i<8; i++){ // check left
			if(c-i<0){
				break;
			} // if
			if(grid[r][c-i].getStyle().equals(opp)){
				checkOpposite = true;
			} // if
			else if(grid[r][c-i].getStyle().equals(turn) && checkOpposite){
				int count = i;
					while(count>=0){
						grid[r][c-count].setStyle(turn);
						count--;
					} // while
				break;
			} // if
			else break;
		} // for	
		
		checkOpposite=false;
		for(int i=1; i<8; i++){ // check down right
			if((r+i>7) || (c+i>7)){
				break;
			} // if
			if(grid[r+i][c+i].getStyle().equals(opp)){
				checkOpposite = true;
			} // if
			else if(grid[r+i][c+i].getStyle().equals(turn) && checkOpposite){
				int count = i;
					while(count>=0){
						grid[r+count][c+count].setStyle(turn);
						count--;
					} // while
				break;
			} // if
			else break;
		} // for
		
		checkOpposite=false;
		for(int i=1; i<8; i++){ // check down left
			if((r+i>7) || (c-i<0)){
				break;
			} // if
			if(grid[r+i][c-i].getStyle().equals(opp)){
				checkOpposite = true;
			} // if
			else if(grid[r+i][c-i].getStyle().equals(turn) && checkOpposite){
				int count = i;
					while(count>=0){
						grid[r+count][c-count].setStyle(turn);
						count--;
					} // while
				break;
			} // if
			else break;
		} // for
		
		checkOpposite=false;
		for(int i=1; i<8; i++){ // check up right
			if((r-i<0) || (c+i>7)){
				break;
			} // if
			if(grid[r-i][c+i].getStyle().equals(opp)){
				checkOpposite = true;
			} // if
			else if(grid[r-i][c+i].getStyle().equals(turn) && checkOpposite){
				int count = i;
					while(count>=0){
						grid[r-count][c+count].setStyle(turn);
						count--;
					} // while
				break;
			} // if
			else break;
		} // for

		checkOpposite=false;
		for(int i=1; i<8; i++){ // check up left
			if((r-i<0) || (c-i<0)){
				break;
			} // if
			if(grid[r-i][c-i].getStyle().equals(opp)){
				checkOpposite = true;
			} // if
			else if(grid[r-i][c-i].getStyle().equals(turn) && checkOpposite){
				int count = i;
					while(count>=0){
						grid[r-count][c-count].setStyle(turn);
						count--;
					} // while
				break;
			} // if
			else break;
		} // for

	} // changeGrid

	/**
	 * Clears the possible moves on the board for the next turn
	 */
	private void clearGrid(){
		for (int r = 0; r < 8; r++){
			for (int c = 0; c< 8; c++){
				if(grid[r][c].getStyle().equals("-fx-background-color: YELLOW; -fx-background-insets: 2;")) {
					grid[r][c].setStyle("-fx-background-color: GREEN; -fx-background-insets: 2;");
				} // if
			} // c
		} // r
	} // clearGrid

	/**
	 * Returns whether or not the game is over
	 * @param color X or O
	 * @return true if grid is full or wipeout
	 */
	private boolean checkOver(){
		int count=0;
		for(int r=0; r<8; r++){ // checks if grid is full
			for(int c=0; c<8; c++){
				if(!grid[r][c].getStyle().equals("-fx-background-color: GREEN; -fx-background-insets: 2;") && !grid[r][c].getStyle().equals("-fx-background-color: YELLOW; -fx-background-insets: 2;")){
					count++;
				} // if
			} // for col
		} // for row
		if(count==64){
			return true;
		} // if
		
		int count2=0,count3=0;
		for(int r=0; r<8; r++){ // checks if there is a wipeout
			for(int c=0; c<8; c++){
			    if(grid[r][c].getStyle().equals(opp)){
					count2++;
				} // if
			    if(grid[r][c].getStyle().equals(turn)){
					count3++;
				} // if
			} // for col
		} // for row
		
		// if either count2 or count 3 are 0, then one piece has been completely flipped over
		return((count2==0) || (count3==0));
	} // checkOver
	
	/**
	 * Checks if the current player can move
	 * @return returns true if player can move
	 */
	private boolean canMove(){
		for(int r=0; r<8; r++){ // looks for a possible move
			for(int c=0; c<8; c++){
				if(grid[r][c].getStyle().equals("-fx-background-color: YELLOW; -fx-background-insets: 2;")){ 
					return true;
				} // if
			} // for col
		} // for row
		return false;
	} // canMove

	/**
	 * Switches the current player and the opponent player
	 */
	private void switchPlayer() {
		if (turn == "-fx-background-color: BLACK; -fx-background-insets: 2;"){
			turn = "-fx-background-color: WHITE; -fx-background-insets: 2;";
			opp = "-fx-background-color: BLACK; -fx-background-insets: 2;";
			
			turnPane.setStyle("-fx-background-color: WHITE;");
			turnText.setText(turn.substring(22,27));
			turnText.setStyle("-fx-fill: BLACK;");
			scoreText.setStyle("-fx-fill: BLACK;");
		} // if
		else{
			turn = "-fx-background-color: BLACK; -fx-background-insets: 2;";
			opp = "-fx-background-color: WHITE; -fx-background-insets: 2;";
			
			turnPane.setStyle("-fx-background-color: BLACK;");
			turnText.setText(turn.substring(22,27));
			turnText.setStyle("-fx-fill: WHITE;");
			scoreText.setStyle("-fx-fill: WHITE;");
		} // else
		scoreText.setText(displayScore());
	} // switchPlayer

	/**
	 * Counts up the total number of a piece
	 * @param color X or O
	 * @return total number of a piece
	 */
	private int calculateScore(String piece){
		int count=0;
		for(int r=0; r<8; r++){
			for(int c=0; c<8; c++){
				if(grid[r][c].getStyle().equals(piece)){ // checks if current piece then adds
					count++;
				} // if
			} // for col
		} // for row
		return count;
	} // calculateScore

	/**
	 * Calculates both players scores and returns the overall score
	 * @return returns the score ex. 0-0
	 */
	private String displayScore() {
		return calculateScore("-fx-background-color: BLACK; -fx-background-insets: 2;") + " - " + calculateScore("-fx-background-color: WHITE; -fx-background-insets: 2;");
	}
} // Reversi