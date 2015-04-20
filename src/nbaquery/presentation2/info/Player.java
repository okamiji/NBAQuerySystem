package nbaquery.presentation2.info;

public class Player {
	private String[] player_info;
	private int index;
	
	public Player(String[] string_info){
		player_info = string_info;
	}
	public void set_name(String set_name){
		player_info[1] = set_name;
	}
	public String get_name(){
		return player_info[1];
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