import java.util.HashMap;
import java.util.Random;

public class LGameManager implements GameManager {
	private static Game lp;
	private static LPlayer red;
	private static LPlayer blue;
	private static Random gen;
	protected static runStates runState;

	public enum runStates {
		DEBUG, RUN, TEST
	}

	public LGameManager() {
	}

	public void run() {
		int moves = 0;
		log(lp.getState().getDict().size()+" possible moves on this board.");
		while (!lp.isOver()) {
			Letterpress.Color currentPlayer = ((Letterpress)lp).getCPlayer();
			Move m=null;
			switch(currentPlayer){
			case BLUE:
				blue.updateGameState(lp.getState());
				m = blue.giveMove();
				break;
			case RED:
				red.updateGameState(lp.getState());
				m = red.giveMove();
				break;
			default:
				log("This switch on cPlayer in LGameManager is broken.");
				m= new LMove();
				break;
			}
			try {
				//log(""+m);
				lp.makeMove(m);
				moves ++;
			} catch (IllegalMoveException e) {
				log(currentPlayer + " submitted " + m + " which is an illegal move.");
			}
			log(currentPlayer + " played " + m);
			Letterpress.dBoard();
		}
		HashMap<Player, Double> score = lp.getScore();
		log(red + ":" + score.get(red));
		log(blue + ":" + score.get(blue));
		log(moves +" moves were made.");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// args[0] = -d is for debug, -r is for running
		if (args.length == 0 || args[0].equals("-r")) {
			runState = runStates.RUN;
		} else if (args[0].equals("-d")) {
			runState = runStates.DEBUG;
		}

		switch (runState) {
		case DEBUG:
			log("Running in DEBUG mode.");
			gen = new Random(0);
			break;
		case RUN:
			log("Running in RUN mode.");
			gen = new Random();
			break;
		case TEST:
			log("Running in TEST mode.");
			break;
		default:
			log("Running in RUN mode.");
			gen = new Random();
			break;

		}
		
		GameManager gm = new LGameManager();
		lp = new Letterpress(gm);
		red = new GreedyPlayer();
		blue = new WeightedGreedyPlayer();
		lp.assignPlayer(red);
		lp.assignPlayer(blue);
		// while (!oneReady || !twoReady) {
		// System.out.println("Waiting for players to be ready...")
		// }

		gm.run();
	}

/*	@Override
	public void notifyReadyToPlay(Player player) {
		LPlayer p = (LPlayer) player;
		lp.assignPlayer(p);
		if (p.getColor() == Letterpress.Color.RED) {
			this.red = p;
		} else if (p.getColor() == Letterpress.Color.BLUE) {
			this.blue = p;
		} else {
			log("Someone else, " + p + ", is ready to play this game.");
		}

	}*/

	@Override
	public Game getGame() {
		return lp;
	}

	public static void log() {
		log("");
	}

	public static void log(String s) {
		System.out.println(s);
	}

	@Override
	public GameState getGameState() {
		return lp.getState();
	}
}
