import java.util.Arrays;
import java.util.Iterator;

public class GreedyPlayer extends LPlayer {

	public GreedyPlayer() {
	
	}

	public Move giveMove() {
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
		if (bestMove == "") {
			return new LMove();
		}
		boolean[][] used = new boolean[5][5];
		for (int j = 0; j < used[0].length; j++) {
			Arrays.fill(used[j], false);
		}
		LMove mv = new LMove();
		mv.setWord(bestMove);
		for (int k = 0; k < bestMove.length(); k++) {
			char c = bestMove.charAt(k);
			LCoord letter = super.getSignificantLetter(used, c);
			used[letter.getRow()][letter.getCol()] = true;
			mv.addLCoord(letter);
		}
		return mv;
	}
}
