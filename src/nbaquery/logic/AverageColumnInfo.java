package nbaquery.logic;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.query.GroupColumnInfo;

public class AverageColumnInfo extends GroupColumnInfo
{
	public String fromName;
	public AverageColumnInfo(Column column)
	{
		super(column.getColumnName(), Float.class);
		this.fromName = column.getColumnName();
	}

	Column fromColumn;
	@Override
	public void retrieve(Table originalTable, Table resultTable)
	{
		fromColumn = originalTable.getColumn(fromName);
	}

	@Override
	public void collapse(Row[] rows, Row resultRow)
	{
		Float sum = 0.f;	int recordLength = 0;
		if(fromColumn.getDataClass().equals(Integer.class))
		{
			for(Row row : rows)
			{
				Integer value = (Integer) fromColumn.getAttribute(row);
				if(value != null) sum += value;
			}
			recordLength = rows.length;
		}
		else
		{
			for(Row row : rows)
			{
				Float value = (Float) fromColumn.getAttribute(row);
				if(value != null) if(!Float.isNaN(value) && !Float.isInfinite(value))
				{
					sum += value;
					recordLength ++;
				}
			}
		}
		getGroupColumn().setAttribute(resultRow, 1.0f * sum / recordLength);
	}
}
