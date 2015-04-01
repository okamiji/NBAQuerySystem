package nbaquery.logic.gross_team;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.logic.TeamDeriveQuery;

public class GrossTeam
{
	public TableHost tableHost;
	protected GrossTeamNaturalJoin joined;
	protected boolean shouldDoQuery = true;
	protected Table table;
	
	public GrossTeam(TableHost tableHost, GrossTeamNaturalJoin joined)
	{
		this.tableHost = tableHost;
		this.joined = joined;
	}
	
	public Table getTable()
	{
		if(shouldDoQuery)
		{
			TeamDeriveQuery deriveQuery = new TeamDeriveQuery(this.joined.getTable());
			tableHost.performQuery(deriveQuery, "gross_team");
			Table interMediateTable = tableHost.getTable("gross_team");
			NaturalJoinQuery joinQuery = new NaturalJoinQuery(interMediateTable, tableHost.getTable("team"),
					new String[]{"team_name_abbr"}, new String[]{"team_name_abbr"});
			tableHost.performQuery(joinQuery, "gross_team");
			table = tableHost.getTable("gross_team");
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
		this.tableHost.deleteTable("gross_team");
		this.joined.destroy();
	}
}
