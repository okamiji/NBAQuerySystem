package nbaquery.presentation2.info;

public class Match {
	private String[] match_info;
	private int index;
	
	
	public Match(String[] string_info){
		//TODO
	/*	for(int i=0; i<string_info.length; i++){
			System.out.print(string_info[i] + "  ");
		}
		System.out.println(" ");*/
		match_info = string_info;
	}
	public String get_id(){
		return match_info[0];
	}
	public String get_season(){
		return match_info[1];
	}
	public String get_date(){
		return match_info[2];
	}
	public String[] get_team(){
		String[] return_str = new String[2];
		return_str[0] = match_info[3];
		return_str[1] = match_info[4];
		return return_str;
	}
	public String[] get_score(){
		String[] return_str = new String[2];
		return_str[0] = match_info[5];
		return_str[1] = match_info[6];
		return return_str;
	}
	public String[] get_logo(){
		//TODO
		String[] return_str = new String[2];
		return_str[0] = match_info[3];
		return_str[1] = match_info[4];
		return return_str;
	}
	
	public void set_index(int i){
		index = i;
	}
	public int get_index(){
		return index;
	}
	
	public String[] get_match_info(){
		return match_info;
	}
}
