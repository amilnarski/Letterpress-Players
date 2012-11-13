
public interface Game {
	public void receiveMove(Move m);
	public void undoMove();
	public void score();
	public GameState state();
	public void assignPlayer (Player player);
}
