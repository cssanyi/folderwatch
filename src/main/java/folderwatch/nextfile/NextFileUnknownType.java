package folderwatch.nextfile;

import java.io.File;

import folderwatch.FolderWatcher;

public class NextFileUnknownType implements NextFileContent {

	private FolderWatcher folderWatcher;
	private String name;

	public NextFileUnknownType(FolderWatcher folderWatcher, String name) {
		this.folderWatcher = folderWatcher;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public File getErrorFolder() {
		return folderWatcher.getUnknownTypeFolder();
	}

	@Override
	public File getDoneFolder() {
		return folderWatcher.getUnknownTypeFolder();
	}

	@Override
	public NextFile process() {
		return folderWatcher.getNextFile();
	}

}
