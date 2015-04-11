package nbaquery.logic.infrustructure;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.DeriveColumnInfo;
import nbaquery.data.query.DeriveQuery;
import nbaquery.logic.LogicPipeline;
import nbaquery.logic.LogicWatcher;

/**
 * team_name_abbr = rival_team_name
 * current_name_abbr = current_team
 * @author aegistudio
 *
 */

public class RivalTeamPerformance implements LogicPipeline
{
	public TableHost tableHost;
	protected boolean shouldDoQuery = true;
	protected Table table;
	public LogicWatcher base;
	
	public RivalTeamPerformance(TableHost tableHost, MatchTeamPerformance base)
	{
		this.tableHost = tableHost;
		this.base = new LogicWatcher(base);
	}
	
	public Table getTable()
	{
		if(base.checkDepenency())
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
			if(table != null)
			{
				Column[] columns = table.getColumns().toArray(new Column[0]);
				for(Column column : columns)
				{
					if(column.getColumnName().equalsIgnoreCase("match_id")) continue;
					if(column.getColumnName().equalsIgnoreCase("match_season")) continue;
					if(column.getColumnName().equalsIgnoreCase("current_name_abbr")) continue;
					table.renameColumn(column.getColumnName(), "rival_".concat(column.getColumnName()));
				}
			}
			shouldDoQuery = false;
		}
		return table;
	}
}
