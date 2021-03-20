package folderwatch;

public class Exit {

	private static Exit instance = new Exit();

	private static int exitDelay;

	private Exit() {
	}

	public static Exit create(int exitDelay) {
		Exit.exitDelay = exitDelay;

		return instance;
	}

}
