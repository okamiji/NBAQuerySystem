package nbaquery.data.sql.query;

import java.util.ArrayList;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.data.query.SortQuery;
import nbaquery.data.sql.QuerySqlTable;
import nbaquery.data.sql.SqlTableHost;

public class SortAlgorithm extends SqlQueryAlgorithm<SortQuery>
{
	@Override
	public Table perform(String tableName, SortQuery query) throws Exception {
		ArrayList<String> columns = new ArrayList<String>();
		ArrayList<Class<?>> dataTypes = new ArrayList<Class<?>>();
		
		for(Column column : query.table.getColumns())
		{
			columns.add(column.getColumnName());
			dataTypes.add(column.getDataClass());
		}
		String queryString = String.format("select * from %s order by %s %s", query.table.getTableName(), query.keyword, query.descend? "desc" : "asc");
		if(query.interval > 0)
			queryString = queryString.concat(String.format(" limit %d", query.interval));
		
		return new QuerySqlTable((SqlTableHost) query.table.getTableHost(), false, tableName,
				columns.toArray(new String[0]), dataTypes.toArray(new Class<?>[0]), queryString, new String[]{query.table.getTableName()});
	}

	@Override
	public Class<SortQuery> getQueryClass() {
		return SortQuery.class;
	}
}