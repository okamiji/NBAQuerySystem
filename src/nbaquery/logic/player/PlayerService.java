package nbaquery.logic.player;

public interface PlayerService
{
	public String[][] searchForPlayers(boolean isGross, int index, boolean isUp, String position, String league);
}
