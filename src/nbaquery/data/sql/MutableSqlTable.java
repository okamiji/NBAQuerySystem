package nbaquery.data.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import nbaquery.data.Column;
import nbaquery.data.Cursor;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.Trigger;

public class MutableSqlTable implements Table
{
	final SqlTableHost tableHost;
	public final String tableName;
	final Statement statement;
	
	TreeMap<String, SqlTableColumn> columns = new TreeMap<String, SqlTableColumn>();
	public PreparedStatement insertionQuery;
	public PreparedStatement selectionQuery;
	public String[] dependencies;
	
	public MutableSqlTable(SqlTableHost tableHost, String tableName, 
			String[] columns, Class<?>[] types, String[] sqlTypes, String keyword, String... dependencies) throws Exception
	{
		this.tableHost = tableHost;
		this.tableName = tableName;
		this.statement = this.tableHost.connection.createStatement();
		this.dependencies = dependencies;
		
		String paramList = columns[0];
		String questionList = "?";
		for(int i = 1; i < columns.length; i ++)
		{
			paramList = paramList.concat(", ").concat(columns[i]);
			questionList = questionList.concat(", ?");
		}
		
		String selectQuery = String.format("select %s from %s", paramList, tableName);
		String insertQuery = String.format("insert into %s (%s) values (%s)", tableName, paramList,  questionList);
		
		this.selectionQuery = this.tableHost.connection.prepareStatement(selectQuery);
		this.insertionQuery = this.tableHost.connection.prepareStatement(insertQuery);
		
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
			if(keyword != null) creationQuery = creationQuery.concat(String.format(", primary key (%s)", keyword));
			creationQuery = creationQuery.concat(")");
			this.statement.execute(creationQuery);
			this.tableHost.declaredTable.add(tableName);
		}
		
		for(int i = 1; i <= columns.length; i ++)
			this.columns.put(columns[i - 1].toLowerCase(), new SqlTableColumn(this, columns[i - 1], types[i - 1], i));
	}
	
	@Override
	public Collection<SqlTableColumn> getColumns() {
		return this.columns.values();
	}

	public MutableSqlRow createRow()
	{
		return new MutableSqlRow(this, this.insertionQuery);
	}
	
	boolean tableLocked = false;
	public void setTableLocked(boolean l)
	{
		this.tableLocked = l;
	}
	
	public boolean getTableLocked()
	{
		return this.tableLocked;
	}
	
	@Override
	public Cursor getRows() {
		try
		{
			while(MutableSqlRow.batchUpdateThread.get(insertionQuery) != null || tableLocked)
				Thread.yield();
			
			return new SqlTableCursor(this)
			{
				@Override
				protected ResultSet getResultSet() throws SQLException {
					// TODO Auto-generated method stub
					return MutableSqlTable.this.selectionQuery.executeQuery();
				}
				
			};
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Column getColumn(String columnName) {
		return this.columns.get(columnName.toLowerCase());
	}

	@Override
	public TableHost getTableHost() {
		return this.tableHost;
	}

	public final HashSet<Object> notifier = new HashSet<Object>(); 
	@Override
	public boolean hasTableChanged(Object accessor) {
		if(this.dependencies != null && this.dependencies.length > 0)
		{
			boolean noChanged = true;
			for(String table : dependencies)
				if(this.tableHost.getTable(table).hasTableChanged(this))
					noChanged = false;
			if(!noChanged) notifier.clear();
		}		
		
		if(!notifier.contains(accessor))
		{
			notifier.add(accessor);
			return true;
		}
		return false;
	}

	@Override
	public Iterator<Row> iterator() {
		return this.getRows();
	}

	@Override
	public String getTableName() {
		return this.tableName;
	}

	public final List<Trigger> triggers = new ArrayList<Trigger>();
	@Override
	public void registerTrigger(Trigger trigger)
	{
		this.triggers.add(trigger);
		trigger.retrieve(this);
	}

}
