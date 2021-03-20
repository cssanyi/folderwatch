package folderwatch;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "FolderWatch", version = "FolderWatch 1.0.0", mixinStandardHelpOptions = true)
public class FolderWatch implements Runnable {

	@Option(names = { "-i" }, required = true, description = "Absolute or relative folder path for incoming files")
	String incomingFolderPath = "";

	@Option(names = { "-n" }, description = "Word pairs are included in the result above this occurence threshold (3)")
	int wordPairOccurenceThreshold = 3;

	@Option(names = { "-t" }, description = "Thread count (1)")
	int threadCount = 1;

	@Option(names = { "-d" }, description = "Delay in seconds to wait for files idle before exit (60)")
	int exitDelay = 60;

	@Override
	public void run() {
		System.out.println("hello: " + incomingFolderPath);
	}

	public static void main(String[] args) {
			new CommandLine(new FolderWatch()).execute(args);
	}

}
