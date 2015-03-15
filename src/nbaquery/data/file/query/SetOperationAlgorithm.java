package nbaquery.data.file.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.data.file.FileTableHost;
import nbaquery.data.file.KeywordTable;
import nbaquery.data.file.MultivaluedTable;
import nbaquery.data.file.Tuple;
import nbaquery.data.query.Query;
import nbaquery.data.query.SetOperation;

public class SetOperationAlgorithm implements FileTableAlgorithm
{

	@Override
	public Table perform(Query query)
	{
		SetOperation setop = (SetOperation) query;
		KeywordTable leftHand = (KeywordTable) setop.leftHand;
		KeywordTable rightHand = (KeywordTable) setop.rightHand;
		
		Collection<? extends Column> leftColumns = leftHand.getColumns();
		Collection<? extends Column> rightColumns = rightHand.getColumns();
		if(leftColumns.size() != rightColumns.size()) return null;
		
		ArrayList<String> columnNames = new ArrayList<String>();
		ArrayList<Class<?>> columnClasses = new ArrayList<Class<?>>();
		for(Column leftColumn : leftColumns)
		{
			Column rightColumn = rightHand.getColumn(leftColumn.getColumnName());
			if(!leftColumn.getDataClass().equals(rightColumn.getDataClass())) return null;
			else
			{
				columnClasses.add(leftColumn.getDataClass());
				columnNames.add(leftColumn.getColumnName());
			}
		}	
		MultivaluedTable resultTable = new MultivaluedTable((FileTableHost) leftHand.getTableHost(), columnNames.toArray(new String[0]), columnClasses.toArray(new Class<?>[0]));
		Set<Object> leftKeySet = leftHand.keyToTupleMap.keySet();
		Set<Object> rightKeySet = rightHand.keyToTupleMap.keySet();
		
		Column[] resultColumns = resultTable.getColumns().toArray(new Column[0]);
		ArrayList<Column> leftHandsideColumns = new ArrayList<Column>();
		for(Column column : resultColumns) leftHandsideColumns.add(leftHand.getColumn(column.getColumnName()));
		
		if(leftHand.getKeyword() != null && rightHand.getKeyword() != null)
		{
			//XXX Compare keywords only in this case. 
			if(setop.operation.equals(SetOperation.EnumSetOperation.INTERSECTION))
			{
				for(Object leftKey : leftKeySet) if(rightKeySet.contains(leftKey))
				{
					Tuple tuple = resultTable.createTuple();
					for(int i = 0; i < resultColumns.length; i ++)
					{
						Object value = leftHandsideColumns.get(i).getAttribute(leftHand.keyToTupleMap.get(leftKey));
						resultColumns[i].setAttribute(tuple, value);
					}
				}
			}
			else if(setop.operation.equals(SetOperation.EnumSetOperation.COMPLEMENT))
			{
				for(Object leftKey : leftKeySet) if(!rightKeySet.contains(leftKey))
				{
					Tuple tuple = resultTable.createTuple();
					for(int i = 0; i < resultColumns.length; i ++)
					{
						Object value = leftHandsideColumns.get(i).getAttribute(leftHand.keyToTupleMap.get(leftKey));
						resultColumns[i].setAttribute(tuple, value);
					}
				}
			}
			else if(setop.operation.equals(SetOperation.EnumSetOperation.UNION))
			{
				for(Object leftKey : leftKeySet)
				{
					Tuple tuple = resultTable.createTuple();
					for(int i = 0; i < resultColumns.length; i ++)
					{
						Object value = leftHandsideColumns.get(i).getAttribute(leftHand.keyToTupleMap.get(leftKey));
						resultColumns[i].setAttribute(tuple, value);
					}
				}
				
				ArrayList<Column> rightHandsideColumns = new ArrayList<Column>();
				for(Column column : resultColumns) rightHandsideColumns.add(rightHand.getColumn(column.getColumnName()));
				
				for(Object rightKey : rightKeySet) if(!leftKeySet.contains(rightKey))
				{
					Tuple tuple = resultTable.createTuple();
					for(int i = 0; i < resultColumns.length; i ++)
					{
						Object value = rightHandsideColumns.get(i).getAttribute(rightHand.keyToTupleMap.get(rightKey));
						resultColumns[i].setAttribute(tuple, value);
					}
				}
			}
		}
		else
		{
			//XXX fully compare attributes this case.
			if(setop.operation.equals(SetOperation.EnumSetOperation.INTERSECTION))
			{
				//NULL
			}
			else if(setop.operation.equals(SetOperation.EnumSetOperation.COMPLEMENT))
			{
				//LEFT HAND
				for(Object leftKey : leftKeySet)
				{
					Tuple tuple = resultTable.createTuple();
					for(int i = 0; i < resultColumns.length; i ++)
					{
						Object value = leftHandsideColumns.get(i).getAttribute(leftHand.keyToTupleMap.get(leftKey));
						resultColumns[i].setAttribute(tuple, value);
					}
				}
			}
			else if(setop.operation.equals(SetOperation.EnumSetOperation.UNION))
			{
				//LEFT HAND AND RIGHT HAND
				for(Object leftKey : leftKeySet)
				{
					Tuple tuple = resultTable.createTuple();
					for(int i = 0; i < resultColumns.length; i ++)
					{
						Object value = leftHandsideColumns.get(i).getAttribute(leftHand.keyToTupleMap.get(leftKey));
						resultColumns[i].setAttribute(tuple, value);
					}
				}
				
				ArrayList<Column> rightHandsideColumns = new ArrayList<Column>();
				for(Column column : resultColumns) rightHandsideColumns.add(rightHand.getColumn(column.getColumnName()));
				
				for(Object rightKey : rightKeySet)
				{
					Tuple tuple = resultTable.createTuple();
					for(int i = 0; i < resultColumns.length; i ++)
					{
						Object value = rightHandsideColumns.get(i).getAttribute(rightHand.keyToTupleMap.get(rightKey));
						resultColumns[i].setAttribute(tuple, value);
					}
				}
			}
		}
		
		return resultTable;
	}

	@Override
	public Class<? extends Query> getProcessingQuery()
	{
		return SetOperation.class;
	}
}