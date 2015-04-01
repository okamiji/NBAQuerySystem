package nbaquery.logic.gross_team;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.GroupQuery;
import nbaquery.logic.SumColumnInfo;
import nbaquery.logic.infrustructure.MatchTeamPerformance;

public class GrossTeamPerformance
{
	public TableHost tableHost;
	protected MatchTeamPerformance base;
	protected boolean shouldDoQuery = true;
	protected Table table;
	
	public GrossTeamPerformance(TableHost tableHost, MatchTeamPerformance base)
	{
		this.tableHost = tableHost;
		this.base = base;
	}
	
	public Table getTable()
	{
		if(shouldDoQuery)
		{
			GroupQuery groupQuery = new GroupQuery(base.getTable(), new String[]{"match_season", "team_name_abbr"},
					new SumColumnInfo("total_game_time", "total_game_time"),
					new SumColumnInfo("foul_shoot_sum", "foul_shoot_sum"),
					new SumColumnInfo("foul_shoot_score_sum", "foul_shoot_score_sum"),
					new SumColumnInfo("shoot_sum", "shoot_sum"),
					new SumColumnInfo("shoot_score_sum", "shoot_score_sum"),
					new SumColumnInfo("three_shoot_sum", "three_shoot_sum"),
					new SumColumnInfo("three_shoot_score_sum", "three_shoot_score_sum"),
					new SumColumnInfo("attack_board_sum", "attack_board_sum"),
					new SumColumnInfo("defence_board_sum", "defence_board_sum"),
					new SumColumnInfo("total_board_sum", "total_board_sum"),
					new SumColumnInfo("assist_sum", "assist_sum"),
					new SumColumnInfo("steal_sum", "steal_sum"),
					new SumColumnInfo("cap_sum", "cap_sum"),
					new SumColumnInfo("foul_sum", "foul_sum"),
					new SumColumnInfo("miss_sum", "miss_sum"),
					new SumColumnInfo("self_score_sum", "self_score_sum"),
					new SumColumnInfo("win_sum", "win"),
					new SumColumnInfo("game_sum", "game")
			);
			tableHost.performQuery(groupQuery, "gross_team_performance");
			table = tableHost.getTable("gross_team_performance");
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
		this.tableHost.deleteTable("gross_team_performance");
		this.base.destroy();
	}
}
