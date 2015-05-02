package nbaquery.presentation3;

public interface DetailedInfoContainer
{
	public void displayPlayerInfo(String playerName);
	
	public void displayTeamInfo(String teamNameOrAbbr, boolean isAbbr);
	
	public void displayMatchInfo(int matchId);
}
