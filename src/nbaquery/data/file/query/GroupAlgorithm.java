package nbaquery.data.file.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.file.FileTableHost;
import nbaquery.data.file.MultivaluedTable;
import nbaquery.data.file.Tuple;
import nbaquery.data.query.GroupQuery;
import nbaquery.data.query.Query;

public class GroupAlgorithm implements FileTableAlgorithm
{

	@Override
	public Table perform(Query query)
	{
		GroupQuery group = (GroupQuery) query;
		
		ArrayList<String> resultColumnsArrayList = new ArrayList<String>();
		ArrayList<Class<?>> resultClassesArrayList = new ArrayList<Class<?>>();
		ArrayList<Column> referColumnsArrayList = new ArrayList<Column>();
		for(String collapseColumn : group.collapseColumn)
		{
			Column column = group.table.getColumn(collapseColumn);
			if(column == null) continue;
			referColumnsArrayList.add(column);
			resultColumnsArrayList.add(collapseColumn);
			resultClassesArrayList.add(column.getDataClass());
		}
		
		for(int i = 0; i < group.derivedColumn.length; i ++)
		{
			resultColumnsArrayList.add(group.derivedColumn[i]);
			resultClassesArrayList.add(group.derivedClass[i]);
		}
		
		final MultivaluedTable resultTable = new MultivaluedTable((FileTableHost) group.table.getTableHost(), resultColumnsArrayList.toArray(new String[0]), resultClassesArrayList.toArray(new Class<?>[0]));
		group.retrieve(resultTable);
		
		Column[] referColumns = referColumnsArrayList.toArray(new Column[0]);
		Row[] rows = group.table.getRows();
		HashMap<GroupKey, ArrayList<Row>> mapper = new HashMap<GroupKey, ArrayList<Row>>();
		for(Row row : rows)
		{
			Object[] selectedField = new Object[referColumns.length];
			for(int i = 0; i < referColumns.length; i ++) selectedField[i] = referColumns[i].getAttribute(row);
			GroupKey key = new GroupKey(selectedField);
			ArrayList<Row> mapping = mapper.get(key);
			if(mapping == null)
			{
				mapping = new ArrayList<Row>();
				mapper.put(key, mapping);
			}
			mapping.add(row);
		}
		
		Column[] resultColumns = new Column[referColumns.length];
		for(int i = 0; i < referColumns.length; i ++)
			resultColumns[i] = resultTable.getColumn(referColumns[i].getColumnName());
		
		for(Entry<GroupKey, ArrayList<Row>> entry : mapper.entrySet())
			if(entry != null)
		{
			if(entry.getKey() == null) continue;
			ThreadGroup thread = new ThreadGroup();
			thread.keywordFields = entry.getKey().objects;
			thread.query = group;
			thread.resultColumns = resultColumns;
			thread.resultTable = resultTable;
			thread.rowsList = entry.getValue();
			thread.start();
			try
			{
				thread.join();
			}
			catch(Exception e)
			{
				
			}
		}
		
		return resultTable;
	}

	private class GroupKey
	{
		Object[] objects;
		int hashCode;
		
		GroupKey(Object[] objects)
		{
			this.objects = objects;
			hashCode = 0;
			for(Object object : objects)
			{
				hashCode <<= 4;
				if(object != null) hashCode += object.hashCode();
			}
		}
		
		public boolean equals(Object object)
		{
			for(int i = 0; i < this.objects.length; i ++)
			{
				Object left = this.objects[i];
				Object right = ((GroupKey)object).objects[i];
				if(left == null){ if(right != null) return false; }
				else if(!left.equals(right)) return false;
			}
			return true;
		}
		
		public int hashCode()
		{
			return hashCode;
		}
	}

	private class ThreadGroup extends Thread
	{
		MultivaluedTable resultTable;
		ArrayList<Row> rowsList;
		GroupQuery query;
		Object[] keywordFields;
		Column[] resultColumns;
		
		public void run()
		{
			try
			{
				Row[] rows = rowsList.toArray(new Row[0]);
				Tuple tuple = resultTable.createTuple();
				for(int i = 0; i < resultColumns.length; i ++)
					resultColumns[i].setAttribute(tuple, keywordFields[i]);
				query.collapse(rows, tuple);
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
	@Override
	public Class<? extends Query> getProcessingQuery()
	{
		return GroupQuery.class;
	}
	
}