
public interface Player {
	//move handling methods
	public Move giveMove();
	//game management methods
	public void giveColor(Letterpress.Color c);
	public void notifyOfTurn();
}
