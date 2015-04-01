package nbaquery.logic;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.query.DeriveColumnInfo;
import nbaquery.data.query.DeriveQuery;

public class TeamDeriveQuery extends DeriveQuery
{

	public TeamDeriveQuery(Table table)
	{
		super(table, new DeriveColumnInfo[]
				{
					new RoundInfo("attack_round", Float.class, false),
					new RoundInfo("defence_round", Float.class, true),
					new RateColumnInfo("three_shoot_rate", "three_shoot_score", "three_shoot"),
					new RateColumnInfo("shoot_rate", "shoot_score", "shoot"),
					new RateColumnInfo("foul_shoot_rate", "foul_shoot_score", "foul_shoot"),
					new DeriveColumnInfo("attack_efficiency", Float.class)
					{
						Column score;
						Column round;
						@Override
						public void retrieve(Table resultTable)
						{
							this.round = resultTable.getColumn("attack_round");
							this.score = resultTable.getColumn("self_score");
						}

						@Override
						public void derive(Row resultRow)
						{
							Float attack_round_n = (Float) this.round.getAttribute(resultRow);
							Integer team_score_n = (Integer) this.score.getAttribute(resultRow);
							getDeriveColumn().setAttribute(resultRow, 100.f * team_score_n / attack_round_n);
						}
					},
					new DeriveColumnInfo("defence_efficiency", Float.class)
					{
						Column score;
						Column round;
						@Override
						public void retrieve(Table resultTable)
						{
							this.round = resultTable.getColumn("defence_round");
							this.score = resultTable.getColumn("rival_self_score");
						}

						@Override
						public void derive(Row resultRow)
						{
							Float defence_round_n = (Float) this.round.getAttribute(resultRow);
							Integer team_score_n = (Integer) this.score.getAttribute(resultRow);
							getDeriveColumn().setAttribute(resultRow, 100.f * team_score_n / defence_round_n);
						}
					},
					new DeriveColumnInfo("steal_efficiency", Float.class)
					{
						Column score;
						Column round;
						@Override
						public void retrieve(Table resultTable)
						{
							this.round = resultTable.getColumn("defence_round");
							this.score = resultTable.getColumn("steal");
						}

						@Override
						public void derive(Row resultRow)
						{
							Float defence_round_n = (Float) this.round.getAttribute(resultRow);
							Integer steal_n = (Integer) this.score.getAttribute(resultRow);
							getDeriveColumn().setAttribute(resultRow, 100.f * steal_n / defence_round_n);
						}
					},
					new DeriveColumnInfo("assist_efficiency", Float.class)
					{
						Column score;
						Column round;
						@Override
						public void retrieve(Table resultTable)
						{
							this.round = resultTable.getColumn("attack_round");
							this.score = resultTable.getColumn("assist");
						}

						@Override
						public void derive(Row resultRow)
						{
							Float attack_round_n = (Float) this.round.getAttribute(resultRow);
							Integer assist_n = (Integer) this.score.getAttribute(resultRow);
							getDeriveColumn().setAttribute(resultRow, 100.f * assist_n / attack_round_n);
						}
					},
					new DeriveColumnInfo("attack_board_efficiency", Float.class)
					{
						Column attack;
						Column defence;
						@Override
						public void retrieve(Table resultTable)
						{
							this.attack = resultTable.getColumn("attack_board");
							this.defence = resultTable.getColumn("rival_defence_board");
						}

						@Override
						public void derive(Row resultRow)
						{
							Integer attack = (Integer) this.attack.getAttribute(resultRow);
							Integer defence = (Integer) this.defence.getAttribute(resultRow);
							getDeriveColumn().setAttribute(resultRow, 1.f * attack /(defence + attack));
						}
					},
					new DeriveColumnInfo("defence_board_efficiency", Float.class)
					{
						Column attack;
						Column defence;
						@Override
						public void retrieve(Table resultTable)
						{
							this.attack = resultTable.getColumn("rival_attack_board");
							this.defence = resultTable.getColumn("defence_board");
						}

						@Override
						public void derive(Row resultRow)
						{
							Integer attack = (Integer) this.attack.getAttribute(resultRow);
							Integer defence = (Integer) this.defence.getAttribute(resultRow);
							getDeriveColumn().setAttribute(resultRow, 1.f * defence /(defence + attack));
						}
					},
					new RateColumnInfo("win_rate", "win", "game")
				}
		);
	}
	
}

class RoundInfo extends DeriveColumnInfo
{

	boolean isRival;
	public RoundInfo(String deriveColumn, Class<?> deriveClasses, boolean isRival)
	{
		super(deriveColumn, deriveClasses);
		this.isRival = isRival;
	}

	Column shoot_score;
	Column shoot_count;
	Column three_shoot_score;
	Column three_shoot_count;
	Column foul_shoot_score;
	Column foul_shoot_count;
	Column attack_board;
	Column rival_defence_board;
	Column miss;
	
	@Override
	public void retrieve(Table resultTable)
	{
		String prefix = "";
		if(isRival) prefix = "rival_";
		
		shoot_count = resultTable.getColumn(prefix + "shoot");
		shoot_score = resultTable.getColumn(prefix + "shoot_score");
		three_shoot_count = resultTable.getColumn(prefix + "three_shoot");
		three_shoot_score = resultTable.getColumn(prefix + "three_shoot_score");
		foul_shoot_count = resultTable.getColumn(prefix + "foul_shoot");
		foul_shoot_score = resultTable.getColumn(prefix + "foul_shoot_score");
		attack_board = resultTable.getColumn(prefix + "attack_board");
		rival_defence_board = resultTable.getColumn(isRival? "defence_board" : "rival_defence_board");
		miss = resultTable.getColumn(prefix + "miss");
	}

	@Override
	public void derive(Row resultRow)
	{
		Integer shoot_count = (Integer) this.shoot_count.getAttribute(resultRow);
		Integer shoot_score = (Integer) this.shoot_score.getAttribute(resultRow);
		Integer three_shoot_count = (Integer) this.three_shoot_count.getAttribute(resultRow);
		Integer three_shoot_score = (Integer) this.three_shoot_score.getAttribute(resultRow);
		Integer foul_shoot_count = (Integer) this.foul_shoot_count.getAttribute(resultRow);
		Integer foul_shoot_score = (Integer) this.foul_shoot_score.getAttribute(resultRow);
		Integer attack_board = (Integer) this.attack_board.getAttribute(resultRow);
		Integer rival_defence_board = (Integer) this.rival_defence_board.getAttribute(resultRow);
		Integer miss = (Integer) this.miss.getAttribute(resultRow);
		
		Integer shoot_total = shoot_count + three_shoot_count + foul_shoot_count;
		Integer shoot_score_sum = shoot_score + three_shoot_score + foul_shoot_score;
		
		Float round = 1.0f * shoot_total + 0.4f * foul_shoot_count - 1.07f * attack_board / (attack_board + rival_defence_board) * (shoot_total - shoot_score_sum) + 1.07f * miss;
		getDeriveColumn().setAttribute(resultRow, round);
	}
}