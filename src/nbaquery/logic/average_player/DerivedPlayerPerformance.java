package nbaquery.logic.average_player;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.logic.PlayerDeriveQuery;
import nbaquery.logic.infrustructure.PlayerPerformance;

public class DerivedPlayerPerformance
{
	public TableHost tableHost; 
	protected boolean shouldDoQuery = true;
	protected Table table;
	protected PlayerPerformance performance;
	
	public DerivedPlayerPerformance(TableHost tableHost, PlayerPerformance performance)
	{
		this.tableHost = tableHost;
		this.performance = performance;
	}
	
	public Table getTable()
	{
		if(shouldDoQuery)
		{
			PlayerDeriveQuery query = new PlayerDeriveQuery(performance.getTable()); 
			tableHost.performQuery(query, "derived_player_performance");
			table = tableHost.getTable("derived_player_performance");
			shouldDoQuery = false;
		}
		return table;
	}
	
	public void markDirty()
	{
		this.shouldDoQuery = true;
	}
	
	public void destroy()
	{
		this.markDirty();
		this.tableHost.deleteTable("derived_player_performance");
		
		this.performance.destroy();
	}
}
