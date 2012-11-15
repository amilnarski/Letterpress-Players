import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Letterpress implements Game {
	private static char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z' };
	private static HashMap<Integer, Character> ltrs;
	private static HashSet<String> dict;
	private static char[][] board;
	private Player red;
	private Player blue;

	protected static enum Status {
		NEUTRAL, RED, BLUE, RED_DEFENDED, BLUE_DEFENDED
	};

	protected static enum Color {
		RED, BLUE
	};

	private static Color cPlayer;
	private LMove bLastMove;
	private LMove rLastMove;
	private static Status[][] status;

	/**
	 * Letterpress constructor. Initializes the board and the dictionary.
	 */
	public Letterpress() {
		Random gen = new Random(0);

		if (gen.nextInt() % 2 == 0)
			Letterpress.cPlayer = Color.RED;
		else
			Letterpress.cPlayer = Color.BLUE;

		Letterpress.ltrs = new HashMap<Integer, Character>(26);
		for (int i = 0; i < letters.length; i++)
			ltrs.put(i, letters[i]);
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
			p(Arrays.toString(ltrCount));
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

			p("LOG: Refine removed " + (dSize - dict.size())
					+ " words from the dictionary. " + dict.size()
					+ " words remain.");
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

	private void receiveMove(LMove m) {
		if (!m.isPass()) {
			// CHECK THE WORD'S VALIDITY FOR THE BOARD
			String mStr = "";
			for (Iterator<LCoord> i = m.iterator(); i.hasNext();) {
				LCoord c = i.next();
				mStr += board[c.getRow()][c.getCol()];
			}

			if (!dict.contains(mStr)) {
				p("LOG: Move recieved from the "
						+ cPlayer
						+ " player was not contained in the dictionary. Returning for now. Probably want to do something more reasonable and exciting here in the future.");
				return;
			} else {
				dict.remove(mStr);
			}

			Iterator<LCoord> i = m.iterator();
			while (i.hasNext()) {
				LCoord c = i.next();
				if (!(Letterpress.status[c.getRow()][c.getCol()] == Status.RED_DEFENDED)
						&& !(Letterpress.status[c.getRow()][c.getCol()] == Status.BLUE_DEFENDED)) {
					switch (Letterpress.cPlayer) {
					case RED:
						Letterpress.status[c.getRow()][c.getCol()] = Status.RED;
						break;
					case BLUE:
						Letterpress.status[c.getRow()][c.getCol()] = Status.BLUE;
						break;
					}
				}
			}
			checkDefended();
		}
		switch (Letterpress.cPlayer) {
		case RED:
			this.rLastMove = m;
			Letterpress.cPlayer = Color.BLUE;
			break;
		case BLUE:
			this.bLastMove = m;
			Letterpress.cPlayer = Color.RED;
			break;
		}
		dBoard();
		notifyReadyForNextMove();
	}

	private void checkDefended() {
		for (int row = 0; row < status.length; row++) {
			for (int col = 0; col < status.length; col++) {
				if (status[row][col] != Status.NEUTRAL) {
					// p("Current Position:  "+row+","+col);
					// build adjacent positions
					HashSet<Status> s = new HashSet<Status>();
					LCoord c;
					try {
						c = new LCoord(row, col - 1);
						// p("left"+c);
						// p(getColor(status[c.getRow()][c.getCol()]));
						s.add(status[c.getRow()][c.getCol()]);
					} catch (BadCoordException e) {
					}
					try {
						c = new LCoord(row, col + 1);
						s.add(status[c.getRow()][c.getCol()]);
						// p("right"+c);
						// p(getColor(status[c.getRow()][c.getCol()]));
					} catch (BadCoordException e) {
					}
					try {
						c = new LCoord(row - 1, col);
						s.add(status[c.getRow()][c.getCol()]);
						// p("up"+c);
						// p(getColor(status[c.getRow()][c.getCol()]));
					} catch (BadCoordException e) {
					}
					try {
						c = new LCoord(row + 1, col);
						s.add(status[c.getRow()][c.getCol()]);
						// p("down"+c);
						// p(getColor(status[c.getRow()][c.getCol()]));
					} catch (BadCoordException e) {
					}
					// check that all surrounding are same color
					// call is defended
					Iterator<Status> i = s.iterator();
					boolean defended = true;
					Color current = getColor(status[row][col]);
					// p("Current Color:"+current);
					// p("sack: "+s);
					while (i.hasNext()) {
						Color neighborColor = getColor(i.next());
						// p("neigh: "+neighborColor);
						// p("equality: "+""+(null==current));
						if (neighborColor == current)
							defended = defended & true;
						else
							defended = defended & false;
					}

					if (defended) {
						isDefended(row, col);
					}
					s.clear();
					defended = true;
				}
			}
		}
	}

	private void notifyReadyForNextMove() {
		switch (Letterpress.cPlayer) {
		case RED:
			this.red.giveMove();
			break;
		case BLUE:
			this.blue.giveMove();
			break;
		}
	}

	private void startGame() {
		notifyReadyForNextMove();
	}

	private static Color getColor(Status s) {
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

	public void assignPlayer(Player player) {
		if (this.red == null) {
			this.red = player;
			this.red.giveColor(Color.RED);
		} else if (this.blue == null) {
			this.blue = player;
			this.blue.giveColor(Color.BLUE);
			startGame();
		} else {
			p("LOG: Player attempted to join the game. Two players are already assigned.");
		}
	}

	private boolean isOver() {
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
	 * @param args
	 *            Generates a new Letterpress game.
	 */
	public static void main(String[] args) {
		Letterpress lp = new Letterpress();
		Player r = new GreedyPlayer(lp);
		Player b = new GreedyPlayer(lp);
		/*
		 * p(Letterpress.cPlayer); LMove one = new LMove(); one.addLCoord(0, 0);
		 * one.addLCoord(0, 1); one.addLCoord(1, 0); lp.receiveMove(one);
		 * dBoard(); LMove two = new LMove(); two.addLCoord(4, 4);
		 * two.addLCoord(3, 4); two.addLCoord(4, 3); lp.receiveMove(two);
		 * dBoard(); LMove three = new LMove(); three.addLCoord(1, 1);
		 * three.addLCoord(1, 2); three.addLCoord(2, 1); lp.receiveMove(three);
		 * dBoard(); p(dict.toString()); p("LOG: Finished running.");
		 */
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

	public Color getcPlayer() {
		return cPlayer;
	}

	@Override
	public void receiveMove(Move move) {
		if (!isOver()) {
			LMove m = (LMove) move;
			receiveMove(m);
		} else {
			score();
		}
	}

	@Override
	public void undoMove() {
		// TODO Auto-generated method stub

	}

	@Override
	public void score() {
		int rScore=0;
		int bScore=0;
		
		for (int row = 0; row < 5; row++) {
			for (int col = 0; col < 5; col++) {
				switch(status[row][col]){
				case RED:
				case RED_DEFENDED:
					rScore +=1;
					break;
				case BLUE:
				case BLUE_DEFENDED:
					bScore +=1;
				default:
					break;
				}
			}
		}
		p("LOG: Final score:\nRed\tBlue\n"+rScore+"\t"+bScore);

	}

	@Override
	public GameState state() {
		return new GameState(board, status, dict);
	}

}
