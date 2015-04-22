package nbaquery.presentation2.card;

import java.util.ArrayList;

import nbaquery.presentation2.addedcard.Card;
import nbaquery.presentation2.addedcard.CardFactory;
import nbaquery.presentation2.util.CardType;
import nbaquery.presentation2.info.Player;
import nbaquery.presentation2.info.Team;
import nbaquery.presentation2.info.Match;

public class CardCreator {
	
	int total_height;
	
	public ArrayList<Card> create_needed_cards(CardType type, String[][] str, boolean view_all){
		ArrayList<Object> list = null;
		if(type.equals(CardType.PLAYER_FLAT) || (type.equals(CardType.PLAYER_RECT))){
			list = turn_player_list(str);
		}
		else if(type.equals(CardType.TEAM_FLAT) || (type.equals(CardType.TEAM_RECT))){
			list = turn_team_list(str);
		}
		else if(type.equals(CardType.MATCH_FLAT) || (type.equals(CardType.MATCH_RECT) || (type.equals(CardType.MATCH_of_PLAYER)))){
			list = turn_match_list(str);
		}
		ArrayList<Card> card_list = null;
		if(!view_all){
			if(list.size() > 19){
				ArrayList<Object> new_list = new ArrayList<Object>();
				for(int i=0; i<19; i++){
					new_list.add(list.get(i));
				}
				list = new_list;
			}
			card_list = create_cards(type, list);
			Card card = CardFactory.create(type, null, true);
			card_list.add(card);
		}
		else{
			card_list = create_cards(type, list);
		}
		CardLocation location = new CardLocation(type);
		for(int i=0; i<card_list.size(); i++){
			card_list.get(i).width = location.get_location(i)[0];
			card_list.get(i).height = location.get_location(i)[1];
		}
		return card_list;
	}
	
	private ArrayList<Object> turn_player_list(String[][] str){
		ArrayList<Object> list = new ArrayList<Object>();
		for(int i=0; i<str.length; i++){
			Player player = new Player(str[i]);
			player.set_index(i);
			list.add(player);
		}
		return list;
	}
	
	private ArrayList<Object> turn_team_list(String[][] str){
		ArrayList<Object> list = new ArrayList<Object>();
		if(str == null){
			System.out.println("is null");
		}
		for(int i=0; i<str.length; i++){
			Team team = new Team(str[i]);
			team.set_index(i);
			list.add(team);
		}
		return list;
	}
	private ArrayList<Object> turn_match_list(String[][] str){
		ArrayList<Object> list = new ArrayList<Object>();
		if(str == null){
			System.out.println("is null");
		}
		for(int i=0; i<str.length; i++){
			Match match = new Match(str[i]);
			match.set_index(i);
			list.add(match);
		}
		return list;
		
	}
	
	private ArrayList<Card> create_cards(CardType type, ArrayList<Object> list){
		ArrayList<Card> card_list = new ArrayList<Card>();
		for(int i=0; i<list.size(); i++){
			Card card = CardFactory.create(type, list.get(i), false);
			card_list.add(card);
		}
		return card_list;
	}
	
	
}