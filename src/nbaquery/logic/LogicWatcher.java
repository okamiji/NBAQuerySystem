package nbaquery.logic;

import nbaquery.data.Table;

public class LogicWatcher
{
	protected Table watchingTable = null;
	
	protected LogicPipeline dependency;
	
	public LogicWatcher(LogicPipeline dependency)
	{
		this.dependency = dependency;
	}
	
	public boolean checkDepenency()
	{
		Table table = dependency.getTable();
		if(table != watchingTable)
		{
			watchingTable = table;
			table.hasTableChanged();
			return true;
		}
		else return table.hasTableChanged();
	}
	
	public Table getTable()
	{
		return this.watchingTable;
	}
}
