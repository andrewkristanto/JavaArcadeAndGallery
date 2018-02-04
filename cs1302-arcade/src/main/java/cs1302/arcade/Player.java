package cs1302.arcade;

public class Player {

	private String name = "";
	private int score = 0;
	
	/**
	 * Creates a player with a name and score
	 * @param name Player's name
	 * @param score Player's score
	 */
	public Player(String name, int score) {
		this.name = name;
		this.score = score;
	}

	/**
	 * Returns the player's name
	 * @return Player's name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the player's score
	 * @return Player's score
	 */
	public int getScore(){
		return score;
	}
}
