package nbaquery.logic.average_team;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.logic.TeamDeriveQuery;
import nbaquery.logic.infrustructure.RivalTeamNaturalJoin;

public class DerivedTeamPerformance
{
	public TableHost tableHost; 
	protected boolean shouldDoQuery = true;
	protected Table table;
	protected RivalTeamNaturalJoin joined;
	
	public DerivedTeamPerformance(TableHost tableHost, RivalTeamNaturalJoin joined)
	{
		this.tableHost = tableHost;
		this.joined = joined;
	}
	
	public Table getTable()
	{
		if(shouldDoQuery)
		{
			TeamDeriveQuery query = new TeamDeriveQuery(joined.getTable()); 
			tableHost.performQuery(query, "derived_team_performance");
			table = tableHost.getTable("derived_team_performance");
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
		this.tableHost.deleteTable("derived_team_performance");
		
		this.joined.destroy();
	}
}
