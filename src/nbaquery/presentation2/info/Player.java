package nbaquery.presentation2.info;

import nbaquery.presentation2.panel.ConcisePara;
import nbaquery.presentation2.util.HotspotType;

public class Player {
	private String[] player_info;
	private int index;
	private boolean is_hot;
	private HotspotType type;
	
	public Player(String[] string_info){
		is_hot = ConcisePara.is_hot;
		if(!is_hot){
			player_info = string_info;
		}
		else{
			type = ConcisePara.hotspot_type;
		}
	}
	public String get_name(){
		String return_str = player_info[1];
		if(is_hot){
			switch(type){
			case SEASON_PLAYER:
				break;
			case DAILY_PLAYER:
				return_str = player_info[0];
				break;
			case PROGRESS_PLAYER:
				return_str = player_info[0];
				break;
			default:
				break;
			}
		}
		return return_str;
	}
	public String get_team(){
		return player_info[2];
	}
	public String get_portrait_path(){
		return player_info[33];
	}
	public String get_action_path(){
		return player_info[34];
	}	
	public String[] get_player_info(){
		return player_info;
	}
	
	public void set_index(int i){
		index = i;
	}
	public int get_index(){
		return index;
	}
}