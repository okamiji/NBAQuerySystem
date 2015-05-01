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
			table.hasTableChanged(this);
			return true;
		}
		else if(table == null) return true;
		else return table.hasTableChanged(this);
	}
	
	public Table getTable()
	{
		return this.watchingTable;
	}
}
