package nbaquery.data.file.query;

import java.util.ArrayList;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.file.FileTableHost;
import nbaquery.data.file.KeywordTable;
import nbaquery.data.file.MultivaluedTable;
import nbaquery.data.file.Tuple;
import nbaquery.data.query.DeriveQuery;
import nbaquery.data.query.Query;

public class DeriveAlgorithm implements FileTableAlgorithm
{

	@Override
	public Table perform(Query query)
	{
		DeriveQuery derive = (DeriveQuery) query;
		KeywordTable originTable = (KeywordTable) derive.table;
		
		ArrayList<Class<?>> resultClasses = new ArrayList<Class<?>>();
		ArrayList<String> resultHeader = new ArrayList<String>();
		
		if(derive.deriveColumns == null || derive.deriveColumns.length == 0) return null;
		if(derive.deriveClasses == null || derive.deriveClasses.length != derive.deriveColumns.length) return null;
		
		boolean createKeywordTable = false;
		if(derive.projectColumns == null || derive.projectColumns.length == 0)
		{
			Column[] columns = originTable.getColumns().toArray(new Column[0]);
			for(int i = 0; i < columns.length; i ++)
			{
				resultHeader.add(columns[i].getColumnName());
				resultClasses.add(columns[i].getDataClass());
			}
			createKeywordTable = originTable.keyword != null;
		}	
		else for(String columnName : derive.projectColumns)
		{
			resultHeader.add(columnName);
			Column column = originTable.getColumn(columnName);
			if(column != null)
			{
				if(column.equals(originTable.getKeyword())) createKeywordTable = true;
				resultClasses.add(column.getDataClass());
			}
			else resultClasses.add(String.class);
		}
		
		Column[] originColumn = new Column[resultHeader.size()];
		for(int i = 0; i < resultHeader.size(); i ++)
			originColumn[i] = originTable.getColumn(resultHeader.get(i));
		
		for(int i = 0; i < derive.deriveColumns.length; i ++)
		{
			resultHeader.add(derive.deriveColumns[i]);
			resultClasses.add(derive.deriveClasses[i]);
		}
		
		KeywordTable resultTable;
		if(createKeywordTable) resultTable = new KeywordTable((FileTableHost) originTable.getTableHost(), resultHeader.toArray(new String[0]), resultClasses.toArray(new Class<?>[0]), originTable.getKeyword().getColumnName());
		else resultTable = new MultivaluedTable((FileTableHost) originTable.getTableHost(), resultHeader.toArray(new String[0]), resultClasses.toArray(new Class<?>[0]));
		
		Column[] resultColumn = new Column[originColumn.length];
		for(int i = 0; i < originColumn.length; i ++)
			resultColumn[i] = resultTable.getColumn(originColumn[i].getColumnName());
		
		derive.retrieve(resultTable);
		
		for(Row row : originTable.getRows())
		{
			Tuple tuple = resultTable.createTuple();
			for(int i = 0; i < resultColumn.length; i ++)
				resultColumn[i].setAttribute(tuple, originColumn[i].getAttribute(row));
			try
			{
				derive.derive(tuple);
			}
			catch(Exception e)
			{
			}
		}
		return resultTable;
	}

	@Override
	public Class<? extends Query> getProcessingQuery()
	{
		return DeriveQuery.class;
	}
	
}
