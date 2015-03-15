package nbaquery.data.query;

import nbaquery.data.Row;
import nbaquery.data.Table;

public abstract class DeriveQuery implements Query
{
	public Table table;
	
	public String[] projectColumns;
	
	public String[] deriveColumns;
	public Class<?>[] deriveClasses;
	
	public void retrieve(Table resultTable)
	{
		
	}
	
	public abstract void derive(Row row);
	
	public DeriveQuery(Table table, String[] deriveColumns, Class<?>[] deriveClasses, String... projectColumns)
	{
		this.table = table;
		this.deriveColumns = deriveColumns;
		this.deriveClasses = deriveClasses;
		this.projectColumns = projectColumns;
	}
	
	public DeriveQuery(Table table, DeriveColumnInfo[] deriveColumnInfos, String... projectColumns)
	{
		this.table = table;
		this.deriveColumns = new String[deriveColumnInfos.length];
		this.deriveClasses = new Class<?>[deriveColumnInfos.length];
		for(int i = 0; i < deriveColumnInfos.length; i ++)
		{
			this.deriveColumns[i] = deriveColumnInfos[i].deriveColumns;
			this.deriveClasses[i] = deriveColumnInfos[i].deriveClasses;
		}
		this.projectColumns = projectColumns;
	}
}
