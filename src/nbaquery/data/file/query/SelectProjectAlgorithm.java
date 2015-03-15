package nbaquery.data.file.query;

import java.util.ArrayList;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.file.FileTableHost;
import nbaquery.data.file.KeywordTable;
import nbaquery.data.file.MultivaluedTable;
import nbaquery.data.file.Tuple;
import nbaquery.data.query.Query;
import nbaquery.data.query.SelectProjectQuery;

public class SelectProjectAlgorithm implements FileTableAlgorithm
{
	@Override
	public Table perform(Query query)
	{
		SelectProjectQuery selproj = (SelectProjectQuery) query;
		KeywordTable originTable = (KeywordTable) selproj.table;
		
		ArrayList<Class<?>> resultClasses = new ArrayList<Class<?>>();
		
		boolean createKeywordTable = false;
		if(selproj.projectColumns == null || selproj.projectColumns.length == 0)
		{
			Column[] columns = originTable.getColumns().toArray(new Column[0]);
			selproj.projectColumns = new String[columns.length];
			for(int i = 0; i < columns.length; i ++)
			{
				selproj.projectColumns[i] = columns[i].getColumnName();
				resultClasses.add(columns[i].getDataClass());
			}
			createKeywordTable = originTable.keyword != null;
		}	
		else for(String columnName : selproj.projectColumns)
		{
			Column column = originTable.getColumn(columnName);
			if(column != null)
			{
				if(column.equals(originTable.getKeyword())) createKeywordTable = true;
				resultClasses.add(column.getDataClass());
			}
			else resultClasses.add(String.class);
		}
		
		KeywordTable resultTable;
		if(createKeywordTable) resultTable = new KeywordTable((FileTableHost) originTable.getTableHost(), selproj.projectColumns, resultClasses.toArray(new Class<?>[0]), originTable.getKeyword().getColumnName());
		else resultTable = new MultivaluedTable((FileTableHost) originTable.getTableHost(), selproj.projectColumns, resultClasses.toArray(new Class<?>[0]));
		
		Column[] originColumn = new Column[selproj.projectColumns.length];
		Column[] resultColumn = new Column[selproj.projectColumns.length];
		
		for(int i = 0; i < selproj.projectColumns.length; i ++)
		{
			originColumn[i] = originTable.getColumn(selproj.projectColumns[i]);
			resultColumn[i] = resultTable.getColumn(selproj.projectColumns[i]);
		}
		
		for(Row row : originTable.getRows())
		{
			boolean shouldAccept = false;
			try
			{
				shouldAccept = (boolean)selproj.expression.calculate(row);
			}
			catch(Exception exception)
			{
				shouldAccept = false;
			}
			if(shouldAccept)
			{
				Tuple tuple = resultTable.createTuple();
				for(int i = 0; i < selproj.projectColumns.length; i ++)
					if(resultColumn[i] != null && originColumn[i] != null)
				{
					resultColumn[i].setAttribute(tuple, originColumn[i].getAttribute(row));
				}
			}
		}
		
		return resultTable;
	}

	@Override
	public Class<? extends Query> getProcessingQuery()
	{
		return SelectProjectQuery.class;
	}
	
}
