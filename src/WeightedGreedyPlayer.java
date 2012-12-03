import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class WeightedGreedyPlayer extends LPlayer {

	private static double[][] positionWeights;

	public WeightedGreedyPlayer(Game game) {
		super(game);
		WeightedGreedyPlayer.positionWeights = new double[][] {{2.0,1.5,1.5,1.5,2.0},{1.5,1.0,1.0,1.0,1.5},{1.5,1.0,1.0,1.0,1.5},{1.5,1.0,1.0,1.0,1.5},{2.0,1.5,1.5,1.5,2.0}};
		super.notifyReadyToPlay();
	}

	public void giveMove(){
		//get the game's current state
		super.updateGameState();
		//update the weights for the new state
		double[][] w =generateWeights(super.currentGameState.getStatus());
		Iterator<String> i = super.currentGameState.getDict().iterator();
		for(int j =0; j<5;j++){
		Letterpress.p(Arrays.toString(w[j]));
		}
		
		//walk through the possible moves and get the best weighted move
		LMove bestMove = null;
		double bestWeight = 0.0;
		while(i.hasNext()){
			String move = i.next();
			//calculate the word value for the given board
			//build the move
			boolean [][]used = new boolean[5][5];
			for (int j = 0; j <used[0].length; j++){
				Arrays.fill(used[j], false);
			}
			LMove mv = new LMove();
			mv.setWord(move);
			for (int k=0;k<move.length();k++){
				char c = move.charAt(k);
				LCoord bestLtr = getBestLtr(w,used,c); 
				used[bestLtr.getRow()][bestLtr.getCol()] = true;
				mv.addLCoord(bestLtr);
			}
			
			//score the move vs. best weight
			double moveWeight = 0.0;
			Iterator<LCoord> it = mv.iterator();
			while (it.hasNext()){
				LCoord c = it.next();
				moveWeight += w[c.getRow()][c.getCol()];
			}
			
			if (moveWeight > bestWeight){
				bestMove = mv;
				bestWeight = moveWeight;
			}
		}
		//get letter positions for the best move
		if (bestMove == null){
			super.g.receiveMove(new LMove());
		} else {
			Letterpress.p(bestMove.getWord());
			Letterpress.p(bestWeight);
			super.g.receiveMove(bestMove);
		}
	}

	private LCoord getBestLtr(double [][] w, boolean[][] used, char l){
		LCoord lBest = null;
		double wBest = 0;
		char[][] board = super.currentGameState.getboard();
		
		for (int r = 0; r < 5; r++){
			for (int c = 0; c < 5; c++){
				if (board[r][c] == l && used[r][c]==false){
					if (w[r][c] > wBest){
						//Letterpress.p("This "+l+" is worth "+(positionWeights[r][c]-wBest)+" more.");
						try{
							lBest = new LCoord(r,c);
							wBest = w[r][c];
						} catch (BadCoordException e){
							Letterpress.p(e);
						}
					}
				}
			}
		}
		//Letterpress.p(wBest);
		return lBest;
	}


	private double[][] generateWeights(Letterpress.Status[][] status){
		double[][] w = new double[5][5];
		for (int i=0; i<5;i++)
			Arrays.fill(w[i], 0.0);
		//determine features for each letter on the board
		//calculate the weights based on relative features
		for (int row = 0; row < status.length; row++) {
			for (int col = 0; col < status.length; col++) {
				// build adjacent positions
				HashSet<Letterpress.Status> s = new HashSet<Letterpress.Status>();
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
				// test each surrounding piece & coordinates to see what this position's weight is
				Iterator<Letterpress.Status> i = s.iterator();
				double weight = 1.0;
				//check undefended opponent
				Letterpress.Status undef;
				if(super.color == Letterpress.Color.RED){
					undef = Letterpress.Status.BLUE;
				}else{
					undef = Letterpress.Status.RED;
				}
				if (status[row][col] == undef){
					//weight+=0.2;
					weight+=0.5;
				}
				//check frontier
				while (i.hasNext()) {
					Letterpress.Color neighborColor = Letterpress.getColor(i.next());
					if (neighborColor == super.color){
						//weight+=0.02;
						weight+=1.0;
					}
				}//end while
				w[row][col] = weight;
			} //end for each col
		}//end for each row
		
		//multiply those weights by the position multiplier
		for(int row1=0; row1 < w.length; row1++){
			for(int col1=0; col1 < w.length; col1++){
				if(Letterpress.getColor(status[row1][col1]) != super.color){
					w[row1][col1] = w[row1][col1]*WeightedGreedyPlayer.positionWeights[row1][col1];
				} else {
					w[row1][col1] = 0.1;
				}
			}
		}

		//return results
		return w;
	}


}
