package nbaquery.logic.match;

public interface MatchService {
	public String[][] searchForMatchs(int index, boolean isUp);
	public String[] searchForOneMatch(int matchID);
	public String[] searchForOneMatchByPlayer(String player_name);
}
