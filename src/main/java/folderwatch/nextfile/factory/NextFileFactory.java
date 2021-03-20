package folderwatch.nextfile.factory;

public class NextFileFactory {

	private static NextFileFactory instance = new NextFileFactory();

	private static int wordPairOccurenceThreshold;

	private NextFileFactory() {
	}

	public static NextFileFactory create(int wordPairOccurenceThreshold) {
		NextFileFactory.wordPairOccurenceThreshold = wordPairOccurenceThreshold;

		return instance;
	}

}
