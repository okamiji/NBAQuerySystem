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
	
	public SqlTableHost(String host, String username, String password) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		host = "jdbc:mysql://".concat(host);
		
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
		
	}
	
	public static void main(String[] arguments) throws Exception
	{
		System.out.print("password: ");
		java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
		SqlTableHost host = new SqlTableHost("localhost", "root", reader.readLine());
		new MutableSqlTable(host, "zz", new String[]{"b", "a", "c"}, new Class<?>[]{Integer.class, String.class, Float.class},
				new String[]{"int", "char(8)", "real"}, "a");
	}
	
	@Override
	public Table getTable(String tableName) {
		return this.tables.get(tableName.toLowerCase());
	}
	
	@Override
	public void deleteTable(String tableName) {
		try
		{
			this.connection.createStatement().execute(String.format("delete from %s"));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void performQuery(Query query, String tableName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DirtyDataInfo nextDirtyDataInfo() {
		return null;
	}
}
