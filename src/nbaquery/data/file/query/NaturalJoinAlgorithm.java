package nbaquery.data.file.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.file.FileTableHost;
import nbaquery.data.file.MultivaluedTable;
import nbaquery.data.file.Tuple;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.data.query.Query;

public class NaturalJoinAlgorithm implements FileTableAlgorithm
{	
	@Override
	public Table perform(Query query)
	{
		NaturalJoinQuery join = (NaturalJoinQuery) query;
		
		if(join.leftTable.getTableHost() != join.rightTable.getTableHost()) return null;
		if(join.joinColumnLeft == null || join.joinColumnRight == null) return null;
		if(join.joinColumnLeft.length != join.joinColumnRight.length) return null;
		
		Column[] jointColumnLeft = new Column[join.joinColumnLeft.length];
		Column[] jointColumnRight = new Column[join.joinColumnLeft.length];
		
		TreeSet<String> intersectColumn = new TreeSet<String>();
		ArrayList<String> resultColumns = new ArrayList<String>();
		ArrayList<Class<?>> resultClasses = new ArrayList<Class<?>>();
		ArrayList<Column> sourceLeftTableColumnGetter = new ArrayList<Column>();
		
		
		for(int i = 0; i < jointColumnLeft.length; i ++)
		{
			jointColumnLeft[i] = join.leftTable.getColumn(join.joinColumnLeft[i]);
			intersectColumn.add(jointColumnLeft[i].getColumnName());
			resultClasses.add(jointColumnLeft[i].getDataClass());
			resultColumns.add(jointColumnLeft[i].getColumnName());
			sourceLeftTableColumnGetter.add(jointColumnLeft[i]);
			
			jointColumnRight[i] = join.rightTable.getColumn(join.joinColumnRight[i]);
			intersectColumn.add(jointColumnRight[i].getColumnName());
		}
		
		Collection<? extends Column> leftTableColumns = join.leftTable.getColumns();
		for(Column column : leftTableColumns)
		{
			String columnName = column.getColumnName();
			if(intersectColumn.contains(columnName)) continue;
			resultClasses.add(column.getDataClass());
			resultColumns.add(column.getColumnName());
			sourceLeftTableColumnGetter.add(column);
		}
		
		Collection<? extends Column> rightTableColumns = join.rightTable.getColumns();
		ArrayList<Column> sourceRightTableColumnGetter = new ArrayList<Column>();
		
		for(Column column : rightTableColumns)
		{
			String columnName = column.getColumnName();
			if(intersectColumn.contains(columnName)) continue;
			resultClasses.add(column.getDataClass());
			resultColumns.add(column.getColumnName());
			sourceRightTableColumnGetter.add(column);
		}
		
		//Put intersections in map.
		
		Row[] rows = join.leftTable.getRows();
		Map<NaturalJoinKey, List<Row>> leftMap = new HashMap<NaturalJoinKey, List<Row>>();
		for(Row row : rows)
		{
			Object[] leftKey = new Object[jointColumnLeft.length];
			for(int i = 0; i < jointColumnLeft.length; i ++) leftKey[i] = jointColumnLeft[i].getAttribute(row);
			NaturalJoinKey leftJoinKey = new NaturalJoinKey(leftKey);
			List<Row> leftList = leftMap.get(leftJoinKey);
			if(leftList == null) leftMap.put(leftJoinKey, (leftList = new ArrayList<Row>()));
			leftList.add(row);
		}
		
		rows = join.rightTable.getRows();
		Map<NaturalJoinKey, List<Row>> rightMap = new HashMap<NaturalJoinKey, List<Row>>();
		for(Row row : rows)
		{
			Object[] rightKey = new Object[jointColumnRight.length];
			for(int i = 0; i < jointColumnRight.length; i ++) rightKey[i] = jointColumnRight[i].getAttribute(row);
			NaturalJoinKey rightJoinKey = new NaturalJoinKey(rightKey);
			List<Row> rightList = rightMap.get(rightJoinKey);
			if(rightList == null) rightMap.put(rightJoinKey, (rightList = new ArrayList<Row>()));
			rightList.add(row);
		}
		
		MultivaluedTable resultTable = new MultivaluedTable((FileTableHost) join.leftTable.getTableHost(),
				resultColumns.toArray(new String[0]), resultClasses.toArray(new Class<?>[0]));
		
		ArrayList<Column> destLeftTableColumnGetter = new ArrayList<Column>();
		for(Column column : sourceLeftTableColumnGetter)
			destLeftTableColumnGetter.add(resultTable.getColumn(column.getColumnName()));
		
		ArrayList<Column> destRightTableColumnGetter = new ArrayList<Column>();
		for(Column column : sourceRightTableColumnGetter)
			destRightTableColumnGetter.add(resultTable.getColumn(column.getColumnName()));
		
		//If two rows could be joint, they should appear at the both side.
		Set<NaturalJoinKey> joinKeySet = leftMap.keySet();
		for(NaturalJoinKey key : joinKeySet)
		{
			List<Row> rightSide = rightMap.get(key);
			if(rightSide == null || rightSide.size() <= 0) continue;
			
			List<Row> leftSide = leftMap.get(key);
			if(leftSide == null || leftSide.size() <= 0) continue;
			
			for(Row left : leftSide) for(Row right : rightSide)
			{
				Tuple tuple = resultTable.createTuple();
				for(int i = 0; i < sourceLeftTableColumnGetter.size(); i ++)
					destLeftTableColumnGetter.get(i).setAttribute(tuple, 
							sourceLeftTableColumnGetter.get(i).getAttribute(left));
				
				for(int i = 0; i < sourceRightTableColumnGetter.size(); i ++)
					destRightTableColumnGetter.get(i).setAttribute(tuple, 
							sourceRightTableColumnGetter.get(i).getAttribute(right));
			}
		}
		
		return resultTable;
	}

	@Override
	public Class<? extends Query> getProcessingQuery()
	{
		return NaturalJoinQuery.class;
	}
	
	private class NaturalJoinKey
	{
		Object[] objects;
		int hashCode;
		
		NaturalJoinKey(Object[] objects)
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
				Object right = ((NaturalJoinKey)object).objects[i];
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
}
