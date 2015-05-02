package nbaquery.presentation3;

import nbaquery.data.Row;

public interface DetailedInfoContainer
{
	public void displayPlayerInfo(Row player);
	
	public void displayTeamInfo(Row team);
	
	public void displayMatchInfo(int matchId);
}
