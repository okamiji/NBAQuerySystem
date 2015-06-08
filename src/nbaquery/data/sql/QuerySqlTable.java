package nbaquery.data.sql;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

import nbaquery.data.Column;
import nbaquery.data.Cursor;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;

public class QuerySqlTable implements Table
{
	public final SqlTableHost tableHost;
	public final TreeMap<String, Column> columns = new TreeMap<String, Column>();
	public final PreparedStatement executeQuery;
	public final String[] dependTables;
	
	public QuerySqlTable(SqlTableHost tableHost, boolean shouldCreateView, String viewName, String[] columns, Class<?>[] types, String viewQuery, String[] dependTables) throws Exception
	{
		this.tableHost = tableHost;
		this.dependTables = dependTables;
		if(shouldCreateView)
		{
			this.tableHost.connection.createStatement().execute(String.format("create or replace view %s as %s", viewName, viewQuery));
			this.executeQuery = this.tableHost.connection.prepareStatement(String.format("select * from %s", viewName));
		}
		else this.executeQuery = this.tableHost.connection.prepareStatement(viewQuery);
		
		for(int i = 0; i < columns.length; i ++)
			this.columns.put(columns[i], new SqlTableColumn(this, columns[i], types[i], i + 1));
		
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

	HashSet<Object> notified = new HashSet<Object>();
	@Override
	public boolean hasTableChanged(Object accessor){
		boolean noChanged = true;
		for(String table : dependTables)
			if(this.tableHost.getTable(table).hasTableChanged(this))
				noChanged = false;
		if(!noChanged) notified.clear();
		
		if(notified.contains(accessor)) return false;
		else
		{
			notified.add(accessor);
			return true;
		}
	}

}
