package nbaqueryBusinessLogic;
import nbaquery.data.Table;
import nbaqueryBusinessLogicService.PlayerService;
import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.ExpressionDeriveColumnInfo;
import nbaquery.data.query.ExpressionDeriveQuery;
import nbaquery.data.query.DeriveColumnInfo;
import nbaquery.data.query.Query;
import nbaquery.data.query.GroupQuery;
import nbaquery.data.query.SortQuery;

import java.util.*;

public class PlayerLogic implements PlayerService{

	TableHost host;
	Table performance=host.getTable("performance");
	Table match=host.getTable("match");
	Table team=host.getTable("team");
	
	public void getTotalData()throws Exception{
		
		GroupQuery querySingle = new GroupQuery(){
			Column three_shoot_score;
			Column three_shoot_number;
			Column three_shoot_sum;
			Column three_shoot_score_sum;
			Column shoot_score;
			Column shoot_number;
			Column shoot_sum;
			Column shoot_score_sum;
			Column total_board;
			Column total_board_sum;
			Column assist;
			Column assist_sum;
			Column game_time_minute;
			Column game_time_second;
			Column game_time_minute_sum;
			Column game_time_second_sum;
			Column attack_board;
			Column defence_board;
			Column attack_board_sum;
			Column defence_board_sum;
			Column steal;
			Column steal_sum;
			Column cap;
			Column cap_sum;
			Column miss;
			Column miss_sum;
			Column foul;
			Column foul_sum;
			Column foul_shoot_number;
			Column foul_shoot_score;
			Column foul_shoot_sum;
			Column foul_shoot_score_sum;
			Column self_score;
			Column self_score_sum;
			Column game_sum;
			
			@Override
			public void retrieve(Table resultTable){
				three_shoot_score = this.table.getColumn("three_shoot_score");
				three_shoot_number = this.table.getColumn("three_shoot_number");
				three_shoot_sum = resultTable.getColumn("three_shoot_sum");
				three_shoot_score_sum = resultTable.getColumn("three_shoot_score_sum");
				shoot_score = this.table.getColumn("shoot_score");
				shoot_number = this.table.getColumn("shoot_number");
				shoot_sum = resultTable.getColumn("shoot_sum");
				shoot_score_sum = resultTable.getColumn("shoot_score_sum");
				total_board = this.table.getColumn("total_board");
				total_board_sum = resultTable.getColumn("total_board_sum");
				assist = this.table.getColumn("assist");
				assist_sum = resultTable.getColumn("assist_sum");
				game_time_minute = this.table.getColumn("game_time_minute");
				game_time_second = this.table.getColumn("game_time_second");
				game_time_minute_sum = resultTable.getColumn("game_time_minute_sum");
				game_time_second_sum = resultTable.getColumn("game_time_second_sum");
				attack_board = this.table.getColumn("attack_board");
				defence_board = this.table.getColumn("defence_board");
				attack_board_sum = resultTable.getColumn("attack_board_sum");
				defence_board_sum = resultTable.getColumn("defence_board_sum");
				steal = this.table.getColumn("steal");
				steal_sum = resultTable.getColumn("steal_sum");
				cap = this.table.getColumn("cap");
				cap_sum = resultTable.getColumn("cap_sum");
				miss = this.table.getColumn("miss");
				miss_sum = resultTable.getColumn("miss_sum");
				foul = this.table.getColumn("foul");
				foul_sum = resultTable.getColumn("foul_sum");
				foul_shoot_number = this.table.getColumn("foul_shoot_number");
				foul_shoot_score = this.table.getColumn("foul_shoot_score");
				foul_shoot_sum = resultTable.getColumn("foul_shoot_sum");
				foul_shoot_score_sum = resultTable.getColumn("foul_shoot_score_sum");
				self_score = this.table.getColumn("self_score");
				self_score_sum = resultTable.getColumn("self_score_sum");
				game_sum = resultTable.getColumn("game_sum");
			}

			@Override
			public void collapse(Row[] rows, Row resultRow){
				Integer three_shoot_score_sum = 0;
				Integer three_shoot_sum = 0;
				Integer shoot_score_sum = 0;
				Integer shoot_sum = 0;
				Integer total_board_sum = 0;
				Integer assist_sum = 0;
				Integer game_time_minute_sum = 0;
				Integer game_time_second_sum = 0;
				Integer attack_board_sum = 0;
				Integer defence_board_sum = 0;
				Integer steal_sum = 0;
				Integer cap_sum = 0;
				Integer miss_sum = 0;
				Integer foul_sum = 0;
				Integer foul_shoot_sum = 0;
				Integer foul_shoot_score_sum = 0;
				Integer self_score_sum = 0;
				Integer game_sum = 0;
				
				for(Row row : rows){
					three_shoot_score_sum += (Integer) three_shoot_score.getAttribute(row);
					three_shoot_sum += (Integer) three_shoot_number.getAttribute(row);
					shoot_score_sum += (Integer) shoot_score.getAttribute(row);
					shoot_sum += (Integer) shoot_number.getAttribute(row);
					total_board_sum += (Integer) total_board.getAttribute(row);
					assist_sum += (Integer) assist.getAttribute(row);
					game_time_minute_sum += (Integer) game_time_minute.getAttribute(row);
					game_time_second_sum += (Integer) game_time_second.getAttribute(row);
					attack_board_sum += (Integer) attack_board.getAttribute(row);
					defence_board_sum += (Integer) defence_board.getAttribute(row);
					steal_sum += (Integer) steal.getAttribute(row);
					cap_sum += (Integer) cap.getAttribute(row);
					miss_sum += (Integer) miss.getAttribute(row);
					foul_sum += (Integer) foul.getAttribute(row);
					foul_shoot_sum += (Integer) foul_shoot_number.getAttribute(row);
					foul_shoot_score_sum += (Integer) foul_shoot_score.getAttribute(row);
					self_score_sum += (Integer) self_score.getAttribute(row);
					game_sum += 1;
				}
				
				this.game_sum.setAttribute(resultRow, game_sum);
				this.three_shoot_score_sum.setAttribute(resultRow, three_shoot_score_sum);
				this.three_shoot_sum.setAttribute(resultRow, three_shoot_sum);
				this.shoot_score_sum.setAttribute(resultRow, shoot_score_sum);
				this.shoot_sum.setAttribute(resultRow, shoot_sum);
				this.total_board_sum.setAttribute(resultRow, total_board_sum);
				this.assist_sum.setAttribute(resultRow, assist_sum);
				this.game_time_minute_sum.setAttribute(resultRow, game_time_minute_sum);
				this.game_time_second_sum.setAttribute(resultRow, game_time_second_sum);
				this.attack_board_sum.setAttribute(resultRow, attack_board_sum);
				this.defence_board_sum.setAttribute(resultRow, defence_board_sum);
				this.steal_sum.setAttribute(resultRow, steal_sum);
				this.cap_sum.setAttribute(resultRow, cap_sum);
				this.miss_sum.setAttribute(resultRow, miss_sum);
				this.foul_sum.setAttribute(resultRow, foul_sum);
				this.foul_shoot_sum.setAttribute(resultRow, foul_shoot_sum);
				this.foul_shoot_score_sum.setAttribute(resultRow, foul_shoot_score_sum);
				this.self_score_sum.setAttribute(resultRow, self_score_sum);
			}
		};
		
		querySingle.table = performance;
		querySingle.collapseColumn = new String[]{"player_name"};
		querySingle.derivedColumn = new String[]{"game_sum","three_shoot_sum", "three_shoot_score_sum", "shoot_sum", "shoot_score_sum",
				"total_board_sum","assist_sum","game_time_minute_sum","game_time_second_sum","attack_board_sum","defence_board_sum",
				"steal_sum","cap_sum","miss_sum","foul_sum","foul_shoot_sum","foul_shoot_score_sum","self_score_sum"};
		querySingle.derivedClass = new Class<?>[]{Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class,
				Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class,
				Integer.class, Integer.class, Integer.class, Integer.class};
		
		host.performQuery(querySingle, "collapsePlayer");
		
		GroupQuery queryTeam = new GroupQuery(){
			Column game_time_minute;
			Column game_time_second;
			Column team_game_time_minute_sum;
			Column team_game_time_second_sum;
			Column total_board;
			Column team_total_board_sum;
			Column shoot_score;
			Column shoot_number;
			Column team_shoot_score_sum;
			Column team_shoot_sum;
			Column three_shoot_number;
			Column three_shoot_score;
			Column team_three_shoot_sum;
			Column team_three_shoot_score_sum;
			Column foul_shoot_number;
			Column team_foul_shoot_sum;
			Column miss;
			Column team_miss_sum;
			
			@Override
			public void retrieve(Table resultTable){
				game_time_minute = this.table.getColumn("game_time_minute");
				game_time_second = this.table.getColumn("game_time_second");
				team_game_time_minute_sum = resultTable.getColumn("team_game_time_minute_sum");
				team_game_time_second_sum = resultTable.getColumn("team_game_time_second_sum");
				total_board = this.table.getColumn("total_board");
				team_total_board_sum = resultTable.getColumn("team_total_board_sum");
				shoot_score = this.table.getColumn("shoot_score");
				shoot_number = this.table.getColumn("shoot_number");
				team_shoot_sum = resultTable.getColumn("team_shoot_sum");
				team_shoot_score_sum = resultTable.getColumn("team_shoot_score_sum");
				three_shoot_score = this.table.getColumn("three_shoot_score");
				three_shoot_number = this.table.getColumn("three_shoot_number");
				team_three_shoot_sum = resultTable.getColumn("three_shoot_sum");
				team_three_shoot_score_sum = resultTable.getColumn("three_shoot_score_sum");
				foul_shoot_number = this.table.getColumn("foul_shoot_number");
				team_foul_shoot_sum = resultTable.getColumn("team_foul_shoot_sum");
				miss = this.table.getColumn("miss");
				team_miss_sum = resultTable.getColumn("team_miss_sum");
			}

			@Override
			public void collapse(Row[] rows, Row resultRow){
				Integer team_game_time_minute_sum = 0;
				Integer team_game_time_second_sum = 0;
				Integer team_total_board_sum = 0;
				Integer team_shoot_sum = 0;
				Integer team_shoot_score_sum = 0;
				Integer team_three_shoot_sum = 0;
				Integer team_three_shoot_score_sum = 0;
				Integer team_foul_shoot_sum = 0;
				Integer team_miss_sum = 0;
				
				for(Row row : rows){
					team_game_time_minute_sum += (Integer) game_time_minute.getAttribute(row);
					team_game_time_second_sum += (Integer) game_time_second.getAttribute(row);
					team_total_board_sum += (Integer) total_board.getAttribute(row);
					team_shoot_sum += (Integer) shoot_number.getAttribute(row);
					team_shoot_score_sum += (Integer) shoot_score.getAttribute(row);
					team_three_shoot_sum += (Integer) three_shoot_number.getAttribute(row);
					team_three_shoot_score_sum += (Integer) three_shoot_score.getAttribute(row);
					team_foul_shoot_sum += (Integer) foul_shoot_number.getAttribute(row);
					team_miss_sum += (Integer) miss.getAttribute(row);
				}
				this.team_game_time_minute_sum.setAttribute(resultRow, team_game_time_minute_sum);
				this.team_game_time_second_sum.setAttribute(resultRow, team_game_time_second_sum);
				this.team_total_board_sum.setAttribute(resultRow, team_total_board_sum);
				this.team_shoot_sum.setAttribute(resultRow, team_shoot_sum);
				this.team_shoot_score_sum.setAttribute(resultRow, team_shoot_score_sum);
				this.team_three_shoot_sum.setAttribute(resultRow, team_three_shoot_sum);
				this.team_three_shoot_score_sum.setAttribute(resultRow, team_three_shoot_score_sum);
				this.team_foul_shoot_sum.setAttribute(resultRow, team_foul_shoot_sum);
				this.team_miss_sum.setAttribute(resultRow, team_miss_sum);
			}
		};
		
		queryTeam.table = performance;
		queryTeam.collapseColumn = new String[]{"team_name_abbr"};
		queryTeam.derivedColumn = new String[]{"team_game_time_minute_sum","team_game_time_second_sum",
				"team_total_board_sum","three_shoot_sum", "three_shoot_score_sum", "shoot_sum", "shoot_score_sum",
				"team_foul_shoot_sum","team_miss_sum"};
		queryTeam.derivedClass = new Class<?>[]{Integer.class, Integer.class, Integer.class, Integer.class,
				Integer.class, Integer.class, Integer.class, Integer.class, Integer.class};
		
		host.performQuery(queryTeam, "collapseTeam");
		
		
		
		
		GroupQuery queryRival = new GroupQuery(){
			Column match_host_abbr;
			Column match_guest_abbr;
			Column team_name_abbr;
			Column team_attack_board_sum;
			Column team_defence_board_sum;
			Column team_total_board_sum;
			Column rival_attack_board_sum;
			Column rival_defence_board_sum;
			Column rival_total_board_sum;
			Column team_shoot_score_sum;
			Column team_shoot_sum;
			Column rival_shoot_sum;
			Column rival_shoot_score_sum;
			
			@Override
			public void retrieve(Table resultTable){
				match_host_abbr = match.getColumn("match_host_abbr");
				match_guest_abbr = match.getColumn("match_guest_abbr");
				team_name_abbr = this.table.getColumn("team_name_abbr");
				team_attack_board_sum = this.table.getColumn("team_attack_board_sum");
				team_defence_board_sum = this.table.getColumn("team_defence_board_sum");
				team_total_board_sum = this.table.getColumn("team_total_board_sum");
				rival_attack_board_sum = resultTable.getColumn("rival_attack_board_sum");
				rival_defence_board_sum = resultTable.getColumn("rival_defence_board_sum");
				rival_total_board_sum = resultTable.getColumn("rival_total_board_sum");
				team_shoot_score_sum = this.table.getColumn("team_shoot_score_sum");
				team_shoot_sum = this.table.getColumn("team_shoot_sum");
				rival_shoot_sum = resultTable.getColumn("rival_shoot_sum");
				rival_shoot_score_sum = resultTable.getColumn("rival_shoot_score_sum");
			}

			@Override
			public void collapse(Row[] rows, Row resultRow){
				
				Row[] rivalRow = match.getRows();
				
				Integer rival_attack_board_sum = 0;
				Integer rival_defence_board_sum = 0;
				Integer rival_total_board_sum = 0;
				Integer rival_shoot_score_sum = 0;
				Integer rival_shoot_sum = 0;
				
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
						if((!(teamName.equals(rivalHostName)))&&(!(teamName.equals(rivalGuestName)))){
							continue;
						}else{
							if(teamName.equals(rivalGuestName)){
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
									rival_attack_board_sum += (Integer) team_attack_board_sum.getAttribute(rowFinal);
									rival_defence_board_sum += (Integer) team_defence_board_sum.getAttribute(rowFinal);
									rival_total_board_sum += (Integer) team_total_board_sum.getAttribute(rowFinal);
									rival_shoot_sum += (Integer) team_shoot_sum.getAttribute(rowFinal);
									rival_shoot_score_sum += (Integer) team_shoot_score_sum.getAttribute(rowFinal);
								}
							}
						}
					}
				}
				
				this.rival_attack_board_sum.setAttribute(resultRow, rival_attack_board_sum);
				this.rival_defence_board_sum.setAttribute(resultRow, rival_defence_board_sum);
				this.rival_total_board_sum.setAttribute(resultRow, rival_total_board_sum);
				this.rival_shoot_sum.setAttribute(resultRow, rival_shoot_sum);
				this.rival_shoot_score_sum.setAttribute(resultRow, rival_shoot_score_sum);
			}
		};
		
		queryRival.table = host.getTable("collapseTeam");
		queryRival.collapseColumn = new String[]{"team_name_abbr"};
		queryRival.derivedColumn = new String[]{"rival_attack_board_sum","rival_defence_board_sum", "rival_total_board_sum", "rival_shoot_sum", "rival_shoot_score_sum"};
		queryRival.derivedClass = new Class<?>[]{Integer.class, Integer.class, Integer.class};
		
		host.performQuery(queryRival, "collapseRival");
	}
	
	Table collapsePlayer = host.getTable("collapsePlayer");
	Table collapseTeam = host.getTable("collapseTeam");
	Table collapseRival = host.getTable("collapseRival");
	
	public void getCompletePlayerTable()throws Exception{
		Query query = new ExpressionDeriveQuery(collapsePlayer, new DeriveColumnInfo[]{
				new ExpressionDeriveColumnInfo("three_shoot_accuracy", Float.class,
						"1.0F * collapsePlayer.three_shoot_score_sum / collapsePlayer.three_shoot_sum")
				,
				new ExpressionDeriveColumnInfo("shoot_accuracy", Float.class,
						"1.0F * collapsePlayer.shoot_score_sum / collapsePlayer.shoot_sum")
				,
				new ExpressionDeriveColumnInfo("foul_accuracy",Float.class,
						"1.0F * collapsePlayer.foul_shoot_score_sum / collapsePlayer.foul_shoot_number_sum")
				,
				new ExpressionDeriveColumnInfo("efficiency",Integer.class,
						"(collapsePlayer.self_score_sum + collapsePlayer.total_board_sum + collapsePlayer.assist_sum + collapsePlayer.steal_sum + collapsePlayer.cap_sum)-(collapsePlayer.shoot_sum + collapsePlayer.three_shoot_sum - collapsePlayer.shoot_score_sum - collapsePlayer.three_shoot_score_sum)-(collapsePlayer.foul_shoot_sum - performance.foul_shoot_score_sum)-collapsePlayer.miss_sum")
				,
				new ExpressionDeriveColumnInfo("GmSc_efficiency",Float.class,
						"collapsePlayer.self_score_sum + 0.4 * collapsePlayer.shoot_score_sum - 0.7 * collapsePlayer.shoot_sum - 0.4 * (collapsePlayer.foul_shoot_sum - collapsePlayer.foul_shoot_score_sum) + 0.7 * collapsePlayer.attack_board_sum + 0.3 * collapsePlayer.defence_board_sum + collapsePlayer.steal_sum + 0.7 * collapsePlayer.assist_sum + 0.7 * collapsePlayer.cap_sum - 0.4 * collapsePlayer.foul_sum - collapsePlayer.miss_sum")
				,
				new ExpressionDeriveColumnInfo("fact_shoot_accuracy",Float.class,
						"collapsePlayer.self_score_sum / (2 * (collapsePlayer.shoot_sum + 0.44 * collapsePlayer.foul_shoot_sum))")
				,
				new ExpressionDeriveColumnInfo("shoot_efficiency",Float.class,
						"(collapsePlayer.shoot_score_sum + 0.5 * collapsePlayer.three_shoot_score_sum)/collapsePlayer.shoot_sum")
				,
				new ExpressionDeriveColumnInfo("error_rate",Float.class,
						"collapsePlayer.miss_sum / (collapsePlayer.shoot_sum + 0.44 * collapsePlayer.foul_shoot_sum + collapsePlayer.miss_sum)")
				,
				new ExpressionDeriveColumnInfo("rebound_rate",Float.class,
						"collapsePlayer.total_board_sum * ((collapseTeam.team_game_time_minute_sum * 60 + collapseTeam.team_game_time_second_sum) / 5) / (collapsePlayer.game_time_minute_sum * 60 + collapsePlayer.game_time_second_sum) / (collapseTeam.team_total_board_sum + collapseRival.rival_total_board_sum)")
				,
				new ExpressionDeriveColumnInfo("attack_board_rate",Float.class,
						"collapsePlayer.attack_board_sum * ((collapseTeam.team_game_time_minute_sum * 60 + collapseTeam.team_game_time_second_sum) / 5) / (collapsePlayer.game_time_minute_sum * 60 + collapsePlayer.game_time_second_sum) / (collapseTeam.team_attack_board_sum + collapseRival.rival_attack_board_sum)")
				,
				new ExpressionDeriveColumnInfo("defence_board_rate",Float.class,
						"collapsePlayer.defence_board_sum * ((collapseTeam.team_game_time_minute_sum * 60 + collapseTeam.team_game_time_second_sum) / 5) / (collapsePlayer.game_time_minute_sum * 60 + collapsePlayer.game_time_second_sum) / (collapseTeam.team_defence_board_sum + collapseRival.rival_defence_board_sum)")
				,
				new ExpressionDeriveColumnInfo("assist_rate",Float.class,
						"collapsePlayer.assist_sum / ((collapsePlayer.game_time_minute_sum * 60 + collapsePlayer.game_time_second_sum) / ((collapseTeam.team_game_time_minute_sum * 60 + collapseTeam.team_game_time_second_sum) / 5) * (collapseTeam.team_shoot_score_sum + collapseTeam.team_three_shoot_score_sum) - (collapsePlayer.shoot_score_sum + collapsePlayer.three_shoot_score_sum))")
				,
				new ExpressionDeriveColumnInfo("steal_rate",Float.class,
						"collapsePlayer.steal_sum * ((collapseTeam.team_game_time_minute_sum * 60 + collapseTeam.team_game_time_second_sum) / 5) / (collapsePlayer.game_time_minute_sum * 60 + collapsePlayer.game_time_second_sum) / collapseRival.rival_attack_board_sum")
				,
				new ExpressionDeriveColumnInfo("cap_rate",Float.class,
						"collapsePlayer.cap_sum * ((collapseTeam.team_game_time_minute_sum * 60 + collapseTeam.team_game_time_second_sum) / 5) / (collapsePlayer.game_time_minute_sum * 60 + collapsePlayer.game_time_second_sum) / collapseRival.rival_shoot_sum")
				,
				new ExpressionDeriveColumnInfo("use_rate",Float.class,
						"((collapsePlayer.shoot_sum + collapsePlayer.three_shoot_sum) + 0.44 * collapsePlayer.foul_shoot_sum + collapsePlayer.miss_sum) * ((collapseTeam.team_game_time_minute_sum * 60 + collapseTeam.team_game_time_second_sum) / 5) / (collapsePlayer.game_time_minute_sum * 60 + collapsePlayer.game_time_second_sum) / (collapseTeam.team_shoot_sum + collapseTeam.team_three_shoot_sum + 0.44 * collapseTeam.team_foul_shoot_sum + collapseTeam.team_miss_sum)")
				}, "three_shoot_sum", "three_shoot_score_sum", "shoot_sum", "shoot_score_sum","foul_shoot_sum","foul_shoot_score_sum","self_score_sum","total_board_sum","assist_sum","steal_sum","cap_sum","miss_sum","foul_sum","game_time_minute_sum","game_time_second_sum","team_game_time_minute_sum","team_game_time_second_sum","team_shoot_sum","team_three_shoot_sum","team_shoots_score_sum","team_three_shoot_score_sum","rival_total_board_sum","rival_attack_board_sum","rival_defence_board_sum","rival_shoot_sum","rival_three_shoots_sum","team_foul_shoot_sum","team_miss_sum","team_total_board_sum","team_attack_board_sum","team_defence_board_sum","player_name");
		host.performQuery(query, "resultPlayer");
	}
	
	Table resultPlayer = host.getTable("resultPlayer");
	
	@Override
	public Table searchForPlayers(boolean type, String head, boolean upOrDown,String position, String league) {
		// TODO Auto-generated method stub
		if(type==true){
			if(position.equals(null)||league.equals(null)){
				if(upOrDown==true){
					SortQuery query = new SortQuery(resultPlayer, head,  true);
					host.performQuery(query, "resultSort");
				}else{
					SortQuery query = new SortQuery(resultPlayer, head,  false);
					host.performQuery(query, "resultSort");
				}
			}else{
				if(upOrDown==true){
					SortQuery query = new SortQuery(resultPlayer, head, 50, true);
					host.performQuery(query, "resultSort");
				}else{
					SortQuery query = new SortQuery(resultPlayer, head, 50, false);
					host.performQuery(query, "resultSort");
				}
			}
		}
		return host.getTable("resultSort");
	}

}
