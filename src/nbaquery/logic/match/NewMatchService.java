package nbaquery.logic.match;

import nbaquery.data.Table;

public interface NewMatchService
{
	public Table searchForMatchesTable(String keyword, boolean isUp);
	public Table searchForOneMatchByIdTable(int matchID);
	public Table searchForMatchsByPlayerTable(String player_name);
	public Table searchForMatchsByDateAndSeasonTable(String date,String season);
	public Table searchForMatchsByTeamTable(String team_name_abbr);
	public Table searchOneMatchByPlayerAndIDTable(String player_name, int matchID);
	public Table searchOneMatchByTeamAndIDTable(String team_name_abbr, int matchID);
}
