package nbaquery.logic.average_team;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.logic.LogicPipeline;
import nbaquery.logic.LogicWatcher;
import nbaquery.logic.TeamDeriveQuery;
import nbaquery.logic.infrustructure.RivalTeamNaturalJoin;

public class DerivedTeamPerformance implements LogicPipeline
{
	public TableHost tableHost; 
	protected boolean shouldDoQuery = true;
	protected Table table;
	protected LogicWatcher joined;
	
	public DerivedTeamPerformance(TableHost tableHost, RivalTeamNaturalJoin joined)
	{
		this.tableHost = tableHost;
		this.joined = new LogicWatcher(joined);
	}
	
	public Table getTable()
	{
		if(joined.checkDepenency())
		{
			TeamDeriveQuery query = new TeamDeriveQuery(joined.getTable()); 
			tableHost.performQuery(query, "derived_team_performance");
			table = tableHost.getTable("derived_team_performance");
		}
		return table;
	}
}
