package nbaquery.data.query;

import nbaquery.data.Table;

public class AliasingQuery implements Query
{
	public final Table table;
	public final String[] columnNames;
	public final String[] aliases;
	
	public AliasingQuery(Table table, String[] columnNames, String[] aliases)
	{
		this.table = table;
		this.columnNames = columnNames;
		this.aliases = aliases;
	}
}
