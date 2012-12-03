import java.util.Arrays;
import java.util.Iterator;

public class GreedyPlayer extends LPlayer {

	public GreedyPlayer(Game game) {
		super(game);
		super.notifyReadyToPlay();
	}

	public void giveMove() {
		// get the game's current state
		super.updateGameState();
		Iterator<String> i = super.currentGameState.getDict().iterator();
		// walk through the possible moves and get the longest move
		String bestMove = "";
		while (i.hasNext()) {
			String move = i.next();
			if (move.length() > bestMove.length()) {
				bestMove = move;
			}
		}
		// get letter positions for the best move
		Letterpress.p(bestMove);
		if (bestMove == "") {
			super.g.receiveMove(new LMove());
		}
		boolean[][] used = new boolean[5][5];
		for (int j = 0; j < used[0].length; j++) {
			Arrays.fill(used[j], false);
		}
		LMove mv = new LMove();
		for (int k = 0; k < bestMove.length(); k++) {
			char c = bestMove.charAt(k);
			LCoord letter = super.getSignificantLetter(used, c);
			used[letter.getRow()][letter.getCol()] = true;
			mv.addLCoord(letter);
		}
		super.g.receiveMove(mv);
	}

//	private LCoord getSignificantLetter(boolean[][] used, char l) {
//		LCoord letter = null;
//		boolean changesState = false;
//		Letterpress.Status undef;
//		switch (super.color) {
//		case BLUE:
//			undef = Letterpress.Status.RED;
//			break;
//		case RED:
//			undef = Letterpress.Status.BLUE;
//			break;
//		default:
//			undef = null;
//			break;
//		}
//
//		char[][] board = super.currentGameState.getboard();
//
//		for (int r = 0; r < 5; r++) {
//			for (int c = 0; c < 5; c++) {
//				if (board[r][c] == l && used[r][c] == false) {
//					Letterpress.Status s = super.currentGameState.getStatus()[r][c];
//					LCoord coord = null;
//					try {
//						coord = new LCoord(r, c);
//					} catch (BadCoordException e) {
//						e.printStackTrace();
//					}
//					if (s == undef || s == Letterpress.Status.NEUTRAL) {
//						letter = coord;
//						changesState = true;
//					} else {
//						if (changesState == false) {
//							letter = coord;
//						}
//					}
//				}
//			}
//		}
//
//		return letter;
//	}

}
