package folderwatch.nextfile.control;

import folderwatch.FolderWatcher;

public class NextFileIdle implements NextFileControl {

	private FolderWatcher folderWatcher;

	public NextFileIdle(FolderWatcher folderWatcher) {
		this.folderWatcher = folderWatcher;
	}

	@Override
	public NextFileControl process() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return folderWatcher.getNextFile();
	}

}
