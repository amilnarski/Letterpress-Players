
public class LCoord {
	private int row;
	private int col;
	
	/**
	 * @param row
	 * @param col
	 * @throws BadCoordException
	 * Creates a new LCoord object with bounds [0,4] for row and col. Throws exception if bounds are not within this range.
	 */
	public LCoord(int row, int col) throws BadCoordException{
		if (checkBounds(row,col)){
			this.row = row;
			this.col = col;
		} else {
			throw new BadCoordException(""+row+", "+col);
		}
	}
	
	/**
	 * @return row
	 */
	public int getRow(){
			return row;
	}
	
	/**
	 * @return col
	 */
	public int getCol(){
		return col;
	}
	
	/**
	 * @param row
	 * @param col
	 * @return validBounds
	 * 
	 * Checks that row and column are contained in [0,4]
	 */
	private boolean checkBounds (int row, int col){
		boolean validBounds = false;
		if (row > -1 && row < 5 && col > -1 && col < 5){
			validBounds = true;
		}
		return validBounds;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return " ["+row+", "+col+"] ";
	}
	
	/**
	 * @param args
	 * Tests the bound checking of the constructor with several boundary tests. Includes test of toString()
	 */
	public static void main (String[] args){
		try {
			LCoord c = new LCoord(0,0);
			Letterpress.p(c);
			Letterpress.p("PASSED 0,0");
		} catch (BadCoordException e) {
			Letterpress.p("FAILED 0,0");
		}
		
		try {
			LCoord d = new LCoord(-1,-1);
			Letterpress.p(d);
			Letterpress.p("FAILED -1,-1");
		} catch (BadCoordException e) {
			Letterpress.p("PASSED -1,-1");
		}
		
		try {
			LCoord e = new LCoord(5,5);
			Letterpress.p(e);
			Letterpress.p("FAILED 5,5");
		} catch (BadCoordException e) {
			Letterpress.p("PASSED 5,5");
		}
		
		try {
			LCoord f = new LCoord(4,4);
			Letterpress.p(f);
			Letterpress.p("PASSED 4,4");
		} catch (BadCoordException e) {
			Letterpress.p("FAILED 4,4");
		}
		
		try {
			LCoord g = new LCoord(2,3);
			Letterpress.p(g);
			Letterpress.p("PASSED 2,3");
		} catch (BadCoordException e) {
			Letterpress.p("FAILED 2,3");
		}
	}

}
