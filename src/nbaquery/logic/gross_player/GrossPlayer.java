package nbaquery.logic.gross_player;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.logic.PlayerDeriveQuery;

public class GrossPlayer
{
	public TableHost tableHost;
	protected GrossPlayerPerformance gross;
	protected boolean shouldDoQuery = true;
	protected Table table;
	
	public GrossPlayer(TableHost tableHost, GrossPlayerPerformance gross)
	{
		this.tableHost = tableHost;
		this.gross = gross;
	}
	
	public Table getTable()
	{
		if(shouldDoQuery)
		{
			PlayerDeriveQuery deriveQuery = new PlayerDeriveQuery(this.gross.getTable());
			tableHost.performQuery(deriveQuery, "gross_player");
			Table intermediateTable = tableHost.getTable("gross_player");
			
			NaturalJoinQuery joinQuery = new NaturalJoinQuery(intermediateTable, tableHost.getTable("team"),
					new String[]{"team_name_abbr"}, new String[]{"team_name_abbr"});
			tableHost.performQuery(joinQuery, "gross_player");
			intermediateTable = tableHost.getTable("gross_player");
			
			joinQuery = new NaturalJoinQuery(intermediateTable, tableHost.getTable("player"),
					new String[]{"player_name"}, new String[]{"player_name"});
			tableHost.performQuery(joinQuery, "gross_player");
			table = tableHost.getTable("gross_player");
			
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
		this.tableHost.deleteTable("gross_player");
		this.gross.destroy();
	}
}
