package nbaquery.logic.match;

import nbaquery.data.Table;

public interface NewMatchService
{
	public Table searchForMatchesTable(String[] keyword, boolean descend);
	
	public Table searchPerformanceByID(int matchID);
	
	public Table searchPerformanceByPlayer(String player_name);
	
	public Table searchMatchesByDateAndSeason(String date, String season);
	
	public Table searchMatchesByTeamNameAbbr(String team_name_abbr);
}
