package nbaquery.logic.average_player;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.logic.LogicPipeline;
import nbaquery.logic.LogicWatcher;
import nbaquery.logic.PlayerDeriveQuery;
import nbaquery.logic.infrustructure.PlayerPerformance;

public class DerivedPlayerPerformance implements LogicPipeline
{
	public TableHost tableHost; 
	protected Table table;
	protected LogicWatcher performance;
	
	public DerivedPlayerPerformance(TableHost tableHost, PlayerPerformance performance)
	{
		this.tableHost = tableHost;
		this.performance = new LogicWatcher(performance);
	}
	
	public Table getTable()
	{
		if(performance.checkDepenency())
		{
			PlayerDeriveQuery query = new PlayerDeriveQuery(performance.getTable()); 
			tableHost.performQuery(query, "derived_player_performance");
			table = tableHost.getTable("derived_player_performance");
		}
		return table;
	}
}
