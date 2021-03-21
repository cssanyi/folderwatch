package folderwatch.nextfile;

import java.io.File;

import folderwatch.FolderWatcher;

public class NextFileText implements NextFileContent {

	private FolderWatcher folderWatcher;
	private String name;
	private int wordPairOccurenceThreshold;

	public NextFileText(FolderWatcher folderWatcher, String name, int wordPairOccurenceThreshold) {
		this.folderWatcher = folderWatcher;
		this.name = name;
		this.wordPairOccurenceThreshold = wordPairOccurenceThreshold;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public File getErrorFolder() {
		return folderWatcher.getTextErrorFolder();
	}

	@Override
	public File getDoneFolder() {
		return folderWatcher.getTextDoneFolder();
	}

	@Override
	public NextFile process() {
		return folderWatcher.getNextFile();
	}

}
