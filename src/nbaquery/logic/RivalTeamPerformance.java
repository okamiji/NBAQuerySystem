package nbaquery.logic;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.DeriveColumnInfo;
import nbaquery.data.query.DeriveQuery;

/**
 * team_name_abbr = rival_team_name
 * current_name_abbr = current_team
 * @author aegistudio
 *
 */

public class RivalTeamPerformance
{
	public TableHost tableHost;
	protected boolean shouldDoQuery = true;
	protected Table table;
	public MatchTeamPerformance base;
	
	public RivalTeamPerformance(TableHost tableHost, MatchTeamPerformance base)
	{
		this.tableHost = tableHost;
		this.base = base;
	}
	
	public Table getTable()
	{
		if(shouldDoQuery)
		{
			DeriveQuery groupQuery= new DeriveQuery(base.getTable(), new DeriveColumnInfo[]
			{
				new DeriveColumnInfo("current_name_abbr", String.class)
				{
					Column match_host_abbr;
					Column match_guest_abbr;
					Column team_name_abbr;
					
					@Override
					public void retrieve(Table resultTable)
					{
						match_host_abbr = resultTable.getColumn("match_host_abbr");
						match_guest_abbr = resultTable.getColumn("match_guest_abbr");
						team_name_abbr = resultTable.getColumn("team_name_abbr");
					}
		
					@Override
					public void derive(Row resultRow)
					{
						String host = (String) match_host_abbr.getAttribute(resultRow);
						String guest = (String) match_guest_abbr.getAttribute(resultRow);
						String team = (String) team_name_abbr.getAttribute(resultRow);
						if(host.equals(team)) super.getDeriveColumn().setAttribute(resultRow, guest);
						else super.getDeriveColumn().setAttribute(resultRow, host);
					}
				}
			});
			tableHost.performQuery(groupQuery, "rival_team_performance");
			table = tableHost.getTable("rival_team_performance");
			shouldDoQuery = false;
		}
		return table;
	}
	
	public void markDirty()
	{
		this.shouldDoQuery = true;
	}
}
