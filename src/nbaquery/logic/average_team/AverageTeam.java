package nbaquery.logic.average_team;

import java.util.ArrayList;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.GroupColumnInfo;
import nbaquery.data.query.GroupQuery;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.logic.AverageColumnInfo;

public class AverageTeam
{
	public TableHost tableHost;
	protected DerivedTeamPerformance joined;
	protected boolean shouldDoQuery = true;
	protected Table table;
	
	public AverageTeam(TableHost tableHost, DerivedTeamPerformance joined)
	{
		this.tableHost = tableHost;
		this.joined = joined;
	}
	
	public Table getTable()
	{
		if(shouldDoQuery)
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
			tableHost.performQuery(group, "average_team");
			Table interMediateTable = tableHost.getTable("average_team");
			NaturalJoinQuery joinQuery = new NaturalJoinQuery(interMediateTable, tableHost.getTable("team"),
					new String[]{"team_name_abbr"}, new String[]{"team_name_abbr"});
			tableHost.performQuery(joinQuery, "average_team");
			table = tableHost.getTable("average_team");
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
