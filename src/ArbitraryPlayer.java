import java.util.Arrays;
import java.util.Iterator;


public class ArbitraryPlayer extends LPlayer {

	public ArbitraryPlayer(Game game) {
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
				LCoord coord = super.getSignificantLetter(used, c);
				used[coord.getRow()][coord.getCol()] = true;
				mv.addLCoord(coord);			
			}
			super.g.receiveMove(mv);
		} else {
			super.g.receiveMove(new LMove());
		}
		
	}
	
	

}
