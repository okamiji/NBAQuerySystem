package nbaquery.presentation2.card;

import java.util.ArrayList;

import nbaquery.presentation2.card.Card;

public class CardLocation {
	//Arrange the location every card should be.
	CardCreator creator;
	
	int start_width;
	int start_height;
	int gap_width;
	int gap_height;
	int total_height;

	int section;
	int cards_per_row;
	int view_limit;
	
	private ArrayList<Card> card_list;
	
	public CardLocation(int player_or_team_or_match){
		creator = new CardCreator();
		
		cards_per_row = CardProperties.get_cards_per_row();
		view_limit = CardProperties.get_view_limit();
		
		section = player_or_team_or_match;
		
		start_height = 25;
		switch(cards_per_row){
		case 1:
			start_width = 30;
			gap_height = 53;
			break;
		case 2:
			start_width = 25;
			gap_width = 270;
			gap_height = 110;
			break;
		}
		
	}
	
	public void run(){
		switch(section){
		case 1: 
			card_list = creator.create_player_cards();
			break;
		case 2:
			card_list = creator.create_team_cards();
			break;
		case 3:
			//TODO
			break;
		}
	}
	
	public ArrayList<Card> get_card_list(){
		return card_list;
	}
	public Integer[] get_location(int index){
		Integer[] return_int = new Integer[2];
		if(cards_per_row == 2){
			int row = index / 2;
			int column = index % 2;
			return_int[0] = start_width + column * gap_width;
			return_int[1] = start_height + row * gap_height;
		}
		else if(cards_per_row == 1){
			return_int[0] = start_width;
			return_int[1] = start_height + index * gap_height;
		}
//		System.out.println("card_width: " + return_int[0] + " card_height: " + return_int[1]);
		return return_int;
	}
	public void set_total_height(int size){
		if(cards_per_row == 2){
			int row = (size + 1) / 2;
			total_height = start_height + row * gap_height;
		}
		else if(cards_per_row == 1){
			total_height = start_height + size * gap_height;
		}
	}
	public int get_total_height(){
		return total_height;
	}
}
