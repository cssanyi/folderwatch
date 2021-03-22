package folderwatch;

import folderwatch.nextfile.control.NextFileControl;
import folderwatch.nextfile.control.NextFileStarter;

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
		NextFileControl nextFile = new NextFileStarter(this.folderWatcher);

		while (nextFile.processable()) {
			nextFile = nextFile.process();
		}
	}

}
