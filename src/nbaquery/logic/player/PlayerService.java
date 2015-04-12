package nbaquery.logic.player;

public interface PlayerService
{
	public String[][] searchForPlayers(boolean isGross, int index, boolean isUp, String position, String league);
	public String[][] searchForTodayHotPlayers(int head);
	public String[][] searchForProgressPlayers(int head);
	public String[][] searchForSeasonHotPlayers(int head);
	public String[] searchForOnePlayer(String playerName);
}
