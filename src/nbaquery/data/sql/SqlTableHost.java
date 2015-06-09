package nbaquery.data.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import nbaquery.data.DirtyDataInfo;
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
			this.tables.put(baseTable.getTableName(), new MutableSqlTable(this, baseTable.getTableName(),
					baseTable.getColumns(), baseTable.getDataClasses(), baseTable.getSqlTypes(), baseTable.getKeyword()));
		
		for(SqlQueryAlgorithm<?> algorithm : algorithms)
			this.algorithms.put(algorithm.getQueryClass(), algorithm);
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
		SqlQueryAlgorithm<?> algorithm = 
			this.algorithms.get(query.getClass());
		if(algorithm != null)
			this.putTable(tableName, algorithm.perform(tableName, query));
	}

	public void putTable(String tableName, Table table)
	{
		this.tables.put(tableName.toLowerCase(), table);
	}
	
	@Override
	public DirtyDataInfo nextDirtyDataInfo() {
		return null;
	}
}
