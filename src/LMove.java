import java.util.ArrayList;

public class LMove {
	boolean isPass;
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
		if (coord != null) {
			coord = new ArrayList<LCoord>();
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
		coord.add(c);
	}

}
