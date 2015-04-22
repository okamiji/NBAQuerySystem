package nbaquery.logic.match;

public interface MatchService {
	public String[][] searchForMatchs(int index, boolean isUp);
	public String[][] searchForOneMatchById(int matchID);
	public String[][] searchForMatchsByPlayer(String player_name);
	public String[][] searchForMatchsByDateAndSeason(String date,String season);
	public String[][] searchForMatchsByTeam(String team_name_abbr);
	public String[][] searchOneMatchByPlayerAndID(String player_name,int matchID);
	public String[][] searchOneMatchByTeamAndID(String team_name_abbr,int matchID);
}
