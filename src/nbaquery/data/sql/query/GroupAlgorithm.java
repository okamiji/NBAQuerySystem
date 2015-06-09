package nbaquery.data.sql.query;

import java.util.ArrayList;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.data.query.GroupQuery;
import nbaquery.data.sql.SqlTableCursor;

public class GroupAlgorithm extends SqlQueryAlgorithm<GroupQuery>
{

	@Override
	public Table perform(String tableName, GroupQuery query) throws Exception {
		ArrayList<String> columns = new ArrayList<String>();
		ArrayList<Class<?>> dataTypes = new ArrayList<Class<?>>();
		
		StringBuilder projections = new StringBuilder();
		boolean isFirst = true;
		for(String columnName : query.collapseColumn)
		{
			Column column = query.table.getColumn(columnName);
			if(!isFirst) projections.append(", ");
			projections.append(column.getColumnName());
			columns.add(column.getColumnName());
			dataTypes.add(column.getDataClass());
			isFirst = false;
		}
		
		for(int i = 0; i < query.derivedColumn.length; i ++)
		{
			columns.add(query.derivedColumn[i]);
			
		}
		
		
		
		new SqlTableCursor(null, null);
		return null;
	}

	@Override
	public Class<GroupQuery> getQueryClass() {
		return GroupQuery.class;
	}

}
