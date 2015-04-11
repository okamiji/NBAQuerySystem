package nbaquery.logic.gross_team;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
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
			tableHost.performQuery(joinQuery, "gross_team_natural_join");
			table = tableHost.getTable("gross_team_natural_join");
			Column[] columns = table.getColumns().toArray(new Column[0]);
			for(Column column : columns)
			{
				if(column.getColumnName().endsWith("_sum"))
					table.renameColumn(column.getColumnName(),
							column.getColumnName().substring(0, column.getColumnName().length() - "_sum".length()));
			}
		}
		return table;
	}
}
