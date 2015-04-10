package nbaquery.presentation2.card;

import java.util.ArrayList;

import nbaquery.presentation2.card.InfoRetriever;
import nbaquery.presentation2.info.Player;
import nbaquery.presentation2.info.Team;

public class InfoFilter {
	//Retrieve information and decide which information to show.
	//Give card creator what cards to create.
	
	private ArrayList<Player> player_list;
	private ArrayList<Team> team_list;
	
	private int view_limit;
	private boolean if_view_all;
	
	protected InfoFilter(){
		view_limit = CardProperties.get_view_limit();
		if_view_all = CardProperties.get_if_view_all();
	}
	
	protected ArrayList<Player> filter_players(){
		InfoRetriever retriever = new InfoRetriever();
		player_list = retriever.search_for_players();
		CardProperties.set_if_view_more_card_needed(false);
		if(!if_view_all){
			filter_player_list();
		}
		return player_list;
	}
	protected ArrayList<Team> filter_teams(){
		InfoRetriever retriever = new InfoRetriever();
		team_list = retriever.search_for_teams();
		CardProperties.set_if_view_more_card_needed(false);
		if(!if_view_all){
			filter_team_list();
		}
		return team_list;
	}
	
	private void filter_player_list(){
		int list_size = player_list.size();
		if(list_size > view_limit){
			ArrayList<Player> tmp_player_list = new ArrayList<Player>();
			for(int i=0; i<view_limit; i++){
				tmp_player_list.add(player_list.get(i));
			}
			player_list = tmp_player_list;
			CardProperties.set_if_view_more_card_needed(true);
		}
	}
	private void filter_team_list(){
		int list_size = team_list.size();
		if(list_size > view_limit){
			ArrayList<Team> tmp_team_list = new ArrayList<Team>();
			for(int i=0; i<view_limit; i++){
				tmp_team_list.add(team_list.get(i));
			}
			team_list = tmp_team_list;
			CardProperties.set_if_view_more_card_needed(true);
		}
	}
	
}
