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
					three_shoot = resultTable.getColumn("three_shoot_count");
					three_shoot_score = resultTable.getColumn("three_shoot_score");
					shoot = resultTable.getColumn("shoot_count");
					shoot_score = resultTable.getColumn("shoot_score");
					foul_shoot = resultTable.getColumn("foul_shoot_count");
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
					three_shoot = resultTable.getColumn("three_shoot_count");
					three_shoot_score = resultTable.getColumn("three_shoot_score");
					shoot = resultTable.getColumn("shoot_count");
					shoot_score = resultTable.getColumn("shoot_score");
					foul = resultTable.getColumn("foul");
					foul_shoot = resultTable.getColumn("foul_shoot_count");
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
			new RateColumnInfo("three_shoot_rate", "three_shoot_score", "three_shoot_count"),
			new RateColumnInfo("shoot_rate", "shoot_score", "shoot_count"),
			new RateColumnInfo("foul_shoot_rate", "foul_shoot_score", "foul_shoot_count"),
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
			
			new DeriveColumnInfo("true_shoot_rate", Float.class)
			{

				Column self_score;
				Column three_shoot;
				Column shoot;
				Column foul_shoot;
				
				@Override
				public void retrieve(Table resultTable)
				{
					self_score = resultTable.getColumn("self_score");
					three_shoot = resultTable.getColumn("three_shoot_count");
					shoot = resultTable.getColumn("shoot_count");
					foul_shoot = resultTable.getColumn("foul_shoot_count");
				}
	
				@Override
				public void derive(Row resultRow)
				{
					Integer self_score_n = (Integer) self_score.getAttribute(resultRow);
					Integer three_shoot_n = (Integer) three_shoot.getAttribute(resultRow);
					Integer shoot_n = (Integer) shoot.getAttribute(resultRow);
					Integer foul_shoot_n = (Integer) foul_shoot.getAttribute(resultRow);
					
					Integer total_shoot_count = shoot_n + three_shoot_n + foul_shoot_n;
					
					Float true_shoot_rate = .5f * self_score_n / (total_shoot_count + 0.44f * foul_shoot_n);
					getDeriveColumn().setAttribute(resultRow, true_shoot_rate);
				}
			},
			
			new DeriveColumnInfo("shoot_efficiency", Float.class)
			{
				Column three_shoot;
				Column three_shoot_score;
				Column shoot;
				Column shoot_score;
				Column foul_shoot;
				Column foul_shoot_score;
				
				@Override
				public void retrieve(Table resultTable)
				{
					three_shoot = resultTable.getColumn("three_shoot_count");
					three_shoot_score = resultTable.getColumn("three_shoot_score");
					shoot = resultTable.getColumn("shoot_count");
					shoot_score = resultTable.getColumn("shoot_score");
					foul_shoot = resultTable.getColumn("foul_shoot_count");
					foul_shoot_score = resultTable.getColumn("foul_shoot_score");
				}
	
				@Override
				public void derive(Row resultRow)
				{
					Integer three_shoot_n = (Integer) three_shoot.getAttribute(resultRow);
					Integer three_shoot_score_n = (Integer) three_shoot_score.getAttribute(resultRow);
					Integer shoot_n = (Integer) shoot.getAttribute(resultRow);
					Integer shoot_score_n = (Integer) shoot_score.getAttribute(resultRow);
					Integer foul_shoot_n = (Integer) foul_shoot.getAttribute(resultRow);
					Integer foul_shoot_score_n = (Integer) foul_shoot_score.getAttribute(resultRow);
					
					Integer total_shoot_score = shoot_score_n + three_shoot_score_n + foul_shoot_score_n;
					Integer total_shoot_count = shoot_n + three_shoot_n + foul_shoot_n;
					
					Float shoot_efficiency = (.5f * three_shoot_score_n + total_shoot_score)/ total_shoot_count;
					getDeriveColumn().setAttribute(resultRow, shoot_efficiency);
				}
			},
			new BoardEfficiencyColumnInfo("total_board_efficiency", "total_board"),
			new BoardEfficiencyColumnInfo("attack_board_efficiency", "attack_board"),
			new BoardEfficiencyColumnInfo("defence_board_efficiency", "defence_board"),
			
			new DeriveColumnInfo("assist_rate", Float.class)
			{
				Column assist;
				Column three_shoot_score;
				Column shoot_score;
				Column foul_shoot_score;
				Column three_shoot_score_sum;
				Column shoot_score_sum;
				Column foul_shoot_score_sum;
				Column game_time_ratio;
				
				@Override
				public void retrieve(Table resultTable)
				{
					assist = resultTable.getColumn("assist");
					three_shoot_score = resultTable.getColumn("three_shoot_score");
					shoot_score = resultTable.getColumn("shoot_score");
					foul_shoot_score = resultTable.getColumn("foul_shoot_score");
					
					three_shoot_score_sum = resultTable.getColumn("three_shoot_score_sum");
					shoot_score_sum = resultTable.getColumn("shoot_score_sum");
					foul_shoot_score_sum = resultTable.getColumn("foul_shoot_score_sum");
					
					game_time_ratio = resultTable.getColumn("game_time_ratio");
				}
	
				@Override
				public void derive(Row resultRow)
				{
					Integer assist_n = (Integer) assist.getAttribute(resultRow);
					Integer three_shoot_score_n = (Integer) three_shoot_score.getAttribute(resultRow);
					Integer shoot_score_n = (Integer) shoot_score.getAttribute(resultRow);
					Integer foul_shoot_score_n = (Integer) foul_shoot_score.getAttribute(resultRow);
					
					Integer three_shoot_score_sum_n = (Integer) three_shoot_score_sum.getAttribute(resultRow);
					Integer shoot_score_sum_n = (Integer) shoot_score_sum.getAttribute(resultRow);
					Integer foul_shoot_score_sum_n = (Integer) foul_shoot_score_sum.getAttribute(resultRow);
					Float game_time_ratio_n = (Float)game_time_ratio.getAttribute(resultRow);
					
					Integer total_shoot_score = shoot_score_n + three_shoot_score_n + foul_shoot_score_n;
					Integer total_shoot_score_sum = shoot_score_sum_n + three_shoot_score_sum_n + foul_shoot_score_sum_n;
					
					Float assist_rate = 1.0f * assist_n/(total_shoot_score_sum / game_time_ratio_n - total_shoot_score);
					getDeriveColumn().setAttribute(resultRow, assist_rate);
				}
			},
			
			new DeriveColumnInfo("steal_rate", Float.class)
			{
				Column steal;
				Column game_time_ratio;
				Column rival_attack_board_sum;
				
				@Override
				public void retrieve(Table resultTable)
				{
					steal = resultTable.getColumn("steal");
					game_time_ratio = resultTable.getColumn("game_time_ratio");
					rival_attack_board_sum = resultTable.getColumn("rival_attack_board_sum");
				}
	
				@Override
				public void derive(Row resultRow)
				{
					Integer steal_n = (Integer) steal.getAttribute(resultRow);
					Float game_time_ratio_n = (Float) game_time_ratio.getAttribute(resultRow);
					Integer rival_attack_board_sum_n = (Integer) rival_attack_board_sum.getAttribute(resultRow);
					
					Float steal_rate = game_time_ratio_n * steal_n / rival_attack_board_sum_n;
					getDeriveColumn().setAttribute(resultRow, steal_rate);
				}
			},
			
			new DeriveColumnInfo("cap_rate", Float.class)
			{
				Column cap;
				Column game_time_ratio;
				Column rival_shoot_sum;
				
				@Override
				public void retrieve(Table resultTable)
				{
					cap = resultTable.getColumn("cap");
					game_time_ratio = resultTable.getColumn("game_time_ratio");
					rival_shoot_sum = resultTable.getColumn("rival_shoot_sum");
				}
	
				@Override
				public void derive(Row resultRow)
				{
					Integer cap_n = (Integer) cap.getAttribute(resultRow);
					Float game_time_ratio_n = (Float) game_time_ratio.getAttribute(resultRow);
					Integer rival_shoot_sum_n = (Integer) rival_shoot_sum.getAttribute(resultRow);
					
					Float cap_rate = game_time_ratio_n * cap_n / rival_shoot_sum_n;
					getDeriveColumn().setAttribute(resultRow, cap_rate);
				}
			},
			
			new DeriveColumnInfo("miss_rate", Float.class)
			{
				Column shoot;
				Column foul_shoot;
				Column miss;
				
				@Override
				public void retrieve(Table resultTable)
				{
					shoot = resultTable.getColumn("shoot_count");
					foul_shoot = resultTable.getColumn("foul_shoot_count");
					miss = resultTable.getColumn("miss");
				}
	
				@Override
				public void derive(Row resultRow)
				{
					Integer shoot_n = (Integer) shoot.getAttribute(resultRow);
					Integer foul_shoot_n = (Integer) foul_shoot.getAttribute(resultRow);
					Integer miss_n = (Integer) miss.getAttribute(resultRow);
					
					Float miss_rate = 1.f * miss_n /(shoot_n + 0.44f * foul_shoot_n + miss_n);
					getDeriveColumn().setAttribute(resultRow, miss_rate);
				}
			},
			
			new DeriveColumnInfo("usage", Float.class)
			{
				Column three_shoot;
				Column shoot;
				Column foul_shoot;
				Column miss;
				
				Column three_shoot_sum;
				Column shoot_sum;
				Column foul_shoot_sum;
				Column miss_sum;
				
				Column game_time_ratio;
				
				@Override
				public void retrieve(Table resultTable)
				{
					three_shoot = resultTable.getColumn("three_shoot_count");
					shoot = resultTable.getColumn("shoot_count");
					foul_shoot = resultTable.getColumn("foul_shoot_count");
					miss = resultTable.getColumn("miss");
					
					three_shoot_sum = resultTable.getColumn("three_shoot_sum");
					shoot_sum = resultTable.getColumn("shoot_sum");
					foul_shoot_sum = resultTable.getColumn("foul_shoot_sum");
					miss_sum = resultTable.getColumn("miss_sum");
					
					game_time_ratio = resultTable.getColumn("game_time_ratio");
				}
	
				@Override
				public void derive(Row resultRow)
				{
					Integer three_shoot_n = (Integer) three_shoot.getAttribute(resultRow);
					Integer shoot_n = (Integer) shoot.getAttribute(resultRow);
					Integer foul_shoot_n = (Integer) foul_shoot.getAttribute(resultRow);
					Integer miss_n = (Integer) miss.getAttribute(resultRow);
					
					Integer three_shoot_sum_n = (Integer) three_shoot_sum.getAttribute(resultRow);
					Integer shoot_sum_n = (Integer) shoot_sum.getAttribute(resultRow);
					Integer foul_shoot_sum_n = (Integer) foul_shoot_sum.getAttribute(resultRow);
					Integer miss_sum_n = (Integer) miss_sum.getAttribute(resultRow);
					
					Float game_time_ratio_n = (Float) game_time_ratio.getAttribute(resultRow);
					
					Integer total_shoot_count = shoot_n + three_shoot_n + foul_shoot_n;
					Integer total_shoot_sum = shoot_sum_n + three_shoot_sum_n + foul_shoot_sum_n;
					
					Float dividend = (float)(total_shoot_count + 0.44f * foul_shoot_n + miss_n);
					Float divisor = (float)(total_shoot_sum + 0.44f * foul_shoot_sum_n + miss_sum_n);
					
					getDeriveColumn().setAttribute(resultRow, dividend * game_time_ratio_n / divisor);
				}
			},
		});
	}
	
}

class BoardEfficiencyColumnInfo extends DeriveColumnInfo
{

	public String boardString;
	public BoardEfficiencyColumnInfo(String deriveColumn, String board)
	{
		super(deriveColumn, Float.class);
		this.boardString = board;
	}

	Column board;
	Column total_board_sum;
	Column rival_total_board;
	Column game_time_ratio;
	
	@Override
	public void retrieve(Table resultTable)
	{
		board = resultTable.getColumn(boardString);
		total_board_sum = resultTable.getColumn("total_board_sum");
		rival_total_board = resultTable.getColumn("rival_total_board_sum");
		game_time_ratio = resultTable.getColumn("game_time_ratio");
	}

	@Override
	public void derive(Row resultRow)
	{
		Integer board_n = (Integer) board.getAttribute(resultRow);
		Integer total_board_sum_n = (Integer) total_board_sum.getAttribute(resultRow);
		Integer rival_total_board_n = (Integer) rival_total_board.getAttribute(resultRow);
		Float game_time_ratio_n = (Float) game_time_ratio.getAttribute(resultRow);
		
		Float result = board_n * game_time_ratio_n / (total_board_sum_n + rival_total_board_n);
		getDeriveColumn().setAttribute(resultRow, result);
	}
}
