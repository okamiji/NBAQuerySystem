package nbaquery.data.sql;

import java.sql.ResultSet;

import nbaquery.data.Table;

public class ResultSetRow implements SqlTableRow
{
	final ResultSet resultSet;
	final Table declaredTable;
	final int rowId;
	
	public ResultSetRow(Table declaredTable, ResultSet resultSet, int rowId)
	{
		this.declaredTable = declaredTable;
		this.resultSet = resultSet;
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
			synchronized(resultSet)
			{
				resultSet.absolute(rowId);
				return converter.read(resultSet, index);
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
			synchronized(resultSet)
			{
				resultSet.absolute(rowId);
				converter.update(resultSet, index, value);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
