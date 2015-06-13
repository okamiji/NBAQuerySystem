package nbaquery.data.file;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;

public class FileTableColumn implements Column
{
	public final Table table;
	public final int columnIndex;
	public final Class<?> dataClass;
	public final Converter<?> converter;
	public final String columnName;
	
	public FileTableColumn(Table table, Class<?> dataType, int columnIndex, String columnName)
	{
		this.table = table;
		this.dataClass = dataType;
		this.columnIndex = columnIndex;
		this.converter = (Converter<?>)Converter.ConverterMap.conversion.get(dataType);
		this.columnName = columnName;
	}
	
	public void setAttribute(Row row, Object value)
	{
		if(table != row.getDeclaredTable()) return;
		if(value != null) if(value instanceof String)
		{
			Object obj = converter.convert((String)value);
			if(obj == null)
				return;
			else value = obj;
		}
		else
		{
			if(!dataClass.equals(value.getClass()))
				return;
		}
		row.getAttributes()[columnIndex] = value;
	}
	
	public Object getAttribute(Row row)
	{
		if(table != row.getDeclaredTable()) return null;
		return row.getAttributes()[columnIndex];
	}
	
	public boolean equals(Object object)
	{
		if(object instanceof FileTableColumn)
		{
			return ((FileTableColumn)object).table == this.table 
				&& ((FileTableColumn)object).columnIndex == this.columnIndex;
		}
		else return false;
	}

	@Override
	public Class<?> getDataClass()
	{
		return this.dataClass;
	}

	@Override
	public Table getDeclaringTable()
	{
		return this.table;
	}

	@Override
	public String getColumnName()
	{
		return this.columnName;
	}
}