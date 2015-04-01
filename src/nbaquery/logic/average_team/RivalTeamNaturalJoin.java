package nbaquery.logic.average_team;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.logic.infrustructure.MatchTeamPerformance;
import nbaquery.logic.infrustructure.RivalTeamPerformance;

public class RivalTeamNaturalJoin
{
	public TableHost tableHost; 
	protected boolean shouldDoQuery = true;
	protected Table table;
	protected RivalTeamPerformance rival;
	protected MatchTeamPerformance team;
	
	public RivalTeamNaturalJoin(TableHost tableHost, RivalTeamPerformance rival, MatchTeamPerformance team)
	{
		this.tableHost = tableHost;
		this.rival = rival;
		this.team = team;
	}
	
	public Table getTable()
	{
		if(shouldDoQuery)
		{
			NaturalJoinQuery joinQuery = new NaturalJoinQuery(team.getTable(), rival.getTable(), new String[]{"match_id", "team_name_abbr"}, new String[]{"match_id", "current_name_abbr"});
			tableHost.performQuery(joinQuery, "rival_team_natural_join");
			table = tableHost.getTable("rival_team_natural_join");
			
			Column[] columns = table.getColumns().toArray(new Column[0]);
			for(Column column : columns)
				if(column.getColumnName().endsWith("_sum"))
					table.renameColumn(column.getColumnName(), column.getColumnName().substring(0, column.getColumnName().length() - "_sum".length()));
			
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
		this.tableHost.deleteTable("rival_team_natural_join");
		
		this.rival.destroy();
		this.team.destroy();
	}
}
