package nbaquery.logic.gross_team;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.GroupQuery;
import nbaquery.logic.SumColumnInfo;
import nbaquery.logic.infrustructure.RivalTeamPerformance;

public class GrossRivalPerformance
{
	public TableHost tableHost;
	protected RivalTeamPerformance base;
	protected boolean shouldDoQuery = true;
	protected Table table;
	
	public GrossRivalPerformance(TableHost tableHost, RivalTeamPerformance base)
	{
		this.tableHost = tableHost;
		this.base = base;
	}
	
	public Table getTable()
	{
		if(shouldDoQuery)
		{
			GroupQuery groupQuery = new GroupQuery(base.getTable(), new String[]{"match_season", "current_name_abbr"},
					new SumColumnInfo("rival_attack_board_sum", "rival_attack_board_sum"),
					new SumColumnInfo("rival_defence_board_sum", "rival_defence_board_sum"),
					new SumColumnInfo("rival_shoot_score_sum", "rival_shoot_score_sum"),
					new SumColumnInfo("rival_three_shoot_score_sum", "rival_three_shoot_score_sum"),
					new SumColumnInfo("rival_foul_shoot_score_sum", "rival_foul_shoot_score_sum"),
					new SumColumnInfo("rival_shoot_sum", "rival_shoot_sum"),
					new SumColumnInfo("rival_three_shoot_sum", "rival_three_shoot_sum"),
					new SumColumnInfo("rival_foul_shoot_sum", "rival_foul_shoot_sum"),
					new SumColumnInfo("rival_miss_sum", "rival_miss_sum"),
					new SumColumnInfo("rival_self_score_sum", "rival_self_score_sum")
			);
			tableHost.performQuery(groupQuery, "gross_rival_performance");
			table = tableHost.getTable("gross_rival_performance");
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
		this.tableHost.deleteTable("gross_rival_performance");
		this.base.destroy();
	}
}
