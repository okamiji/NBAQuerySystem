package nbaquery.logic.gross_team;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.NaturalJoinQuery;

public class GrossTeamNaturalJoin
{
	public TableHost tableHost;
	protected GrossRivalPerformance rival;
	protected GrossTeamPerformance team;
	protected boolean shouldDoQuery = true;
	protected Table table;
	
	public GrossTeamNaturalJoin(TableHost tableHost, GrossRivalPerformance rival, GrossTeamPerformance team)
	{
		this.tableHost = tableHost;
		this.rival = rival;
		this.team = team;
	}
	
	public Table getTable()
	{
		if(shouldDoQuery)
		{
			NaturalJoinQuery joinQuery = new NaturalJoinQuery(team.getTable(), rival.getTable(), new String[]{"team_name_abbr"}, new String[]{"current_name_abbr"});
			tableHost.performQuery(joinQuery, "gross_team_natural_join");
			table = tableHost.getTable("gross_team_natural_join");
			Column[] columns = table.getColumns().toArray(new Column[0]);
			for(Column column : columns)
			{
				if(column.getColumnName().endsWith("_sum"))
					table.renameColumn(column.getColumnName(),
							column.getColumnName().substring(0, column.getColumnName().length() - "_sum".length()));
			}
			
			shouldDoQuery = false;
		}
		return table;
	}
	
	public void markDirty()
	{
		this.shouldDoQuery = true;
	}
	
	public void destroy()
	{
		this.markDirty();
		this.tableHost.deleteTable("gross_team_natural_join");
		this.team.destroy();
		this.rival.destroy();
	}
}
