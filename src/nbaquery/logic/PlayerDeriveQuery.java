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
		super(table, new DeriveColumnInfo[]
		{
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
			},
			new DeriveColumnInfo("gmsc_efficiency", Float.class)
			{
				Column self_score;
				Column attack_board;
				Column defence_board;
				Column assist;
				Column steal;
				Column cap;
				Column three_shoot;
				Column three_shoot_score;
				Column shoot;
				Column shoot_score;
				Column foul;
				Column foul_shoot;
				Column foul_shoot_score;
				Column miss;
				
				@Override
				public void retrieve(Table resultTable)
				{
					self_score = resultTable.getColumn("self_score");
					attack_board = resultTable.getColumn("attack_board");
					defence_board = resultTable.getColumn("defence_board");
					assist = resultTable.getColumn("assist");
					steal = resultTable.getColumn("steal");
					cap = resultTable.getColumn("cap");
					three_shoot = resultTable.getColumn("three_shoot");
					three_shoot_score = resultTable.getColumn("three_shoot_score");
					shoot = resultTable.getColumn("shoot");
					shoot_score = resultTable.getColumn("shoot_score");
					foul = resultTable.getColumn("foul");
					foul_shoot = resultTable.getColumn("foul_shoot");
					foul_shoot_score = resultTable.getColumn("foul_shoot_score");
					miss = resultTable.getColumn("miss");
				}
	
				@Override
				public void derive(Row resultRow)
				{
					Integer self_score_n = (Integer) self_score.getAttribute(resultRow);
					Integer attack_board_n = (Integer) attack_board.getAttribute(resultRow);
					Integer defence_board_n = (Integer) defence_board.getAttribute(resultRow);
					Integer assist_n = (Integer) assist.getAttribute(resultRow);
					Integer steal_n = (Integer) steal.getAttribute(resultRow);
					Integer cap_n = (Integer) cap.getAttribute(resultRow);
					Integer three_shoot_n = (Integer) three_shoot.getAttribute(resultRow);
					Integer three_shoot_score_n = (Integer) three_shoot_score.getAttribute(resultRow);
					Integer shoot_n = (Integer) shoot.getAttribute(resultRow);
					Integer shoot_score_n = (Integer) shoot_score.getAttribute(resultRow);
					Integer foul_n = (Integer) foul.getAttribute(resultRow);
					Integer foul_shoot_n = (Integer) foul_shoot.getAttribute(resultRow);
					Integer foul_shoot_score_n = (Integer) foul_shoot_score.getAttribute(resultRow);
					Integer miss_n = (Integer) miss.getAttribute(resultRow);
					
					Integer total_shoot_score = shoot_score_n + three_shoot_score_n + foul_shoot_score_n;
					Integer total_shoot_count = shoot_n + three_shoot_n + foul_shoot_n;
					
					Float gmsc_efficiency = self_score_n + 0.4f * total_shoot_score - 0.7f * total_shoot_count
							- 0.4f * (foul_shoot_n - foul_shoot_score_n) + 0.7f * attack_board_n + 0.3f * defence_board_n
							+ steal_n + 0.7f * assist_n + 0.7f * cap_n - 0.4f * foul_n - miss_n;
					getDeriveColumn().setAttribute(resultRow, gmsc_efficiency);
				}
			},
			new DeriveColumnInfo("game_time", Integer.class)
			{
				Column game_time_minute;
				Column game_time_second;
				
				@Override
				public void retrieve(Table resultTable)
				{
					game_time_minute = resultTable.getColumn("game_time_minute");
					game_time_second = resultTable.getColumn("game_time_second");
				}

				@Override
				public void derive(Row resultRow)
				{
					Integer result = 0;
					
					Integer minute = (Integer) game_time_minute.getAttribute(resultRow);
					if(minute != null) result += minute * 60;
					
					Integer second = (Integer) game_time_second.getAttribute(resultRow);
					if(second != null) result += second; 
					getDeriveColumn().setAttribute(resultRow, result);
				}
			},
			new RateColumnInfo("three_shoot_rate", "three_shoot_score", "three_shoot"),
			new RateColumnInfo("shoot_rate", "shoot_score", "shoot"),
			new RateColumnInfo("foul_shoot_rate", "foul_shoot_score", "foul_shoot"),
			new DeriveColumnInfo("game_time_ratio", Float.class)
			{
				Column game_time;
				Column total_game_time;
				@Override
				public void retrieve(Table resultTable)
				{
					game_time = resultTable.getColumn("game_time");
					total_game_time = resultTable.getColumn("total_game_time");
				}

				@Override
				public void derive(Row resultRow)
				{
					Integer game_time_n = (Integer) game_time.getAttribute(resultRow);
					Integer total_game_time_n = (Integer) total_game_time.getAttribute(resultRow);
					getDeriveColumn().setAttribute(resultRow, 0.2f * total_game_time_n / game_time_n);
				}
			},
		});
	}
	
}
