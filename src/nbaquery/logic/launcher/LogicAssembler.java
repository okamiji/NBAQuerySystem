package nbaquery.logic.launcher;

import nbaquery.data.TableHost;
import nbaquery.logic.average_team.AverageTeam;
import nbaquery.logic.average_team.DerivedTeamPerformance;
import nbaquery.logic.average_team.RivalTeamNaturalJoin;
import nbaquery.logic.gross_team.GrossRivalPerformance;
import nbaquery.logic.gross_team.GrossTeam;
import nbaquery.logic.gross_team.GrossTeamNaturalJoin;
import nbaquery.logic.gross_team.GrossTeamPerformance;
import nbaquery.logic.infrustructure.MatchNaturalJoinPerformance;
import nbaquery.logic.infrustructure.MatchTeamPerformance;
import nbaquery.logic.infrustructure.RivalTeamPerformance;
import nbaquery.logic.team.TeamService;
import nbaquery.logic.team.TeamServiceAdapter;

public class LogicAssembler implements ILogicAssembler
{

	TeamService team_service;
	
	@Override
	public void assemble(TableHost tableHost)
	{
		/**
		 * infrustructures
		 */
		MatchNaturalJoinPerformance match_natural_join_performance = new MatchNaturalJoinPerformance(tableHost);
		MatchTeamPerformance match_team_performance = new MatchTeamPerformance(tableHost, match_natural_join_performance);
		RivalTeamPerformance rival_team_performance = new RivalTeamPerformance(tableHost, match_team_performance);
		
		/**
		 * gross_team
		 */
		GrossTeamPerformance gross_team_performance = new GrossTeamPerformance(tableHost, match_team_performance);
		GrossRivalPerformance gross_rival_performance = new GrossRivalPerformance(tableHost, rival_team_performance);
		GrossTeamNaturalJoin gross_team_natural_join = new GrossTeamNaturalJoin(tableHost, gross_rival_performance, gross_team_performance);
		GrossTeam gross_team = new GrossTeam(tableHost, gross_team_natural_join);
		
		/**
		 * average
		 */
		
		RivalTeamNaturalJoin rival_team_natural_join = new RivalTeamNaturalJoin(tableHost, rival_team_performance, match_team_performance);
		DerivedTeamPerformance derived_team_performance = new DerivedTeamPerformance(tableHost, rival_team_natural_join);
		AverageTeam average_team = new AverageTeam(tableHost, derived_team_performance);
		
		/**
		 * team
		 */
		team_service = new TeamServiceAdapter(tableHost, gross_team, average_team, new String[]
				{
				"match_season", //"赛季"
				"team_name", //"球队名称"
				"game",//"比赛场数"
				"shoot_score",//"投篮命中数"
				"shoot",//"投篮出手次数"
				"three_shoot_score",//"三分命中数"
				"three_shoot",//"三分出手数"
				"foul_shoot_score",//"罚球命中数"
				"foul_shoot",//"罚球出手数"
				"attack_board",//"进攻篮板数"
				"defence_board",//"防守篮板数"
				"total_board",//"篮板数"
				"assist",//"助攻数"
				"steal",//"抢断数"
				"cap",//"盖帽数"
				"miss",//"失误数"
				"foul",//"犯规数"
				"self_score",//"比赛得分"
				"shoot_rate",//"投篮命中率"
				"three_shoot_rate",//"三分命中率"
				"foul_shoot_rate",//"罚球命中率"
				"win_rate",//"胜率"
				"attack_round",//"进攻回合"
				"attack_efficiency",//"进攻效率"
				"defence_efficiency",//"防守效率"
				"attack_board_efficiency",//"进攻篮板效率"
				"defence_board_efficiency",//"防守篮板效率"
				"steal_efficiency",//"抢断效率"
				"assist_efficiency",//"助攻效率"
				});
		
		
	}

	@Override
	public TeamService getTeamService()
	{
		return team_service;
	}
	
}
