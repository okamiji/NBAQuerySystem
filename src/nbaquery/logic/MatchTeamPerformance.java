package nbaquery.logic;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.GroupColumnInfo;
import nbaquery.data.query.GroupQuery;

public class MatchTeamPerformance
{
	public TableHost tableHost;
	protected GroupQuery groupQuery; 
	protected boolean shouldDoQuery = true;
	protected Table table;
	public MatchNaturalJoinPerformance base;
	
	public MatchTeamPerformance(TableHost tableHost, MatchNaturalJoinPerformance base)
	{
		this.tableHost = tableHost;
		this.base = base;
		this.groupQuery = new GroupQuery(null, new String[]{"match_id", "match_season", "team_name_abbr", "match_host_abbr", "match_guest_abbr", "match_host_score", "match_guest_score"},
				new GroupColumnInfo("total_game_time", Integer.class)
		{
			Column game_time_minute;
			Column game_time_second;
			
			@Override
			public void retrieve(Table originalTable, Table resultTable)
			{
				game_time_minute = originalTable.getColumn("game_time_minute");
				game_time_second = originalTable.getColumn("game_time_second");
			}

			@Override
			public void collapse(Row[] rows, Row resultRow)
			{
				Integer result = 0;
				for(Row row : rows)
				{
					Integer minute = (Integer) game_time_minute.getAttribute(row);
					if(minute != null) result += minute * 60;
					
					Integer second = (Integer) game_time_second.getAttribute(row);
					if(second != null) result += second; 
				}
				getGroupColumn().setAttribute(resultRow, result);
			}
		}
		,		new SumColumnInfo("shoot_sum", "shoot_count")
		,		new SumColumnInfo("shoot_score_sum", "shoot_score")
		,		new SumColumnInfo("three_shoot_sum", "three_shoot_count")
		,		new SumColumnInfo("three_shoot_score_sum", "three_shoot_score")
		,		new SumColumnInfo("foul_shoot_sum", "foul_shoot_count")
		,		new SumColumnInfo("foul_shoot_score_sum", "foul_shoot_score")
		,		new SumColumnInfo("attack_board_sum", "attack_board")
		,		new SumColumnInfo("defence_board_sum", "defence_board")
		,		new SumColumnInfo("total_board_sum", "total_board")
		,		new SumColumnInfo("defence_board_sum", "defence_board")
		,		new SumColumnInfo("assist_sum", "assist")
		,		new SumColumnInfo("steal_sum", "steal")
		,		new SumColumnInfo("cap_sum", "cap")
		,		new SumColumnInfo("foul_sum", "foul")
		);
	}
	
	public Table getTable()
	{
		if(shouldDoQuery)
		{
			groupQuery.table = base.getTable();
			tableHost.performQuery(groupQuery, "match_team_performance");
			table = tableHost.getTable("match_team_performance");
			shouldDoQuery = false;
		}
		return table;
	}
	
	public void markDirty()
	{
		this.shouldDoQuery = true;
	}
}