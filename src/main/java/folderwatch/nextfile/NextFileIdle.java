package folderwatch.nextfile;

import folderwatch.FolderWatcher;

public class NextFileIdle implements NextFile {

	private FolderWatcher folderWatcher;

	public NextFileIdle(FolderWatcher folderWatcher) {
		this.folderWatcher = folderWatcher;
	}

	@Override
	public NextFile process() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return folderWatcher.getNextFile();
	}

}
