/*
 * @author Dimitrios Schoinas
 * 
 */
public class Game {

	//Constants
	private static final String NORMAL = "normal";
	private static final String CRAB = "crab";
	private static final String HELL = "hell";
	private static final String DEATH = "death";
	private static final String SENTENCE = "sentence";
	private static final String BIRD = "bird";
	private static final String WIN = "win";
	private static final int FIRST_SQUARE = 1;
	private static final int BIRD_SKIP = 9;
	private static final int NOT_FOUND = -1;

	//Instance variables
	private Square[] board;
	private Player[] players;
	private Player winner;
	private int lastSquare;
	private int currentPlayers;
	private int turn;
	private int moves;
	private boolean deathSquaresActive;
	private boolean isFirstRoll;

	//Constructors
	public Game(int p) {
		currentPlayers = p;
		players = new Player[p];
		turn = 0;
		deathSquaresActive = true;
		isFirstRoll = true;
	}
	
	public Iterator rankingIteratorPlaying() {
		Player[] aux = new Player[players.length];
		for (int i = 0; i < players.length; i++)
			aux[i] = players[i];
		sortByRanking(aux, players.length);
		return new Iterator(aux, currentPlayers, 0);
	}

	public Iterator rankingIteratorEliminated() {
		Player[] aux = new Player[players.length];
		for (int i = 0; i < players.length; i++)
			aux[i] = players[i];
		sortByRanking(aux, players.length);
		return new Iterator(aux, players.length, currentPlayers);
	}

	public Iterator alivePlayersIterator() {
		Player[] aux = new Player[currentPlayers];
		for (int i = 0; i < currentPlayers; i++)
			aux[i] = rankingIteratorPlaying().getPlayer(i);
		sortBySquare(aux, currentPlayers);
		return new Iterator(aux, currentPlayers, 0);
	}

	private void sortByRanking(Player[] aux, int size) {
		for (int i = 0; i < size - 1; i++) {
			int minIdx = i;
			for (int j = i + 1; j < size; j++) {
				minIdx = i;
				if (aux[j].compareTo(aux[minIdx]) > 0)
					minIdx = j;
				Player tmp = aux[i];
				aux[i] = aux[minIdx];
				aux[minIdx] = tmp;
			}
		}
	}

	private void sortBySquare(Player[] aux, int size) {
		for (int i = 0; i < size - 1; i++) {
			int minIdx = i;
			for (int j = i + 1; j < size; j++) {
				minIdx = i;
				if (aux[j].compareToSquare(aux[minIdx]) > 0)
					minIdx = j;
				Player tmp = aux[i];
				aux[i] = aux[minIdx];
				aux[minIdx] = tmp;
			}
		}
	}

	public void listRanking(Game board) {
		rankingIteratorPlaying().rankingPlaying(board);
		rankingIteratorEliminated().rankingEliminated(board);
	}

	private Player createPlayer(char colour, int number) {
		return new Player(colour, number);
	}

	public void addPlayers(String colours) {
		char colour;
		for (int number = 1; number - 1 < colours.length(); number++) {
			colour = colours.charAt(number - 1);
			players[number - 1] = createPlayer(colour, number);
		}
	}

	public void setLastSquare(int finish) {
		lastSquare = finish;
		board = new Square[finish];
	}

	public void createBoard(int finishLine) {
		setLastSquare(finishLine);
		addNormalSquares();
		addBirdSquares();
		addWinSquare();
	}

	private void addNormalSquares() {
		for (int i = 0; i < lastSquare; i++)
			board[i] = new Square(0, "normal");
	}

	private void addBirdSquares() {
		int birdSquares = ((lastSquare - (lastSquare % BIRD_SKIP)) / BIRD_SKIP);
		for (int i = 1; i <= birdSquares; i++)
			board[i * BIRD_SKIP - 1] = new Square(0, "bird");
	}

	public void addSpecialSquare(int i, int sentence, String type) {
		board[i - 1] = new Square(sentence, type);
	}

	private void addWinSquare() {
		board[lastSquare - 1] = new Square(0, "win");
	}

	public Player playerTurn() {
		if (turn == players.length) {
			turn = 0;
			isFirstRoll = false;
		}
		while (players[turn].isEliminated() || players[turn].getSentence() > 0) {
			if (players[turn].getSentence() > 0)
				players[turn].reduceSentence();
			nextTurn();
			playerTurn();
		}
		return players[turn];
	}

	private void nextTurn() {
		turn++;
	}

	public Player findPlayer(char colour) {
		return players[getPlayerIndex(colour)];
	}

	public int getPlayerIndex(char colour) {
		int i = 0;
		while (i < players.length && players[i].getPColour() != colour)
			i++;
		if (i < players.length)
			return i;
		else
			return NOT_FOUND;
	}

	public boolean playerExists(char colour) {
		int i = 0;
		boolean found = false;
		while (i < players.length && !found) {
			if (players[i++].getPColour() == colour)
				found = true;
		}
		return found;
	}

	public int rollDice(int first, int second) {
		moves = first + second;
		return moves;
	}

	public boolean isFirstRollSpecial(int first, int second) {
		return first == 6 && second == 3 || first == 3 && second == 6;
	}

	public void move(int first, int second) {
		Player p = playerTurn();
		if (isFirstRoll && isFirstRollSpecial(first, second))
			p.goToSquare(getLastSquare());
		else
			p.movePlayer(rollDice(first, second));
		if (p.getPSquare() > getLastSquare())
			p.goToSquare(getLastSquare());
		nextTurn();
		executeSquareFunction(p);
		playerTurn();
	}

	private String getLandedSquare(Player p) {
		return board[p.getPSquare() - 1].getType();
	}

	private void nextGame() {
		if (isDeathSquareActive()) {
			alivePlayersIterator().getLast().eliminatePlayer(currentPlayers);
			currentPlayers--;
		}
		if (!isCupOver())
			resetGame();
	}

	private void resetGame() {
		for (int i = 0; i < players.length; i++) {
			players[i].goToSquare(FIRST_SQUARE);
			players[i].resetSentence();
		}
		turn = 0;
		deathSquaresActive = true;
		isFirstRoll = true;
		winner = null;
	}

	private void executeSquareFunction(Player p) {

		switch (getLandedSquare(p)) {
		case NORMAL:
			break;
		case CRAB:
			executeCrabFunction(p);
			break;
		case HELL:
			executeHellFunction(p);
			break;
		case DEATH:
			executeDeathFunction(p);
			break;
		case SENTENCE:
			executeSentenceFunction(p);
			break;
		case BIRD:
			executeBirdFunction(p);
			break;
		case WIN:
			executeWinFunction(p);
		}

	}

	private void executeCrabFunction(Player p) {
		p.movePlayer(-2 * moves);
		if (p.getPSquare() < 1)
			p.goToSquare(FIRST_SQUARE);
	}

	private void executeHellFunction(Player p) {
		p.goToSquare(FIRST_SQUARE);
	}

	private void executeDeathFunction(Player p) {
		if (isDeathSquareActive()) {
			p.eliminatePlayer(currentPlayers);
			deactivateDeathSquares();
			currentPlayers--;
		}
		if(isCupOver())
			executeWinFunction(alivePlayersIterator().getFirst());
	}

	private void executeSentenceFunction(Player p) {
		p.addSentence(board[p.getPSquare() - 1].getSentence());
	}

	private void executeBirdFunction(Player p) {
		p.movePlayer(BIRD_SKIP);
		if (p.getPSquare() >= getLastSquare()) {
			p.goToSquare(getLastSquare());
			executeWinFunction(p);
		}
	}

	private void executeWinFunction(Player p) {
		winner = p;
		p.wonGame();
		nextGame();
	}

	private void deactivateDeathSquares() {
		deathSquaresActive = false;
	}
	public Player getWinner() {
		return winner;
	}

	public int getLastSquare() {
		return lastSquare;
	}

	public int getAlivePlayers() {
		return currentPlayers;
	}

	public boolean isDeathSquareActive() {
		return deathSquaresActive;
	}
	public boolean isCupOver() {
		return currentPlayers == 1;
	}
}