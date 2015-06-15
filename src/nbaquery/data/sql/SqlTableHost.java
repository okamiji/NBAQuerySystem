package nbaquery.data.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.Query;
import nbaquery.data.sql.query.SqlQueryAlgorithm;

public class SqlTableHost implements TableHost
{
	public final Connection connection;
	final DatabaseMetaData metadata;
	
	final Set<String> declaredTable = new TreeSet<String>();
	final TreeMap<String, Table> tables = new TreeMap<String, Table>();
	final HashMap<Class<? extends Query>, SqlQueryAlgorithm<?>> algorithms
		= new HashMap<Class<? extends Query>, SqlQueryAlgorithm<?>>();
	
	public SqlTableHost(String host, String username, String password, BaseTableConstants[] baseTables, SqlQueryAlgorithm<?>[] algorithms) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		host = "jdbc:mysql://".concat(host).concat("?rewriteBatchedStatements=true");
		
		if(username == null && password == null)
			connection = java.sql.DriverManager.getConnection(host);
		else connection = java.sql.DriverManager.getConnection(host, username, password);

		Statement statement = connection.createStatement();
		
		metadata = connection.getMetaData();
		
		ResultSet catalogs = metadata.getCatalogs();
		boolean nbaqueryDatabaseExists = false;
		while(catalogs.next())
			if(catalogs.getString(1).equals("nbaquery"))
			{
				nbaqueryDatabaseExists = true;
				break;
			}
		
		if(!nbaqueryDatabaseExists)
			statement.execute("create database nbaquery");
		
		connection.setCatalog("nbaquery");
		ResultSet tables = metadata.getTables("nbaquery", null, null, null);
		while(tables.next())
			this.declaredTable.add(tables.getString(3));
		
		for(BaseTableConstants baseTable : baseTables)
			this.tables.put(baseTable.getTableName().toLowerCase(), new MutableSqlTable(this, baseTable.getTableName(),
					baseTable.getColumns(), baseTable.getDataClasses(), baseTable.getSqlTypes(), baseTable.getKeyword()));
		
		new MutableSqlTable(this, "table_updtracker", new String[]{"table_name", "latest_update"}, 
				new Class<?>[]{String.class, Date.class}, new String[]{"char(50)", "bigint"}, "table_name");
		
		table_checkupd = this.connection.prepareStatement("select latest_update from table_updtracker where table_name=?");
		table_setupd = this.connection.prepareStatement("update table_updtracker set latest_update=? where table_name=?");
		
		for(SqlQueryAlgorithm<?> algorithm : algorithms)
			this.algorithms.put(algorithm.getQueryClass(), algorithm);
	}
	
	final PreparedStatement table_checkupd;
	final PreparedStatement table_setupd;
	
	public long getLastestUpdate(Table table)
	{
		try {
			table_checkupd.setString(1, table.getTableName());
			ResultSet result = table_checkupd.executeQuery();
			if(result.next()) return result.getLong(1);
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0L;
		}
	}
	
	public void setLatestUpdate(Table table)
	{
		try
		{
			table_setupd.setLong(1, System.currentTimeMillis());
			table_setupd.setString(2, table.getTableName());
			table_setupd.execute();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public Table getTable(String tableName) {
		return this.tables.get(tableName.toLowerCase());
	}
	
	@Override
	public void deleteTable(String tableName) {
		try
		{
			this.connection.createStatement().execute(String.format("delete from %s", tableName));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void performQuery(Query query, String tableName) {
		Class<?> specificClass = query.getClass();
		SqlQueryAlgorithm<?> algorithm;
		while((algorithm = algorithms.get(specificClass)) == null)
		{
			specificClass = specificClass.getSuperclass();
			if(!Query.class.isAssignableFrom(specificClass)) return;
		}
		
		this.putTable(tableName, algorithm.perform(tableName, query));
	}

	public void putTable(String tableName, Table table)
	{
		this.tables.put(tableName.toLowerCase(), table);
	}

}
