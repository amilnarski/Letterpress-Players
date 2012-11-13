
public interface Game {
	public void receiveMove();
	public void undoMove();
	public void score();
	public Game state();
	public void assignPlayer (Player player);
}
