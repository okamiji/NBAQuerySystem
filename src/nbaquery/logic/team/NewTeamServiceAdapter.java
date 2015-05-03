package nbaquery.logic.team;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.query.SortQuery;
import nbaquery.logic.average_team.AverageTeam;
import nbaquery.logic.gross_team.GrossTeam;

public class NewTeamServiceAdapter implements NewTeamService
{
	protected GrossTeam gross;
	protected AverageTeam average;
	public TableHost tableHost;
	public String[] columnNames,oneTeamColumns;
	
	public NewTeamServiceAdapter(TableHost tableHost, GrossTeam gross, AverageTeam average)
	{
		this.tableHost = tableHost;
		this.gross = gross;
		this.average = average;
		
		this.gross.getTable();
		this.average.getTable();
	}
	
	@Override
	public Table searchForTeams(boolean isGross, String[] keywords,
			boolean descend)
	{
		Table queryResult;
		if(isGross) queryResult = this.gross.getTable();
		else queryResult = this.average.getTable();
		
		if(keywords == null || keywords.length == 0) keywords = new String[]{"team_name"};
		
		for(int i = keywords.length - 1; i >= 0; i --)
		{
			SortQuery sort = new SortQuery(queryResult, keywords[i]);
			tableHost.performQuery(sort, "team_query_result");
			queryResult = tableHost.getTable("team_query_result");
		}
		
		return queryResult;
	}

	@Override
	public Table searchSeasonHotTeams(String head)
	{
		/*
		return this.searchForTeams(true, new String[]{keywords}, true);
		*/
		Table matchTable = tableHost.getTable("match");
		if(matchTable.hasTableChanged("setchForSeasonHotTeams"))
		{
			Row[] rows = matchTable.getRows();
			Column season = matchTable.getColumn("match_season");
			String latestSeason = (String) season.getAttribute(rows[rows.length - 1]);
			
			Table grossTable = this.gross.getTable();
			try
			{
				tableHost.performQuery(new SelectProjectQuery("gross_team.match_season==\"%season\"".replace("%season", latestSeason), grossTable), "season_hot_team_result");
			}
			catch(Exception e)
			{

			}
		}
		tableHost.performQuery(new SortQuery(tableHost.getTable("season_hot_team_result"), head, 5, true), "season_hot_team_result_final");
		return tableHost.getTable("season_hot_player_team_final");
	}

	@Override
	public Table searchInfoByName(String teamNameAbbr, boolean isAbbr)
	{
		Table queryResult = tableHost.getTable("team");
		SelectProjectQuery query = null;
		try {
			if(isAbbr) query = new SelectProjectQuery("team_query_result.TEAM_NAME_ABBR=='%1'".replace("%1", teamNameAbbr), queryResult);
			else query = new SelectProjectQuery("team_query_result.TEAM_NAME=='%1'".replace("%1", teamNameAbbr), queryResult);
		} catch (Exception e) {	

		}
		tableHost.performQuery(query, "team_query_result");
		return tableHost.getTable("team_query_result");
	}
}
