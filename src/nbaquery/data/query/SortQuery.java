package nbaquery.data.query;

import nbaquery.data.Table;

public class SortQuery implements Query
{
	public Table table;
	
	public String keyword;
	
	public boolean ascend = false;
}
