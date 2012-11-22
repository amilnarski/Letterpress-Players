import java.util.Arrays;
import java.util.Iterator;


public class GreedyPlayer extends LPlayer {

	public GreedyPlayer(Game game) {
		super(game);
	}

	public void giveMove(){
		//get the game's current state
		super.updateGameState();
		Iterator<String> i = super.currentGameState.getDict().iterator();
		//walk through the possible moves and get the longest move
		String bestMove = "";
		while(i.hasNext()){
			String move = i.next();
			if (move.length() > bestMove.length()){
				bestMove = move;
			}
		}
		//get letter positions for the best move
		Letterpress.p(bestMove);
		if (bestMove == ""){
			super.g.receiveMove(new LMove());
		}
		boolean [][]used = new boolean[5][5];
		for (int j = 0; j <used[0].length; j++){
			Arrays.fill(used[j], false);
		}
		LMove mv = new LMove();
		for (int k=0;k<bestMove.length();k++){
			char c = bestMove.charAt(k);
			for (int l =0; l<5; l++){
				boolean found = false;
				for(int m = 0; m<5; m++){
					if (super.currentGameState.getboard()[l][m] == c && used[l][m] == false){
						mv.addLCoord(l, m);
						used[l][m] = true;
						found = true;
						break;
					}
				}
				if (found)
					break;
			}
		}
		super.g.receiveMove(mv);
	}

}
