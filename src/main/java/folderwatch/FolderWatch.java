package folderwatch;

public class FolderWatch implements Runnable {

	private FolderWatcher folderWatcher;

	private FolderWatch(FolderWatcher folderWatcher) {
		this.folderWatcher = folderWatcher;
	}

	public static FolderWatch create(FolderWatcher folderWatcher) {
		return new FolderWatch(folderWatcher);
	}

	public void start() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName());
	}

}
