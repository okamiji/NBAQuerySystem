package nbaquery.launcher;

import nbaquery.data.TableHost;
import nbaquery.logic.average_player.AveragePlayer;
import nbaquery.logic.average_player.DerivedPlayerPerformance;
import nbaquery.logic.average_team.AverageTeam;
import nbaquery.logic.average_team.DerivedTeamPerformance;
import nbaquery.logic.gross_player.GrossPlayer;
import nbaquery.logic.gross_player.GrossPlayerPerformance;
import nbaquery.logic.gross_team.GrossRivalPerformance;
import nbaquery.logic.gross_team.GrossTeam;
import nbaquery.logic.gross_team.GrossTeamNaturalJoin;
import nbaquery.logic.gross_team.GrossTeamPerformance;
import nbaquery.logic.hot_player_today.HotPlayerToday;
import nbaquery.logic.hot_player_today.HotPlayerTodayPerformanceSelect;
import nbaquery.logic.infrustructure.DirectMatchNaturalJoinPerformance;
import nbaquery.logic.infrustructure.MatchNaturalJoinPerformance;
import nbaquery.logic.infrustructure.MatchTeamPerformance;
import nbaquery.logic.infrustructure.PlayerPerformance;
import nbaquery.logic.infrustructure.RivalTeamNaturalJoin;
import nbaquery.logic.infrustructure.RivalTeamPerformance;
import nbaquery.logic.player.PlayerService;
import nbaquery.logic.player.PlayerServiceAdapter;
import nbaquery.logic.progress_player.ProgressPlayer;
import nbaquery.logic.progress_player.ProgressPlayerGroup;
import nbaquery.logic.team.TeamService;
import nbaquery.logic.team.TeamServiceAdapter;

public class LogicAssembler implements ILogicAssembler
{

	TeamService team_service;
	PlayerService player_service;
	
	@Override
	public void assemble(TableHost tableHost)
	{
		/**
		 * infrustructures
		 */
		MatchNaturalJoinPerformance match_natural_join_performance = new DirectMatchNaturalJoinPerformance(tableHost);
		//MatchNaturalJoinPerformance match_natural_join_performance = new MatchNaturalJoinPerformance(tableHost);
		
		MatchTeamPerformance match_team_performance = new MatchTeamPerformance(tableHost, match_natural_join_performance);
		RivalTeamPerformance rival_team_performance = new RivalTeamPerformance(tableHost, match_team_performance);
		RivalTeamNaturalJoin rival_team_natural_join = new RivalTeamNaturalJoin(tableHost, rival_team_performance, match_team_performance);
		PlayerPerformance player_performance = new PlayerPerformance(tableHost, match_natural_join_performance, match_team_performance, rival_team_performance);
		
		/**
		 * gross_team
		 */
		GrossTeamPerformance gross_team_performance = new GrossTeamPerformance(tableHost, match_team_performance);
		GrossRivalPerformance gross_rival_performance = new GrossRivalPerformance(tableHost, rival_team_performance);
		GrossTeamNaturalJoin gross_team_natural_join = new GrossTeamNaturalJoin(tableHost, gross_rival_performance, gross_team_performance);
		GrossTeam gross_team = new GrossTeam(tableHost, gross_team_natural_join);
		
		/**
		 * gross_player
		 */
		GrossPlayerPerformance gross_player_performance = new GrossPlayerPerformance(tableHost, player_performance);
		GrossPlayer gross_player = new GrossPlayer(tableHost, gross_player_performance);
		
		/**
		 * average_team
		 */
		
		DerivedTeamPerformance derived_team_performance = new DerivedTeamPerformance(tableHost, rival_team_natural_join);
		AverageTeam average_team = new AverageTeam(tableHost, derived_team_performance);
		
		/**
		 * average_player
		 */
		DerivedPlayerPerformance derive_player_performance = new DerivedPlayerPerformance(tableHost, player_performance);
		AveragePlayer average_player = new AveragePlayer(tableHost, derive_player_performance);
		/**
		 * hot_player
		 */
		HotPlayerTodayPerformanceSelect hot_player_today_p_s = new HotPlayerTodayPerformanceSelect(tableHost,match_natural_join_performance);
		HotPlayerToday hot_player_today=new HotPlayerToday(tableHost,hot_player_today_p_s);
		
		/**
		 * progress_player
		 */
		
		ProgressPlayerGroup progress_player_group = new ProgressPlayerGroup(tableHost,player_performance);
		ProgressPlayer progress_player=new ProgressPlayer(tableHost,progress_player_group);
		
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
				"team_logo"	//"�����־"
		});
		
		player_service = new PlayerServiceAdapter(tableHost, gross_player, average_player,hot_player_today,progress_player, new String[]
		{
				"match_season",//"����"
				"player_name",//"����"
				"team_name",//"���"
				"game_count",//"��������"
				"first_count",//"�ȷ�����"
				"total_board",//"����"
				"assist",//"����"
				"game_time",//"�ڳ�ʱ��"
				"shoot_rate",//"Ͷ��������"
				"three_shoot_rate",//"����������"
				"foul_shoot_rate",//"����������"
				"attack_board",//"����"
				"defence_board",//"����"
				"steal",//"����"
				"cap",//"��ñ"
				"miss",//"ʧ��"
				"foul",//"����"
				"self_score",//"�÷�"
				"efficiency",//"Ч��"
				"gmsc_efficiency",//"GmSc"
				"true_shoot_rate",//"��ʵ������"
				"shoot_efficiency",//"Ͷ��Ч��"
				"total_board_efficiency",//"������"
				"attack_board_efficiency",//"����������"
				"defence_board_efficiency",//"����������"
				"assist_rate",//"������"
				"steal_rate",//"������"
				"cap_rate",//"��ñ��"
				"miss_rate",//"ʧ����"
				"usage",//"ʹ����"
				"player_position",//"��Աλ��"
				"team_sector",//"����"
				"score_board_assist",//"��/��/��"
				"player_portrait", //ͷ��
				"player_action"	//ȫ����
		},
		new String[]{
				"player_name"	, //��Ա����
				"player_number",  //���±��
				"player_position",//��Աλ��
				"player_height"	, //��Ա���
				"player_weight"	, //��Ա����
				"player_birth"	, //��������
				"player_age"	, //����
				"player_exp"	, //����
				"player_school"	, //��ҵѧУ
				"player_portrait",//������
				"player_action"	, //ȫ����
				"self_score",//"�÷�"
				"total_board",//"����"
				"assist",//"����"
				"cap",//"��ñ"
				"steal",//"����"
				
		},new String[]{
				"player_name"	, //��Ա����
				"player_number",  //���±��
				"player_position",//��Աλ��
				"player_height"	, //��Ա���
				"player_weight"	, //��Ա����
				"player_birth"	, //��������
				"player_age"	, //����
				"player_exp"	, //����
				"player_school"	, //��ҵѧУ
				"player_portrait",//������
				"player_action"	, //ȫ����
				"self_score",//���峡�����÷�
				"total_board",//"���峡����"
				"assist"//"���峡����"
		},new String[]{
				"player_name"	, //��Ա����
				"player_number",  //���±��
				"player_position",//��Աλ��
				"player_height"	, //��Ա���
				"player_weight"	, //��Ա����
				"player_birth"	, //��������
				"player_age"	, //����
				"player_exp"	, //����
				"player_school"	, //��ҵѧУ
				"player_portrait",//������
				"player_action"	, //ȫ����
		}
		);
	}

	@Override
	public TeamService getTeamService()
	{
		return team_service;
	}

	@Override
	public PlayerService getPlayerService()
	{
		return player_service;
	}
	
}
