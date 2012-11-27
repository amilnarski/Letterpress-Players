import java.util.Arrays;
import java.util.Iterator;


public class RandomPlayer extends LPlayer {

	public RandomPlayer(Game game) {
		super(game);
		super.notifyReadyToPlay();
	}
	
	public void giveMove(){
		super.updateGameState();
		Iterator<String> i = super.currentGameState.getDict().iterator();
		if (i.hasNext()){
			String move = i.next();
			Letterpress.p(move);
			boolean [][]used = new boolean[5][5];
			for (int j = 0; j <used[0].length; j++){
				Arrays.fill(used[j], false);
			}
			LMove mv = new LMove();
			for (int k=0;k<move.length();k++){
				char c = move.charAt(k);
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
		} else {
			super.g.receiveMove(new LMove());
		}
		
	}

}
