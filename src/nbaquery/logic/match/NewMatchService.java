package nbaquery.logic.match;

import nbaquery.data.Table;

public interface NewMatchService
{
	public boolean shouldRedoQuery(Object host);
	
	public Table searchForMatchesTable(String[] keyword, String date, String season, boolean descend);
	
	public Table searchForMatchesTable(String[] keyword, String date, String season, boolean[] descend);
	
	public Table listTodayMatches();
	
	public Table searchPerformanceByID(int matchID, String[] header, boolean descend);
	
	public Table searchQuarterScoreByID(int matchID);
	
	public Table searchMatchesByTeamNameAbbr(String team_name_abbr);

	public Table searchMatchesByPlayer(String player_name);

	/**
	 * Search all calculated performance record by player name, used for statistic.
	 * @param player_name the player to select, must be the primary key of player table.
	 * @return the table which is the subset of derived_player_performance, ordered by match_id ascend.
	 */
	public Table searchPerformancesByPlayer(String player_name);
}
