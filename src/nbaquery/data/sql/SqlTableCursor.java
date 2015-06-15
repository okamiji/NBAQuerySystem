package nbaquery.data.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import nbaquery.data.Cursor;
import nbaquery.data.Row;
import nbaquery.data.Table;

public abstract class SqlTableCursor implements Cursor
{
	int cursor = 1;
	final Table declaredTable;
	ResultSet resultSet;
	final int resultLength;
	
	public SqlTableCursor(Table declaredTable) throws Exception
	{
		this.declaredTable = declaredTable;
		this.resultSet = this.getResultSet();
		this.resultSet.last();
		this.resultLength = resultSet.getRow();
	}
	
	protected abstract ResultSet getResultSet() throws SQLException;
	
	@Override
	public boolean hasNext() {
		return cursor <= resultLength;
	}

	@Override
	public Row next()
	{
		if(cursor <= resultLength)
		{
			Row returnRow = new ResultSetRow(declaredTable, this, cursor);
			cursor ++;
			return returnRow;
		}
		else return null;
	}

	@Override
	public void absolute(int position) {
		this.cursor = position + 1;
	}

	@Override
	public int getLength() {
		return resultLength;
	}
}
