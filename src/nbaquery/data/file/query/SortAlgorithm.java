package nbaquery.data.file.query;

import java.util.HashMap;
import java.util.Map;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.file.FileTableHost;
import nbaquery.data.file.KeywordTable;
import nbaquery.data.file.MultivaluedTable;
import nbaquery.data.file.Tuple;
import nbaquery.data.query.Query;
import nbaquery.data.query.SortQuery;

public class SortAlgorithm implements FileTableAlgorithm
{

	@Override
	public Table perform(Query query)
	{
		SortQuery sort = (SortQuery) query;
		Column[] columns = sort.table.getColumns().toArray(new Column[0]);
		String[] columnNames = new String[columns.length];
		Class<?>[] columnDataClasses = new Class<?>[columns.length];
		
		Column keyword = null;
		for(int i = 0; i < columns.length; i ++)
		{
			columnNames[i] = columns[i].getColumnName();
			columnDataClasses[i] = columns[i].getDataClass();
			if(columns[i].getColumnName().equalsIgnoreCase(sort.keyword))
				keyword = columns[i];
		}
		
		MultivaluedTable resultTable = new MultivaluedTable((FileTableHost) sort.table.getTableHost(), columnNames, columnDataClasses);
		if(keyword == null)
		{
			KeywordTable table = ((KeywordTable)sort.table);
			keyword = table.keyword;
		}
		
		Column[] resultColumns = resultTable.getColumns().toArray(new Column[0]);
		Column[] originColumns = new Column[resultColumns.length];
		
		for(int i = 0; i < resultColumns.length; i ++)
			originColumns[i] = sort.table.getColumn(resultColumns[i].getColumnName());
		
		KeywordMapper mapper = keywordMappers.get(keyword.getDataClass());
		if(mapper != null)
		{
			Row[] row = sort.table.getRows();
		
			final RowLinkedListNode dummyHead = new RowLinkedListNode();
			RowLinkedListNode current = dummyHead;
			for(int i = 0; i < row.length; i ++)
			{
				RowLinkedListNode node = new RowLinkedListNode();
				node.row = row[i];
				
				node.keyword = mapper.getKeyword(keyword.getAttribute(row[i]));
				if(sort.ascend) node.keyword = -node.keyword;
				
				current.next = node;
				current = node;
			}
			RadixSort.sort(dummyHead);
			
			LinkedListNode iterator = dummyHead.next;
			while(iterator != null)
			{
				Row currentRow = ((RowLinkedListNode)iterator).row;
				Tuple tuple = resultTable.createTuple();
				for(int i = 0; i < resultColumns.length; i ++)
					resultColumns[i].setAttribute(tuple, originColumns[i].getAttribute(currentRow));
				
				iterator = iterator.next;
			}
		}
		
		return resultTable;
	}
	
	@Override
	public Class<? extends Query> getProcessingQuery()
	{
		return SortQuery.class;
	}
	
	interface KeywordMapper
	{
		public Integer getKeyword(Object mapping);
	}
	
	static final Map<Class<?>, KeywordMapper> keywordMappers = new HashMap<Class<?>, KeywordMapper>();
	static
	{
		keywordMappers.put(Integer.class, new KeywordMapper()
		{
			@Override
			public Integer getKeyword(Object mapping)
			{
				return (int)mapping;
			}
		});
		
		keywordMappers.put(Float.class, new KeywordMapper()
		{
			@Override
			public Integer getKeyword(Object mapping)
			{
				return Float.floatToRawIntBits((float)mapping);
			}
		});
	}
}

class RowLinkedListNode extends LinkedListNode
{
	public Row row;
}