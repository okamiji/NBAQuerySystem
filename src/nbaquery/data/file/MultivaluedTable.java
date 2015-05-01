package nbaquery.data.file;

public class MultivaluedTable extends KeywordTable
{
	public int rowId = 0;
	
	public MultivaluedTable(FileTableHost host, String[] header, Class<?>[] dataTypes)
	{
		super(host, header, dataTypes, null);
	}
	
	public synchronized Tuple createTuple()
	{
		gainLock();
		notify.clear();
		Tuple tuple = new Tuple();
		tuple.attributes = new Object[super.headerLength + 1];
		tuple.attributes[super.headerLength] = rowId;
		tuple.table = this;
		super.keyToTupleMap.put(rowId, tuple);
		rowId ++;
		releaseLock();
		return tuple;
	}
	
	public FileTableColumn getKeyword()
	{
		return null;
	}
}