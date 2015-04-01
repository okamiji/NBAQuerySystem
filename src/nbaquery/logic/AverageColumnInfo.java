package nbaquery.logic;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.query.GroupColumnInfo;

public class AverageColumnInfo extends GroupColumnInfo
{
	public String fromName;
	public AverageColumnInfo(String fromName)
	{
		super(fromName.concat("_avg"), Float.class);
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
		getGroupColumn().setAttribute(resultRow, 1.0f * sum / rows.length);
	}
}
