import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Letterpress implements Game {

	// STATIC VARIABLES
	private static char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z' };
	private static HashMap<Integer, Character> ltrs;
	private static HashSet<String> dict;
	private static GameManager gm;
	// private static HashSet<String> playedWords;
	private static char[][] board;

	// CLASS VARIABLES
	private Player red;
	private Player blue;

	// private boolean redReady;
	// private boolean blueReady;

	protected static enum Status {
		NEUTRAL, RED, BLUE, RED_DEFENDED, BLUE_DEFENDED
	};

	protected static enum Color {
		RED, BLUE
	};

	private Player cPlayer;
	private LMove bLastMove;
	private LMove rLastMove;
	private static Status[][] status;

	// GAMEFLOW VALUES
	@SuppressWarnings("unused")
	private boolean waitForMove;
	private LMove receivedMv;

	// METHODS
	public void assignPlayer(Player player) {
		if (this.red == null) {
			this.red = player;
			this.red.giveColor(Color.RED);
		} else if (this.blue == null) {
			this.blue = player;
			this.blue.giveColor(Color.BLUE);

			// choose the first player
			Random gen = new Random(0);
			if (gen.nextInt() % 2 == 0)
				cPlayer = red;
			else
				cPlayer = blue;
		} else {
			p("LOG: Player attempted to join the game. Two players are already assigned.");
		}
	}

	private void checkDefended() {
		for (int row = 0; row < status.length; row++) {
			for (int col = 0; col < status.length; col++) {
				if (status[row][col] != Status.NEUTRAL) {
					// build adjacent positions
					HashSet<Status> s = new HashSet<Status>();
					LCoord c;
					try {
						c = new LCoord(row, col - 1);
						s.add(status[c.getRow()][c.getCol()]);
					} catch (BadCoordException e) {
					}
					try {
						c = new LCoord(row, col + 1);
						s.add(status[c.getRow()][c.getCol()]);
					} catch (BadCoordException e) {
					}
					try {
						c = new LCoord(row - 1, col);
						s.add(status[c.getRow()][c.getCol()]);
					} catch (BadCoordException e) {
					}
					try {
						c = new LCoord(row + 1, col);
						s.add(status[c.getRow()][c.getCol()]);
					} catch (BadCoordException e) {
					}
					// check that all surrounding are same color
					// call is defended
					Iterator<Status> i = s.iterator();
					boolean defended = true;
					Color current = getColor(status[row][col]);
					while (i.hasNext()) {
						Color neighborColor = getColor(i.next());
						if (neighborColor == current) {
							defended = defended & true;
						} else {
							defended = defended & false;
						}
					}

					if (defended) {
						isDefended(row, col);
					} else {
						switch (status[row][col]) {
						case NEUTRAL:
							break;
						case RED_DEFENDED:
						case RED:
							status[row][col] = Status.RED;
							break;
						case BLUE_DEFENDED:
						case BLUE:
							status[row][col] = Status.BLUE;
							break;
						}
					}
					s.clear();
					defended = true;
				}
			}
		}
	}

	/**
	 * Prints out the current game board with indices to aid in debugging.
	 */
	public static void dBoard() {
		p();
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
		p();
	}

	protected static Color getColor(Status s) {
		switch (s) {
		case RED:
		case RED_DEFENDED:
			return Color.RED;
		case BLUE:
		case BLUE_DEFENDED:
			return Color.BLUE;
		default:
			return null;
		}

	}

	public Color getCPlayer() {
		LPlayer c = (LPlayer) cPlayer;
		if (c.equals(this.red)) {
			return Color.RED;
		} else if (c.equals(this.blue)) {
			return Color.BLUE;
		} else {
			p("LOG: This getCPlayer() is crap and the equality will require Comparable.");
			System.exit(-1);
			return null;
		}
	}

	/**
	 * Initializes the game board with a random set of letters and sets each
	 * letter to be NEUTRAL.
	 */
	private void initializeBoard() {
		Letterpress.board = new char[5][5];
		Random gen;
		switch (LGameManager.runState) {
		case DEBUG:
			gen = new Random(0);
			break;
		case RUN:
			gen = new Random();
			break;
		case TEST:
			gen = new Random();
			break;
		default:
			gen = new Random();
			break;
		}

		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board.length; col++) {
				Letterpress.board[row][col] = ltrs.get(gen.nextInt(26));
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
		File dictionary = new File("SortedDictionary.txt");
		Scanner reader;
		try {
			reader = new Scanner(dictionary);
		} catch (FileNotFoundException e) {
			p("LOG: Could not find the dictionary file. Proceeding with empty dictionary.");
			reader = new Scanner("");
		}
		while (reader.hasNextLine()) {
			String word = reader.nextLine();
			dict.add(word.toUpperCase());
		}
		p("Dictionary size is: " + dict.size());
	}

	private void isDefended(int row, int col) {
		switch (Letterpress.status[row][col]) {
		case RED:
		case RED_DEFENDED:
			Letterpress.status[row][col] = Status.RED_DEFENDED;
			break;
		case BLUE:
		case BLUE_DEFENDED:
			Letterpress.status[row][col] = Status.BLUE_DEFENDED;
			break;
		default:
			p("LOG: checkDefended is broken or switch in isDefended is not working.—Passed "
					+ status[row][col]);
			break;
		}
	}

	public boolean isOver() {
		boolean over = false;
		if (bLastMove == null || rLastMove == null) {
			over = false;
		} else if (this.bLastMove.isPass() && this.rLastMove.isPass()) {
			over = true;
		} else {
			boolean neutral = false;
			for (int row = 0; row < 5; row++) {
				for (int col = 0; col < 5; col++) {
					if (status[row][col] == Status.NEUTRAL) {
						neutral = true;
						break;
					}
				}
				if (neutral) {
					break;
				}
			}
			if (!neutral) {
				over = true;
			}
		}
		return over;
	}

	/**
	 * Letterpress constructor. Initializes the board and the dictionary.
	 */
	public Letterpress(GameManager gm) {

		// set up the [0,25] --> letter map
		Letterpress.ltrs = new HashMap<Integer, Character>(26);
		for (int i = 0; i < letters.length; i++)
			ltrs.put(i, letters[i]);
		// build the game board
		initializeBoard();
		// build the dictionary
		initializeDictionary();
		// supplementDictionary();
		refineDictionary();
		// initialize any other variables for play
	}

	public void makeMove(LMove m) throws IllegalMoveException {
		if (validateMove(m)) {
			Status changeTo = null;
			Status noChange = null;
			switch (this.getCPlayer()) {
			case RED:
				changeTo = Status.RED;
				noChange = Status.BLUE_DEFENDED;
				this.rLastMove = m;
				break;
			case BLUE:
				changeTo = Status.BLUE;
				noChange = Status.RED_DEFENDED;
				this.bLastMove = m;
				break;
			}
			if (m.isPass()) {
				p("[PASS]");

			} else {
				// throw all prefixes of the word out of the dictionary
				String mStr = "";
				for (Iterator<LCoord> i = m.iterator(); i.hasNext();) {
					LCoord c = i.next();
					mStr += board[c.getRow()][c.getCol()];
				}

				for (Iterator<String> i = dict.iterator(); i.hasNext();) {
					if (mStr.startsWith(i.next())) {
						i.remove();
					}
				}
				// change the color of the tiles if that's applicable
				switch (this.getCPlayer()) {
				case RED:
					changeTo = Status.RED;
					noChange = Status.BLUE_DEFENDED;
					break;
				case BLUE:
					changeTo = Status.BLUE;
					noChange = Status.RED_DEFENDED;
					break;
				}
				Iterator<LCoord> i = m.iterator();
				while (i.hasNext()) {
					LCoord c = i.next();
					if (Letterpress.status[c.getRow()][c.getCol()] != noChange) {
						Letterpress.status[c.getRow()][c.getCol()] = changeTo;
					}
				}
				// update the status of the game board
				this.checkDefended();
			}
			switch (this.getCPlayer()) {
			case RED:
				this.cPlayer = this.blue;
				break;
			case BLUE:
				this.cPlayer = this.red;
				break;
			}
		} else {
			throw new IllegalMoveException();
		}
	}

	/**
	 * @param s
	 *            Makes printing that much easier.
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

	private void refineDictionary() {
		int dSize = dict.size();
		if (board != null && dict != null && dict.size() > 0) {
			// build counted list of char in the game board and a list of their
			// counts
			int[] ltrCount = new int[26];
			for (int row = 0; row < board.length; row++) {
				for (int col = 0; col < board.length; col++) {
					char ltr = board[row][col];
					int i = 0;
					while (ltr != letters[i]) {
						i++;
					}
					ltrCount[i] += 1;
				}
			}
			// loop through the dictionary and remove words that cannot be
			// played
			Iterator<String> iter = dict.iterator();
			while (iter.hasNext()) {
				String word = iter.next();
				if (word.length() < 2) {
					iter.remove();
					continue;
				}
				int[] wordLtrCount = new int[26];

				for (int i = 0; i < word.length(); i++) {
					char ltr = word.charAt(i);
					int j = 0;
					while (ltr != letters[j]) {
						j++;
					}
					wordLtrCount[j] += 1;
				}

				for (int i = 0; i < 26; i++) {
					if ((ltrCount[i] - wordLtrCount[i]) < 0) {
						iter.remove();
						break;
					}
				}
			} // end while iter.hasNext

			/*
			 * p("LOG: Refine removed " + (dSize - dict.size()) +
			 * " words from the dictionary. " + dict.size() + " words remain.");
			 */
		} else {
			p("LOG: Unable to refine dictionary.");
		}
	}

	@Override
	public HashMap<Player, Double> getScore() {
		double rScore = 0;
		double bScore = 0;
		for (int row = 0; row < 5; row++) {
			for (int col = 0; col < 5; col++) {
				switch (status[row][col]) {
				case RED:
				case RED_DEFENDED:
					rScore += 1;
					break;
				case BLUE:
				case BLUE_DEFENDED:
					bScore += 1;
				default:
					break;
				}
			}
		}
		HashMap<Player, Double> score = new HashMap<Player, Double>();
		score.put(red, rScore);
		score.put(blue, bScore);
		return score;
	}

	public int score(Player p) {
		int rScore = 0;
		int bScore = 0;

		for (int row = 0; row < 5; row++) {
			for (int col = 0; col < 5; col++) {
				switch (status[row][col]) {
				case RED:
				case RED_DEFENDED:
					rScore += 1;
					break;
				case BLUE:
				case BLUE_DEFENDED:
					bScore += 1;
				default:
					break;
				}
			}
		}

		if (p == red) {
			return rScore;
		} else if (p == blue) {
			return bScore;
		} else {
			p("LOG: Score called by unknown player.");
			return -1;
		}
	}

	@Override
	public GameState getState() {
		return new GameState(board, status, dict);
	}

	private void supplementDictionary() {
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

		p("LOG: Supplemented the dictionary with " + (dict.size() - dSize)
				+ " words. Now have " + dict.size() + " words.");
	}

	@Override
	public void undoMove() {
		// stub
	}

	public boolean validateMove(LMove m) {
		boolean valid;
		// check if move is a pass
		if (m.isPass()) {
			valid = true;
		} else {
			// build the word described by m from the board
			String mStr = "";
			for (Iterator<LCoord> i = m.iterator(); i.hasNext();) {
				LCoord c = i.next();
				mStr += board[c.getRow()][c.getCol()];
			}
			// check length to make sure it's >= 2
			// check string is in the current dictionary
			if (mStr.length() >= 2 && dict.contains(mStr)) {
				valid = true;
			} else {
				p("LOG: Submitted move was found to be too short or was a word no longer in the dictionary");
				valid = false;
			}
		}
		return valid;
	}

	@Override
	public void setGameManager(GameManager gm) {
		this.gm = gm;

	}

	@Override
	public void start() {
		// nothing for this to do yet.

	}

	@Override
	public void makeMove(Move m) throws IllegalMoveException {
		makeMove((LMove) m);

	}
}
