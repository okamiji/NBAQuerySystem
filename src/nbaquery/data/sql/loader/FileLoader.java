package nbaquery.data.sql.loader;

import java.io.File;

public interface FileLoader
{
	public void setRoot(File root);
	
	public void load(File aFile) throws Exception;
}
