package nbaquery.logic.infrustructure;

import java.util.ArrayList;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.AliasingQuery;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.logic.LogicPipeline;
import nbaquery.logic.LogicWatcher;

public class RivalTeamNaturalJoin implements LogicPipeline
{
	public TableHost tableHost;
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
			tableHost.performQuery(joinQuery, "rival_team_natural_join_joined");
			table = tableHost.getTable("rival_team_natural_join_joined");
			
			Column[] columns = table.getColumns().toArray(new Column[0]);
			ArrayList<String> columnNames = new ArrayList<String>();
			ArrayList<String> aliases = new ArrayList<String>();
			for(Column column : columns)
			{
				if(column.getColumnName().endsWith("_sum"))
				{
					columnNames.add(column.getColumnName());
					aliases.add(column.getColumnName().substring(0, column.getColumnName().length() - "_sum".length()));
				}
				else
				{
					columnNames.add(column.getColumnName());
					aliases.add(column.getColumnName());
				}
			}
			tableHost.performQuery(new AliasingQuery(table, columnNames.toArray(new String[0]), aliases.toArray(new String[0])),
					"rival_team_natural_join");
			table = tableHost.getTable("rival_team_natural_join");
		}
		return table;
	}
}
