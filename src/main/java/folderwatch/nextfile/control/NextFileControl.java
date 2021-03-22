package folderwatch.nextfile.control;

public interface NextFileControl {

	default boolean processable() {
		return true;
	}

	NextFileControl process();

}
