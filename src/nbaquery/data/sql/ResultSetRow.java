package nbaquery.data.sql;

import nbaquery.data.Table;

public class ResultSetRow implements SqlTableRow
{
	final SqlTableCursor cursor;
	final Table declaredTable;
	final int rowId;
	
	public ResultSetRow(Table declaredTable, SqlTableCursor cursor, int rowId)
	{
		this.declaredTable = declaredTable;
		this.cursor = cursor;
		this.rowId = rowId;
	}
	
	@Override
	public Object[] getAttributes() {
		return null;
	}

	@Override
	public Table getDeclaredTable() {
		return this.declaredTable;
	}

	@Override
	public Object getAttribute(int index, SqlObjectConverter<?> converter) {
		try
		{
			synchronized(cursor.resultSet)
			{
				if(cursor.resultSet == null || cursor.resultSet.isClosed()) 
					cursor.resultSet = this.cursor.getResultSet();
				cursor.resultSet.absolute(rowId);
				return converter.read(cursor.resultSet, index);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setAttribute(int index, SqlObjectConverter<?> converter, Object value) {
		try
		{
			synchronized(cursor.resultSet)
			{
				if(cursor.resultSet == null || cursor.resultSet.isClosed()) 
					cursor.resultSet = this.cursor.getResultSet();
				cursor.	resultSet.absolute(rowId);
				converter.update(cursor.resultSet, index, value);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
