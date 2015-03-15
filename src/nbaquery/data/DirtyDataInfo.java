package nbaquery.data;

/**
 * A record of the dirty data, containing the info, like in which table,
 * on which row, to which column, and what dirty data occurs.
 * @author luohaoran
 * @see nbaquery.data.TableHost
 */

public class DirtyDataInfo
{
	public Table table;
	
	public Row row;
	
	public Column column;
	
	public Object data;
}
