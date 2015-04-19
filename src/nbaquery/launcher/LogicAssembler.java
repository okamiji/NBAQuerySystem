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
				"team_logo"	//"队伍标志"
		});
		
		player_service = new PlayerServiceAdapter(tableHost, gross_player, average_player,hot_player_today,progress_player, new String[]
		{
				"match_season",//"赛季"
				"player_name",//"姓名"
				"team_name",//"球队"
				"game_count",//"参赛场数"
				"first_count",//"先发场数"
				"total_board",//"篮板"
				"assist",//"助攻"
				"game_time",//"在场时间"
				"shoot_rate",//"投篮命中率"
				"three_shoot_rate",//"三分命中率"
				"foul_shoot_rate",//"罚球命中率"
				"attack_board",//"进攻"
				"defence_board",//"防守"
				"steal",//"抢断"
				"cap",//"盖帽"
				"miss",//"失误"
				"foul",//"犯规"
				"self_score",//"得分"
				"efficiency",//"效率"
				"gmsc_efficiency",//"GmSc"
				"true_shoot_rate",//"真实命中率"
				"shoot_efficiency",//"投篮效率"
				"total_board_efficiency",//"篮板率"
				"attack_board_efficiency",//"进攻篮板率"
				"defence_board_efficiency",//"防守篮板率"
				"assist_rate",//"助攻率"
				"steal_rate",//"抢断率"
				"cap_rate",//"盖帽率"
				"miss_rate",//"失误率"
				"usage",//"使用率"
				"player_position",//"球员位置"
				"team_sector",//"联盟"
				"score_board_assist",//"分/板/助"
				"player_portrait", //头像
				"player_action"	//全身照
		},
		new String[]{
				"player_name"	, //球员名称
				"player_number",  //球衣编号
				"player_position",//球员位置
				"player_height"	, //球员身高
				"player_weight"	, //球员体重
				"player_birth"	, //出生日期
				"player_age"	, //年龄
				"player_exp"	, //球龄
				"player_school"	, //毕业学校
				"player_portrait",//半身照
				"player_action"	, //全身照
				"self_score",//"得分"
				"total_board",//"篮板"
				"assist",//"助攻"
				"cap",//"盖帽"
				"steal",//"抢断"
				
		},new String[]{
				"player_name"	, //球员名称
				"player_number",  //球衣编号
				"player_position",//球员位置
				"player_height"	, //球员身高
				"player_weight"	, //球员体重
				"player_birth"	, //出生日期
				"player_age"	, //年龄
				"player_exp"	, //球龄
				"player_school"	, //毕业学校
				"player_portrait",//半身照
				"player_action"	, //全身照
				"self_score",//近五场场均得分
				"total_board",//"近五场篮板"
				"assist"//"近五场助攻"
		},new String[]{
				"player_name"	, //球员名称
				"player_number",  //球衣编号
				"player_position",//球员位置
				"player_height"	, //球员身高
				"player_weight"	, //球员体重
				"player_birth"	, //出生日期
				"player_age"	, //年龄
				"player_exp"	, //球龄
				"player_school"	, //毕业学校
				"player_portrait",//半身照
				"player_action"	, //全身照
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
