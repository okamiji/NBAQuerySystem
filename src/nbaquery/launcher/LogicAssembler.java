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
import nbaquery.logic.hot_team_today.HotTeamToday;
import nbaquery.logic.hot_team_today.HotTeamTodayPerformanceSelect;
import nbaquery.logic.infrustructure.DirectMatchNaturalJoinPerformance;
import nbaquery.logic.infrustructure.MatchNaturalJoinPerformance;
import nbaquery.logic.infrustructure.MatchTeamPerformance;
import nbaquery.logic.infrustructure.PlayerPerformance;
import nbaquery.logic.infrustructure.RivalTeamNaturalJoin;
import nbaquery.logic.infrustructure.RivalTeamPerformance;
import nbaquery.logic.match.MatchService;
import nbaquery.logic.match.MatchServiceAdapter;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.logic.player.NewPlayerServiceAdapter;
import nbaquery.logic.player.PlayerService;
import nbaquery.logic.player.PlayerServiceAdapter;
import nbaquery.logic.progress_player.ProgressPlayer;
import nbaquery.logic.progress_player.ProgressPlayerGroup;
import nbaquery.logic.team.NewTeamService;
import nbaquery.logic.team.NewTeamServiceAdapter;
import nbaquery.logic.team.TeamService;
import nbaquery.logic.team.TeamServiceAdapter;

public class LogicAssembler implements ILogicAssembler
{

	TeamService team_service;
	PlayerService player_service;
	MatchService match_service;
	
	NewTeamService new_team_service;
	NewPlayerService new_player_service;
	
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
		
		ProgressPlayerGroup progress_player_group = new ProgressPlayerGroup(tableHost,match_natural_join_performance);
		ProgressPlayer progress_player = new ProgressPlayer(tableHost,progress_player_group);
		
		/**
		 * hot_team
		 */
		HotTeamTodayPerformanceSelect hot_team_select = new HotTeamTodayPerformanceSelect(tableHost, match_natural_join_performance);
		HotTeamToday hot_team_today = new HotTeamToday(tableHost, hot_team_select);
		
		new_team_service = new NewTeamServiceAdapter(tableHost, gross_team, average_team, hot_team_today);
		new_player_service = new NewPlayerServiceAdapter(tableHost, gross_player, average_player,hot_player_today,progress_player);
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

	@Override
	public MatchService getMatchService() {
		// TODO Auto-generated method stub
		return match_service;
	}
	
	public NewTeamService getNTS(){
		return new_team_service;
	}
	public NewPlayerService getNPS(){
		return new_player_service;
	}
	
}
