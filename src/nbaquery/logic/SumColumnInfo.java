package nbaquery.logic;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.query.GroupColumnInfo;

public class SumColumnInfo extends GroupColumnInfo
{
	public String fromName;
	public SumColumnInfo(String collapsedName, String fromName)
	{
		super(collapsedName, Integer.class);
		this.fromName = fromName;
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
		Integer sum = 0;
		for(Row row : rows)
		{
			Integer value = (Integer) fromColumn.getAttribute(row);
			if(value != null) sum += value;
		}
		getGroupColumn().setAttribute(resultRow, sum);
	}
}
