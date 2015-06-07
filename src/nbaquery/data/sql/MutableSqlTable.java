package nbaquery.data.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import nbaquery.data.Column;
import nbaquery.data.Cursor;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;

public class MutableSqlTable implements Table
{
	final SqlTableHost tableHost;
	final String tableName;
	final Statement statement;
	
	TreeMap<String, Column> columns = new TreeMap<String, Column>();
	public String insertQuery;
	public PreparedStatement selectionQuery;
	
	public MutableSqlTable(SqlTableHost tableHost, String tableName, 
			String[] columns, Class<?>[] types, String[] sqlTypes, String keyword) throws Exception
	{
		this.tableHost = tableHost;
		this.tableName = tableName;
		this.statement = this.tableHost.connection.createStatement();
		
		String paramList = columns[0];
		String questionList = "?";
		for(int i = 1; i < columns.length; i ++)
		{
			paramList = paramList.concat(", ").concat(columns[i]);
			questionList = questionList.concat(", ?");
		}
		
		String selectQuery = String.format("select %s from %s", paramList, tableName);
		insertQuery = String.format("insert into %s (%s) values (%s)", tableName, paramList,  questionList);
		
		this.selectionQuery = this.tableHost.connection.prepareStatement(selectQuery);
		
		boolean shouldMakeTable = true;
		if(this.tableHost.declaredTable.contains(tableName))
		{
			ResultSet prefetch = this.statement.executeQuery(String.format("select * from %s where 0", tableName));
			ResultSetMetaData rsmeta = prefetch.getMetaData();

			shouldMakeTable = false;
			for(int i = 1; i <= rsmeta.getColumnCount(); i ++)
			{
				if(!columns[i - 1].equalsIgnoreCase(rsmeta.getColumnName(i)))
				{
					shouldMakeTable = true;
					break;
				}
			}
			if(shouldMakeTable) this.statement.execute(String.format("drop table %s", tableName));
		}
		
		if(shouldMakeTable)
		{
			String creationQuery = String.format("create table %s (%s %s", tableName, columns[0], sqlTypes[0]);
			for(int i = 1; i < columns.length; i ++)
				creationQuery = creationQuery.concat(String.format(", %s %s", columns[i], sqlTypes[i]));
			if(keyword != null) creationQuery = creationQuery.concat(String.format(", primary key(%s)", keyword));
			creationQuery = creationQuery.concat(")");
			this.statement.execute(creationQuery);
		}
		
		for(int i = 1; i <= columns.length; i ++)
			this.columns.put(columns[i - 1].toLowerCase(), new SqlTableColumn(this, columns[i - 1], types[i - 1], i));
		this.tableHost.tables.put(tableName, this);
	}
	
	@Override
	public Collection<? extends Column> getColumns() {
		return this.columns.values();
	}

	public MutableSqlRow createRow()
	{
		try {
			PreparedStatement statement = this.tableHost.connection.prepareStatement(insertQuery);
			return new MutableSqlRow(this, statement, this.columns.size());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Cursor getRows() {
		return null;
	}

	@Override
	public Column getColumn(String columnName) {
		return this.getColumn(columnName.toLowerCase());
	}

	@Override
	public TableHost getTableHost() {
		return this.tableHost;
	}
	
	@Override
	public boolean hasTableChanged(Object accessor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<Row> iterator() {
		return this.getRows();
	}

}
