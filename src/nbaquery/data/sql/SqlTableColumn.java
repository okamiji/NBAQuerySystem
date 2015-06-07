package nbaquery.data.sql;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;

public class SqlTableColumn implements Column
{
	public final int index;
	public final Table table;
	public final String columnName;
	public final Class<?> dataClass;
	public final SqlObjectConverter<?> converter;
	
	public SqlTableColumn(Table table, String columnName, Class<?> dataClass, int index)
	{
		this.table = table;
		this.columnName = columnName;
		this.dataClass = dataClass;
		this.index = index;
		this.converter = SqlObjectConverter.converters.get(dataClass);
	}

	@Override
	public String getColumnName() {
		return this.columnName;
	}

	@Override
	public Class<?> getDataClass() {
		return this.dataClass;
	}

	@Override
	public Table getDeclaringTable() {
		return this.table;
	}

	@Override
	public Object getAttribute(Row row) {
		if(!(row.getDeclaredTable() == this.table)) return null;
		return ((SqlTableRow)row).getAttribute(index, this.converter);
	}

	@Override
	public void setAttribute(Row row, Object value){
		if(!(row.getDeclaredTable() == this.table)) return;
		((SqlTableRow)row).setAttribute(index, this.converter, value);
	}
}
