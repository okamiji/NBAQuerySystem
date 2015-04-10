package nbaquery.logic.infrustructure;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.logic.LogicPipeline;

public class PlayerPerformance implements LogicPipeline
{
	public TableHost tableHost; 
	protected boolean shouldDoQuery = true;
	protected Table table;
	protected MatchTeamPerformance team;
	protected RivalTeamPerformance rival;
	protected MatchNaturalJoinPerformance player;
	
	public PlayerPerformance(TableHost tableHost, MatchNaturalJoinPerformance player, MatchTeamPerformance team, RivalTeamPerformance rival)
	{
		this.tableHost = tableHost;
		this.team = team;
		this.rival = rival;
		this.player = player;
	}
	
	public Table getTable()
	{
		if(shouldDoQuery)
		{
			NaturalJoinQuery joinQuery = new NaturalJoinQuery(team.getTable(), rival.getTable(), new String[]{"match_id", "team_name_abbr"}, new String[]{"match_id", "current_name_abbr"});
			tableHost.performQuery(joinQuery, "player_performance");
			table = tableHost.getTable("player_performance");
			
			joinQuery = new NaturalJoinQuery(player.getTable(), table, new String[]{"match_id", "team_name_abbr"}, new String[]{"match_id", "team_name_abbr"});
			tableHost.performQuery(joinQuery, "player_performance");
			table = tableHost.getTable("player_performance");
			
			shouldDoQuery = false;
		}
		return table;
	}
}
