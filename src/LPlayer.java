
public abstract class LPlayer implements Player{
	protected Game g;
	protected GameState currentGameState;
	protected Letterpress.Color color;
	
	public LPlayer(Game game){
		this.g = game;
		g.assignPlayer(this);
	}
	
	public void giveMove(){
		Letterpress.p("LOG: LPlayer's giveMove() called. This needs to be overridden by any subclasses.");
	}
	
	public void giveColor(Letterpress.Color c){
		this.color = c;
		Letterpress.p(this+" was assigned the color "+this.color);
	}
	
	protected void updateGameState(){
		this.currentGameState = g.state();
	}
}
