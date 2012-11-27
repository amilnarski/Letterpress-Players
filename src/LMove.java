import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Aaron Miller
 * 
 * Class maintains a Letterpress move, consisting as a set of Letterpress coordinates.
 * Coordinates may be added as int row, col or as an LCoord. Bad coordinates are rejected.
 * If no valid coordinates are passed the move will be flagged as a pass.
 *
 */
public class LMove extends Move{
	private boolean isPass;
	private String word;
	ArrayList<LCoord> coord;

	public LMove(LCoord[] ltrCoords) {
		if (ltrCoords != null && ltrCoords.length > 0) {
			coord = new ArrayList<LCoord>(ltrCoords.length);
			for (int i = 0; i < ltrCoords.length; i++) {
				if (!coord.contains(ltrCoords[i]))
					coord.add(ltrCoords[i]);
				isPass = false;
			}
		} else {
			Letterpress
					.p("LOG: Move created with empty or null array of coordinates. Move continued as PASS.");
			isPass = true;
		}
	}

	public LMove() {
		isPass = true;
	}

	public void addLCoord(int row, int col) {
		if (coord == null) {
			coord = new ArrayList<LCoord>();
			isPass = true;
		}
		
		try {
			coord.add(new LCoord(row, col));
			isPass = false;
		} catch (BadCoordException e) {
			Letterpress
					.p("LOG: Bad coordinates passed to LMove.addLCoord(). Adding no new coordinates.");
		}
	}

	public void addLCoord(LCoord c) {
		if (coord == null) {
			coord = new ArrayList<LCoord>();
			isPass = true;
		}
		
		coord.add(c);
		isPass = false;
	}
	
	public boolean isPass(){
		return isPass;
	}
	
	public Iterator<LCoord> iterator(){
		if (coord!=null)
			return coord.iterator();
		else if (!isPass){
			Letterpress.p("LOG: LMove.iterator called when coord is null.");
			return new ArrayList<LCoord>(0).iterator();
		} else {
			Letterpress.p("LOG: LMove.iterator called on a pass move.");
			return new ArrayList<LCoord>(0).iterator();
		}
	}
	
	public void setWord(String w){
		this.word = w;
	}
	
	public String getWord(){
		if (this.word != null){
			return this.word;
		} else {
			return "";
		}
	}

}
