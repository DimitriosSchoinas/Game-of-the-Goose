/*
 * @author Dimitrios Schoinas
 * 
 */
public class Iterator {

	//Instance variables
	private Player[] players;
	private int size;
	private int nextIndex;

	public Iterator(Player[] players, int playersAmount, int start) {
		size = playersAmount;
		this.players = players;
		nextIndex = start;
	}
	
	private boolean hasNext() {
		return size > nextIndex;
	}

	private Player next(Player[] players) {
		return players[nextIndex++];
	}

	public void rankingPlaying(Game board) {
		Iterator it = board.rankingIteratorPlaying();
		while (it.hasNext()) {
			Player p = it.next(players);
			System.out.println(p.getPColour() + ": " + p.getGamesWon() + " games won; on square " + p.getPSquare() + ".");
		}
	}

	public void rankingEliminated(Game board) {
		Iterator it = board.rankingIteratorEliminated();
		while (it.hasNext()) {
			Player p = it.next(players);
			System.out.println(p.getPColour() + ": " + p.getGamesWon() + " games won; eliminated.");
		}
	}

	public Player getPlayer(int i) {
		return players[i];
	}

	public Player getFirst() {
		return players[0];
	}

	public Player getLast() {
		return players[size - 1];
	}
}
