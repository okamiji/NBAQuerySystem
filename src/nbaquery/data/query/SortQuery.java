package nbaquery.data.query;

import nbaquery.data.Table;

public class SortQuery implements Query
{
	public Table table;
	
	public String keyword;
	
	public boolean descend;
	
	public int interval;
	
	public SortQuery(Table table, String keyword, int interval)
	{
		this(table, keyword, interval, false);
	}
	
	public SortQuery(Table table, String keyword, boolean descend)
	{
		this(table, keyword, -1, descend);
	}
	
	public SortQuery(Table table, String keyword)
	{
		this(table, keyword, -1, false);
	}
	
	public SortQuery(Table table, String keyword, int interval, boolean descend)
	{
		this.table = table; this.keyword = keyword;
		this.descend = descend;
		this.interval = interval;
	}
}
