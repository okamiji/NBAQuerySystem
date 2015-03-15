package nbaquery.data.file;

import nbaquery.data.Row;
import nbaquery.data.Table;

public class Tuple implements Row
{
	public Table table;
	public Object[] attributes;
	
	@Override
	public Object[] getAttributes()
	{
		return attributes;
	}

	@Override
	public Table getDeclaredTable()
	{
		return table;
	}
}
