package folderwatch;

public class Exit {

	private static Exit instance = new Exit();

	private long exitDelay;
	private long idleSince;

	private boolean activated;

	private Exit() {
	}

	public static Exit create(int exitDelay) {
		instance.exitDelay = exitDelay * 1000;
		instance.idleSince = System.currentTimeMillis();

		return instance;
	}

	public boolean activated() {
		return this.activated;
	}

	public void reset() {
		this.idleSince = System.currentTimeMillis();
	}

	public boolean check() {
		if (this.exitDelay < System.currentTimeMillis() - this.idleSince) {
			this.activated = true;
		}
		return this.activated;
	}

}
