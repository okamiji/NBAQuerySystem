package nbaquery.logic.average_player;

import java.util.ArrayList;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.GroupColumnInfo;
import nbaquery.data.query.GroupQuery;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.logic.AverageColumnInfo;

public class AveragePlayer
{
	public TableHost tableHost; 
	protected boolean shouldDoQuery = true;
	protected Table table;
	protected DerivedPlayerPerformance player;
	
	public AveragePlayer(TableHost tableHost, DerivedPlayerPerformance player)
	{
		this.tableHost = tableHost;
		this.player = player;
	}
	
	public Table getTable()
	{
		if(shouldDoQuery)
		{
			Table table = this.player.getTable();
			ArrayList<GroupColumnInfo> groupColumns = new ArrayList<GroupColumnInfo>();
			Column[] columns = table.getColumns().toArray(new Column[0]);
			for(Column column : columns)
			{
				if(column.getDataClass().equals(String.class)) continue;
				if(column.getDataClass().equals(Character.class)) continue;
				if(column.getColumnName().equalsIgnoreCase("match_id")) continue;
				groupColumns.add(new AverageColumnInfo(column));
			}
			groupColumns.add(new GroupColumnInfo("game_count", Integer.class)
			{

				@Override
				public void retrieve(Table originalTable, Table resultTable)
				{
					
				}

				@Override
				public void collapse(Row[] rows, Row resultRow)
				{
					getGroupColumn().setAttribute(resultRow, rows.length);
				}
				
			});
			groupColumns.add(new GroupColumnInfo("first_count", Integer.class)
			{
				Column player_position;
				
				@Override
				public void retrieve(Table originalTable,
						Table resultTable)
				{
					player_position = originalTable.getColumn("player_position");
				}

				@Override
				public void collapse(Row[] rows, Row resultRow)
				{
					Integer first_count = 0;
					for(Row row : rows)
					{
						Character player_position_n = (Character) player_position.getAttribute(row);
						if(player_position_n == null) continue;
						if(player_position_n == '\0') continue;
						first_count ++;
					}
					getGroupColumn().setAttribute(resultRow, first_count);
				}
			});
	
			
			GroupQuery group = new GroupQuery(table, new String[]{"match_season", "player_name", "team_name_abbr"}, groupColumns.toArray(new GroupColumnInfo[0]));
			tableHost.performQuery(group, "average_player");
			table = tableHost.getTable("average_player");
			
			NaturalJoinQuery join = new NaturalJoinQuery(table, tableHost.getTable("team"), new String[]{"team_name_abbr"}, new String[]{"team_name_abbr"});
			tableHost.performQuery(join, "average_player");
			table = tableHost.getTable("average_player");
			
			join = new NaturalJoinQuery(table, tableHost.getTable("player"), new String[]{"player_name"}, new String[]{"player_name"}); 
			tableHost.performQuery(join, "average_player");
			this.table = tableHost.getTable("average_player");
			
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
		this.tableHost.deleteTable("average_player");
		
		this.player.destroy();
	}
}