package nbaquery.data.file;

import java.util.Collection;
import java.util.TreeMap;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;

public class KeywordTable implements Table
{
	public final FileTableHost host;
	public final int headerLength;
	public final KeywordColumn keyword;
	public TreeMap<Object, Tuple> keyToTupleMap = new TreeMap<Object, Tuple>();
	public TreeMap<String, FileTableColumn> index = new TreeMap<String, FileTableColumn>();
	
	public KeywordTable(FileTableHost host, String[] header, Class<?>[] dataType, String keyword)
	{
		this.host = host;
		this.headerLength = header.length;
		if(header.length != dataType.length) 
			throw new IllegalArgumentException("Mismatch between the length of header and data type array!");
		for(int i = 0; i < headerLength; i ++)
		{
			FileTableColumn column = new FileTableColumn(this, dataType[i], i, header[i]);
			index.put(header[i].toUpperCase(), column);
		}
		if(keyword != null)
		{
			keyword = keyword.toUpperCase();
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
	
	public Tuple createTuple()
	{
		hasTableChanged = true;
		Tuple tuple = new Tuple();
		tuple.attributes = new Object[headerLength];
		tuple.table = this;
		return tuple;
	}
	
	public Collection<Tuple> listTuples()
	{
		return this.keyToTupleMap.values();
	}
	
	public void removeTuple(Tuple tuple)
	{
		if(tuple.table != this) return;
		this.keyToTupleMap.remove(keyword.getAttribute(tuple));
	}

	@Override
	public FileTableColumn getColumn(String columnName)
	{
		return this.index.get(columnName.toUpperCase());
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
	public Row[] getRows()
	{
		return this.keyToTupleMap.values().toArray(new Row[0]);
	}

	@Override
	public TableHost getTableHost()
	{
		return this.host;
	}
	
	protected boolean hasTableChanged = true;
	
	public boolean hasTableChanged()
	{
		if(hasTableChanged)
		{
			hasTableChanged = false;
			return true;
		}
		return false;
	}

	@Override
	public void renameColumn(String columnName, String newColumnName)
	{
		Column originalColumn = this.index.get(columnName.toUpperCase());
		if(originalColumn != null)
		{
			FileTableColumn oldColumn = ((FileTableColumn)originalColumn);
			FileTableColumn newColumn = new FileTableColumn(this, oldColumn.dataClass, oldColumn.columnIndex, newColumnName);
			this.index.remove(columnName.toUpperCase());
			this.index.put(newColumnName.toUpperCase(), newColumn);
		}
	}
}