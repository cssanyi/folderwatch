package folderwatch.nextfile;

import java.io.File;

import folderwatch.nextfile.control.NextFileControl;

public interface NextFile extends NextFileControl {

	String getName();

	File getErrorFolder();

	File getDoneFolder();

}
