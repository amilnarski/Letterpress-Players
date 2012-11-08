import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Letterpress {
	static char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z' };
	static HashMap<Integer, Character> ltrs;
	static HashSet<String> dict;
	private static char[][] board;

	public enum Status {
		NEUTRAL, RED, BLUE, RED_DEFENDED, BLUE_DEFENDED
	};

	private static Status[][] status;

	/**
	* Letterpress constructor. Intializes the board and the dictionary.
	*/
	public Letterpress() {
		Letterpress.ltrs = new HashMap <Integer, Character> (26);
		for (int i=0; i<letters.length;i++)
			ltrs.put(i,letters[i]);
		initializeBoard();
		initializeDictionary();
		supplementDictionary();
		refineDictionary();
	}

	/**
	 * Initializes the game board with a random set of letters and sets each
	 * letter to be NEUTRAL.
	 */
	private void initializeBoard() {
		Letterpress.board = new char[5][5];
		Random gen = new Random(0);
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board.length; col++) {
				Letterpress.board[row][col] = (Character)ltrs.get(gen.nextInt(26));
			}
		}

		Letterpress.status = new Status[5][5];
		for (int i = 0; i < status.length; i++)
			Arrays.fill(status[i], Status.NEUTRAL); // need to loop to do this
													// since Arrays errors out
													// on a matrix
	}

	/**
	 * Reads in the words from pDictionary.txt and adds them to the in-memory
	 * dictionary.
	 */
	private void initializeDictionary() {
		Letterpress.dict = new HashSet<String>();
		File dictionary = new File("pDictionary.txt");
		Scanner reader;
		try {
			reader = new Scanner(dictionary);
		} catch (FileNotFoundException e) {
			p("LOG: Could not find the dictionary file. Proceeding with empty dictionary.");
			reader = new Scanner("");
		}
		while (reader.hasNextLine()) {
			String word = reader.nextLine();
			dict.add(word);
		}
	}
	
	private void supplementDictionary(){
		int dSize = dict.size();
		File dictionary = new File("SortedDictionary.txt");
		Scanner reader;
		try {
			reader = new Scanner(dictionary);
		} catch (FileNotFoundException e) {
			p("LOG: Could not find the supplemental dictionary file. Proceeding with empty dictionary.");
			reader = new Scanner("");
		}
		while (reader.hasNextLine()) {
			String word = reader.nextLine();
			word = word.toUpperCase();
			if (!dict.contains(word))
				dict.add(word);
		}
		
		p("LOG: Supplemented the dictionary with "+(dict.size()-dSize)+" words. Now have "+dict.size()+" words.");
	}

	private void refineDictionary() {
		int dSize = dict.size();
		if (board != null && dict !=null && dict.size() > 0){
			//build counted list of char in the game board and a list of their counts
			int[] ltrCount = new int[26];
			for(int row=0; row<board.length; row++){
				for (int col=0; col<board.length; col++){
					char ltr = board[row][col];
					int i = 0;
					while (ltr != letters[i]){
						i++;
					}
					ltrCount[i]+=1;
				}
			}
			p(Arrays.toString(ltrCount));
			//loop through the dictionary and remove words that cannot be played
			Iterator <String> iter = dict.iterator();
			while (iter.hasNext()){
				String word = iter.next();
				int[] wordLtrCount = new int [26];
				
				for(int i = 0; i < word.length();i++){
					char ltr = word.charAt(i);
					int j = 0;
					while (ltr != letters[j]){
						j++;
					}
					wordLtrCount[j]+=1;
				}
				
				for(int i=0;i<26;i++){
					if ((ltrCount[i]-wordLtrCount[i]) < 0){
						iter.remove();
						break;
					}
				}	
			} //end while iter.hasNext
			
			p("LOG: Refine removed "+(dSize-dict.size())+" words from the dictionary. "+dict.size()+" words remain.");
		} else {
			p("LOG: Unable to refine dictionary.");
		}
	}
	/**
	 * Prints out the current board.
	 */
	public static void pBoard() {
		for (int row = 0; row < 5; row++) {
			String rowStr = "";
			for (int col = 0; col < 5; col++) {
				rowStr += board[row][col] + " ";
			}
			rowStr += "\t" + " ";
			for (int col = 0; col < 5; col++) {
				switch (status[row][col]) {
				case NEUTRAL:
					rowStr += "_" + " ";
					break;
				case RED:
					rowStr += "r" + " ";
					break;
				case BLUE:
					rowStr += "b" + " ";
					break;
				case RED_DEFENDED:
					rowStr += "R" + " ";
					break;
				case BLUE_DEFENDED:
					rowStr += "B" + " ";
					break;
				default:
					rowStr += " " + " ";
				}
			}
			p(rowStr);
		}
		p("");
	}

	/**
	 * Prints out the current game board with indices to aid in debugging.
	 */
	public static void dBoard() {
		System.out.println("  0 1 2 3 4\t  0 1 2 3 4");
		for (int row = 0; row < 5; row++) {
			String rowStr = row + " ";
			for (int col = 0; col < 5; col++) {
				rowStr += board[row][col] + " ";
			}
			rowStr += "\t" + row + " ";
			for (int col = 0; col < 5; col++) {
				switch (status[row][col]) {
				case NEUTRAL:
					rowStr += "_" + " ";
					break;
				case RED:
					rowStr += "r" + " ";
					break;
				case BLUE:
					rowStr += "b" + " ";
					break;
				case RED_DEFENDED:
					rowStr += "R" + " ";
					break;
				case BLUE_DEFENDED:
					rowStr += "B" + " ";
					break;
				default:
					rowStr += " " + " ";
				}
			}
			p(rowStr);
		}
		p("");
	}

	/**
	 * @param args
	 * Generates a new Letterpress game.
	 */
	public static void main(String[] args) {
		Letterpress lp = new Letterpress();
		status[0][0] = Status.BLUE_DEFENDED;
		status[0][1] = Status.BLUE;
		status[1][0] = Status.BLUE;
		status[4][4] = Status.RED_DEFENDED;
		status[3][4] = Status.RED;
		status[4][3] = Status.RED;
		dBoard();
		p(dict.toString());
	}

	/**
	 * @param s
	 * Makes printing that much easier.
	 */
	public static void p(Object s) {
		System.out.println(s);
	}
	
	/**
	 * Makes printing that much easier.
	 */
	public static void p() {
		System.out.println();
	}

}
