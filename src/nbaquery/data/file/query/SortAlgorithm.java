package nbaquery.data.file.query;

import java.util.Date;
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
			final RowLinkedListNode dummyHead = new RowLinkedListNode();
			RowLinkedListNode current = dummyHead;
			for(Row row : sort.table)
			{
				RowLinkedListNode node = new RowLinkedListNode();
				node.row = row;
				
				Object keywordObject = keyword.getAttribute(row);
				if(keywordObject != null) node.keyword = mapper.getKeyword(keywordObject);
				else node.keyword = -1;
				if(sort.descend) node.keyword = -node.keyword;
				
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
				
				sort.interval --;
				if(sort.interval == 0) break;
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
		public Long getKeyword(Object mapping);
	}
	
	static final Map<Class<?>, KeywordMapper> keywordMappers = new HashMap<Class<?>, KeywordMapper>();
	static
	{
		keywordMappers.put(Integer.class, new KeywordMapper()
		{
			@Override
			public Long getKeyword(Object mapping)
			{
				return (long)((Integer)mapping);
			}
		});
		
		keywordMappers.put(Long.class, new KeywordMapper()
		{
			@Override
			public Long getKeyword(Object mapping)
			{
				return (Long)mapping;
			}
		});
		
		keywordMappers.put(Date.class, new KeywordMapper()
		{
			@Override
			public Long getKeyword(Object mapping)
			{
				return ((Date)mapping).getTime();
			}
		});
		
		keywordMappers.put(Float.class, new KeywordMapper()
		{
			@Override
			public Long getKeyword(Object mapping)
			{
				int rawBits = Float.floatToRawIntBits((float)mapping);
				if(rawBits < 0) rawBits = - rawBits - Integer.MAX_VALUE;
				return (long)rawBits;
			}
		});
		
		keywordMappers.put(Character.class, new KeywordMapper()
		{
			@Override
			public Long getKeyword(Object mapping)
			{
				return (long)((Character)mapping);
			}
		});

		keywordMappers.put(String.class, new KeywordMapper()
		{
			static final int worldLength = 8;
			@Override
			public Long getKeyword(Object mapping)
			{
				String theString  = (String) mapping;
				long order = 0;
				char[] arrayOfString = theString.toCharArray();
				for(int i = 0; i < worldLength; i ++)
				{
					char current;
					if(i >= arrayOfString.length) current = '0';
					else current = arrayOfString[i];
					
					long preorder = order;
					
					if('a'<=current && current<='z')
					{
						order = order * 36;
						order = order + current - 'a' + 10;
					}
					else if('A'<=current && current<='Z') 
					{
						order = order * 36;
						order = order + current - 'A' + 10;
					}
					else if('0'<=current && current<='9')
					{
						order = order * 36;
						order = order + current - '0';
					}
					else order = order * 36;
					
					if(preorder > 0 && order <= 0)
					{
						order = preorder;
						break;
					}
				}
				return order;
			}
		});
		
	}
}

class RowLinkedListNode extends LinkedListNode
{
	public Row row;
}