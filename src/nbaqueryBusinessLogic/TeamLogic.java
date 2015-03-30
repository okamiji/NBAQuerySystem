package nbaqueryBusinessLogic;
import java.util.ArrayList;

import nbaquery.data.Table;
import nbaqueryBusinessLogicService.TeamService;
import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.TableHost;
import nbaquery.data.query.ExpressionDeriveColumnInfo;
import nbaquery.data.query.ExpressionDeriveQuery;
import nbaquery.data.query.DeriveColumnInfo;
import nbaquery.data.query.GroupQuery;
import nbaquery.data.query.Query;
import nbaquery.data.query.SortQuery;

public class TeamLogic implements TeamService{

	TableHost host;
	public TeamLogic(TableHost host)
	{
		this.host = host;
		performance = host.getTable("performance");
		match = host.getTable("match");
	}
	
	Table performance;
	Table match;
	
	public void getTotalData()throws Exception{
		GroupQuery queryTeam = new GroupQuery(){
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
				Integer team_total_board_sum = 0;
				Integer team_shoot_sum = 0;
				Integer team_shoot_score_sum = 0;
				Integer team_three_shoot_sum = 0;
				Integer team_three_shoot_score_sum = 0;
				Integer team_foul_shoot_sum = 0;
				Integer team_miss_sum = 0;
				
				for(Row row : rows){
					team_total_board_sum += (Integer) total_board.getAttribute(row);
					team_shoot_sum += (Integer) shoot_number.getAttribute(row);
					team_shoot_score_sum += (Integer) shoot_score.getAttribute(row);
					team_three_shoot_sum += (Integer) three_shoot_number.getAttribute(row);
					team_three_shoot_score_sum += (Integer) three_shoot_score.getAttribute(row);
					team_foul_shoot_sum += (Integer) foul_shoot_number.getAttribute(row);
					team_miss_sum += (Integer) miss.getAttribute(row);
				}
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
		queryTeam.derivedColumn = new String[]{"team_total_board_sum","three_shoot_sum", "three_shoot_score_sum", "shoot_sum", "shoot_score_sum",
				"team_foul_shoot_sum","team_miss_sum"};
		queryTeam.derivedClass = new Class<?>[]{Integer.class, Integer.class, Integer.class, Integer.class,
				Integer.class, Integer.class, Integer.class};
		
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
			
			@Override
			public void retrieve(Table resultTable){
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
									rival_attack_board_sum += (Integer) team_attack_board_sum.getAttribute(rowFinal);
									rival_defence_board_sum += (Integer) team_defence_board_sum.getAttribute(rowFinal);
								}
							}
						}
					}
				}
				
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
		queryRival.derivedColumn = new String[]{"rival_attack_board_sum","rival_defence_board_sum","team_success","rival_success","team_score","rival_score"};
		queryRival.derivedClass = new Class<?>[]{Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class};
		
		host.performQuery(queryRival, "collapseRivalTotal");
		
		collapseTeamTotal = host.getTable("collapseTeamTotal");
		collapseRivalTotal = host.getTable("collapseRivalTotal");
	}
	
	Table collapseTeamTotal;
	Table collapseRivalTotal;
	
	public void getCompleteTeamTable()throws Exception{
		Query query = new ExpressionDeriveQuery(collapseTeamTotal, new DeriveColumnInfo[]{
				new ExpressionDeriveColumnInfo("three_shoot_accuracy", Float.class,
						"1.0F * collapseTeamTotal.three_shoot_score_sum / collapseTeamTotal.three_shoot_sum")
				,
				new ExpressionDeriveColumnInfo("shoot_accuracy", Float.class,
						"1.0F * collapseTeamTotal.shoot_score_sum / collapseTeamTotal.shoot_sum")
				,
				new ExpressionDeriveColumnInfo("foul_accuracy",Float.class,
						"1.0F * collapseTeamTotal.team_foul_shoot_score_sum/collapseTeamTotal.team_foul_shoot_sum")
				,
				new ExpressionDeriveColumnInfo("win_rate",Float.class,
						"collapseRivalTotal.team_success / (collapseRivalTotal.team_success + collapseRivalTotal.rival_success)")
				,
				new ExpressionDeriveColumnInfo("attack_round",Float.class,
						"collapseTeamTotal.team_shoot_sum + 0.4 * collapseTeamTotal.team_foul_shoot_sum - 1.07 * (collapseTeamTotal.team_attack_board_sum / (collapseTeamTotal.team_attack_board_sum + collapseRivalTotal.team_defence_board_sum) * (collapseTeamTotal.team_shoot_sum - collapseTeamTotal.team_shoot_score_sum)) + 1.07 * collapseTeamTotal.team_miss_sum")
				,
				new ExpressionDeriveColumnInfo("defence_round",Float.class,
						"collapseTeamTotal.team_shoot_sum + 0.4 * collapseTeamTotal.team_foul_shoot_sum - 1.07 * (collapseTeamTotal.team_defence_board_sum / (collapseTeamTotal.team_defence_board_sum + collapseRivalTotal.team_attack_board_sum) * (collapseTeamTotal.team_shoot_sum - collapseTeamTotal.team_shoot_score_sum)) + 1.07 * collapseTeamTotal.team_miss_sum")
				,
				new ExpressionDeriveColumnInfo("attack_efficiency",Float.class,
						"collapseRivalTotal.team_score / attack_round * 100")
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
				}, "team_three_shoot_sum", "team_three_shoot_score_sum", "team_shoot_sum", "team_shoot_score_sum", "team_foul_shoot_sum","attack_round","defence_round","team_steal_sum","team_assist_sum","player_name");
		
		host.performQuery(query, "resultTeam");
		resultTeam = host.getTable("resultTeam");
	}
	
	Table resultTeam;
	
	@Override
	public Table searchForTeams(boolean type, String head, boolean upOrDown) {
		// TODO Auto-generated method stub
		if(type==true){
			if(upOrDown==true){
				SortQuery query = new SortQuery(resultTeam, head,  true);
				host.performQuery(query, "resultSort");
			}else{
				SortQuery query = new SortQuery(resultTeam, head,  false);
				host.performQuery(query, "resultSort");
			}
		}
		return host.getTable("resultSort");
	}

}
