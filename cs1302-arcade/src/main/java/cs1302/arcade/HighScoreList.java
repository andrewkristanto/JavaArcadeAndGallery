package cs1302.arcade;


public class HighScoreList {

	private static Player[] highScores = new Player[20];
	
	public static void setInitialPlayer(){
		Player p = new Player("Guest", 0);
	}
	
	public static void addNewScore(Player p){
		int index = 0;
		while(index < size() && highScores[index] != null && p.getScore() < highScores[index].getScore()){
			index++;
		}//loops thru list until the score passed in is greater than score at index
		for (int i = size(); i > index; i--){
			highScores[i] = highScores[i-1];
		}//shifts players to the right
		highScores[index] = p; //adds player at index
	} // addNewScore
	
	public static String getHighScoreList(){
		
		String scoreList = "";
		if(size() == 0){
			return scoreList;
		}
		else{
			for(int i = 0; i < size(); i++){
				scoreList += highScores[i].getName() + ": " + highScores[i].getScore() + "\n";
				if(i == 4){
					return scoreList;
				}
			}
		} // if
		return scoreList;
	} // getHighScoreList

	private static int size(){
		int count = 0;
		for (int i = 0; i < highScores.length; i++){
			if (highScores[i] != null){
				count++;
			}
		}
		return count;
	}
}
