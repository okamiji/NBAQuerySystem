package nbaquery.logic;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.query.DeriveColumnInfo;
import nbaquery.data.query.DeriveQuery;

public class PlayerDeriveQuery extends DeriveQuery
{

	public PlayerDeriveQuery(Table table)
	{
		super(table, new DeriveColumnInfo[]{
				
			new DeriveColumnInfo("efficiency", Integer.class)
			{
				Column self_score;
				Column total_board;
				Column assist;
				Column steal;
				Column cap;
				Column three_shoot;
				Column three_shoot_score;
				Column shoot;
				Column shoot_score;
				Column foul_shoot;
				Column foul_shoot_score;
				Column miss;
				
				@Override
				public void retrieve(Table resultTable)
				{
					self_score = resultTable.getColumn("self_score");
					total_board = resultTable.getColumn("total_board");
					assist = resultTable.getColumn("assist");
					steal = resultTable.getColumn("steal");
					cap = resultTable.getColumn("cap");
					three_shoot = resultTable.getColumn("three_shoot");
					three_shoot_score = resultTable.getColumn("three_shoot_score");
					shoot = resultTable.getColumn("shoot");
					shoot_score = resultTable.getColumn("shoot_score");
					foul_shoot = resultTable.getColumn("foul_shoot");
					foul_shoot_score = resultTable.getColumn("foul_shoot_score");
					miss = resultTable.getColumn("miss");
				}
	
				@Override
				public void derive(Row resultRow)
				{
					Integer self_score_n = (Integer) self_score.getAttribute(resultRow);
					Integer total_board_n = (Integer) total_board.getAttribute(resultRow);
					Integer assist_n = (Integer) assist.getAttribute(resultRow);
					Integer steal_n = (Integer) steal.getAttribute(resultRow);
					Integer cap_n = (Integer) cap.getAttribute(resultRow);
					Integer three_shoot_n = (Integer) three_shoot.getAttribute(resultRow);
					Integer three_shoot_score_n = (Integer) three_shoot_score.getAttribute(resultRow);
					Integer shoot_n = (Integer) shoot.getAttribute(resultRow);
					Integer shoot_score_n = (Integer) shoot_score.getAttribute(resultRow);
					Integer foul_shoot_n = (Integer) foul_shoot.getAttribute(resultRow);
					Integer foul_shoot_score_n = (Integer) foul_shoot_score.getAttribute(resultRow);
					Integer miss_n = (Integer) miss.getAttribute(resultRow);
					
					Integer efficiency = (self_score_n + total_board_n + assist_n + steal_n + cap_n)
							- ((shoot_n + three_shoot_n + foul_shoot_n) - (shoot_score_n + three_shoot_score_n + foul_shoot_score_n))
							- (foul_shoot_n - foul_shoot_score_n) - miss_n;
					getDeriveColumn().setAttribute(resultRow, efficiency);
				}
			}
		});
	}
	
}
