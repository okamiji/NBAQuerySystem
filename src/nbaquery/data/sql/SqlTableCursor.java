package nbaquery.data.sql;

import java.sql.ResultSet;

import nbaquery.data.Cursor;
import nbaquery.data.Row;
import nbaquery.data.Table;

public class SqlTableCursor implements Cursor
{
	int cursor = 1;
	final Table declaredTable;
	final ResultSet resultSet;
	final int resultLength;
	public SqlTableCursor(Table declaredTable, ResultSet resultSet) throws Exception
	{
		this.declaredTable = declaredTable;
		this.resultSet = resultSet;
		resultSet.last();
		this.resultLength = resultSet.getRow();
	}
	
	@Override
	public boolean hasNext() {
		return cursor <= resultLength;
	}

	@Override
	public Row next()
	{
		Row returnRow = new ResultSetRow(declaredTable, resultSet, cursor);
		cursor ++;
		return returnRow;
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
