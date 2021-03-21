package folderwatch.nextfile.factory;

import folderwatch.FolderWatcher;
import folderwatch.nextfile.NextFileContent;
import folderwatch.nextfile.NextFileImage;
import folderwatch.nextfile.NextFileText;
import folderwatch.nextfile.NextFileUnknownType;

public class NextFileFactory {

	private static NextFileFactory instance = new NextFileFactory();

	private int wordPairOccurenceThreshold;

	private NextFileFactory() {
	}

	public static NextFileFactory getInstance(int wordPairOccurenceThreshold) {
		instance.wordPairOccurenceThreshold = wordPairOccurenceThreshold;

		return instance;
	}

	public NextFileContent getNextFile(String name, FolderWatcher folderWatcher) {
		if (name.length() > 4 && name.endsWith(".txt")) {
			return new NextFileText(folderWatcher, name, wordPairOccurenceThreshold);
		}
		if (name.length() > 4
				&& (name.endsWith(".bmp") || name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png"))) {
			return new NextFileImage(folderWatcher, name);
		}
		return new NextFileUnknownType(folderWatcher, name);
	}

}
