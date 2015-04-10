package nbaquery.data.file.loader;

import java.io.File;
import java.util.TreeSet;

public class FileMonitor extends Thread
{
	public static long interval = 1000L;
	public static boolean isSystemRunning = true;
	
	protected TreeSet<String> loadedFile = new TreeSet<String>();
	
	protected File root;
	protected FileLoader loader;
	
	public FileMonitor(File root, FileLoader loader)
	{
		this.root = root;
		this.loader = loader;
	}
	
	public void run()
	{
		while(isSystemRunning) try
		{
			File[] files = root.listFiles();
			if(files != null) for(File file : files)
			{
				String fileName = file.getName();
				if(!loadedFile.contains(fileName))
				{
					loader.load(file);
					loadedFile.add(fileName);
				}
			}
			Thread.sleep(interval);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
