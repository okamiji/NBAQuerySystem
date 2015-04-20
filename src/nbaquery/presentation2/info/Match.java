package nbaquery.presentation2.info;

public class Match {
	private String[] match_info;
	private int index;
	
	
	public Match(String[] string_info){
		/*
		for(int i=0; i<string_info.length; i++){
			System.out.print(string_info[i] + "  ");
		}
		System.out.println(" ");*/
		match_info = string_info;
	}
	public void set_name(String set_name){
		match_info[1] = set_name;
	}
	public String get_name(){
		return match_info[1];
	}
	public String get_team(){
		return match_info[2];
	}
	public String get_portrait_path(){
		//TODO to be changed
		return match_info[33];
	}
	public String get_action_path(){
		return match_info[34];
	}	
	public String[] get_player_info(){
		return match_info;
	}
	
	public void set_index(int i){
		index = i;
	}
	public int get_index(){
		return index;
	}
}
