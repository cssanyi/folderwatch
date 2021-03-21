package folderwatch.nextfile;

import java.io.File;

import folderwatch.FolderWatcher;

public class NextFileImage implements NextFileContent {

	private FolderWatcher folderWatcher;
	private String name;

	public NextFileImage(FolderWatcher folderWatcher, String name) {
		this.folderWatcher = folderWatcher;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public File getErrorFolder() {
		return folderWatcher.getImageErrorFolder();
	}

	@Override
	public NextFile process() {
		return folderWatcher.getNextFile();
	}

}
