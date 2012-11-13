import java.util.HashSet;


public class GameState {
	private char[][]board;
	private Letterpress.Status[][] status;
	private HashSet<String> dict;
	
	public GameState(char[][]board, Letterpress.Status[][] status, HashSet <String>dict){
		this.board = board;
		this.status = status;
		this.dict = dict;
	}
	
	public char[][] getboard(){
		return this.board;
	}
	
	public Letterpress.Status[][] getStatus(){
		return this.status;
	}
	
	public HashSet<String> getDict(){
		return this.dict;
	}
	
	public String toString(){
		String toRet="\n";
		toRet+= ("\n  0 1 2 3 4\t  0 1 2 3 4");
		for (int row = 0; row < 5; row++) {
			String rowStr = row + " ";
			for (int col = 0; col < 5; col++) {
				rowStr += board[row][col] + " ";
			}
			rowStr += "\t" + row + " ";
			for (int col = 0; col < 5; col++) {
				switch (status[row][col]) {
				case NEUTRAL:
					rowStr += "_" + " ";
					break;
				case RED:
					rowStr += "r" + " ";
					break;
				case BLUE:
					rowStr += "b" + " ";
					break;
				case RED_DEFENDED:
					rowStr += "R" + " ";
					break;
				case BLUE_DEFENDED:
					rowStr += "B" + " ";
					break;
				default:
					rowStr += " " + " ";
				}
			}
			toRet += "\n"+rowStr;
		}
		toRet+="\n";
		return toRet;
	}
}
