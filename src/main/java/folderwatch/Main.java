package folderwatch;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import folderwatch.FolderWatcher.FolderWatcherBuilder;
import folderwatch.nextfile.factory.NextFileFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;

@Command(name = "FolderWatch", version = "FolderWatch 1.0.0", mixinStandardHelpOptions = true)
public class Main implements Runnable {

	private String incomingFolderPath;
	int wordPairOccurenceThreshold = 3;
	int threadCount = 1;
	int exitDelay = 60;

	@Spec
	CommandSpec spec;

	@Option(names = { "-i" }, required = true, description = "Absolute or relative folder path for incoming files")
	public void setIncomingFolderPath(String value) {
		if (!isPathValid(value)) {
			throw new ParameterException(spec.commandLine(),
					String.format("Invalid value '%s' for option '-i'.", value));
		}
		incomingFolderPath = value;
	}

	@Option(names = { "-n" }, description = "Word pairs are included in the result above this occurence threshold (3)")
	public void setWordPairOccurenceThreshold(int value) {
		if (value < 0) {
			throw new ParameterException(spec.commandLine(),
					String.format("Invalid value '%d' for option '-n': " + "value should not be negative.", value));
		}
		wordPairOccurenceThreshold = value;
	}

	@Option(names = { "-t" }, description = "Thread count (1)")
	public void setThreadCount(int value) {
		if (value < 1 || value > 99) {
			throw new ParameterException(spec.commandLine(), String.format(
					"Invalid value '%d' for option '-t': " + "value should be between 1 and 99 inclusive.", value));
		}
		threadCount = value;
	}

	@Option(names = { "-d" }, description = "Delay in seconds to wait for files idle before exit (60)")
	public void setExitDelay(int value) {
		if (value < 0) {
			throw new ParameterException(spec.commandLine(), String.format(
					String.format("Invalid value '%d' for option '-d': " + "value should not be negative.", value)));
		}
		exitDelay = value;
	}

	private static boolean isPathValid(String path) {
		try {
			Paths.get(path);
		} catch (InvalidPathException ex) {
			return false;
		}
		return true;
	}

	@Override
	public void run() {
		FolderWatcher folderWatcher = new FolderWatcherBuilder().withIncomingFolderPath(incomingFolderPath)
				.withNextFileFactory(NextFileFactory.create(wordPairOccurenceThreshold))
				.withExit(Exit.create(exitDelay)).build();

		IntStream.rangeClosed(2, threadCount).forEach(i -> FolderWatch.create(folderWatcher).start());

		FolderWatch.create(folderWatcher).run();
	}

	public static void main(String[] args) {
		new CommandLine(new Main()).execute(args);
	}

}
