package nbaquery.logic.team;

import nbaquery.data.Column;
import nbaquery.data.Cursor;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.query.SortQuery;
import nbaquery.logic.average_team.AverageTeam;
import nbaquery.logic.gross_team.GrossTeam;
import nbaquery.logic.hot_team_today.HotTeamToday;

public class NewTeamServiceAdapter implements NewTeamService
{
	protected GrossTeam gross;
	protected AverageTeam average;
	protected HotTeamToday hot;
	public TableHost tableHost;
	public String[] columnNames,oneTeamColumns;
	
	public NewTeamServiceAdapter(TableHost tableHost, GrossTeam gross, AverageTeam average, HotTeamToday hot)
	{
		this.tableHost = tableHost;
		this.gross = gross;
		this.average = average;
		this.hot = hot;
		
		this.gross.getTable();
		this.average.getTable();
		this.hot.getTable();
	}
	
	@Override
	public Table searchForTeams(boolean isGross, String[] keywords,
			boolean descend[])
	{
		Table queryResult;
		if(isGross) queryResult = this.gross.getTable();
		else queryResult = this.average.getTable();

		for(int i = keywords.length - 1; i >= 0; i --)
		{
			SortQuery sort = new SortQuery(queryResult, keywords[i], descend[i]);
			tableHost.performQuery(sort, "team_query_result_sorted_" + i);
			queryResult = tableHost.getTable("team_query_result_sorted_" + i);
		}
		
		return queryResult;
	}
	
	@Override
	public Table searchForTeams(boolean isGross, String[] keywords,
			boolean descend)
	{
		boolean[] sortDescend;
		if(keywords == null || keywords.length == 0)
		{
			keywords = new String[]{"team_name"};
			sortDescend = new boolean[]{descend};
		}
		else
		{
			sortDescend = new boolean[keywords.length];
			for(int i = 0; i < keywords.length; i ++)
				sortDescend[i] = descend;
		}
		
		return this.searchForTeams(isGross, keywords, sortDescend);
	}

	@Override
	public Table searchSeasonHotTeams(String head)
	{
		/*
		return this.searchForTeams(true, new String[]{keywords}, true);
		*/
		Table matchTable = tableHost.getTable("matches");
		if(matchTable.hasTableChanged("setchForSeasonHotTeams"))
		{
			Cursor rows = matchTable.getRows();
			rows.absolute(rows.getLength() - 1);
			Column season = matchTable.getColumn("match_season");
			String latestSeason = (String) season.getAttribute(rows.next());
			
			Table grossTable = this.gross.getTable();
			try
			{
				tableHost.performQuery(new SelectProjectQuery(String.format("match_season==\"%s\"", latestSeason), grossTable), "season_hot_team_result");
			}
			catch(Exception e)
			{

			}
		}
		tableHost.performQuery(new SortQuery(tableHost.getTable("season_hot_team_result"), head, 5, true), "season_hot_team_result_final");
		return tableHost.getTable("season_hot_team_result_final");
	}

	@Override
	public Table searchInfoByName(String teamNameAbbr, boolean isAbbr)
	{
		Table queryResult = tableHost.getTable("team");
		SelectProjectQuery query = null;
		try {
			if(isAbbr) query = new SelectProjectQuery(String.format("team_name_abbr=='%s'", teamNameAbbr), queryResult);
			else query = new SelectProjectQuery(String.format("team_name=='%s'", teamNameAbbr), queryResult);
		} catch (Exception e) {	
		}
		tableHost.performQuery(query, "team_query_result");
		return tableHost.getTable("team_query_result");
	}

	@Override
	public boolean shouldRedoQuery(Object host)
	{
		boolean shouldRedo = tableHost.getTable("team").hasTableChanged(host);
		shouldRedo |= tableHost.getTable("matches").hasTableChanged(host);
		shouldRedo |= tableHost.getTable("match_natural_join_performance").hasTableChanged(host);
		return shouldRedo;
	}

	@Override
	public Table searchTodayHotTeams(String head)
	{
		Table table = this.hot.getTable();
		SortQuery sort = new SortQuery(table, head, 5, true);		//"5" stands for top 5 here.
		tableHost.performQuery(sort, "today_hot_team_query_result");
		return tableHost.getTable("today_hot_team_query_result");
	}
}
