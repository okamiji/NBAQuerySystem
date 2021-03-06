package nbaquery.logic.match;

import nbaquery.data.Cursor;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.AliasingQuery;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.query.SortQuery;

public class NewMatchServiceAdapter implements NewMatchService
{
	TableHost tableHost;
	
	public NewMatchServiceAdapter(TableHost tableHost)
	{
		this.tableHost = tableHost;
	}

	public Table searchMatchesByDateAndSeason(String date, String season)
	{
		Table table = tableHost.getTable("matches");
		if(date == null && season == null) return table;
		
		SelectProjectQuery query = null;
		try
		{
			if(date == null)
				query = new SelectProjectQuery("match_season='%season'"
						.replace("%season", season), table);
			else query = new SelectProjectQuery("match_season='%season' and match_date='%date'"
					.replace("%season", season).replace("%date", date), table);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		tableHost.performQuery(query, "match_query_result_date_season");
		return tableHost.getTable("match_query_result_date_season");
	}

	@Override
	public Table searchMatchesByTeamNameAbbr(String team_name_abbr)
	{
		SelectProjectQuery query = null;
		Table table = this.searchForMatchesTable(new String[]{"match_id"}, null, null, true);
		try
		{
			query = new SelectProjectQuery("match_host_abbr='%1' or match_guest_abbr='%1'"
					.replace("%1", team_name_abbr), table);
		}
		catch (Exception e)
		{
			
		}
		tableHost.performQuery(query, "match_query_result_team");
		Table queryResult = tableHost.getTable("match_query_result_team");
		return queryResult;
	}
	
	@Override
	public Table searchMatchesByPlayer(String player_name){
		SelectProjectQuery query = null;
		Table table = tableHost.getTable("match_natural_join_performance");
		try
		{
			query = new SelectProjectQuery("match_natural_join_performance.PLAYER_NAME='%1'".replace("%1", player_name), table, "match_id");
		}
		catch (Exception e)
		{
			
		}
		tableHost.performQuery(query, "match_query_result_player_select");
		Table queryResult = tableHost.getTable("match_query_result_player_select");

		Table matches = this.searchForMatchesTable(new String[]{"match_id"}, null, null, true);
		NaturalJoinQuery naturalJoin = new NaturalJoinQuery(matches, queryResult, new String[]{"match_id"}, new String[]{"match_id"});
		tableHost.performQuery(naturalJoin, "match_query_result_player_joined");
		queryResult = tableHost.getTable("match_query_result_player_joined");

		SortQuery sort = new SortQuery(queryResult, "match_id", true);
		tableHost.performQuery(sort, "match_query_result_player");
		queryResult = tableHost.getTable("match_query_result_player");
		
		return queryResult;
	}

	@Override
	public Table searchPerformanceByID(int matchID, String[] header, boolean descend){
		SelectProjectQuery query = null;
		try
		{
			query = new SelectProjectQuery("match_natural_join_performance.MATCH_ID=".concat(Integer.toString(matchID)),
					tableHost.getTable("match_natural_join_performance"));
		}
		catch (Exception e)
		{

		}
		tableHost.performQuery(query, "match_query_result_id_select");
		Table queryResult = tableHost.getTable("match_query_result_id_select");
		
		NaturalJoinQuery naturalJoin = new NaturalJoinQuery(this.tableHost.getTable("team"),
				queryResult, new String[]{"team_name_abbr"}, new String[]{"team_name_abbr"});
		tableHost.performQuery(naturalJoin, "match_query_result_id_joined1");
		queryResult = tableHost.getTable("match_query_result_id_joined1");
		
		naturalJoin = new NaturalJoinQuery(this.tableHost.getTable("player"),
				queryResult, new String[]{"player_name"}, new String[]{"player_name"});
		tableHost.performQuery(naturalJoin, "match_query_result_id_joined2");
		queryResult = tableHost.getTable("match_query_result_id_joined2");
		
		for(int i = header.length - 1; i >= 0; i --)
		{
			SortQuery sort = new SortQuery(queryResult, header[i], descend);
			tableHost.performQuery(sort, "match_query_result_id_" + i);
			queryResult = tableHost.getTable("match_query_result_id_" + i);
		}
		
		return queryResult;
	}
	
	@Override
	public Table searchForMatchesTable(String[] keyword, String season, String date, boolean[] descend)
	{
		Table queryResult = this.searchMatchesByDateAndSeason(date, season);
		
		AliasingQuery match_host_alias
			= new AliasingQuery(tableHost.getTable("team"), new String[]{"team_name_abbr", "team_logo"}, new String[]{"match_host_abbr", "match_host_image"});
		tableHost.performQuery(match_host_alias, "match_host_alias");
		Table match_host_alias_table = tableHost.getTable("match_host_alias");

		AliasingQuery match_guest_alias
			= new AliasingQuery(tableHost.getTable("team"), new String[]{"team_name_abbr", "team_logo"}, new String[]{"match_guest_abbr", "match_guest_image"});
		tableHost.performQuery(match_guest_alias, "match_guest_alias");
		Table match_guest_alias_table = tableHost.getTable("match_guest_alias");
		
		NaturalJoinQuery match_query_host_name_joined = new NaturalJoinQuery(queryResult, match_host_alias_table, new String[]{"match_host_abbr"}, new String[]{"match_host_abbr"});
		tableHost.performQuery(match_query_host_name_joined, "match_query_host_name_joined");
		queryResult = tableHost.getTable("match_query_host_name_joined");

		NaturalJoinQuery match_query_guest_name_joined = new NaturalJoinQuery(queryResult,match_guest_alias_table, new String[]{"match_guest_abbr"}, new String[]{"match_guest_abbr"});
		tableHost.performQuery(match_query_guest_name_joined, "match_query_guest_name_joined");
		queryResult = tableHost.getTable("match_query_guest_name_joined");
		
		if(keyword != null) for(int i = keyword.length - 1; i >= 0; i --)
		{
			SortQuery sort = new SortQuery(queryResult, keyword[i], descend[i]);
			tableHost.performQuery(sort, "match_query_result_" + i);
			queryResult = tableHost.getTable("match_query_result_" + i);
		}
		return queryResult;
	}
	
	@Override
	public Table searchForMatchesTable(String[] keyword, String season, String date, boolean descend)
	{
		boolean[] sortDescend = null;
		if(keyword != null)
		{
			sortDescend = new boolean[keyword.length];
			for(int i = 0; i < sortDescend.length; i ++)
				sortDescend[i] = true;
		}
		return this.searchForMatchesTable(keyword, season, date, sortDescend);
	}
	
	@Override
	public Table searchQuarterScoreByID(int matchID)
	{
		SelectProjectQuery query = null;
		try
		{
			query = new SelectProjectQuery("match_id=".concat(Integer.toString(matchID)),
					tableHost.getTable("quarter_score"));
		}
		catch (Exception e)
		{

		}
		tableHost.performQuery(query, "quarter_query_result_id");
		Table queryResult = tableHost.getTable("quarter_query_result_id");
		return queryResult;
	}

	@Override
	public boolean shouldRedoQuery(Object host)
	{
		return tableHost.getTable("matches").hasTableChanged(host);
	}

	@Override
	public Table listTodayMatches()
	{
		Cursor rows = tableHost.getTable("matches").getRows();
		String season = null, date = null;
		if(rows.getLength() > 0)
		{
			rows.absolute(rows.getLength() - 1);
			Row current = rows.next();
			season = (String) tableHost.getTable("matches").getColumn("match_season").getAttribute(current);
			date = (String) tableHost.getTable("matches").getColumn("match_date").getAttribute(current);
		}
		return this.searchForMatchesTable(new String[]{"match_id"}, season, date, true);
	}

	@Override
	public Table searchPerformancesByPlayer(String player_name) {
		SelectProjectQuery select_project_query = null;
		try {
			select_project_query = new SelectProjectQuery(String.format("player_name=='%s'", player_name), tableHost.getTable("derived_player_performance"));
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		tableHost.performQuery(select_project_query, "single_player_performance_select");
		Table selected = tableHost.getTable("single_player_performance_select");
		
		SortQuery sort_query = new SortQuery(selected, "match_id");
		tableHost.performQuery(sort_query, "single_player_performance");		
		
		return tableHost.getTable("single_player_performance");
	}
}
