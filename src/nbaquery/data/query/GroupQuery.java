package nbaquery.data.query;

import nbaquery.data.Row;
import nbaquery.data.Table;

public abstract class GroupQuery implements Query
{
	public Table table;
	public String collapseColumn[];
	public String derivedColumn[];
	public Class<?> derivedClass[];
	
	public void retrieve(Table resultTable)
	{
		
	}
	
	public abstract void collapse(Row[] rows, Row resultRow);
}
