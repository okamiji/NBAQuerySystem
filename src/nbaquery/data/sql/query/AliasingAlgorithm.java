package nbaquery.data.sql.query;

import java.util.ArrayList;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.data.query.AliasingQuery;
import nbaquery.data.sql.QuerySqlTable;
import nbaquery.data.sql.SqlTableHost;

public class AliasingAlgorithm extends SqlQueryAlgorithm<AliasingQuery>
{
	@Override
	public Table perform(String tableName, AliasingQuery query) throws Exception
	{
		StringBuilder queryString = new StringBuilder("select ");
		
		ArrayList<String> columns = new ArrayList<String>();
		ArrayList<Class<?>> dataTypes = new ArrayList<Class<?>>();
		
		boolean isFirst = true;
		for(int i = 0; i < query.columnNames.length; i ++)
		{
			Column column = query.table.getColumn(query.columnNames[i]);
			columns.add(query.aliases[i]);
			dataTypes.add(column.getDataClass());
			if(!isFirst) queryString.append(", ");
			queryString.append(String.format("%s as %s", query.columnNames[i], query.aliases[i]));
			isFirst = false;
		}
		
		boolean notDependsViewTable = query.table instanceof QuerySqlTable && ((QuerySqlTable)query.table).query != null;
		
		if(notDependsViewTable)
			queryString = queryString.append(String.format(" from (%s) as %s", ((QuerySqlTable)query.table).query, query.table.getTableName()));
		else queryString = queryString.append(String.format(" from %s", query.table.getTableName()));
		
		return new QuerySqlTable(((SqlTableHost)query.table.getTableHost()), !notDependsViewTable, tableName,
				columns.toArray(new String[0]), dataTypes.toArray(new Class<?>[0]), new String(queryString), new String[]{query.table.getTableName()});
	}

	@Override
	public Class<AliasingQuery> getQueryClass() {
		return AliasingQuery.class;
	}
}
