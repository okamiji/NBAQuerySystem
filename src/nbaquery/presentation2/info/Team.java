package nbaquery.presentation2.info;

public class Team {
	private String[] team_info;
	
	public Team(String[] string_info){
		team_info = string_info;
	}
	public String get_name(){
		return team_info[0];
	}
	public String get_portrait_path(){
		//TODO to be changed
		return team_info[1];
	}
	public String[] get_team_info(){
		return team_info;
	}
}
