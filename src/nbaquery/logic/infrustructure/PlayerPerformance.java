package nbaquery.logic.infrustructure;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.NaturalJoinQuery;

public class PlayerPerformance
{
	public TableHost tableHost; 
	protected boolean shouldDoQuery = true;
	protected Table table;
	protected RivalTeamNaturalJoin joined;
	protected MatchNaturalJoinPerformance match;
	
	public PlayerPerformance(TableHost tableHost, RivalTeamNaturalJoin joined, MatchNaturalJoinPerformance match)
	{
		this.tableHost = tableHost;
		this.joined = joined;
		this.match = match;
	}
	
	public Table getTable()
	{
		if(shouldDoQuery)
		{
			NaturalJoinQuery joinQuery = new NaturalJoinQuery(match.getTable(), joined.getTable(), new String[]{"match_id", "team_name_abbr"}, new String[]{"match_id", "team_name_abbr"});
			tableHost.performQuery(joinQuery, "player_performance");
			table = tableHost.getTable("player_performance");
			
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
		this.tableHost.deleteTable("player_performance");
		
		this.joined.destroy();
	}
}
