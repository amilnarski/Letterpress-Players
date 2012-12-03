import java.util.Iterator;
import java.util.Scanner;


public class HumanPlayer extends LPlayer {

	public HumanPlayer() {
		super();
	}

	public LMove giveMove(){
		pGameForUser(); //give the user a visual representation on the game
		Scanner keyboard = new Scanner (System.in);
		String mStr = keyboard.nextLine();
		if((mStr.length() % 2) == 1){
			Letterpress.p("Please ensure that your coordinates are balanced.");
			giveMove();
		}
		LMove m = new LMove(); //create a new move from the user's input
		for (int i=0;i<mStr.length(); i++){
			
			try {
				LCoord c = new LCoord(Integer.parseInt(mStr.charAt(i)+""),Integer.parseInt(mStr.charAt(++i)+""));
				m.addLCoord(c);
			} catch (NumberFormatException e) {
				Letterpress.p("Please ensure that your coordinates are integers.");
				giveMove();
			} catch (BadCoordException e) {
				Letterpress.p("Please ensure that your coordinates are within the range [0,4].");
				giveMove();
			}
		}
		//evaluate the move as being a valid word
		Iterator <LCoord>i = m.iterator();
		char[][] board = super.currentGameState.getboard();
		String mWord = "";
		while(i.hasNext()){
			LCoord c = i.next();
			mWord += board[c.getRow()][c.getCol()];
		}
		Letterpress.p(mWord);
		if(super.currentGameState.getDict().contains(mWord)){
			return m; //return the move
		} else {
			Letterpress.p("That word was not found in the dictionary or has already been played. Try a different word.");
			giveMove();
		}
		return null;
		
	}
	
	private void pGameForUser(){
		//super.updateGameState();
		Letterpress.p(super.currentGameState);
		Letterpress.p("Enter your next move as a string of unseperated coordinates, eg. 0011223344.");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
