package nbaquery.logic.infrustructure;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.logic.LogicPipeline;
import nbaquery.logic.LogicWatcher;

public class PlayerPerformance implements LogicPipeline
{
	public TableHost tableHost; 
	protected Table table;
	protected LogicWatcher team;
	protected LogicWatcher rival;
	protected LogicWatcher player;
	
	public PlayerPerformance(TableHost tableHost, MatchNaturalJoinPerformance player, MatchTeamPerformance team, RivalTeamPerformance rival)
	{
		this.tableHost = tableHost;
		this.team = new LogicWatcher(team);
		this.rival = new LogicWatcher(rival);
		this.player = new LogicWatcher(player);
	}
	
	public Table getTable()
	{
		boolean teamChanged = team.checkDepenency();
		boolean rivalChanged = rival.checkDepenency();
		boolean playerChanged = player.checkDepenency();
		
		if(teamChanged || rivalChanged || playerChanged)
		{
			NaturalJoinQuery joinQuery = new NaturalJoinQuery(team.getTable(), rival.getTable(), new String[]{"match_id", "team_name_abbr"}, new String[]{"match_id", "current_name_abbr"});
			tableHost.performQuery(joinQuery, "player_performance_joined");
			table = tableHost.getTable("player_performance_joined");
			
			joinQuery = new NaturalJoinQuery(player.getTable(), table, new String[]{"match_id", "team_name_abbr"}, new String[]{"match_id", "team_name_abbr"});
			tableHost.performQuery(joinQuery, "player_performance");
			table = tableHost.getTable("player_performance");
		}
		return table;
	}
}
