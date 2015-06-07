package nbaquery.data.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

import nbaquery.data.DirtyDataInfo;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.Query;

public class SqlTableHost implements TableHost
{
	protected final Connection connection;
	protected final Statement statement;
	
	public SqlTableHost(String host, String username, String password) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		host = "jdbc:mysql://".concat(host);
		
		if(username == null && password == null)
			connection = java.sql.DriverManager.getConnection(host);
		else connection = java.sql.DriverManager.getConnection(host, username, password);

		statement = connection.createStatement();
		
		DatabaseMetaData metadata = connection.getMetaData();
		
		ResultSet tables = metadata.getCatalogs();
		boolean nbaqueryDatabaseExists = false;
		while(tables.next())
			if(tables.getString(1).equals("nbaquery"))
			{
				nbaqueryDatabaseExists = true;
				break;
			}
		
		if(!nbaqueryDatabaseExists)
			statement.execute("create database nbaquery");
		
		connection.setCatalog("nbaquery");
	}
	
	public static void main(String[] arguments) throws Exception
	{
		System.out.print("password: ");
		java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
		new SqlTableHost("localhost", "root", reader.readLine());
	}
	
	@Override
	public Table getTable(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void deleteTable(String tableName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performQuery(Query query, String tableName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DirtyDataInfo nextDirtyDataInfo() {
		// TODO Auto-generated method stub
		return null;
	}
}
