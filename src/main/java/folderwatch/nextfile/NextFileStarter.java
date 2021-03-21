package folderwatch.nextfile;

import folderwatch.FolderWatcher;

public class NextFileStarter implements NextFile {

	private FolderWatcher folderWatcher;

	public NextFileStarter(FolderWatcher folderWatcher) {
		this.folderWatcher = folderWatcher;
	}

	@Override
	public NextFile process() {
		return folderWatcher.getNextFile();
	}

}
