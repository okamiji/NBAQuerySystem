package nbaquery.data.file.query;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.file.FileTableHost;
import nbaquery.data.file.MultivaluedTable;
import nbaquery.data.file.Tuple;
import nbaquery.data.query.JoinQuery;
import nbaquery.data.query.Query;

public class JoinAlgorithm implements FileTableAlgorithm
{	
	@Override
	public Table perform(Query query)
	{
		JoinQuery join = (JoinQuery) query;
		
		if(join.leftTable.getTableHost() != join.rightTable.getTableHost()) return null;
		
		Class<?>[] projectDataClass = new Class<?>[join.projectColumns.length]; 
		Column[] originalColumn = new Column[join.projectColumns.length];
		boolean[] ofRightTable = new boolean[join.projectColumns.length];
		
		for(int i = 0; i < join.projectColumns.length; i ++)
		{
			Column column = null;
			String columnName = join.projectColumns[i];
			if(!columnName.matches(".+[.].+"))
			{
				column = join.leftTable.getColumn(columnName);
				if(column == null)
				{
					column = join.rightTable.getColumn(columnName);
					ofRightTable[i] = true;
				}
			}
			else
			{
				String[] splitted = columnName.split("[,]");
				column = join.leftTable.getTableHost().getTable(splitted[0]).getColumn(splitted[1]);
				if(column != null) if(column.getDeclaringTable().equals(join.rightTable))
					ofRightTable[i] = true;
			}
			
			if(column != null)
			{
				originalColumn[i] = column;
				projectDataClass[i] = column.getDataClass();
			}
			else
			{
				originalColumn[i] = null;
				projectDataClass[i] = String.class;
			}
		}

		MultivaluedTable resultTable = new MultivaluedTable((FileTableHost) join.leftTable.getTableHost(), join.projectColumns, projectDataClass);
		
		Column[] resultColumn = new Column[originalColumn.length];
		for(int i = 0; i < resultColumn.length; i ++) if(originalColumn[i] != null)
			resultColumn[i] = resultTable.getColumn(originalColumn[i].getColumnName());
		
		for(Row left : join.leftTable) for(Row right : join.rightTable) try
		{
			boolean shouldJoin = (boolean) join.expression.calculate(left, right);
			if(shouldJoin)
			{
				Tuple tuple = resultTable.createTuple();
				for(int i = 0; i < resultColumn.length; i ++) if(originalColumn[i] != null)
				{
					resultColumn[i].setAttribute(tuple, 
							originalColumn[i].getAttribute(ofRightTable[i]? right : left)
					);
				}
			}
		}
		catch(Exception e)
		{
			
		}
		
		return resultTable;
	}

	@Override
	public Class<? extends Query> getProcessingQuery()
	{
		return JoinQuery.class;
	}
	
}
