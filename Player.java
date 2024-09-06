/*
 * @author Dimitrios Schoinas
 * 
 */
public class Player {

	//Instance variables
	private boolean isEliminated;
	private char playerColour;
	private int playerNumber;
	private int gamesWon;
	private int square;
	private int sentence;
	private int eliminationNumber;

	public Player(char colour, int number) {
		isEliminated = false;
		playerColour = colour;
		playerNumber = number;
		gamesWon = 0;
		square = 1;
		sentence = 0;
		eliminationNumber = 0;
	}

	public void eliminatePlayer(int num) {
		eliminationNumber = num;
		isEliminated = true;
	}

	public void movePlayer(int moves) {
		square += moves;
	}

	public void goToSquare(int s) {
		square = s;
	}

	public void wonGame() {
		gamesWon++;
	}

	public void addSentence(int s) {
		sentence += s;
	}

	public void resetSentence() {
		sentence = 0;
	}

	public void reduceSentence() {
		sentence--;
	}

	public boolean isEliminated() {
		return isEliminated;
	}

	public char getPColour() {
		return playerColour;
	}

	public int getPNumber() {
		return playerNumber;
	}

	public int getElimNum() {
		return eliminationNumber;
	}

	public int getGamesWon() {
		return gamesWon;
	}

	public int getPSquare() {
		return square;
	}

	public int getSentence() {
		return sentence;
	}
	public int compareTo(Player other) {
		int i;
		i = other.getElimNum() - this.getElimNum();
		if (i == 0)
			i = this.getGamesWon() - other.getGamesWon();
		if (i == 0)
			i = this.getPSquare() - other.getPSquare();
		if(i == 0)
			i = other.getPNumber() - this.getPNumber();
		return i;
	}
	public int compareToSquare(Player other) {
		int i;
		i = other.getElimNum() - this.getElimNum();
		if (i == 0)
			i = this.getPSquare() - other.getPSquare();
		if(i == 0)
			i = other.getPNumber() - this.getPNumber();
		return i;
	}
}
