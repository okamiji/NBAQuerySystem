package nbaquery.presentation2.info;

public class Team {
	private String[] team_info;
	private int index;
	
	public Team(String[] string_info){
		team_info = string_info;
		for(int i=0; i<team_info.length; i++){
			System.out.println(i + " " + team_info[i]);
		}
	}
	public String get_name(){
		return team_info[1];
	}
	public String get_portrait_path(){
		return team_info[29];
	}
	public String[] get_team_info(){
		return team_info;
	}
	
	public void set_index(int i){
		index = i;
	}
	public int get_index(){
		return index;
	}
}
