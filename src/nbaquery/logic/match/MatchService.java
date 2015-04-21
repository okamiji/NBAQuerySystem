package nbaquery.logic.match;

public interface MatchService {
	public String[][] searchForMatchs(int index, boolean isUp);
	public String[][] searchForOneMatchById(int matchID);
	public String[][] searchForMatchsByPlayer(String player_name);
	public String[][] searchForMatchsByDate(String date);
	public String[][] searchOneMatchByPlayerAndID(String player_name,int matchID);
	public String[][] searchOneMatchByTeamAndID(String team_name_abbr,int matchID);
}
