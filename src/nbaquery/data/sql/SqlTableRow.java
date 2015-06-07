package nbaquery.data.sql;

import nbaquery.data.Row;

public interface SqlTableRow extends Row
{
	public Object getAttribute(int index, SqlObjectConverter<?> converter);
	
	public void setAttribute(int index, SqlObjectConverter<?> converter, Object value);
}
