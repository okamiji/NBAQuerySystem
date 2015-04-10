package nbaquery.logic.gross_player;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.logic.LogicWatcher;
import nbaquery.logic.NativeTablePipeline;
import nbaquery.logic.PlayerDeriveQuery;

public class GrossPlayer
{
	public TableHost tableHost;
	protected LogicWatcher gross, nativePlayer, nativeTeam;
	protected Table table;
	
	public GrossPlayer(TableHost tableHost, GrossPlayerPerformance gross)
	{
		this.tableHost = tableHost;
		this.gross = new LogicWatcher(gross);
		
		this.nativePlayer = new LogicWatcher(new NativeTablePipeline(tableHost, "player"));
		this.nativeTeam = new LogicWatcher(new NativeTablePipeline(tableHost, "team"));
	}
	
	public Table getTable()
	{
		boolean playerChanged = this.gross.checkDepenency();
		boolean nativePlayerChanged = this.nativePlayer.checkDepenency();
		boolean nativeTeamChanged = this.nativeTeam.checkDepenency();
		if(playerChanged || nativePlayerChanged || nativeTeamChanged)
		{
			PlayerDeriveQuery deriveQuery = new PlayerDeriveQuery(this.gross.getTable());
			tableHost.performQuery(deriveQuery, "gross_player");
			Table intermediateTable = tableHost.getTable("gross_player");
			
			NaturalJoinQuery joinQuery = new NaturalJoinQuery(intermediateTable, nativeTeam.getTable(),
					new String[]{"team_name_abbr"}, new String[]{"team_name_abbr"});
			tableHost.performQuery(joinQuery, "gross_player");
			intermediateTable = tableHost.getTable("gross_player");
			
			joinQuery = new NaturalJoinQuery(intermediateTable, nativePlayer.getTable(),
					new String[]{"player_name"}, new String[]{"player_name"});
			tableHost.performQuery(joinQuery, "gross_player");
			table = tableHost.getTable("gross_player");
		}
		return table;
	}
}
