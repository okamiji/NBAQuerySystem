package nbaquery.presentation3;

import nbaquery.data.Row;

public interface DetailedInfoContainer
{
	public void displayPlayerInfo(Row player, boolean stacked);
	
	public void displayTeamInfo(Row team, boolean stacked);
	
	public void displayMatchInfo(int matchId, boolean stacked);
}
