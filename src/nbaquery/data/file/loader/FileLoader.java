package nbaquery.data.file.loader;

import java.io.File;

public interface FileLoader
{
	public void setRoot(File root);
	
	public void load(File aFile) throws Exception;
}
