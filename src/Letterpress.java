import java.util.Random;

public class Letterpress {
	static char[] ltrs = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	private static char[][] board;
	/**
	 * @param args
	 */
	
	public Letterpress(){
		initializeBoard();
	}
	
	private void initializeBoard(){
		this.board = new char[5][5];
		Random gen = new Random(0);
		int pos = 0;
		for (int row = 0; row < board.length; row++){
			for (int col = 0; col < board.length; col++){
				this.board[row][col] = ltrs[gen.nextInt(26)];
			}
		}
	}
	
	public static void pBoard(){
		for (int row = 0; row < 5; row++){
			String rowStr = "";
			for (int col = 0; col < 5; col++){
				rowStr += board[row][col]+" ";
			}
			p(rowStr);
		}
	}
	
	public static void dBoard(){
		System.out.println("  0 1 2 3 4");
		for (int row = 0; row < 5; row++){
			String rowStr = row+" ";
			for (int col = 0; col < 5; col++){
				rowStr += board[row][col]+" ";
			}
			p(rowStr);
		}
	}
	
	public static void main(String[] args) {
		Letterpress lp = new Letterpress();
		dBoard();
	

	}
	
	public static void p(String s){
		System.out.println(s);
	}
	
	

}
