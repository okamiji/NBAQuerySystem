package nbaquery.data.sql.loader.file;

import java.io.File;
import java.util.TreeSet;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.sql.MutableSqlRow;
import nbaquery.data.sql.MutableSqlTable;
import nbaquery.data.sql.SqlTableHost;

public class SqlFileMonitor extends Thread
{
	public static long interval = 1000L;
	public static boolean isSystemRunning = true;
	
	protected File root;
	protected SqlFileLoader loader;
	
	protected TreeSet<String> loadedFile = new TreeSet<String>();
	protected MutableSqlTable traceTable;
	protected Column file_name;
	
	public SqlFileMonitor(File root, SqlFileLoader loader, SqlTableHost host) throws Exception
	{
		this.root = root;
		this.loader = loader;
		traceTable = new MutableSqlTable(host, "file_trace_".concat(loader.getLoaderName()), new String[]{"file_name"},
				new Class<?>[]{String.class}, new String[]{"char(64)"}, "file_name");
		file_name = traceTable.getColumn("file_name");
		for(Row row : traceTable) loadedFile.add((String) file_name.getAttribute(row));
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
					MutableSqlRow row = traceTable.createRow();
					this.file_name.setAttribute(row, fileName);
					row.submit();
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
