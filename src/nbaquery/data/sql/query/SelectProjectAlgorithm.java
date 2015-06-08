package nbaquery.data.sql.query;

import java.util.ArrayList;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.sql.QuerySqlTable;
import nbaquery.data.sql.SqlTableHost;

public class SelectProjectAlgorithm extends SqlQueryAlgorithm<SelectProjectQuery>
{

	@Override
	public Table perform(String tableName, SelectProjectQuery query) throws Exception
	{
		String queryString = "select ";
		
		ArrayList<String> columns = new ArrayList<String>();
		ArrayList<Class<?>> dataTypes = new ArrayList<Class<?>>();
		
		boolean isFirst = true;
		if(query.projectColumns == null || query.projectColumns.length == 0)
		{
			for(Column column : query.table.getColumns())
			{
				columns.add(column.getColumnName());
				dataTypes.add(column.getDataClass());;
				if(!isFirst) queryString = queryString.concat(", ");
				queryString = queryString.concat(column.getColumnName());
				isFirst = false;
			}
		}
		else 
		{
			Column column;
			for(String projection : query.projectColumns)
				if((column = query.table.getColumn(projection)) != null)
			{
				columns.add(column.getColumnName());
				dataTypes.add(column.getDataClass());
				if(!isFirst) queryString = queryString.concat(", ");
				queryString = queryString.concat(column.getColumnName());
				isFirst = false;
			}
		}
		
		boolean notDependsViewTable = query.table instanceof QuerySqlTable && ((QuerySqlTable)query.table).query != null;
		
		if(notDependsViewTable)
			queryString = queryString.concat(String.format(" from (%s) as %s", ((QuerySqlTable)query.table).query, query.table.getTableName()));
		else queryString = queryString.concat(String.format(" from %s", query.table.getTableName()));
		
		if(query.expression != null)
			queryString = queryString.concat(" where ").concat(query.expression.rebuild());
		
		System.out.println(queryString);
		return new QuerySqlTable(((SqlTableHost)query.table.getTableHost()), !notDependsViewTable, tableName,
				columns.toArray(new String[0]), dataTypes.toArray(new Class<?>[0]), queryString, new String[]{query.table.getTableName()});
	}

	@Override
	public Class<SelectProjectQuery> getQueryClass() {
		return SelectProjectQuery.class;
	}

}
