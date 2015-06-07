package nbaquery.data.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import nbaquery.data.DirtyDataInfo;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.Query;

public class SqlTableHost implements TableHost
{
	final Connection connection;
	final DatabaseMetaData metadata;
	
	final Set<String> declaredTable = new TreeSet<String>();
	final TreeMap<String, Table> tables = new TreeMap<String, Table>();
	
	public SqlTableHost(String host, String username, String password, BaseTableConstants[] baseTables) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		host = "jdbc:mysql://".concat(host).concat("?rewriteBatchedStatements=true");
		
		if(username == null && password == null)
			connection = java.sql.DriverManager.getConnection(host);
		else connection = java.sql.DriverManager.getConnection(host, username, password);

		connection.setCatalog("nbaquery");
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
		
		ResultSet tables = metadata.getTables("nbaquery", null, null, null);
		while(tables.next())
			this.declaredTable.add(tables.getString(3));
		
		for(BaseTableConstants baseTable : baseTables)
			this.tables.put(baseTable.getTableName(), new MutableSqlTable(this, baseTable.getTableName(),
					baseTable.getColumns(), baseTable.getDataClasses(), baseTable.getSqlTypes(), baseTable.getKeyword()));
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
	
	}

	@Override
	public DirtyDataInfo nextDirtyDataInfo() {
		return null;
	}
}
