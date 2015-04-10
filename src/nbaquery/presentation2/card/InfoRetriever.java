package nbaquery.presentation2.card;

import java.util.ArrayList;

import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.TeamService;
import nbaquery.presentation2.info.Player;
import nbaquery.presentation2.info.Team;
import nbaquery.presentation2.panel.PanelSet;

public class InfoRetriever {
	
	private String[][] player_str;
	private String[][] team_str;
	private ArrayList<Player> player_list;
	private ArrayList<Team> team_list;
	private PlayerService ps;
	private TeamService ts;
	
	public ArrayList<Player> search_for_players(){
		ps = PanelSet.get_player_service();
		
		set_players_str();
		set_players_list();
		
		distinguish_player();
		delete_null_player();
		
		return player_list;
	}
	public ArrayList<Team> search_for_teams(){
		ts = PanelSet.get_team_service();
		
		set_teams_str();
		set_teams_list();
		
		delete_null_team();
		
		return team_list;
	}
	

	
	private void set_players_str(){
		boolean player_isGross = CardProperties.get_player_isGross();
		int player_index = CardProperties.get_player_index();
		boolean player_isUp = CardProperties.get_player_isUp();
		String player_position = CardProperties.get_player_position();
		String player_league = CardProperties.get_player_league();
		player_str = ps.searchForPlayers(player_isGross, player_index, player_isUp, player_position, player_league);
	}
	private void set_teams_str(){
		boolean team_isGross = CardProperties.get_team_isGross();
		int team_index = CardProperties.get_team_index();
		boolean team_isUp = CardProperties.get_team_isUp();
		team_str = ts.searchForTeams(team_isGross, team_index, team_isUp);
	}
	
	private void set_players_list(){
		player_list = new ArrayList<Player>();
		for(int i=0; i<player_str.length; i++){
			Player player = new Player(player_str[i]);
			player_list.add(player);
		}
	}
	private void set_teams_list(){
		team_list = new ArrayList<Team>();
		for(int i=0; i<team_str.length; i++){
			Team team = new Team(team_str[i]);
			team_list.add(team);
		}
	}
	
	//TODO??
	public void set_players(ArrayList<Player> set_player_list){
		player_list = set_player_list;
	}
	public void set_teams(ArrayList<Team> set_team_list){
		team_list = set_team_list;
	}
	
	//TODO ???
	public Team get_team_by_name(String team_name){
		Team return_team = null;
		for(Team team : team_list){
			if(team.get_name().equals(team_name)){
				return_team = team;
				break;
			}
		}
		return return_team;
	}
	
	
	private void distinguish_player(){
		//得到在一个赛季中转会过的球员并处理
		for(int i=0; i<player_list.size(); i++){
			for(int j = i + 1; j<player_list.size(); j++){
				if((player_list.get(i).get_name().equals((player_list).get(j).get_name())) && (i != j)){
					Player player1 = player_list.remove(i);
					Player player2 = player_list.remove(j - 1);
					player1.set_name(player1.get_name() + "（" + player1.get_team() + "）");
					player2.set_name(player2.get_name() + "（" + player2.get_team() + "）");
					player_list.add(i, player1);
					player_list.add(j, player2);
				}
			}
		}
	}
	private void delete_null_player(){
		for(int i=0; i<player_list.size(); i++){
			if(player_list.get(i).get_player_info().length == 0){
				player_list.remove(i);
			}
		}
	}
	private void delete_null_team(){
		for(int i=0; i<team_list.size(); i++){
			if(team_list.get(i).get_team_info().length == 0){
				team_list.remove(i);
			}
		}
	}
	
}
