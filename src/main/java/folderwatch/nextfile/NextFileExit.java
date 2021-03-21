package folderwatch.nextfile;

public class NextFileExit implements NextFile {

	@Override
	public boolean processable() {
		return false;
	}

	@Override
	public NextFile process() {
		return null;
	}

}
