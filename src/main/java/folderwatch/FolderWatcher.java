package folderwatch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import folderwatch.nextfile.NextFile;
import folderwatch.nextfile.control.NextFileControl;
import folderwatch.nextfile.control.NextFileExit;
import folderwatch.nextfile.control.NextFileIdle;
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

	public synchronized NextFileControl getNextFile() {
		if (exit.activated()) {
			return new NextFileExit();
		}
		for (String name : incomingFolder.list()) {
			File file = new File(this.incomingFolder, name);
			if (file.isDirectory()) {
				continue;
			}
			exit.reset();

			NextFile nextFile = nextFileFactory.getNextFile(name, this);
			moveFile(this.incomingFolder, nextFile.getErrorFolder(), name);

			return nextFile;
		}
		if (exit.check()) {
			return new NextFileExit();
		} else {
			return new NextFileIdle(this);
		}
	}

	public void moveFile(File sourceFolder, File targetFolder, String name) {
		File source = new File(sourceFolder, name);
		File target = new File(targetFolder, name);

		try {
			Files.move(source.toPath(), target.toPath(), StandardCopyOption.ATOMIC_MOVE);
		} catch (IOException e) {
			throw new RuntimeException(new StringBuilder().append("Cant move ").append(source.getAbsolutePath())
					.append(System.lineSeparator()).append(e.getLocalizedMessage()).toString());
		}
	}
}
