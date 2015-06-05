package nbaquery.logic.gross_team;

import java.util.ArrayList;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.AliasingQuery;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.logic.LogicPipeline;
import nbaquery.logic.LogicWatcher;

public class GrossTeamNaturalJoin implements LogicPipeline
{
	public TableHost tableHost;
	protected LogicWatcher rival;
	protected LogicWatcher team;
	protected Table table;
	
	public GrossTeamNaturalJoin(TableHost tableHost, GrossRivalPerformance rival, GrossTeamPerformance team)
	{
		this.tableHost = tableHost;
		this.rival = new LogicWatcher(rival);
		this.team = new LogicWatcher(team);
	}
	
	public Table getTable()
	{
		boolean rivalChanged = this.rival.checkDepenency();
		boolean teamChanged = this.team.checkDepenency();
		if(rivalChanged || teamChanged)
		{
			NaturalJoinQuery joinQuery = new NaturalJoinQuery(team.getTable(), rival.getTable(), new String[]{"team_name_abbr"}, new String[]{"current_name_abbr"});
			tableHost.performQuery(joinQuery, "gross_team_natural_join_joined");
			table = tableHost.getTable("gross_team_natural_join_joined");
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
					"gross_team_natural_join");
			table = tableHost.getTable("gross_team_natural_join");
		}
		return table;
	}
}
