package nbaquery.data.file;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import nbaquery.data.Cursor;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.Trigger;

public class KeywordTable implements Table
{
	public final FileTableHost host;
	public final int headerLength;
	public final KeywordColumn keyword;
	public TreeMap<Object, Tuple> keyToTupleMap = new TreeMap<Object, Tuple>();
	public TreeMap<String, FileTableColumn> index = new TreeMap<String, FileTableColumn>();
	
	public final List<Trigger> triggers = new ArrayList<Trigger>();
	
	boolean tableLocked = false;
	public void gainLock()
	{
		while(tableLocked)
		try
		{
			//Thread.sleep(1);
			Thread.yield();
		}
		catch(Exception e)
		{
		
		}
		tableLocked = true;
	}
	public void releaseLock()
	{
		tableLocked = false;
	}
	
	public KeywordTable(FileTableHost host, String[] header, Class<?>[] dataType, String keyword)
	{
		this.host = host;
		this.headerLength = header.length;
		
		if(header.length != dataType.length) 
			throw new IllegalArgumentException("Mismatch between the length of header and data type array!");
		for(int i = 0; i < headerLength; i ++)
		{
			FileTableColumn column = new FileTableColumn(this, dataType[i], i, header[i].toLowerCase());
			index.put(header[i].toLowerCase(), column);
		}
		if(keyword != null)
		{
			keyword = keyword.toLowerCase();
			FileTableColumn keywordColumn = index.get(keyword);
			if(keywordColumn != null)
			{
				this.keyword = new KeywordColumn(keywordColumn);
				index.put(keyword, this.keyword);
			}
			else this.keyword = null;
		}
		else this.keyword = null;
	}
	
	public synchronized Tuple createTuple()
	{
		gainLock();
		synchronized(notify)
		{
			notify.clear();
		}
		
		Tuple tuple = new Tuple();
		tuple.attributes = new Object[headerLength];
		tuple.table = this;
		releaseLock();
		return tuple;
	}
	
	public synchronized Collection<Tuple> listTuples()
	{
		gainLock();
		Collection<Tuple> returnValue = this.keyToTupleMap.values();
		releaseLock();
		return returnValue;
	}
	
	public synchronized void removeTuple(Tuple tuple)
	{
		if(tuple.table != this) return;
		
		gainLock();
		synchronized(notify)
		{
			notify.clear();
		}
		
		this.keyToTupleMap.remove(keyword.getAttribute(tuple));
		releaseLock();
	}

	@Override
	public FileTableColumn getColumn(String columnName)
	{
		return this.index.get(columnName.toLowerCase());
	}

	public FileTableColumn getKeyword()
	{
		return keyword;
	}
	
	public class KeywordColumn extends FileTableColumn
	{
		public KeywordColumn(FileTableColumn parent)
		{
			super(parent.table, parent.dataClass, parent.columnIndex, parent.columnName);
		}
		
		public void setAttribute(Row tuple, Object keyword)
		{
			if(keyword == null) return;
			Object originKeyword = this.getAttribute(tuple);
			super.setAttribute(tuple, keyword);
			if(keyToTupleMap.get(keyword) != null) throw new IllegalArgumentException("A collision in keyword with key " + keyword + " was detected!");
			if(originKeyword != null) ((KeywordTable)table).keyToTupleMap.remove(originKeyword);
			
			((KeywordTable)table).keyToTupleMap.put(keyword, (Tuple)tuple);
		}
	}

	@Override
	public Collection<FileTableColumn> getColumns()
	{
		return this.index.values();
	}
	
	@Override
	public Cursor getRows()
	{
		gainLock();
		Row[] rows = this.keyToTupleMap.values().toArray(new Row[0]);
		releaseLock();
		return new FileTableCursor(rows);
	}

	@Override
	public TableHost getTableHost()
	{
		return this.host;
	}
	
	protected HashSet<Object> notify = new HashSet<Object>();
	
	public synchronized boolean hasTableChanged(Object accessor)
	{
		synchronized(notify)
		{
			if(notify.contains(accessor)) return false;
			notify.add(accessor); return true;
		}
	}
	@Override
	public final Iterator<Row> iterator() {
		return this.getRows();
	}
	
	/**
	 * File Table Hosts Doesn't Need Rebuild Operation.
	 */
	@Override
	public String getTableName() {
		return this.toString();
	}
	
	@Override
	public void registerTrigger(Trigger trigger) {
		this.triggers.add(trigger);
		trigger.retrieve(this);
	}
}