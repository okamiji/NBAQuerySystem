package nbaquery.data.sql;

import java.util.Collection;
import java.util.Iterator;

import nbaquery.data.Column;
import nbaquery.data.Cursor;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;

public class MutableSqlTable implements Table
{
	SqlTableHost tableHost;
	
	public MutableSqlTable(SqlTableHost tableHost, String tableName, 
			String[] table, Class<?>[] types, String keyword) throws Exception
	{
		this.tableHost = tableHost;
		
	}
	
	@Override
	public Collection<? extends Column> getColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor getRows() {
		return null;
	}

	@Override
	public Column getColumn(String columnName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TableHost getTableHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasTableChanged(Object accessor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<Row> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
