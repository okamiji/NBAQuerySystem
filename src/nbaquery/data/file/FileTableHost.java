package nbaquery.data.file;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.file.loader.FileLoader;
import nbaquery.data.file.loader.MatchLoader;
import nbaquery.data.file.loader.PlayerLoader;
import nbaquery.data.file.loader.TeamLoader;
import nbaquery.data.file.query.DeriveAlgorithm;
import nbaquery.data.file.query.FileTableAlgorithm;
import nbaquery.data.file.query.GroupAlgorithm;
import nbaquery.data.file.query.JoinAlgorithm;
import nbaquery.data.file.query.NaturalJoinAlgorithm;
import nbaquery.data.file.query.SelectProjectAlgorithm;
import nbaquery.data.file.query.SetOperationAlgorithm;
import nbaquery.data.file.query.SortAlgorithm;
import nbaquery.data.query.Query;

public class FileTableHost implements TableHost
{
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

	protected TreeMap<String, Table> tables = new TreeMap<String, Table>();
	protected TreeSet<String> protectedTable = new TreeSet<String>();
	
	@Override
	public Table getTable(String tableName)
	{
		return tables.get(tableName.toUpperCase());
	}
	
	public FileTableHost(File root)
	{
		this(root, new Class<?>[]{MatchLoader.class, PlayerLoader.class, TeamLoader.class});
	}
	
	public FileTableHost(File root, Class<?>[] loaders)
	{
		this(root, loaders, new Class<?>[]{SetOperationAlgorithm.class, SelectProjectAlgorithm.class, DeriveAlgorithm.class, JoinAlgorithm.class, SortAlgorithm.class, GroupAlgorithm.class, NaturalJoinAlgorithm.class});
	}
	
	public FileTableHost(final File root, Class<?>[] loaderClasses, Class<?>[] queryAlgorithmClasses)
	{
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
						loader.setRoot(root);
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
	
	public void makeProtectedTable(String tableName, Table theTable)
	{
		protectedTable.add(tableName.toUpperCase());
		tables.put(tableName.toUpperCase(), theTable);
	}
	
	public Table getTableFromPreset(EnumTable enumTable)
	{
		Table theTable = null;
		if(enumTable.getPrimaryKey() != null) theTable = new KeywordTable(this, enumTable.getTableAttributes(), enumTable.getDataClasses(), enumTable.getPrimaryKey().toString().toUpperCase());
		else theTable = new MultivaluedTable(this, enumTable.getTableAttributes(), enumTable.getDataClasses());
		
		return theTable;
	}
}
