
public abstract class LPlayer implements Player, Comparable<LPlayer>{
	protected GameState currentGameState;
	protected Letterpress.Color color;
	
	public LPlayer(){
	}
	
	public void notifyOfTurn(){
		giveMove();
	}
	
	public Move giveMove(){
		Letterpress.p("LOG: LPlayer's giveMove() called. This needs to be overridden by any subclasses.");
		return null;
	}
	
	public void giveColor(Letterpress.Color c){
		this.color = c;
		Letterpress.p(this+" was assigned the color "+this.color);
	}
	
	protected void updateGameState(GameState state){
		this.currentGameState = state;
	}
	
	protected LCoord getSignificantLetter(boolean[][] used, char l) {
		LCoord letter = null;
		boolean changesState = false;
		Letterpress.Status undef;
		switch (color) {
		case BLUE:
			undef = Letterpress.Status.RED;
			break;
		case RED:
			undef = Letterpress.Status.BLUE;
			break;
		default:
			undef = null;
			break;
		}

		char[][] board = currentGameState.getboard();

		for (int r = 0; r < 5; r++) {
			for (int c = 0; c < 5; c++) {
				if (board[r][c] == l && used[r][c] == false) {
					Letterpress.Status s = currentGameState.getStatus()[r][c];
					LCoord coord = null;
					try {
						coord = new LCoord(r, c);
					} catch (BadCoordException e) {
						e.printStackTrace();
					}
					if (s == undef || s == Letterpress.Status.NEUTRAL) {
						letter = coord;
						changesState = true;
					} else {
						if (changesState == false) {
							letter = coord;
						}
					}
				}
			}
		}

		return letter;
	}
	
	public Letterpress.Color getColor(){
		return color;
	}
	
	public int compareTo(LPlayer p){
		int equality;
		if (color == p.getColor()){
			equality = 0;
		} else {
			equality = -1;
		}
		return equality;
	}
	
	public String toString(){
		return ""+color;
	}
}
