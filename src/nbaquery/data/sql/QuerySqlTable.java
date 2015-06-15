package nbaquery.data.sql;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import nbaquery.data.Column;
import nbaquery.data.Cursor;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.Trigger;

public class QuerySqlTable implements Table
{
	public final SqlTableHost tableHost;
	public final TreeMap<String, SqlTableColumn> columns = new TreeMap<String, SqlTableColumn>();
	public final PreparedStatement executeQuery;
	public final String query;
	public final String[] dependTables;
	public final String tableName;
	
	public QuerySqlTable(SqlTableHost tableHost, boolean shouldCreateView, String viewName, String[] columns, Class<?>[] types, String viewQuery, String[] dependTables) throws Exception
	{
		this.tableHost = tableHost;
		this.dependTables = dependTables;
		if(shouldCreateView)
		{
			this.tableHost.connection.createStatement().execute(String.format("create or replace view %s as %s", viewName, viewQuery));
			this.executeQuery = this.tableHost.connection.prepareStatement(String.format("select * from %s", viewName));
			this.query = null;
		}
		else
		{
			this.executeQuery = this.tableHost.connection.prepareStatement(viewQuery);
			this.query = viewQuery;
		}
		this.tableName = viewName;
		
		for(int i = 0; i < columns.length; i ++)
			this.columns.put(columns[i].toLowerCase(), new SqlTableColumn(this, columns[i].toLowerCase(), types[i], i + 1));
		
		//System.out.println(this.tableName + " : " + this.query);
	}
	
	@Override
	public Iterator<Row> iterator() {
		return this.getRows();
	}

	@Override
	public Collection<? extends Column> getColumns() {
		return this.columns.values();
	}

	@Override
	public Cursor getRows() {
		try
		{
			return new SqlTableCursor(this, this.executeQuery.executeQuery());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Column getColumn(String columnName) {
		return this.columns.get(columnName);
	}

	@Override
	public TableHost getTableHost() {
		return this.tableHost;
	}
	
	@Override
	public boolean hasTableChanged(Object accessor){
		boolean noChanged = true;
		for(String table : dependTables) try
		{
			if(this.tableHost.getTable(table).hasTableChanged(accessor))
				noChanged = false;
		}
		catch(Exception e)
		{
			
		}
		return !noChanged;
	}

	@Override
	public String getTableName() {
		return tableName;
	}
	
	public boolean isTableLocked()
	{
		for(String table :dependTables)
		{
			Table theTable = this.tableHost.getTable(table);
			if(theTable instanceof MutableSqlTable)
				if(((MutableSqlTable) theTable).getTableLocked()) return true;
		}
		return false;
	}

	@Override
	public void registerTrigger(Trigger trigger)
	{
		throw new RuntimeException("Cannot register trigger for a table that's not mutable!");
	}
}
