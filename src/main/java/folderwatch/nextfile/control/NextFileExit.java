package folderwatch.nextfile.control;

public class NextFileExit implements NextFileControl {

	@Override
	public boolean processable() {
		return false;
	}

	@Override
	public NextFileControl process() {
		return null;
	}

}
