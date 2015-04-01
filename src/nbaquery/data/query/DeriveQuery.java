package nbaquery.data.query;

import nbaquery.data.Row;
import nbaquery.data.Table;

public class DeriveQuery implements Query
{
	public Table table;
	
	public String[] projectColumns;
	
	public String[] deriveColumns;
	public Class<?>[] deriveClasses;
	
	protected DeriveColumnInfo[] deriveColumnInfos;
	
	public void retrieve(Table resultTable)
	{
		for(int i = 0; i < deriveColumnInfos.length; i ++)
		{
			deriveColumnInfos[i].resultColumn = resultTable.getColumn(deriveColumns[i]);
			deriveColumnInfos[i].retrieve(resultTable);
		}
	}
	
	public void derive(Row row)
	{
		for(int i = 0; i < deriveColumnInfos.length; i ++)
			deriveColumnInfos[i].derive(row);
	}
	
	public DeriveQuery(Table table, DeriveColumnInfo[] deriveColumnInfos, String... projectColumns)
	{
		this.table = table;
		this.deriveColumns = new String[deriveColumnInfos.length];
		this.deriveClasses = new Class<?>[deriveColumnInfos.length];
		for(int i = 0; i < deriveColumnInfos.length; i ++)
		{
			this.deriveColumns[i] = deriveColumnInfos[i].deriveColumn;
			this.deriveClasses[i] = deriveColumnInfos[i].deriveClass;
		}
		this.projectColumns = projectColumns;
		this.deriveColumnInfos = deriveColumnInfos;
	}
}
