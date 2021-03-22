package folderwatch.nextfile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import folderwatch.FolderWatcher;
import folderwatch.nextfile.control.NextFileControl;

public class NextFileText implements NextFile {

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
	public NextFileControl process() {
		try (BufferedReader reader = new BufferedReader(
				new FileReader(new File(this.getErrorFolder(), this.name), Charset.forName("UTF-8")));
				PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(
						new File(this.getDoneFolder(), name.substring(0, name.lastIndexOf(".")) + "-pairs.txt"),
						Charset.forName("UTF-8"))))) {

			Map<String, AtomicInteger> pairFrequency = collectPairFrequency(reader);

			Function<? super Entry<String, AtomicInteger>, ? extends StringBuilder> mapper = e -> new StringBuilder()
					.append(e.getKey().replace('-', ' ')).append(" : ").append(e.getValue().get());

			pairFrequency.entrySet().stream().filter(e -> e.getValue().get() > this.wordPairOccurenceThreshold)
					.sorted((o1, o2) -> o2.getValue().get() - o1.getValue().get()).map(mapper)
					.forEach(line -> writer.println(line));

			reader.close();
			folderWatcher.moveFile(this.getErrorFolder(), this.getDoneFolder(), name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return folderWatcher.getNextFile();
	}

	private Map<String, AtomicInteger> collectPairFrequency(BufferedReader reader) throws IOException {
		Map<String, AtomicInteger> pairFrequency = new HashMap<>();
		StringBuilder word = new StringBuilder();

		String current = null;
		String previous = null;

		int r;
		while ((r = reader.read()) != -1) {
			char ch = (char) r;

			if (Character.isLetter(ch)) {
				word.append(ch);

			} else if (!word.isEmpty()) {
				current = word.toString();
				word = new StringBuilder();

				if (previous != null) {
					StringBuilder keyBuilder = new StringBuilder().append(previous).append("-").append(current);
					String key = keyBuilder.toString().toLowerCase();
					pairFrequency.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();
				}
				previous = current;
			}
			if (word.isEmpty() && !Character.isWhitespace(ch)) {
				previous = null;
			}
		}
		return pairFrequency;
	}

}
