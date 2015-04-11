package nbaquery.presentation2.info;

public class Player {
	private String[] player_info;
	
	public Player(String[] string_info){
		/*
		for(int i=0; i<string_info.length; i++){
			System.out.print(string_info[i] + "  ");
		}
		System.out.println(" ");*/
		player_info = string_info;
	}
	public void set_name(String set_name){//InfoRetriever
		player_info[1] = set_name;
	}
	public String get_name(){
		return player_info[1];
	}
	public String get_team(){
		return player_info[2];
	}
	public String get_portrait_path(){
		//TODO to be changed
		return player_info[33];
	}
	public String[] get_player_info(){
		return player_info;
	}
}