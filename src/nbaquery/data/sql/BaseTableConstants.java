package nbaquery.data.sql;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import nbaquery.data.Image;

public abstract class BaseTableConstants
{
	public static final HashMap<Class<?>, String> sqlTypeMap = new HashMap<Class<?>, String>();
	static
	{
		sqlTypeMap.put(String.class, "char(32)");
		sqlTypeMap.put(Integer.class, "int");
		sqlTypeMap.put(Float.class, "real");
		sqlTypeMap.put(Character.class, "tinyint");
		sqlTypeMap.put(Date.class, "bigint");
		sqlTypeMap.put(Image.class, "char(255)");
	}
	
	ArrayList<String> columns = new ArrayList<String>();
	ArrayList<Class<?>> dataClasses = new ArrayList<Class<?>>();
	ArrayList<String> sqlTypes = new ArrayList<String>();
	String tableName;
	public BaseTableConstants(String tableName)
	{
		this.tableName = tableName;
	}
	public String getTableName()
	{
		return this.tableName;
	}
	public String[] getColumns()
	{
		return this.columns.toArray(new String[0]);
	}
	public Class<?>[] getDataClasses()
	{
		return this.dataClasses.toArray(new Class<?>[0]);
	}
	public String[] getSqlTypes()
	{
		return this.sqlTypes.toArray(new String[0]);
	}
	public abstract String getKeyword();
	
	protected void addColumn(String columnName, Class<?> dataClass, String sqlType) {
		this.columns.add(columnName);
		this.dataClasses.add(dataClass);
		this.sqlTypes.add(sqlType);
	}

	public static final BaseTableConstants player = new BaseTableConstants("player")
	{
		{
			this.addColumn("player_name", String.class, "char(32)");
			this.addColumn("player_number", Integer.class, "smallint");
			this.addColumn("player_position", Character.class, "tinyint");
			this.addColumn("player_height", String.class, "char(10)");
			this.addColumn("player_weight", String.class, "smallint");
			this.addColumn("player_birth", Date.class, "bigint");
			this.addColumn("player_age", Integer.class, "tinyint");
			this.addColumn("player_exp", Integer.class, "tinyint");
			this.addColumn("player_school", String.class, "char(64)");
			this.addColumn("player_portrait", Image.class, "char(255)");
			this.addColumn("player_action", Image.class, "char(255)");
		}
		
		@Override
		public String getKeyword() {
			return "player_name";
		}
	};
	
	public static final BaseTableConstants team = new BaseTableConstants("team")
	{
		{
			this.addColumn("team_name_abbr", String.class, "char(3)");
			this.addColumn("team_name", String.class, "char(32)");
			this.addColumn("team_location", String.class, "char(32)");
			this.addColumn("team_match_area", String.class, "char(12)");
			this.addColumn("team_sector", String.class, "char(12)");
			this.addColumn("team_host", String.class, "char(32)");
			this.addColumn("team_foundation", Integer.class, "smallint");
			this.addColumn("team_logo", Image.class, "char(255)");
		}
		
		@Override
		public String getKeyword() {
			return "team_name_abbr";
		}
	};
	
	public static final BaseTableConstants match = new BaseTableConstants("matches")
	{
		{
			this.addColumn("match_id", Integer.class, "int");
			this.addColumn("match_season", String.class, "char(5)");
			this.addColumn("match_date", String.class, "char(5)");
			this.addColumn("match_host_abbr", String.class, "char(3)");
			this.addColumn("match_host_score", Integer.class, "smallint");
			this.addColumn("match_guest_abbr", String.class, "char(3)");
			this.addColumn("match_guest_score", Integer.class, "smallint");
		}
		
		@Override
		public String getKeyword() {
			return "match_id";
		}
	};
	
	public static final BaseTableConstants quarter_score = new BaseTableConstants("quarter_score")
	{
		{
			this.addColumn("match_id", Integer.class, "int");
			this.addColumn("quarter_number", Integer.class, "tinyint");
			this.addColumn("quarter_host_score", Integer.class, "tinyint");
			this.addColumn("quarter_guest_score", Integer.class, "tinyint");
		}
		
		@Override
		public String getKeyword() {
			return "match_id, quarter_number";
		}
	};
	
	public static final BaseTableConstants performance = new BaseTableConstants("performance")
	{
		{
			this.addColumn("match_id", Integer.class, "int");
			this.addColumn("team_name_abbr", String.class, "char(3)");
			this.addColumn("player_name", String.class, "char(32)");
			this.addColumn("player_position", Character.class, "tinyint");
			this.addColumn("game_time_minute", Integer.class, "smallint");
			this.addColumn("game_time_second", Integer.class, "tinyint");
			this.addColumn("shoot_score", Integer.class, "tinyint");
			this.addColumn("shoot_count", Integer.class, "tinyint");
			this.addColumn("three_shoot_score", Integer.class, "tinyint");
			this.addColumn("three_shoot_count", Integer.class, "tinyint");
			this.addColumn("foul_shoot_score", Integer.class, "tinyint");
			this.addColumn("foul_shoot_count", Integer.class, "tinyint");
			this.addColumn("attack_board", Integer.class, "tinyint");
			this.addColumn("defence_board", Integer.class, "tinyint");
			this.addColumn("total_board", Integer.class, "tinyint");
			this.addColumn("assist", Integer.class, "tinyint");
			this.addColumn("steal", Integer.class, "tinyint");
			this.addColumn("cap", Integer.class, "tinyint");
			this.addColumn("miss", Integer.class, "tinyint");
			this.addColumn("foul", Integer.class, "tinyint");
			this.addColumn("self_score", Integer.class, "smallint");
		}
		
		@Override
		public String getKeyword() {
			return "match_id, team_name_abbr, player_name";
		}
	};
}
