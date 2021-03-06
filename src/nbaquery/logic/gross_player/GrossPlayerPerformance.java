package nbaquery.logic.gross_player;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.GroupColumnInfo;
import nbaquery.data.query.GroupQuery;
import nbaquery.logic.LogicPipeline;
import nbaquery.logic.LogicWatcher;
import nbaquery.logic.SumColumnInfo;
import nbaquery.logic.infrustructure.PlayerPerformance;

public class GrossPlayerPerformance implements LogicPipeline
{
	public TableHost tableHost;
	protected LogicWatcher base;
	protected Table table;
	
	public GrossPlayerPerformance(TableHost tableHost, PlayerPerformance base)
	{
		this.tableHost = tableHost;
		this.base = new LogicWatcher(base);
	}
	
	public Table getTable()
	{
		if(base.checkDepenency())
		{
			GroupQuery groupQuery = new GroupQuery(base.getTable(), new String[]{"match_season", "player_name", "team_name_abbr"},
					new SumColumnInfo("foul_shoot_count", "foul_shoot_count"),
					new SumColumnInfo("foul_shoot_sum", "foul_shoot_sum"),
					new SumColumnInfo("foul_shoot_score", "foul_shoot_score"),
					new SumColumnInfo("foul_shoot_score_sum", "foul_shoot_score_sum"),
					new SumColumnInfo("shoot_count", "shoot_count"),
					new SumColumnInfo("shoot_sum", "shoot_sum"),
					new SumColumnInfo("shoot_score", "shoot_score"),
					new SumColumnInfo("shoot_score_sum", "shoot_score_sum"),
					new SumColumnInfo("three_shoot_count", "three_shoot_count"),
					new SumColumnInfo("three_shoot_sum", "three_shoot_sum"),
					new SumColumnInfo("three_shoot_score", "three_shoot_score"),
					new SumColumnInfo("three_shoot_score_sum", "three_shoot_score_sum"),
					new SumColumnInfo("attack_board", "attack_board"),
					new SumColumnInfo("defence_board", "defence_board"),
					new SumColumnInfo("total_board", "total_board"),
					new SumColumnInfo("assist", "assist"),
					new SumColumnInfo("steal", "steal"),
					new SumColumnInfo("cap", "cap"),
					new SumColumnInfo("foul", "foul"),
					new SumColumnInfo("miss", "miss"),
					new SumColumnInfo("miss_sum", "miss_sum"),
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
					new SumColumnInfo("game_time_minute", "game_time_minute"),
					new SumColumnInfo("game_time_second", "game_time_second"),
					new SumColumnInfo("total_game_time", "total_game_time"),
					new SumColumnInfo("total_board_sum", "total_board_sum"),
					new SumColumnInfo("rival_total_board_sum", "rival_total_board_sum"),
					new SumColumnInfo("rival_attack_board_sum", "rival_attack_board_sum"),
					new SumColumnInfo("rival_shoot_sum", "rival_shoot_sum")
			);
			tableHost.performQuery(groupQuery, "gross_player_performance");
			table = tableHost.getTable("gross_player_performance");
		}
		return table;
	}
}
