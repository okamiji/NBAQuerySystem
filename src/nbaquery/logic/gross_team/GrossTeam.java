package nbaquery.logic.gross_team;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.logic.LogicWatcher;
import nbaquery.logic.NativeTablePipeline;
import nbaquery.logic.TeamDeriveQuery;

public class GrossTeam
{
	public TableHost tableHost;
	protected LogicWatcher joined, nativeTeam;
	protected Table table;
	
	public GrossTeam(TableHost tableHost, GrossTeamNaturalJoin joined)
	{
		this.tableHost = tableHost;
		this.joined = new LogicWatcher(joined);
		
		this.nativeTeam = new LogicWatcher(new NativeTablePipeline(tableHost, "team"));
	}
	
	public Table getTable()
	{
		boolean joinedChanged = joined.checkDepenency();
		boolean teamChanged = nativeTeam.checkDepenency();
		if(joinedChanged || teamChanged)
		{
			TeamDeriveQuery deriveQuery = new TeamDeriveQuery(this.joined.getTable());
			tableHost.performQuery(deriveQuery, "gross_team");
			Table interMediateTable = tableHost.getTable("gross_team");
			NaturalJoinQuery joinQuery = new NaturalJoinQuery(interMediateTable, tableHost.getTable("team"),
					new String[]{"team_name_abbr"}, new String[]{"team_name_abbr"});
			tableHost.performQuery(joinQuery, "gross_team");
			table = tableHost.getTable("gross_team");
		}
		return table;
	}
}
