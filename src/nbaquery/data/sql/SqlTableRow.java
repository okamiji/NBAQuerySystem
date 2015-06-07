package nbaquery.data.sql;

import nbaquery.data.Row;

public interface SqlTableRow extends Row
{
	public Object getAttribute(int index);
	
	public void setAttribute(int index, Class<?> dataClass, Object value);
}
