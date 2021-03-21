package folderwatch;

import java.io.File;

import folderwatch.nextfile.NextFile;
import folderwatch.nextfile.NextFileContent;
import folderwatch.nextfile.NextFileIdle;
import folderwatch.nextfile.factory.NextFileFactory;

public class FolderWatcher {

	private File incomingFolder;
	private File unknownTypeFolder;
	private File imageDoneFolder;
	private File imageErrorFolder;
	private File textDoneFolder;
	private File textErrorFolder;

	private NextFileFactory nextFileFactory;
	private Exit exit;

	private FolderWatcher() {
	}

	public File getIncomingFolder() {
		return incomingFolder;
	}

	public File getUnknownTypeFolder() {
		return unknownTypeFolder;
	}

	public File getImageDoneFolder() {
		return imageDoneFolder;
	}

	public File getImageErrorFolder() {
		return imageErrorFolder;
	}

	public File getTextDoneFolder() {
		return textDoneFolder;
	}

	public File getTextErrorFolder() {
		return textErrorFolder;
	}

	public static class FolderWatcherBuilder {
		private static final String UNKNOWN_TYPE = "unknown-type";
		private static final String IMAGE_DONE = "image-done";
		private static final String IMAGE_ERROR = "image-error";
		private static final String TEXT_DONE = "text-done";
		private static final String TEXT_ERROR = "text-error";

		private String incomingFolderPath;
		private NextFileFactory nextFileFactory;
		private Exit exit;

		public FolderWatcherBuilder withIncomingFolderPath(String incomingFolderPath) {
			this.incomingFolderPath = incomingFolderPath;
			return this;
		}

		public FolderWatcherBuilder withNextFileFactory(NextFileFactory nextFileFactory) {
			this.nextFileFactory = nextFileFactory;
			return this;
		}

		public FolderWatcherBuilder withExit(Exit exit) {
			this.exit = exit;
			return this;
		}

		public FolderWatcher build() {
			FolderWatcher folderWatcher = new FolderWatcher();

			folderWatcher.incomingFolder = new File(this.incomingFolderPath);
			folderWatcher.unknownTypeFolder = new File(folderWatcher.incomingFolder, UNKNOWN_TYPE);
			folderWatcher.imageDoneFolder = new File(folderWatcher.incomingFolder, IMAGE_DONE);
			folderWatcher.imageErrorFolder = new File(folderWatcher.incomingFolder, IMAGE_ERROR);
			folderWatcher.textDoneFolder = new File(folderWatcher.incomingFolder, TEXT_DONE);
			folderWatcher.textErrorFolder = new File(folderWatcher.incomingFolder, TEXT_ERROR);

			createFolder(folderWatcher.incomingFolder);
			createFolder(folderWatcher.unknownTypeFolder);
			createFolder(folderWatcher.imageDoneFolder);
			createFolder(folderWatcher.imageErrorFolder);
			createFolder(folderWatcher.textDoneFolder);
			createFolder(folderWatcher.textErrorFolder);

			folderWatcher.nextFileFactory = this.nextFileFactory;
			folderWatcher.exit = this.exit;

			return folderWatcher;
		}

		private void createFolder(File folder) {
			if (folder.exists() && !folder.isDirectory()) {
				throw new RuntimeException(folder.getAbsolutePath() + " refers to a file.");
			}
			if (!folder.exists()) {
				folder.mkdirs();
			}
			if (!folder.exists()) {
				throw new RuntimeException("Could not create " + folder.getAbsolutePath());
			}
		}
	}

	public synchronized NextFile getNextFile() {
		for (String name : incomingFolder.list()) {
			File file = new File(this.incomingFolder, name);
			if (file.isDirectory()) {
				continue;
			}
			NextFileContent nextFile = nextFileFactory.getNextFile(name, this);
			moveFromIncomingFolder(nextFile.getErrorFolder(), nextFile.getName());
			return nextFile;
		}
		return new NextFileIdle(this);
	}

	private void moveFromIncomingFolder(File errorFolder, String name) {
		// TODO should be move instead of copy !!!
		File source = new File(this.getIncomingFolder(), name);
		File target = new File(errorFolder, name);

		source.renameTo(target);
		source.delete();
	}
}
