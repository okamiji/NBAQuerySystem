package nbaqueryBusinessLogic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nbaquery.data.Table;
import nbaqueryBusinessLogicService.*;
import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.TableHost;
import nbaquery.data.query.DeriveQuery;
import nbaquery.data.query.ExpressionDeriveColumnInfo;
import nbaquery.data.query.DeriveColumnInfo;
import nbaquery.data.query.GroupQuery;
import nbaquery.data.query.Query;
import nbaquery.data.query.SortQuery;

public class TeamLogic implements TeamService{

	TableHost host;
	public TeamLogic(TableHost host) throws Exception
	{
		this.host = host;
		performance = host.getTable("performance");
		match = host.getTable("match");
		
		this.getTotalData();
		this.getCompleteTeamTable();
		this.getAverageTeamTable();
	}
	
	Table performance;
	Table match;
	
	public void getTotalData()throws Exception{
		GroupQuery queryTeam = new GroupQuery(){
			Column total_board;
			Column team_total_board_sum;
			Column shoot_score;
			Column shoot_count;
			Column team_shoot_score_sum;
			Column team_shoot_sum;
			Column three_shoot_count;
			Column three_shoot_score;
			Column team_three_shoot_sum;
			Column team_three_shoot_score_sum;
			Column foul_shoot_count;
			Column team_foul_shoot_sum;
			Column miss;
			Column team_miss_sum;
			Column team_game_sum;
			Column steal;
			Column team_steal_sum;
			Column assist;
			Column team_assist_sum;
			
			@Override
			public void retrieve(Table resultTable){
				team_game_sum = resultTable.getColumn("team_game_sum");
				total_board = this.table.getColumn("total_board");
				team_total_board_sum = resultTable.getColumn("team_total_board_sum");
				shoot_score = this.table.getColumn("shoot_score");
				shoot_count = this.table.getColumn("shoot_count");
				team_shoot_sum = resultTable.getColumn("team_shoot_sum");
				team_shoot_score_sum = resultTable.getColumn("team_shoot_score_sum");
				three_shoot_score = this.table.getColumn("three_shoot_score");
				three_shoot_count = this.table.getColumn("three_shoot_count");
				team_three_shoot_sum = resultTable.getColumn("team_three_shoot_sum");
				team_three_shoot_score_sum = resultTable.getColumn("team_three_shoot_score_sum");
				foul_shoot_count = this.table.getColumn("foul_shoot_count");
				team_foul_shoot_sum = resultTable.getColumn("team_foul_shoot_sum");
				miss = this.table.getColumn("miss");
				team_miss_sum = resultTable.getColumn("team_miss_sum");
				steal = this.table.getColumn("steal");
				team_steal_sum = resultTable.getColumn("team_steal_sum");
				assist = this.table.getColumn("assist");
				team_assist_sum = resultTable.getColumn("team_assist_sum");
			}

			@Override
			public void collapse(Row[] rows, Row resultRow){
				Integer team_game_sum = 0;
				Integer team_total_board_sum = 0;
				Integer team_shoot_sum = 0;
				Integer team_shoot_score_sum = 0;
				Integer team_three_shoot_sum = 0;
				Integer team_three_shoot_score_sum = 0;
				Integer team_foul_shoot_sum = 0;
				Integer team_miss_sum = 0;
				Integer team_steal_sum = 0;
				Integer team_assist_sum = 0;
				
				for(Row row : rows){
					team_game_sum += 1;
					team_total_board_sum += (Integer) total_board.getAttribute(row);
					team_shoot_sum += (Integer) shoot_count.getAttribute(row);
					team_shoot_score_sum += (Integer) shoot_score.getAttribute(row);
					team_three_shoot_sum += (Integer) three_shoot_count.getAttribute(row);
					team_three_shoot_score_sum += (Integer) three_shoot_score.getAttribute(row);
					team_foul_shoot_sum += (Integer) foul_shoot_count.getAttribute(row);
					team_miss_sum += (Integer) miss.getAttribute(row);
					team_steal_sum += (Integer)steal.getAttribute(row);
					team_assist_sum += (Integer)assist.getAttribute(row);
				}
				this.team_game_sum.setAttribute(resultRow, team_game_sum);
				this.team_total_board_sum.setAttribute(resultRow, team_total_board_sum);
				this.team_shoot_sum.setAttribute(resultRow, team_shoot_sum);
				this.team_shoot_score_sum.setAttribute(resultRow, team_shoot_score_sum);
				this.team_three_shoot_sum.setAttribute(resultRow, team_three_shoot_sum);
				this.team_three_shoot_score_sum.setAttribute(resultRow, team_three_shoot_score_sum);
				this.team_foul_shoot_sum.setAttribute(resultRow, team_foul_shoot_sum);
				this.team_miss_sum.setAttribute(resultRow, team_miss_sum);
				this.team_steal_sum.setAttribute(resultRow, team_steal_sum);
				this.team_assist_sum.setAttribute(resultRow, team_assist_sum);
			}
		};
		
		queryTeam.table = performance;
		queryTeam.collapseColumn = new String[]{"team_name_abbr"};
		queryTeam.derivedColumn = new String[]{"team_game_sum","team_total_board_sum","team_three_shoot_sum", "team_three_shoot_score_sum", "team_shoot_sum", "team_shoot_score_sum",
				"team_foul_shoot_sum","team_miss_sum","team_steal_sum","team_assist_sum","team_name_abbr"};
		queryTeam.derivedClass = new Class<?>[]{Integer.class, Integer.class, Integer.class, Integer.class, Integer.class,
				Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, String.class};
		
		host.performQuery(queryTeam, "collapseTeamTotal");
		
		
		
		
		GroupQuery queryRival = new GroupQuery(){
			Column match_host_abbr;
			Column match_guest_abbr;
			Column team_name_abbr;
			Column team_attack_board_sum;
			Column team_defence_board_sum;
			Column rival_attack_board_sum;
			Column rival_defence_board_sum;
			Column team_success;
			Column rival_success;
			Column team_score;
			Column rival_score;
			Column the_host_score;
			Column the_guest_score;
			Column rival_game_sum;
			
			@Override
			public void retrieve(Table resultTable){
				rival_game_sum = resultTable.getColumn("rival_game_sum");
				match_host_abbr = match.getColumn("match_host_abbr");
				match_guest_abbr = match.getColumn("match_guest_abbr");
				team_name_abbr = this.table.getColumn("team_name_abbr");
				team_attack_board_sum = this.table.getColumn("team_attack_board_sum");
				team_defence_board_sum = this.table.getColumn("team_defence_board_sum");
				rival_attack_board_sum = resultTable.getColumn("rival_attack_board_sum");
				rival_defence_board_sum = resultTable.getColumn("rival_defence_board_sum");
				team_success = resultTable.getColumn("team_success");
				rival_success = resultTable.getColumn("rival_success");
				the_host_score = match.getColumn("host_score");
				the_guest_score = match.getColumn("guest_score");
				team_score = resultTable.getColumn("team_score");
				rival_score = resultTable.getColumn("rival_score");
			}

			@Override
			public void collapse(Row[] rows, Row resultRow){
				
				Row[] rivalRow = match.getRows();
				
				Integer rival_game_sum = 0;
				Integer rival_attack_board_sum = 0;
				Integer rival_defence_board_sum = 0;
				Integer team_success = 0;
				Integer rival_success = 0;
				Integer team_score = 0;
				Integer rival_score = 0;
				Integer host_score = 0;
				Integer guest_score = 0;
				
				String teamName = "";
				String rivalHostName = "";
				String rivalGuestName = "";
				String division = "oneTeamDivision";
				ArrayList<String> rivalsNames = new ArrayList<String>();
				String teamNow = "";
				
				for(Row row : rows){
					
					teamName = (String) team_name_abbr.getAttribute(row);
					for(Row rowRival : rivalRow){
						rivalHostName = (String) match_host_abbr.getAttribute(rowRival);
						rivalGuestName = (String) match_guest_abbr.getAttribute(rowRival);
						host_score = (Integer) match_host_abbr.getAttribute(rowRival);
						guest_score = (Integer) match_host_abbr.getAttribute(rowRival);
						if((!(teamName.equals(rivalHostName)))&&(!(teamName.equals(rivalGuestName)))){
							continue;
						}else{
							if(teamName.equals(rivalGuestName)){
								if(host_score < guest_score){
									team_success += 1;
								}else{
									rival_success += 1;
								}
								team_score = (Integer) the_guest_score.getAttribute(rowRival);
								rival_score = (Integer) the_host_score.getAttribute(rowRival);
								int size = rivalsNames.size();
								for(int i = 0 ; i < size ; i ++){
									if(rivalHostName.equals(rivalsNames.get(i))){
										continue;
									}else{
										if(i == size - 1){
											rivalsNames.add(rivalHostName);
										}
									}
								}
							}else{
								if(teamName.equals(rivalHostName)){
									if(host_score > guest_score){
										team_success += 1;
									}else{
										rival_success += 1;
									}
									team_score = (Integer) the_host_score.getAttribute(rowRival);
									rival_score = (Integer) the_guest_score.getAttribute(rowRival);
									int size = rivalsNames.size();
									for(int i = 0 ; i < size ; i ++){
										if(rivalGuestName.equals(rivalsNames.get(i))){
											continue;
										}else{
											if(i == size - 1){
												rivalsNames.add(rivalGuestName);
											}
										}
									}
								}
							}
						}
					}
					rivalsNames.add(division);
					
					int count = 0;
					
					for(Row rowSelect : rows){
						teamNow = (String) team_name_abbr.getAttribute(rowSelect);
						ArrayList<String> selectNames = new ArrayList<String>();
						for(int i = count ; i < rivalsNames.size() ; i++){
							String theName = rivalsNames.get(i);
							if(theName.equals(division)){
								count = count + i + 1;
								break;
							}else{
								selectNames.add(theName);
							}
						}
						for(int j = 0 ; j < selectNames.size() ; j ++){
							String teamCon = selectNames.get(j);
							for(Row rowFinal : rows){
								String nameCheck = (String) team_name_abbr.getAttribute(rowFinal);
								if(teamCon.equals(nameCheck)){
									rival_game_sum += 1;
									rival_attack_board_sum += (Integer) team_attack_board_sum.getAttribute(rowFinal);
									rival_defence_board_sum += (Integer) team_defence_board_sum.getAttribute(rowFinal);
								}
							}
						}
					}
				}
				
				this.rival_game_sum.setAttribute(resultRow, rival_game_sum);
				this.rival_attack_board_sum.setAttribute(resultRow, rival_attack_board_sum);
				this.rival_defence_board_sum.setAttribute(resultRow, rival_defence_board_sum);
				this.team_success.setAttribute(resultRow, team_success);
				this.rival_success.setAttribute(resultRow, rival_success);
				this.team_score.setAttribute(resultRow, team_score);
				this.rival_score.setAttribute(resultRow, rival_score);
			}
		};
		queryRival.table = host.getTable("collapseTeamTotal");
		queryRival.collapseColumn = new String[]{"team_name_abbr"};
		queryRival.derivedColumn = new String[]{"rival_game_sum","rival_attack_board_sum","rival_defence_board_sum","team_success","rival_success","team_score","rival_score"};
		queryRival.derivedClass = new Class<?>[]{Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class};
		
		host.performQuery(queryRival, "collapseRivalTotal");
		
		collapseTeamTotal = host.getTable("collapseTeamTotal");
		collapseRivalTotal = host.getTable("collapseRivalTotal");
	}
	
	Table collapseTeamTotal;
	Table collapseRivalTotal;
	
	public void getCompleteTeamTable()throws Exception{
		Row[] rows = collapseRivalTotal.getRows();
		final Map<String, Row> theMap = new HashMap<String, Row>();
		Column team_name_abbr = collapseRivalTotal.getColumn("team_name_abbr");
		for(int i = 0; i < rows.length; i ++)
		{
			String name_abbr = (String) team_name_abbr.getAttribute(rows[i]);
			theMap.put(name_abbr, rows[i]);
		}
		
		Query query = new DeriveQuery(collapseTeamTotal, new DeriveColumnInfo[]{
				new ExpressionDeriveColumnInfo("three_shoot_accuracy", Float.class,
						"1.0F * collapseTeamTotal.three_shoot_score_sum / collapseTeamTotal.three_shoot_sum")
				,
				new ExpressionDeriveColumnInfo("shoot_accuracy", Float.class,
						"1.0F * collapseTeamTotal.shoot_score_sum / collapseTeamTotal.shoot_sum")
				,
				new ExpressionDeriveColumnInfo("foul_accuracy",Float.class,
						"1.0F * collapseTeamTotal.team_foul_shoot_score_sum/collapseTeamTotal.team_foul_shoot_sum")
				,
				new DeriveColumnInfo("win_rate",Float.class)
				{
					Column team_success = collapseRivalTotal.getColumn("team_success");
					Column rival_success = collapseRivalTotal.getColumn("rival_success");
					Column team_name_abbr;
					Column win_rate;
					
					@Override
					public void retrieve(Table resultTable)
					{
						team_name_abbr = resultTable.getColumn("team_name_abbr");
						win_rate = resultTable.getColumn("win_rate");
					}

					@Override
					public void derive(Row resultRow)
					{
						String name_abbr = (String) team_name_abbr.getAttribute(resultRow);
						Row rivalRow = theMap.get(name_abbr);
						Integer teamSuccess = (Integer) team_success.getAttribute(rivalRow);
						Integer rivalSuccess = (Integer) rival_success.getAttribute(rivalRow);
						win_rate.setAttribute(resultRow, 1.0 * (teamSuccess) / (teamSuccess + rivalSuccess));
					}
					//collapseRivalTotal.team_success / (collapseRivalTotal.team_success + collapseRivalTotal.rival_success)
					
					
				}
				,
				new ExpressionDeriveColumnInfo("attack_round",Float.class,
						"collapseTeamTotal.team_shoot_sum + 0.4 * collapseTeamTotal.team_foul_shoot_sum - 1.07 * (collapseTeamTotal.team_attack_board_sum / (collapseTeamTotal.team_attack_board_sum + collapseRivalTotal.team_defence_board_sum) * (collapseTeamTotal.team_shoot_sum - collapseTeamTotal.team_shoot_score_sum)) + 1.07 * collapseTeamTotal.team_miss_sum")
				,
				new ExpressionDeriveColumnInfo("defence_round",Float.class,
						"collapseTeamTotal.team_shoot_sum + 0.4 * collapseTeamTotal.team_foul_shoot_sum - 1.07 * (collapseTeamTotal.team_defence_board_sum / (collapseTeamTotal.team_defence_board_sum + collapseRivalTotal.team_attack_board_sum) * (collapseTeamTotal.team_shoot_sum - collapseTeamTotal.team_shoot_score_sum)) + 1.07 * collapseTeamTotal.team_miss_sum")
				,
				new DeriveColumnInfo("attack_efficiency",Float.class)
				{
					Column attack_round;
					Column team_score;
					Column attack_efficiency;
					
					@Override
					public void retrieve(Table resultTable) {
						attack_round = resultTable.getColumn("attack_round");
						team_score = collapseTeamTotal.getColumn("team_score");
						attack_efficiency = resultTable.getColumn("attack_efficiency");
					}

					@Override
					public void derive(Row resultRow) {
						Integer teamScore = (Integer) team_score.getAttribute(resultRow);
						Integer attackRound = (Integer) attack_round.getAttribute(resultRow);
						attack_efficiency.setAttribute(resultRow, 1.0f * teamScore / attackRound * 100);
					}
					
				}
				,
				new ExpressionDeriveColumnInfo("defence_efficiency",Float.class,
						"collapseRivalTotal.team_score / defence_round * 100")
				,
				new ExpressionDeriveColumnInfo("attack_board_efficiency",Float.class,
						"collapseTeamTotal.team_attack_board_sum / (collapseTeamTotal.team_attack_board_sum + collapseRivalTotal.rival_defence_board_sum)")
				,
				new ExpressionDeriveColumnInfo("defence_board_efficiency",Float.class,
						"collapseTeamTotal.team_defence_board_sum / (collapseTeamTotal.team_defence_board_sum + collapseRivalTotal.rival_attack_board_sum)")
				,
				new ExpressionDeriveColumnInfo("steal_efficiency",Float.class,
						"collapseTeamTotal.team_steal_sum / defence_round * 100")
				,
				new ExpressionDeriveColumnInfo("assist_efficiency",Float.class,
						"collapseTeamTotal.team_assist_sum / attack_round * 100")
				}, "team_three_shoot_sum", "team_three_shoot_score_sum", "team_shoot_sum", "team_shoot_score_sum", "team_foul_shoot_sum","team_steal_sum","team_assist_sum","team_name_abbr");
		
		host.performQuery(query, "resultTeam");
		resultTeam = host.getTable("resultTeam");
	}
	
	Table resultTeam;
	
	public void getAverageTeamTable()throws Exception{
		Query query = new DeriveQuery(collapseTeamTotal, new DeriveColumnInfo[]{
				new ExpressionDeriveColumnInfo("three_shoot_accuracy", Float.class,
						"1.0F * collapseTeamTotal.team_three_shoot_score_sum / collapseTeamTotal.team_three_shoot_sum")
				,
				new ExpressionDeriveColumnInfo("shoot_accuracy", Float.class,
						"1.0F * collapseTeamTotal.team_shoot_score_sum / collapseTeamTotal.team_shoot_sum")
				,
				new ExpressionDeriveColumnInfo("foul_accuracy",Float.class,
						"1.0F * collapseTeamTotal.team_foul_shoot_score_sum/collapseTeamTotal.team_foul_shoot_sum")
				,
				new ExpressionDeriveColumnInfo("attack_round",Float.class,
						"(collapseTeamTotal.team_shoot_sum + 0.4 * collapseTeamTotal.team_foul_shoot_sum) / collapseTeamTotal.team_game_sum - 1.07 * ((collapseTeamTotal.team_attack_board_sum / collapseTeamTotal.team_game_sum) / (collapseTeamTotal.team_attack_board_sum / collapseTeamTotal.team_game_sum + collapseRivalTotal.team_defence_board_sum / collapseRivalTotal.team_game_sum) * (collapseTeamTotal.team_shoot_sum / collapseTeamTotal.team_game_sum - collapseTeamTotal.team_shoot_score_sum / collapseTeamTotal.team_game_sum)) + 1.07 * collapseTeamTotal.team_miss_sum / collapseTeamTotal.team_game_sum")
				,
				new ExpressionDeriveColumnInfo("defence_round",Float.class,
						"(collapseTeamTotal.team_shoot_sum + 0.4 * collapseTeamTotal.team_foul_shoot_sum) / collapseTeamTotal.team_game_sum - 1.07 * ((collapseTeamTotal.team_defence_board_sum / collapseTeamTotal.team_game_sum) / (collapseTeamTotal.team_defence_board_sum / collapseTeamTotal.team_game_sum + collapseRivalTotal.team_attack_board_sum / collapseRivalTotal.rival_game_sum) * (collapseTeamTotal.team_shoot_sum - collapseTeamTotal.team_shoot_score_sum) / collapseTeamTotal.team_game_sum) + 1.07 * collapseTeamTotal.team_miss_sum / collapseTeamTotal.team_game_sum")
				,
				new ExpressionDeriveColumnInfo("attack_board_efficiency",Float.class,
						"collapseTeamTotal.team_attack_board_sum / collapseTeamTotal.team_game_sum / (collapseTeamTotal.team_attack_board_sum / collapseTeamTotal.team_game_sum + collapseRivalTotal.rival_defence_board_sum / collapseRivalTotal.rival_game_sum)")
				,
				new ExpressionDeriveColumnInfo("defence_board_efficiency",Float.class,
						"collapseTeamTotal.team_defence_board_sum / collapseTeamTotal.team_game_sum / (collapseTeamTotal.team_defence_board_sum / collapseTeamTotal.team_game_sum + collapseRivalTotal.rival_attack_board_sum / collapseRivalTotal.rival_game_sum)")
				,
				}, "team_three_shoot_sum", "team_three_shoot_score_sum", "team_shoot_sum", "team_shoot_score_sum", "team_foul_shoot_sum","attack_round","defence_round","team_steal_sum","team_assist_sum","team_name");
		
		host.performQuery(query, "resultTeamAverage");
		resultTeamAverage = host.getTable("resultTeamAverage");
	}
	
	Table resultTeamAverage;
	
	public String[][] searchForTeams(boolean type, String head, boolean upOrDown) {
		// TODO Auto-generated method stub
		String[][] result = null;
		if(type==true){
			if(upOrDown==true){
				SortQuery query = new SortQuery(resultTeam, head,  true);
				host.performQuery(query, "resultSort");
			}else{
				SortQuery query = new SortQuery(resultTeam, head,  false);
				host.performQuery(query, "resultSort");
			}
		}else{
			if(upOrDown==true){
				SortQuery query = new SortQuery(resultTeamAverage, head,  true);
				host.performQuery(query, "resultSort");
			}else{
				SortQuery query = new SortQuery(resultTeamAverage, head,  false);
				host.performQuery(query, "resultSort");
			}
		}
		return result;
	}

}
