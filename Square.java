/*
 * @author Dimitrios Schoinas
 * 
 */
public class Square {

	//Instance variables
	private String squareType;
	private int sentence;

	public Square(int sentenceVal, String type) {
		squareType = type;
		sentence = sentenceVal;
	}

	public void setSentence(int s) {
		sentence = s;
	}

	public String getType() {
		return squareType;
	}

	public int getSentence() {
		return sentence;
	}
}
