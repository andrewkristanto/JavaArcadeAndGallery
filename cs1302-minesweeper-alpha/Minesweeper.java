
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

/**
 * This class represents a Minesweeper game.
 *
 * @author Andrew Kristanto <ank27711@uga.edu>
 */
public class Minesweeper {

	int rows;
	int cols;
	int[][] mineGrid;
	String[][] markGrid;
	int mines;
	int rounds;
	boolean wantsToPlay=true;
	Scanner scan = new Scanner(System.in);
	String response;
	String[] fixedResponse;
	boolean nofog = false;
	
    /**
     * Constructs an object instance of the {@link Minesweeper} class using the
     * information provided in <code>seedFile</code>. Documentation about the 
     * format of seed files can be found in the project's <code>README.md</code>
     * file.
     *
     *
     * @param seedFile the seed file used to construct the game
     * @see            <a href="https://github.com/mepcotterell-cs1302/cs1302-minesweeper-alpha/blob/master/README.md#seed-files">README.md#seed-files</a>
     */
    public Minesweeper(File seedFile) {

    	try {
	    File f = new File(seedFile.getName());
	    Scanner scan = new Scanner(f);
    	    rows = scan.nextInt();
    	    cols = scan.nextInt();
	    mineGrid = new int[rows][cols];
	    markGrid = new String[rows][cols];
	    mines= scan.nextInt();
	    int count = mines;
	    while(count>0) {
       		try {
		    int r = scan.nextInt();
		    int c = scan.nextInt();
		    mineGrid[r][c] = 9;
		    count--;
    		}
    		catch(NumberFormatException e) {
		    System.out.println("Cannot create game with " + seedFile.getName() + ", because it is not formatted correctly.");
		    System.exit(0);
    		}
    		catch(ArrayIndexOutOfBoundsException e) {
		    System.out.println("Cannot create game with " + seedFile.getName() + ", because it is not formatted correctly.");
		    System.exit(0);
    		}
	    }
    	} catch (FileNotFoundException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (NumberFormatException e) {
    		System.out.println("Cannot create game with " + seedFile.getName() + ", because it is not formatted correctly.");
    		System.exit(0);
    	}

	// TODO implement

    } // Minesweeper


    /**
     * Constructs an object instance of the {@link Minesweeper} class using the
     * <code>rows</code> and <code>cols</code> values as the game grid's number
     * of rows and columns respectively. Additionally, One quarter (rounded up) 
     * of the squares in the grid will be assigned mines, randomly.
     *
     * @param rows the number of rows in the game grid
     * @param cols the number of cols in the game grid
     */
    public Minesweeper(int rows, int cols) {

    	if(rows>10 || cols>10 || rows<1 || cols<1){  // checks if size is within required range
    		System.out.println("\nಠ_ಠ says, \"Cannot create a mine field with that many rows and/or columns!\"");
    		System.exit(0);
    	}
		this.rows=rows;
		this.cols=cols;
		mineGrid= new int[rows][cols];
		markGrid= new String[rows][cols];
		mines = (int) Math.ceil(rows*cols*0.25);
		
		Random random = new Random();
		for(int count=0; count<mines; count++){ // randomly assigns mines
			int r = random.nextInt(rows);
			int c = random.nextInt(cols);
			if(mineGrid[r][c]!=9){
				mineGrid[r][c]=9;
			}else{
				count--;
			}
		}
    } // Minesweeper

    /**
     * Starts the game and execute the game loop.
     */
    public void run() {

    	setBoard();
	    while(wantsToPlay){
	    	printBoard();
	    	actionCommand();
	    	if(checkWin()){
	    		printDoge();
	    		System.exit(0);
	    	}
		    rounds++;
	    }
	    // TODO implement

    } // run

    /**
     * creates the mine field along with numbers
     */
    public void setBoard(){
	
    	for(int r=0; r<rows; r++){ // sets the board
	    	for(int c=0; c<cols; c++){
	    		markGrid[r][c]=" ";
	    	}
	    }		
    	
		for(int r=0; r<rows; r++){ // assigns numbers while checking if within bounds
			for(int c=0; c<cols; c++){
				int count=0;
				if(mineGrid[r][c]!=9){
					if((r-1)>=0 && (c-1)>=0 && mineGrid[r-1][c-1]==9){
						count++;
					}
					if((r-1)>=0 && mineGrid[r-1][c]==9){
						count++;
					}
					if((r-1)>=0 && (c+1)<cols && mineGrid[r-1][c+1]==9){
						count++;
					}
					if((c-1)>=0 && mineGrid[r][c-1]==9){
						count++;
					}
					if((c+1)<cols && mineGrid[r][c+1]==9){
						count++;
					}
					if((r+1)<rows && (c-1)>=0 && mineGrid[r+1][c-1]==9){
						count++;
					}
					if((r+1)<rows && mineGrid[r+1][c]==9){
						count++;
					}
					if((r+1)<rows && (c+1)<cols && mineGrid[r+1][c+1]==9){
						count++;
					}
					mineGrid[r][c]=count;
				}
			}
		}
    } // setBoard
    
    /**
     * prints out the board 
     */
    public void printBoard(){
    	System.out.println("\nRounds Completed: " + rounds);
	    for(int r=0; r<=rows; r++){
	    	if(r<rows){
	    		System.out.print("\n" + r + " |");
	    	}else{
	    		System.out.print("\n   ");
	    	}
	    	for(int c=0; c<cols; c++){
	    		if(r<rows){
	    			if(nofog && mineGrid[r][c]==9){  // changes board if nofog command was called
	    				System.out.print("<" + markGrid[r][c] + ">|");
	    			}
	    			else{
	    				System.out.print(" " + markGrid[r][c] +" |");
	    			}
	    		}else{
	    			System.out.print(" " + c + "  ");
	    		}
	    	}
	    }
	    
    } // printBoard
    
    /**
     * retrieves user response and takes the action
     */
    public void actionCommand(){
    	System.out.print("\n\nminesweeper-alpha$ ");
	    response = scan.nextLine().trim().replaceAll(" +", " ");
	    fixedResponse = response.split(" "); // splits the user response to an array
	    String command = fixedResponse[0];
	    
	    if(command.equals("help") || command.equals("h")){  // help action
	    	System.out.println("\nCommands Available...");
	    	System.out.println(" - Reavel: r/reveal row col");
	    	System.out.println(" -   Mark: m/mark   row col");
	    	System.out.println(" -  Guess: g/guess  row col");
	    	System.out.println(" -   Help: h/help");
	    	System.out.println(" -   Quit: q/quit");
	    }
	    else if(command.equals("quit") || command.equals("q")){  // quit action
	    	System.out.println("\nლ(ಠ_ಠლ)");
	    	System.out.println("Y U NO PLAY MORE?");
	    	System.out.println("Bye!\n");
	    	System.exit(0);
	    }
	    else if(command.equals("nofog")){  // nofog action
	    	nofog = true;
	    }
	    else if(fixedResponse.length==3 && (command.equals("reveal") || command.equals("r"))){ // reveal action
	    	try{
		    	int inputRow = Integer.parseInt(fixedResponse[1]);
			    int inputCol = Integer.parseInt(fixedResponse[2]);
				try{
				    if(mineGrid[inputRow][inputCol]==9){
				    	System.out.println("\nOh no... You revealed a mine!\n");
				    	System.out.println("  __ _  __ _ _ __ ___   ___    _____   _____ _ __ ");
				    	System.out.println(" / _` |/ _` | \'_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ \'__|");
				    	System.out.println("| (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |   ");
				    	System.out.println(" \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|   ");
				    	System.out.println(" |___/ \n");
				    	System.exit(0);
				    }else{
				    	markGrid[inputRow][inputCol]=Integer.toString(mineGrid[inputRow][inputCol]);
				    }
				}
				catch(ArrayIndexOutOfBoundsException e){
					System.out.println("\nಠ_ಠ says, \"Command not recognized!\"");
				}
	    	}
	    	catch(NumberFormatException nfe){
	    		System.out.println("\nಠ_ಠ says, \"Command not recognized!\"");
	    	}
	    }
	    else if(fixedResponse.length==3 && (command.equals("mark") || command.equals("m"))){  // mark action
	    	try{
		    	int inputRow = Integer.parseInt(fixedResponse[1]);
			    int inputCol = Integer.parseInt(fixedResponse[2]);
			    try{
			    	markGrid[inputRow][inputCol]="F";
			    }
		    	catch(ArrayIndexOutOfBoundsException e){
		    		System.out.println("\nಠ_ಠ says, \"Command not recognized!\"");
		    	}
	    	}
	    	catch(NumberFormatException nfe){
	    		System.out.println("\nಠ_ಠ says, \"Command not recognized!\"");
	    	}
	    }
	    else if(fixedResponse.length==3 && (command.equals("guess") || command.equals("g"))){  // guess action
	    	try{
		    	int inputRow = Integer.parseInt(fixedResponse[1]);
			    int inputCol = Integer.parseInt(fixedResponse[2]);
			    try{
			    	markGrid[inputRow][inputCol]="?";
			    }
		    	catch(ArrayIndexOutOfBoundsException e){
		    		System.out.println("\nಠ_ಠ says, \"Command not recognized!\"");
		    	}
	    	}
	    	catch(NumberFormatException nfe){
	    		System.out.println("\nಠ_ಠ says, \"Command not recognized!\"");
	    	}
	    }
	    else{
	    	System.out.println("\nಠ_ಠ says, \"Command not recognized!\"");
	    }
    } // actionCommand

    /**
     * checks if all mines are marked and number of marks equals number of mines
     * @return true if player wins or false if player has not won yet
     */
    public boolean checkWin(){
    	int count = 0;
    	for(int r = 0; r < rows; r++){
    		for(int c = 0; c < cols; c++){
    			if(markGrid[r][c].equals("F") && mineGrid[r][c]==9){
    				count++;
    			}
    		}
    	}
    	return count==mines;
    } // checkWin
    
    /**
     * prints out Doge with score
     */
    public void printDoge(){
    	int score = (rows*cols) - mines - rounds;
    	
    	System.out.println("\n ░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░\"So Doge\"");
    	System.out.println(" ░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░");
    	System.out.println(" ░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░\"Such Score\"");
    	System.out.println(" ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░");
    	System.out.println(" ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░ \"Much Minesweeping\"");
    	System.out.println(" ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░");
    	System.out.println(" ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░ \"Wow\"");
    	System.out.println(" ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░");
    	System.out.println(" ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌");
    	System.out.println(" ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░");
    	System.out.println(" ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░");
    	System.out.println(" ▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒▌");
    	System.out.println(" ▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░");
    	System.out.println(" ░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░");
    	System.out.println(" ░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░");
    	System.out.println(" ░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░");
    	System.out.println(" ░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░ CONGRATULATIONS!");
    	System.out.println(" ░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░ YOU HAVE WON!");
    	System.out.println(" ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░ SCORE: " + score + "\n");
    } // printDoge
    
    /**
     * The entry point into the program. This main method does implement some
     * logic for handling command line arguments. If two integers are provided
     * as arguments, then a Minesweeper game is created and started with a 
     * grid size corresponding to the integers provided and with 10% (rounded
     * up) of the squares containing mines, placed randomly. If a single word 
     * string is provided as an argument then it is treated as a seed file and 
     * a Minesweeper game is created and started using the information contained
     * in the seed file. If none of the above applies, then a usage statement
     * is displayed and the program exits gracefully. 
     *
     * @param args the shell arguments provided to the program
     */
    public static void main(String[] args) {

	/*
	  The following switch statement has been designed in such a way that if
	  errors occur within the first two cases, the default case still gets
	  executed. This was accomplished by special placement of the break
	  statements.
	*/
    	
    System.out.println("        _");
    System.out.println("  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __"); 
    System.out.println(" /    \\| | \'_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ \'_ \\ / _ \\ \'__|");
    System.out.println("/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |");
    System.out.println("\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|");
    System.out.println("                             ALPHA EDITION |_| v2017.f");
    
	Minesweeper game = null;

	switch (args.length) {

        // random game
	case 2: 

	    int rows, cols;

	    // try to parse the arguments and create a game
	    try {
		rows = Integer.parseInt(args[0]);
		cols = Integer.parseInt(args[1]);
		game = new Minesweeper(rows, cols);
		break;
	    } catch (NumberFormatException nfe) { 
		// line intentionally left blank
	    } // try

	// seed file game
	case 1: 

	    String filename = args[0];
	    File file = new File(filename);

	    if (file.isFile()) {
		game = new Minesweeper(file);
		break;
	    } // if
    
        // display usage statement
	default:

	    System.out.println("Usage: java Minesweeper [FILE]");
	    System.out.println("Usage: java Minesweeper [ROWS] [COLS]");
	    System.exit(0);

	} // switch

	// if all is good, then run the game
	game.run();

    } // main


} // Minesweeper
