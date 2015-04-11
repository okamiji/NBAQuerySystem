package nbaquery.presentation2.card;

import java.util.ArrayList;

import nbaquery.presentation2.card.Card;
import nbaquery.presentation2.info.Player;
import nbaquery.presentation2.info.Team;

public class CardCreator {
	//Create cards with given information from filter.
	
	protected ArrayList<Card> create_player_cards(){
		InfoFilter filter = new InfoFilter();
		ArrayList<Player> player_list = filter.filter_players();
		ArrayList<Card> card_list = new ArrayList<Card>();
		
		for(int i=0; i<player_list.size(); i++){
			Player player = player_list.get(i);
			Card card = new Card();
			card.set_player_info(player, i+1);
			card_list.add(card);
		}
		if(!CardProperties.get_view_more_forbid()){
			if(CardProperties.get_if_view_more_card_needed()){
				Card added_card = new Card();
				added_card.set_view_more(1);
				card_list.add(added_card);
			}			
		}
		
		return card_list;
	}
	protected ArrayList<Card> create_team_cards(){
		InfoFilter filter = new InfoFilter();
		ArrayList<Team> team_list = filter.filter_teams();
		ArrayList<Card> card_list = new ArrayList<Card>();

		for(int i=0; i<team_list.size(); i++){
			Team team = team_list.get(i);
			Card card = new Card();
			card.set_team_info(team, i+1);
			card_list.add(card);
		}
		if(!CardProperties.get_view_more_forbid()){
			if(CardProperties.get_if_view_more_card_needed()){
				Card added_card = new Card();
				added_card.set_view_more(2);
				card_list.add(added_card);
			}
		}
		
		return card_list;
	}

}