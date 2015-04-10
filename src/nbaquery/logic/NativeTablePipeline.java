package nbaquery.logic;

import nbaquery.data.Table;
import nbaquery.data.TableHost;

public class NativeTablePipeline implements LogicPipeline
{
	Table nativeTable;
	
	public NativeTablePipeline(TableHost nativeHost, String tableName)
	{
		this.nativeTable = nativeHost.getTable(tableName);
	}
	
	@Override
	public Table getTable()
	{
		return nativeTable;
	}
	
}
