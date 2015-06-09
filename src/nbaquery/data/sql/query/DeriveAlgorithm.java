package nbaquery.data.sql.query;

import java.sql.ResultSet;
import java.util.ArrayList;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.data.query.DeriveQuery;
import nbaquery.data.sql.BaseTableConstants;
import nbaquery.data.sql.MutableSqlRow;
import nbaquery.data.sql.MutableSqlTable;
import nbaquery.data.sql.QuerySqlTable;
import nbaquery.data.sql.SqlTableColumn;
import nbaquery.data.sql.SqlTableHost;

public class DeriveAlgorithm extends SqlQueryAlgorithm<DeriveQuery>
{	
	@Override
	public Table perform(String tableName, DeriveQuery query) throws Exception {
		SqlTableHost host = (SqlTableHost) query.table.getTableHost();
		ArrayList<String> columns = new ArrayList<String>();
		ArrayList<String> sqlTypes = new ArrayList<String>();
		ArrayList<Class<?>> dataTypes = new ArrayList<Class<?>>();
		StringBuilder projection = new StringBuilder();
		boolean isFirst = true;
		
		if(query.projectColumns != null && query.projectColumns.length > 0)
		{
			for(String project : query.projectColumns)
			{
				if(!isFirst) projection.append(", ");
				projection.append(project);
				
				columns.add(query.table.getColumn(project).getColumnName());
				dataTypes.add(query.table.getColumn(project).getDataClass());
				sqlTypes.add(BaseTableConstants.sqlTypeMap.get(query.table.getColumn(project).getDataClass()));
				isFirst = false;
			}
		}
		else
		{
			for(Column column : query.table.getColumns())
			{
				if(!isFirst) projection.append(", ");
				projection.append(column.getColumnName());
				
				columns.add(column.getColumnName());
				dataTypes.add(column.getDataClass());
				sqlTypes.add(BaseTableConstants.sqlTypeMap.get(column.getDataClass()));
				isFirst = false;
			}
		}
		
		String[] projectColumns = columns.toArray(new String[0]);
		
		for(Class<?> dataClass : dataTypes)
			sqlTypes.add(BaseTableConstants.sqlTypeMap.get(dataClass));
		
		for(int i = 0; i < query.deriveColumns.length; i ++)
		{
			columns.add(query.deriveColumns[i]);
			dataTypes.add(query.deriveClasses[i]);
			sqlTypes.add(BaseTableConstants.sqlTypeMap.get(query.deriveClasses[i]));
		}
		
		MutableSqlTable mutable = new MutableSqlTable(host,
				tableName, columns.toArray(new String[0]), dataTypes.toArray(new Class<?>[0]),
				sqlTypes.toArray(new String[0]), new String(projection), query.table.getTableName());
		
		String originTableDenotion = query.table.getTableName();
		if(query.table instanceof QuerySqlTable && ((QuerySqlTable)query.table).query != null)
			originTableDenotion = String.format("(%s) as %s", ((QuerySqlTable)query.table).query, originTableDenotion);
		
		/**
		 * This part is a bit complex, since I need to make derive operation incremental.
		 */
		
		/**
		 * Firstly, remove all row that is not in the projection of original table. since they have been modified.
		 */
		host.connection.createStatement().execute(String.format("delete from %s where (%s) not in (select %s from %s)",
				tableName, new String(projection), new String(projection), originTableDenotion));
		
		/**
		 * Then, find what is not in the result table, which are created or modified.
		 */
		ResultSet resultSet = host.connection.createStatement().executeQuery(String.format("select %s from %s where (%s) not in (select %s from %s)",
				new String(projection), originTableDenotion, new String(projection), new String(projection), tableName));
		
		/**
		 * Finally, derive to the rows which are just created or modified.
		 */
		
		int[] projections = new int[projectColumns.length];
		SqlTableColumn[] sqlTableColumns = new SqlTableColumn[projectColumns.length];
		for(int i = 0; i < projectColumns.length; i ++)
		{
			String project = projectColumns[i];
			projections[i] = resultSet.findColumn(project);
			sqlTableColumns[i] = (SqlTableColumn) mutable.getColumn(project);
		}
		
		query.retrieve(mutable);
		while(resultSet.next())
		{
			MutableSqlRow row = mutable.createRow();
			for(int i = 0; i < projections.length; i ++)
				sqlTableColumns[i].setAttribute(row, sqlTableColumns[i].converter.read(resultSet, projections[i]));
			query.derive(row);
			row.submit();
		}
		
		return mutable;
	}

	@Override
	public Class<DeriveQuery> getQueryClass() {
		return DeriveQuery.class;
	}
	
}
