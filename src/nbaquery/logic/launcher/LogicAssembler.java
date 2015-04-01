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
				"match_season", //"����"
				"team_name", //"�������"
				"game",//"��������"
				"shoot_score",//"Ͷ��������"
				"shoot",//"Ͷ�����ִ���"
				"three_shoot_score",//"����������"
				"three_shoot",//"���ֳ�����"
				"foul_shoot_score",//"����������"
				"foul_shoot",//"���������"
				"attack_board",//"����������"
				"defence_board",//"����������"
				"total_board",//"������"
				"assist",//"������"
				"steal",//"������"
				"cap",//"��ñ��"
				"miss",//"ʧ����"
				"foul",//"������"
				"self_score",//"�����÷�"
				"shoot_rate",//"Ͷ��������"
				"three_shoot_rate",//"����������"
				"foul_shoot_rate",//"����������"
				"win_rate",//"ʤ��"
				"attack_round",//"�����غ�"
				"attack_efficiency",//"����Ч��"
				"defence_efficiency",//"����Ч��"
				"attack_board_efficiency",//"��������Ч��"
				"defence_board_efficiency",//"��������Ч��"
				"steal_efficiency",//"����Ч��"
				"assist_efficiency",//"����Ч��"
				});
		
		
	}

	@Override
	public TeamService getTeamService()
	{
		return team_service;
	}
	
}
