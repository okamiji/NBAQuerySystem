package nbaquery.data.sql.loader.file;

import java.io.File;

public interface SqlFileLoader
{
	public void setRoot(File root) throws Exception;
	
	public void load(File aFile) throws Exception;
	
	public String getLoaderName();
}
