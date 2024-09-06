import java.util.Scanner;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class Main{

	// Constants
	private static final String PLAYER = "player";
	private static final String SQUARE = "square";
	private static final String STATUS = "status";
	private static final String RANKING = "ranking";
	private static final String DICE = "dice";
	private static final String EXIT = "exit";
	private static final String FILE = "boards.txt";

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(System.in);
		Scanner read = new Scanner(new FileReader(FILE));
		String colours = in.next();
		int players = colours.length();
		int bNum = in.nextInt();
		Game board = new Game(players);
		setBoard(read, board, bNum);
		board.addPlayers(colours);
		read.close();
		String command = null;
		do {
			command = in.next();
			executeCommand(in, command, board);
		} while (!command.equals(EXIT));
		in.close();
	}

	private static void setBoard(Scanner read, Game board, int bNum) throws FileNotFoundException {
		for (int i = 1; i <= bNum; i++) {
			board.createBoard(read.nextInt());
			createSentenceSquares(board, read);
			createCliffSquares(board, read);
		}
	}

	private static void createSentenceSquares(Game board, Scanner read) throws FileNotFoundException {
		int sentenceSquares = read.nextInt();
		for (int i = 0; i < sentenceSquares; i++)
			board.addSpecialSquare(read.nextInt(), read.nextInt(), "sentence");
	}

	private static void createCliffSquares(Game board, Scanner read) throws FileNotFoundException {
		int cliffSquares = read.nextInt();
		for (int i = 0; i < cliffSquares; i++)
			board.addSpecialSquare(read.nextInt(), 0, read.next());
	}

	private static void executeCommand(Scanner in, String command, Game board) {

		switch (command) {
		case PLAYER:
			whoIsNext(in, board);
			break;
		case SQUARE:
			getSquare(in, board);
			break;
		case STATUS:
			getStatus(in, board);
			break;
		case RANKING:
			getRanking(board);
			break;
		case DICE:
			rollDice(in, board);
			break;
		case EXIT:
			exit(board);
			break;
		default:
			System.out.println("Invalid command");
			in.nextLine();
			break;
		}
	}

	private static void whoIsNext(Scanner in, Game board) {
		if (board.isCupOver())
			System.out.println("The cup is over");
		else
			System.out.println("Next to play: " + board.playerTurn().getPColour());
	}

	private static void getSquare(Scanner in, Game board) {
		String colour = in.nextLine();
		colour = colour.trim();
		char pColour = colour.charAt(0);
		if (colour.length() != 1 || !board.playerExists(pColour))
			System.out.println("Nonexistent player");
		else if (board.findPlayer(colour.charAt(0)).isEliminated())
			System.out.println("Eliminated player");
		else
			System.out.println(colour + " is on square " + board.findPlayer((pColour)).getPSquare());
	}

	private static void getStatus(Scanner in, Game board) {
		String colour = in.nextLine();
		colour = colour.trim();
		char pColour = colour.charAt(0);
		if (colour.length() != 1 || !board.playerExists(pColour))
			System.out.println("Nonexistent player");
		else if (board.isCupOver())
			System.out.println("The cup is over");
		else if (board.findPlayer((pColour)).isEliminated())
			System.out.println("Eliminated player");
		else if (board.findPlayer(pColour).getSentence() > 0)
			System.out.println(colour + " cannot roll the dice");
		else
			System.out.println(colour + " can roll the dice");
	}

	private static void getRanking(Game board) {
		board.listRanking(board);
	}

	private static void rollDice(Scanner in, Game board) {
		int first = in.nextInt();
		int second = in.nextInt();
		if (first < 1 || first > 6 || second < 1 || second > 6)
			System.out.println("Invalid dice");
		else if (board.isCupOver())
			System.out.println("The cup is over");
		else
			board.move(first, second);
	}

	private static void exit(Game board) {
		if (board.isCupOver())
			System.out.println(board.getWinner().getPColour() + " won the cup!");
		else
			System.out.println("The cup was not over yet...");
	}
}