package nbaquery.data.query.expression;

import nbaquery.data.Column;
import nbaquery.data.Row;

public class ColumnOperator implements Operator
{
	public Column column;
	protected int index = -1;
	
	@Override
	public Object calculate(Row... row)
	{
		if(index < 0)
		{
			for(int i = 0; i < row.length; i ++)
				if(column.getDeclaringTable() == row[i].getDeclaredTable())
				{
					index = i;
					return column.getAttribute(row[i]);
				}
			return null;
		}
		else return column.getAttribute(row[index]);
		
	}
	
}
