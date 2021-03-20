package folderwatch;

import folderwatch.nextfile.factory.NextFileFactory;

public class FolderWatcher {

	private String incomingFolderPath;
	private NextFileFactory nextFileFactory;
	private Exit exit;

	private FolderWatcher() {
	}

	public static class FolderWatcherBuilder {
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

			folderWatcher.incomingFolderPath = this.incomingFolderPath;
			folderWatcher.nextFileFactory = this.nextFileFactory;
			folderWatcher.exit = this.exit;

			return folderWatcher;
		}
	}
}
