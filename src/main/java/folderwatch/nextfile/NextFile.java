package folderwatch.nextfile;

public interface NextFile {

	default boolean processable() {
		return true;
	}

	NextFile process();

}
