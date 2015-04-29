package nbaquery.logic.match;

import nbaquery.data.Column;
import nbaquery.data.Image;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.DeriveColumnInfo;
import nbaquery.data.query.DeriveQuery;
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
	
	@Override
	public Table searchForOneMatchByIdTable(int matchID)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Table searchForMatchsByPlayerTable(String player_name)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Table searchForMatchsByDateAndSeasonTable(String date, String season)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Table searchForMatchsByTeamTable(String team_name_abbr)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Table searchOneMatchByPlayerAndIDTable(String player_name,
			int matchID)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Table searchOneMatchByTeamAndIDTable(String team_name_abbr,
			int matchID)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Table searchForMatchesTable(String[] keyword, boolean isUp)
	{
		Table queryResult = tableHost.getTable("match");
		
		NaturalJoinQuery joinQuery = new NaturalJoinQuery(queryResult, tableHost.getTable("team"), new String[]{"match_host_abbr"}, new String[]{"team_name_abbr"});
		tableHost.performQuery(joinQuery, "match_query_result");
		queryResult = tableHost.getTable("match_query_result");
		
		DeriveQuery derive = null;
		try
		{
			derive = new DeriveQuery(queryResult,
					new DeriveColumnInfo[]
					{
						new ImageJointer(tableHost, "match_host_image"),
						new ImageJointer(tableHost, "match_guest_image")
					});
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		tableHost.performQuery(derive, "match_query_result");
		queryResult = tableHost.getTable("match_query_result");
		
		for(int i = keyword.length - 1; i >= 0; i --)
		{
			SortQuery sort = new SortQuery(queryResult, keyword[i], isUp);
			tableHost.performQuery(sort, "match_query_result");
			queryResult = tableHost.getTable("match_query_result");
		}
		return queryResult;
	}
	
	class ImageJointer extends DeriveColumnInfo
	{
		public ImageJointer(TableHost host, String columnName)
		{
			super(columnName, Image.class);
			this.host = host;
		}
		Column team_logo;
		TableHost host;
		Table table;

		@Override
		public void retrieve(Table resultTable)
		{
			table = host.getTable("team");
			team_logo = table.getColumn("team_logo");
		}

		@Override
		public void derive(Row resultRow)
		{
			Table team_info = host.getTable("team_info_".concat((String)(team_logo.getAttribute(resultRow))));
			if(team_info == null)
			try
			{
				SelectProjectQuery query = new SelectProjectQuery("team.team_name_abbr='%1'"
						.replace("%1", (String)(team_logo.getAttribute(resultRow))), table);
				host.performQuery(query, "team_info_".concat((String)(team_logo.getAttribute(resultRow))));
				
				team_info = host.getTable("team_info_".concat((String)(team_logo.getAttribute(resultRow))));
			}
			catch(Exception e)
			{
				
			}
			getDeriveColumn().setAttribute(resultRow, team_info.getColumn("team_logo").getAttribute(team_info.getRows()[0]));
		}
	}
}
