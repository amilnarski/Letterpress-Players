import java.util.HashMap;

public interface Game {
	//move handling methods
	public void makeMove (Move m) throws IllegalMoveException;
	public void undoMove();
	//state methods
	public GameState getState();
	public HashMap<Player,Double> getScore();
	//game managment methods
	public void assignPlayer(Player player);
	public boolean isOver();
	public void setGameManager(GameManager gm);
	public void start();
	//public void assignPlayer (Player player);
	//public void readyToPlay(Player Player);
	
}
