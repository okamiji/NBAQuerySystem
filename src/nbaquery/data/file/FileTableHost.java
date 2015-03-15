package nbaquery.data.file;

import java.io.File;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import nbaquery.data.DirtyDataInfo;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.file.loader.FileLoader;
import nbaquery.data.file.loader.MatchLoader;
import nbaquery.data.file.loader.PlayerLoader;
import nbaquery.data.file.loader.TeamLoader;
import nbaquery.data.file.query.DeriveAlgorithm;
import nbaquery.data.file.query.FileTableAlgorithm;
import nbaquery.data.file.query.SelectProjectAlgorithm;
import nbaquery.data.file.query.SetOperationAlgorithm;
import nbaquery.data.query.Query;

public class FileTableHost implements TableHost
{
	protected Deque<DirtyDataInfo> dirtyData = new LinkedList<DirtyDataInfo>();
	protected Map<Class<? extends Query>, FileTableAlgorithm> queryAlgorithm = new HashMap<Class<? extends Query>, FileTableAlgorithm>();
	
	@Override
	public void performQuery(Query query, String tableName)
	{
		if(protectedTable.contains(tableName.toUpperCase()))
			throw new IllegalArgumentException("You should never try to replace the protected tables in the host!");
		FileTableAlgorithm algorithm;
		Class<?> specificClass = query.getClass();
		while((algorithm = queryAlgorithm.get(specificClass)) == null)
		{
			specificClass = specificClass.getSuperclass();
			if(!Query.class.isAssignableFrom(specificClass)) return;
		}
		
		tables.put(tableName.toUpperCase(), algorithm.perform(query));
	}

	@Override
	public DirtyDataInfo nextDirtyDataInfo()
	{
		return dirtyData.removeFirst();
	}
	
	public void processDirtyData(Row tuple, FileTableColumn column, Object value)
	{
		DirtyDataInfo dirtyDataInfo = new DirtyDataInfo();
		dirtyDataInfo.column = column;
		dirtyDataInfo.data = value;
		dirtyDataInfo.table = column.table;
		dirtyDataInfo.row = tuple;

		this.dirtyData.addLast(dirtyDataInfo);
	}

	protected TreeMap<String, Table> tables = new TreeMap<String, Table>();
	protected TreeSet<String> protectedTable = new TreeSet<String>();
	
	@Override
	public Table getTable(String tableName)
	{
		return tables.get(tableName.toUpperCase());
	}
	
	public FileTableHost(File root)
	{
		this(root, new Class<?>[]{MatchLoader.class, PlayerLoader.class, TeamLoader.class},
				new Class<?>[]{SetOperationAlgorithm.class, SelectProjectAlgorithm.class, DeriveAlgorithm.class});
	}
	
	public FileTableHost(final File root, Class<?>[] loaderClasses, Class<?>[] queryAlgorithmClasses)
	{
		for(EnumTable enumTable : EnumTable.values())
		{
			Table theTable = null;
			if(enumTable.getPrimaryKey() != null) theTable = new KeywordTable(this, enumTable.getTableAttributes(), enumTable.getDataClasses(), enumTable.getPrimaryKey().toString().toUpperCase());
			else theTable = new MultivaluedTable(this, enumTable.getTableAttributes(), enumTable.getDataClasses());
			
			protectedTable.add(enumTable.toString().toUpperCase());
			tables.put(enumTable.toString().toUpperCase(), theTable);
		}
		for(Class<?> loaderClass : loaderClasses)
		{
			if(!FileLoader.class.isAssignableFrom(loaderClass)) continue;
			final FileLoader loader;
			try
			{
				loader = (FileLoader) loaderClass.getConstructor(FileTableHost.class).newInstance(this);
				Thread runningThread = new Thread()
				{
					
					public void run()
					{
						loader.load(root);
					}
				};
				runningThread.start();
				runningThread.join();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				continue;
			}
		}
		
		for(Class<?> queryAlgorithmClass : queryAlgorithmClasses)
		{
			if(!FileTableAlgorithm.class.isAssignableFrom(queryAlgorithmClass)) continue;
			try
			{
				FileTableAlgorithm algorithmObject = (FileTableAlgorithm) queryAlgorithmClass.getConstructor().newInstance();
				this.queryAlgorithm.put(algorithmObject.getProcessingQuery(), algorithmObject);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				continue;
			}
		}
	}
	
	public FileTableColumn getColumn(String name)
	{
		String[] splitted = name.split("[.]");
		if(splitted.length == 2)
		{
			Table table = this.tables.get(splitted[0].toUpperCase());
			if(table == null) return null;
			return (FileTableColumn)table.getColumn(splitted[1].toUpperCase());
		}
		else
		{
			for(Table table : this.tables.values())
			{
				FileTableColumn column = (FileTableColumn) table.getColumn(splitted[0].toUpperCase());
				if(column != null) return column;
			}
			return null;
		}
	}

	@Override
	public void deleteTable(String tableName)
	{
		if(protectedTable.contains(tableName.toUpperCase()))
			throw new IllegalArgumentException("You should never try to delete a protected table in the host!");
		else this.tables.remove(tableName.toUpperCase());
	}
}
