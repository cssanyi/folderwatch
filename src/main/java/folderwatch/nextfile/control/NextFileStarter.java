package folderwatch.nextfile.control;

import folderwatch.FolderWatcher;

public class NextFileStarter implements NextFileControl {

	private FolderWatcher folderWatcher;

	public NextFileStarter(FolderWatcher folderWatcher) {
		this.folderWatcher = folderWatcher;
	}

	@Override
	public NextFileControl process() {
		return folderWatcher.getNextFile();
	}

}
