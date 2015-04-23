package nbaquery.presentation2.card;

import nbaquery.presentation2.util.CardType;

public class CardLocation {	
	int start_width;
	int start_height;
	int gap_width;
	int gap_height;
	int total_height;
	
	boolean is_flat;
	
	CardType type_;
	
	public CardLocation(CardType type){
		type_ = type;
		if(type.equals(CardType.MATCH_FLAT) || (type.equals(CardType.PLAYER_FLAT)) || (type.equals(CardType.TEAM_FLAT))){
			is_flat = true;
		}
		else if(type.equals(CardType.MATCH_RECT) || (type.equals(CardType.PLAYER_RECT)) || (type.equals(CardType.TEAM_RECT))){
			is_flat = false;
		}
		
		start_height = 15;
		if(type.equals(CardType.PLAYER_of_MATCH)){
			start_height = 130;
		}
		if(is_flat){
			start_width = 30;
			gap_height = 53;
		}
		else{
			start_width = 25;
			gap_width = 270;
			gap_height = 110;
			if(type.equals(CardType.MATCH_of_PLAYER) || type.equals(CardType.PLAYER_of_MATCH)){
				gap_height = 220;
			}
		}
	}
	
	public Integer[] get_location(int index){
		Integer[] return_int = new Integer[2];
		if(is_flat){
			return_int[0] = start_width;
			return_int[1] = start_height + index * gap_height;
		}
		else{
			int row = index / 2;
			int column = index % 2;
			return_int[0] = start_width + column * gap_width;
			return_int[1] = start_height + row * gap_height;
		}
		return return_int;
	}
	public int get_total_height(int size){
		if(is_flat){
			total_height = start_height + size * gap_height;			
		}
		else{
			int row = (size + 1) / 2;
			total_height = start_height + row * gap_height;
		}
		return total_height;
	}
}
