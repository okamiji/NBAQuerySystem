package nbaquery.logic.infrustructure;

import nbaquery.data.Table;
import nbaquery.data.TableHost;

public class DirectMatchNaturalJoinPerformance extends MatchNaturalJoinPerformance
{
	
	public DirectMatchNaturalJoinPerformance(TableHost tableHost)
	{
		super(tableHost);
	}
	
	public Table getTable()
	{
		Table theTable = tableHost.getTable("match_natural_join_performance");
		if(theTable == null) throw new Error();
		return theTable;
	}
	
	public void markDirty()
	{
		
	}
	
	public void destroy()
	{
		
	}
}
