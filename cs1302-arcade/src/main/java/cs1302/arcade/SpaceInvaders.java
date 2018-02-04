package cs1302.arcade;


import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.animation.KeyFrame;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.control.TextField;


import javafx.animation.Animation;

/**
 * This class is used to create a new scene that will set up the Space Invaders game
 * @author Morgan, Andrew
 *
 */
@SuppressWarnings("restriction")
public class SpaceInvaders extends Pane{
	
		private Stage stage = null;
		private Scene scene = null;
		
		private int Score = 0;
		private int level = 1;
		private int LEFT_BOUND = 10;
		private int RIGHT_BOUND = 600;
		private int UPPER_BOUND = 0;
		private int LOWER_BOUND = 480;
		private int numEndGame = 0;
		
		private Text scoreText = new Text("Score: " + Score);
		private Text levelText = new Text("Level: " + level());
		private Text lives = new Text("Lives: ");
		
		private ArrayList<ImageView> livesRemaining = new ArrayList<>(4);
		private String shipDownloaded = "unnamed.png";
		private ImageView ship = new ImageView(new Image(shipDownloaded));
		private Image shipImage = new Image(shipDownloaded, 20, 20 , true, true);
		private ImageView[][] spaceInvaders = new ImageView[5][11];
		
		private boolean movingRight = true;
		
		private Rectangle bullet = null;
		private Rectangle bulletA = null;
		private Rectangle checkEdge = new Rectangle(100, 100);
		
		private String red = "http://rs345.pbsrc.com/albums/p386/WackoWeasel/invader1.gif~c200";
		private String skull = "https://mir-s3-cdn-cf.behance.net/project_modules/disp/b1fdaa23273823.56047a9db1a79.gif";
		private String normal = "http://tilsplatform.com/wp-content/uploads/2016/11/invmed.gif";
		
		private String input = "invmed.gif";
		private Image normalImage = new Image(input, 30, 30 ,true, true);
		
		private String redAlien = "red.gif";
		private Image redImage = new Image(redAlien,30, 30, true, true);
		
		private String skullAlien = "skull.gif";
		private Image skullImage = new Image(skullAlien, 40, 40, true, true);
		
		Timeline t = null;
		Timeline bulletLife = null;
		Timeline AlienbulletLife = null;
		
		private String name = null;

		/**
		 * Constructor that creates a new Pane for the Space Invaders game. 
		 * @param stage				ArcadeApp stage, used to return to main screen
		 * @param scene				ArcadeApp scene, used to return to main screen
		 * @param currentScene		the present scene
		 */
		public SpaceInvaders(Stage stage, Scene scene, Scene currentScene){
			this.stage = stage;
			this.scene  = scene;
			
			//background of the scene
			String BackgroundStars = "ScreenshotStarfield.png";
			ImageView background = new ImageView(new Image(BackgroundStars, 640, 480, false, true));	
			this.getChildren().add(background);
			
			//ship dimentions
			ship.setPreserveRatio(true);
			ship.setFitWidth(40);
			ship.setX(320);
			ship.setY(375);
			
			//Score text 
			scoreText.setFill(Color.YELLOW);
			scoreText.setFont(new Font("Courier New", 20.5));
			scoreText.setX(500);
			scoreText.setY(20);
			
			//Level text
			levelText.setFill(Color.YELLOW);
			levelText.setFont(new Font("Courier New", 20.5));
			levelText.setX(500);
			levelText.setY(440);
	
			//Lives text
			lives.setFill(Color.YELLOW);
			lives.setFont(new Font("Courier New", 20.5));
			lives.setX(5);
			lives.setY(20);
			livesRemaining.add(new ImageView(shipImage));
			livesRemaining.add(new ImageView(shipImage));
			livesRemaining.add(new ImageView(shipImage));
			lives(false,false);
			
			//Lower bound line
			Rectangle line = new Rectangle(1000, 1);
			line.setX(0);
			line.setY(413);
			line.setFill(Color.LIMEGREEN);
		
			//moves ship
			currentScene.setOnKeyPressed(e -> shipMovement(e));
			
			//Timeline for moving aliens
			Duration d = new Duration(28);
			KeyFrame f = new KeyFrame(d, e -> {
				moveAliens();
				shootAliens();
				});
			t = new Timeline(f);
			t.setCycleCount(Animation.INDEFINITE);
			t.play();
			
			makeAliens();
			
			//add nodes to scene
			this.getChildren().addAll(ship, scoreText, levelText, lives, line);
		}

		/**
		 * This method assigns keyboard buttons to movements of the ship and the shooting of bullets
		 * @param move
		 */
		private void shipMovement(KeyEvent move){
			if (move.getCode().equals(KeyCode.LEFT)){
				ship.setX(ship.getX() - 10);
			}
			else if (move.getCode().equals(KeyCode.RIGHT)){
				ship.setX(ship.getX() + 10);
			}
			else if (move.getCode().equals(KeyCode.SPACE)){
				bullet();
			}
		}
		
		/**
		 * This method is used for determining the movement of the aliens. Each imageView alien moves slightly to the left or right
		 * until they run into the Left or Right bounds, at which point the group of aliens will move 20 pixels closer to the ship.
		 * As more aliens are removed, the speed at which the aliens move across the screen increases. If the aliens reach the green
		 * line where the ship sits, the game is terminated.
		 * 
		 */
		private void moveAliens(){
			if (movingRight){ //checks if the aliens are moving to the right
				if (rightBorder() >= RIGHT_BOUND){
					movingRight = false;
					//this double for loop moves each space invader down 20 pixels, if the aliens reach the green line, the game is over
					for (int i = 0; i < spaceInvaders.length; i++){
						for (int j = 0; j <spaceInvaders[0].length; j++){
							if (spaceInvaders[i][j] != null){
								spaceInvaders[i][j].setY(spaceInvaders[i][j].getY() + 20);
								if (spaceInvaders[i][j].getY() >= 400){
									if (numEndGame == 0)
										endGame();
								}
									
							}//if
							
						}//col
					}//rows
				}//if
				//double for loop that moves each space invader to the right 20 pixels
				for (int i = 0; i < spaceInvaders.length; i++){
					for (int j = 0; j <spaceInvaders[0].length; j++){
						if (spaceInvaders[i][j] != null){
							spaceInvaders[i][j].setX(spaceInvaders[i][j].getX() + level/2.0);
						}//if	
					}//col
				}//rows
			}
			else
			{
				if (leftBorder() <= LEFT_BOUND){
					movingRight = true;
					for (int i = 0; i < spaceInvaders.length; i++){
						for (int j = 0; j < spaceInvaders[0].length; j++){
							if (spaceInvaders[i][j] != null){
								spaceInvaders[i][j].setY(spaceInvaders[i][j].getY() + 20);
								if (spaceInvaders[i][j].getY() >= 400){
									if (numEndGame == 0)
										endGame();
								}

							}//if
							
						}//col
					}//rows	
				}//if
				for (int i = 0; i < spaceInvaders.length; i++){
					for (int j = 0; j <spaceInvaders[0].length; j++){
						if (spaceInvaders[i][j] != null){
							spaceInvaders[i][j].setX(spaceInvaders[i][j].getX() - level/2.0);
						}//if	
					}//col
				}//rows
			}
		/**
		 * this for loop determines if there are any remaining space invaders. At the beginning of each round, there are 55
		 * aliens, so this loop counts any null spaces- where a space invader was removed. If int empty reaches 55, that means
		 * there are no remaining aliens and that the next round should begin.
		 */
			int empty = 0;
			for (int i = 0; i < spaceInvaders.length; i++){
				for (int j = 0; j <spaceInvaders[0].length; j++){
					if (spaceInvaders[i][j] == null){
						empty++;
					}//if	
				}//col
			}//rows
			
			//makes a new round of aliens and adds a life
			if (empty == 55){
				makeAliens();
				lives(false,true);
			}
		}
		/**
		 * This method allows for the aliens to randomly shooot at the players ship
		 */
		private void shootAliens() {
			for(int j = 0; j < spaceInvaders[0].length; j++) {
				for(int i = spaceInvaders.length - 1; i >= 0; i--) {
					if(spaceInvaders[i][j] != null) {
						Random rNum = new Random();
						if(rNum.nextInt(100000) > 99950) {
							bulletAlien(i,j);
						}
					}
				}
			} // for alien bullets
		}
		/**
		 * Creates a bullet used for the aliens to shoot the players ship. 
		 * @param i			int used to determine the row of the space invader shooting at the ship
		 * @param j			int used to determine the column of the space invader shooting at the ship
		 */
		private void bulletAlien(int i, int j){
			if (bulletA == null){
				bulletA = new Rectangle(2,6);
				bulletA.setFill(Color.WHITE);
				this.getChildren().add(bulletA);
				bulletA.setX(spaceInvaders[i][j].getX() + 18);
				bulletA.setY(spaceInvaders[i][j].getY());
				
				Duration bulletD = new Duration(5);
				KeyFrame bulletF = new KeyFrame(bulletD, e -> {
						if (bulletA != null){
							bulletA.setY(bulletA.getY() + 1);
							hitsAlien();
						}
				});
				AlienbulletLife = new Timeline(bulletF);
				AlienbulletLife.setCycleCount(Animation.INDEFINITE);
				AlienbulletLife.play();
			}
		}
		/**
		 * Method used to determine the left most space invader. Used so that the grouping of aliens will go all the way to
		 * the left side even if the columns closest to the left side of the screen are removed.
		 * @return		x-coordinate of the left most space invader
		 */
		private double leftBorder(){
			for (int i = 0; i < spaceInvaders[0].length; i++){
				for (int j = 0; j < spaceInvaders.length; j++){
					if(!(spaceInvaders[j][i] == null)){
						return spaceInvaders[j][i].getX();
					}
				}
			}
			return LEFT_BOUND;
		}
		/**
		 * Method used to determine the right most space invader. Used so that the grouping of aliens will go all the way to
		 * the right side even if the columns closest to the right side of the screen are removed.
		 * @return		x-coordinate of the right most space invader
		 */
		private double rightBorder(){
			for (int i = spaceInvaders[0].length -1; i > 0; i--){
				for (int j = 0; j < spaceInvaders.length; j++){
					if(!(spaceInvaders[j][i] == null)){
						return spaceInvaders[j][i].getX();
					}
				}
			}
			return RIGHT_BOUND;
		}
		/**
		 * Creates a new array of aliens
		 */
		private void makeAliens(){
			for (int i = 0; i < 5; i++){
				for (int j = 0; j <spaceInvaders[0].length; j++){
					if(i < 1)
						spaceInvaders[i][j] = new ImageView(redImage);
					if (i >= 1 && i < 3)
						spaceInvaders[i][j] = new ImageView(skullImage);
					if (i >= 3)
						spaceInvaders[i][j] = new ImageView(normalImage);
					spaceInvaders[i][j].setX(j*35 + 100);
					spaceInvaders[i][j].setY(i*30 + 40);
					this.getChildren().add(spaceInvaders[i][j]);
				}
			}
		}
	
		/**
		 * Creates a bullet for the ship to use. Only one bullet can be on the screen at a given point in time. A small rectangle
		 * is used to represent the bullet and is sent upwards from the x and y coordinates of the ship.
		 */
		private void bullet(){
			if (bullet == null){
				bullet = new Rectangle(2,6);
				bullet.setFill(Color.WHITE);
				
				this.getChildren().add(bullet);
				
				bullet.setX(ship.getX() + 18); //18 added so that it would appear that the bullet is coming from the center most point on the ship
				bullet.setY(ship.getY());
				
				Duration bulletD = new Duration(5);
				KeyFrame bulletF = new KeyFrame(bulletD, e -> {
						if (bullet != null){
							bullet.setY(bullet.getY() - 10);
							hits();
						}
				});
				bulletLife = new Timeline(bulletF);
				bulletLife.setCycleCount(Animation.INDEFINITE);
				bulletLife.play();
			}
		}

		/**
		 * Method used to determine if the bullet has hit an alien. Two for loops run through the spaceInvaders double array
		 * and determine if the bullet passed through the alien. This is used by getting the spaceInvaders[i][j] left corner's
		 * x coordinates and then the x coordinates of the right corner, and seeing if the bullet passed anywhere between those values.
		 * Y coordinates were also gathered similarly to make sure that the bullet was at the same heigt as the image.
		 */
		private void hits(){
			for (int i = 0; i < spaceInvaders.length; i++){
				for (int j = 0; j < spaceInvaders[0].length; j++){
					if (bullet != null && spaceInvaders[i][j] != null){
						if (bullet.getX() < spaceInvaders[i][j].getX() + 30
								&& bullet.getY() < spaceInvaders[i][j].getY() + 30
								&& bullet.getX() + bullet.getWidth() > spaceInvaders[i][j].getX() 
								&& bullet.getY() + bullet.getHeight() > spaceInvaders[i][j].getY()){
							if (spaceInvaders[i][j].getImage().equals(redImage)){
								Score += 30;
							}
							if (spaceInvaders[i][j].getImage().equals(skullImage)){
								Score += 20;
							}
							if (spaceInvaders[i][j].getImage().equals(normalImage)){
								Score += 10;	
							}
							spaceInvaders[i][j].setVisible(false); //makes image transparent
							spaceInvaders[i][j] = null;			  //removes image from array
							this.getChildren().remove(bullet);	  //removes bullet from screen
							this.getChildren().remove(spaceInvaders[i][j]); ///removes alien from screen
							bullet = null;
							bulletLife.stop();
							scoreText.setText("Score: " + Score);	//increases score
							levelText.setText("Level: " + level());	//updates level
						}//check Alien dimensions
					}//bullet null
				}//col loop
			}//row loop
			//if the bullet does not hit an alien, it is set to null and removed from the scene once it passes the top
			//of the screen
			if (bullet != null){
				if (bullet.getY() + bullet.getHeight() < UPPER_BOUND){
					this.getChildren().remove(bullet);
					bullet = null;
					bulletLife.stop();
				}
			}
		}
		
		/**
		 * This method is used to determine if an alien bullet hits the ship. If the alien bullet's coordinates are within the left and
		 * right most x coordinates of the ship at the same level or y coordinate of the ship, it will count as a hit and one of the ship's
		 * lives will be taken away. If the bullet does not hit the ship, it will become null once it reaches the bottom of the screen.
		 */
		private void hitsAlien(){
			if (bulletA != null && ship != null){
				if (bulletA.getX() < ship.getX() + 30 //checks if bullet is in same area as ship
						&& bulletA.getY() < ship.getY() + 30
						&& bulletA.getX() + bulletA.getWidth() > ship.getX() 
						&& bulletA.getY() + bulletA.getHeight() > ship.getY()){
					this.getChildren().remove(bulletA);
					bulletA = null;
					AlienbulletLife.stop();
					lives(true,false);
				}//check Alien dimensions
			}//bullet null
			if (bulletA != null){
				if (bulletA.getY() > LOWER_BOUND){
					this.getChildren().remove(bulletA);
					bulletA = null;
					AlienbulletLife.stop();
				}
			}
		}

		/**
		 * Used to determine the level that the player is on. Every 330 points the players level increases, meaning that they go through 3 levels
		 * per game, and each level up aligns with the aliens moving slightly faster across the screen.
		 * @return 			an int determining the level the player is on
		 */
		private int level(){
				return level = Score / 330 + 1;
		}
		
		/**
		 * Method used to determine the amount of remaining lives the player has and whether or not the game is over.
		 * If a bullet hits the players ship, the number of lives decreases by 1, and if the player makes it to the next
		 * round, the player gains a life, with a max number of lives being 4. If the player has no more lives, the end
		 * screen is executed.
		 * @param hit				boolean determining if the players ship was hit
		 * @param nextLevel			boolean determining if the player made it to another level
		 */
		public void lives(boolean hit, boolean nextLevel){
			
			for (int i = 0; i < livesRemaining.size(); i++){
				this.getChildren().remove(livesRemaining.get(i));
			}
			//if bullet hits ship
			if (hit){
				livesRemaining.remove(0);
			}
			//if round is over
			if (nextLevel){
				if(livesRemaining.size() < 4){
					livesRemaining.add(new ImageView(shipImage));
				}
			}
			//place ships next to "Lives: "
			for (int i = 0; i < livesRemaining.size(); i++){
				livesRemaining.get(i).setX(20 * i + 80);
				livesRemaining.get(i).setY(5);
				this.getChildren().add(livesRemaining.get(i));
			}
			
			if (livesRemaining.isEmpty()){
				if(numEndGame == 0)
					endGame();
			}
		}
		
		/**
		 * Method used to signal that the game is over. It creates a new stage and scene that displays the users score as well
		 * as a button directing them back to the home screen.
		 */
		private void endGame(){
			 numEndGame++;
			 t.stop(); 
			 VBox winVbox = new VBox(20);
	         Stage winStage = new Stage();
	         Scene winScene = new Scene(winVbox);
	         Text win = new Text("GAME OVER\n Score: "  + Score);
	         win.setFont(new Font(30));
	         
	         TextField enterName = new TextField("Name");
	         Button addName = new Button("Add name to High Scores");
	         	addName.setOnAction(e -> {
	         		if (enterName.getText() != "Name"){
	         			name = enterName.getText();
	         			cs1302.arcade.Player p = new cs1302.arcade.Player(name,Score);
	         			HighScoreList.addNewScore(p);
	         			stage.setScene(scene);
	    	        	winStage.close();
	         		}
	         	});
	         	
	         
	         winVbox.setPadding(new Insets(10, 50, 50, 50));
		     winVbox.setSpacing(10);
		     winVbox.setAlignment(Pos.CENTER);
	         winVbox.getChildren().addAll(win, enterName, addName);
	          
	         winStage.setTitle("Game Over!");
	         winStage.setScene(winScene);
	         winStage.sizeToScene();
	         winStage.initModality(Modality.APPLICATION_MODAL);
	         winStage.show();
		}
} // SpaceInvaders