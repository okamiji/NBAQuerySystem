package nbaquery.logic.match;

import nbaquery.data.Table;

public interface NewMatchService
{
	public Table searchForMatchesTable(String[] keyword, String date, String season, boolean descend);
	
	public Table searchPerformanceByID(int matchID);
	
	public Table searchPerformanceByPlayer(String player_name);
	
	public Table searchMatchesByTeamNameAbbr(String team_name_abbr);
}
