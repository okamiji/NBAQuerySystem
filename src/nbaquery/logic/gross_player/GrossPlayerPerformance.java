package nbaquery.logic.gross_player;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.GroupColumnInfo;
import nbaquery.data.query.GroupQuery;
import nbaquery.logic.SumColumnInfo;
import nbaquery.logic.infrustructure.PlayerPerformance;

public class GrossPlayerPerformance
{
	public TableHost tableHost;
	protected PlayerPerformance base;
	protected boolean shouldDoQuery = true;
	protected Table table;
	
	public GrossPlayerPerformance(TableHost tableHost, PlayerPerformance base)
	{
		this.tableHost = tableHost;
		this.base = base;
	}
	
	public Table getTable()
	{
		if(shouldDoQuery)
		{
			GroupQuery groupQuery = new GroupQuery(base.getTable(), new String[]{"match_season", "player_name", "team_name_abbr"},
					new SumColumnInfo("foul_shoot", "foul_shoot"),
					new SumColumnInfo("foul_shoot_score", "foul_shoot_score"),
					new SumColumnInfo("shoot", "shoot"),
					new SumColumnInfo("shoot_score", "shoot_score"),
					new SumColumnInfo("three_shoot", "three_shoot"),
					new SumColumnInfo("three_shoot_score", "three_shoot_score"),
					new SumColumnInfo("attack_board", "attack_board"),
					new SumColumnInfo("defence_board", "defence_board"),
					new SumColumnInfo("total_board", "total_board"),
					new SumColumnInfo("assist", "assist"),
					new SumColumnInfo("steal", "steal"),
					new SumColumnInfo("cap", "cap"),
					new SumColumnInfo("foul", "foul"),
					new SumColumnInfo("miss", "miss"),
					new SumColumnInfo("self_score", "self_score"),
					new GroupColumnInfo("game_count", Integer.class)
					{

						@Override
						public void retrieve(Table originalTable,
								Table resultTable)
						{
						}

						@Override
						public void collapse(Row[] rows, Row resultRow)
						{
							getGroupColumn().setAttribute(resultRow, rows.length);
						}
					},
					new GroupColumnInfo("first_count", Integer.class)
					{
						Column player_position;
						
						@Override
						public void retrieve(Table originalTable,
								Table resultTable)
						{
							player_position = originalTable.getColumn("player_position");
						}

						@Override
						public void collapse(Row[] rows, Row resultRow)
						{
							Integer first_count = 0;
							for(Row row : rows)
							{
								Character player_position_n = (Character) player_position.getAttribute(row);
								if(player_position_n == null) continue;
								if(player_position_n == '\0') continue;
								first_count ++;
							}
							getGroupColumn().setAttribute(resultRow, first_count);
						}
					},
					new GroupColumnInfo("game_time", Integer.class)
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
					},
					new SumColumnInfo("total_game_time", "total_game_time")
			);
			tableHost.performQuery(groupQuery, "gross_player_performance");
			table = tableHost.getTable("gross_player_performance");
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
		this.tableHost.deleteTable("gross_player_performance");
		this.base.destroy();
	}
}
