package nbaquery.logic.average_team;

import java.util.ArrayList;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.GroupColumnInfo;
import nbaquery.data.query.GroupQuery;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.logic.AverageColumnInfo;
import nbaquery.logic.LogicWatcher;
import nbaquery.logic.NativeTablePipeline;

public class AverageTeam
{
	public TableHost tableHost;
	protected LogicWatcher joined, nativeTeam;
	protected Table table;
	
	public AverageTeam(TableHost tableHost, DerivedTeamPerformance joined)
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
			Table theTable = joined.getTable();
			Column[] columns = theTable.getColumns().toArray(new Column[0]);
			ArrayList<GroupColumnInfo> groupInfos = new ArrayList<GroupColumnInfo>();
			for(Column column : columns)
			{
				String columnName = column.getColumnName();
				if(columnName.equals("match_id")) continue;
				if(column.getDataClass().equals(String.class)) continue;
				else groupInfos.add(new AverageColumnInfo(column));
			}
			
			GroupQuery group = new GroupQuery(joined.getTable(), new String[]{"team_name_abbr", "match_season"}, groupInfos.toArray(new GroupColumnInfo[0]));
			tableHost.performQuery(group, "average_team_group");
			Table interMediateTable = tableHost.getTable("average_team_group");
			
			NaturalJoinQuery joinQuery = new NaturalJoinQuery(interMediateTable, nativeTeam.getTable(),
					new String[]{"team_name_abbr"}, new String[]{"team_name_abbr"});
			tableHost.performQuery(joinQuery, "average_team");
			table = tableHost.getTable("average_team");
		}
		return table;
	}
}
