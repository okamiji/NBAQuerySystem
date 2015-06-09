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
		sqlTypeMap.put(Image.class, "char(128)");
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
			this.addColumn("player_position", Character.class, "char(1)");
			this.addColumn("player_height", String.class, "char(10)");
			this.addColumn("player_weight", String.class, "smallint");
			this.addColumn("player_birth", Date.class, "bigint");
			this.addColumn("player_age", Integer.class, "tinyint");
			this.addColumn("player_exp", Integer.class, "tinyint");
			this.addColumn("player_school", String.class, "char(32)");
			this.addColumn("player_portrait", String.class, "char(128)");
			this.addColumn("player_action", String.class, "char(128)");
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
			this.addColumn("team_logo", String.class, "char(128)");
		}
		
		@Override
		public String getKeyword() {
			return "team_name_abbr";
		}
	};
}
