package nbaquery.logic.infrustructure;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.logic.LogicPipeline;
import nbaquery.logic.LogicWatcher;

public class RivalTeamNaturalJoin implements LogicPipeline
{
	public TableHost tableHost; 
	protected boolean shouldDoQuery = true;
	protected Table table;
	protected LogicWatcher rival;
	protected LogicWatcher team;
	
	public RivalTeamNaturalJoin(TableHost tableHost, RivalTeamPerformance rival, MatchTeamPerformance team)
	{
		this.tableHost = tableHost;
		this.rival = new LogicWatcher(rival);
		this.team = new LogicWatcher(team);
	}
	
	public Table getTable()
	{
		boolean checkRival = this.rival.checkDepenency();
		boolean checkTeam = this.team.checkDepenency();
		
		if(checkRival || checkTeam)
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
}
