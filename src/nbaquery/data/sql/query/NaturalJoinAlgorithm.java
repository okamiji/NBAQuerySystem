package nbaquery.data.sql.query;

import java.util.ArrayList;
import java.util.TreeSet;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.data.sql.QuerySqlTable;
import nbaquery.data.sql.SqlTableHost;

public class NaturalJoinAlgorithm extends SqlQueryAlgorithm<NaturalJoinQuery>
{

	@Override
	public Table perform(String tableName, NaturalJoinQuery query) throws Exception {
		StringBuilder queryString = new StringBuilder("select ");
		
		ArrayList<String> columns = new ArrayList<String>();
		ArrayList<Class<?>> dataTypes = new ArrayList<Class<?>>();
		
		TreeSet<String> rightColumnExclusives = new TreeSet<String>();
		for(String rightColumnName : query.joinColumnRight)
			rightColumnExclusives.add(rightColumnName.toLowerCase());
		
		for(Column column : query.leftTable.getColumns())
			rightColumnExclusives.add(column.getColumnName());
		
		boolean isFirst = true;
		for(Column column : query.leftTable.getColumns())
		{
			columns.add(column.getColumnName());
			dataTypes.add(column.getDataClass());
			if(!isFirst) queryString.append(", ");
			queryString.append(String.format("%s.%s as %s",
					column.getDeclaringTable().getTableName(),
					column.getColumnName(),
					column.getColumnName()));
			isFirst = false;
		}
		
		for(Column column : query.rightTable.getColumns())
			if(!rightColumnExclusives.contains(column.getColumnName().toLowerCase()))
		{
			columns.add(column.getColumnName());
			dataTypes.add(column.getDataClass());
			queryString.append(", ");
			queryString.append(String.format("%s.%s as %s",
					column.getDeclaringTable().getTableName(),
					column.getColumnName(),
					column.getColumnName()));
		}
		
		boolean shouldBeView = true;
		String leftOperand = query.leftTable.getTableName();
		if(query.leftTable instanceof QuerySqlTable && ((QuerySqlTable)query.leftTable).query != null)
		{
			leftOperand = String.format("(%s) as %s", ((QuerySqlTable)query.leftTable).query, leftOperand);
			shouldBeView = false;
		}
		
		String rightOperand = query.rightTable.getTableName();
		if(query.rightTable instanceof QuerySqlTable && ((QuerySqlTable)query.rightTable).query != null)
		{
			rightOperand = String.format("(%s) as %s", ((QuerySqlTable)query.rightTable).query, rightOperand);
			shouldBeView = false;
		}
		
		queryString.append(String.format(" from %s inner join %s on ", leftOperand, rightOperand));
		isFirst = true;
		for(int i = 0; i < query.joinColumnLeft.length; i ++)
		{
			if(!isFirst) queryString.append(" and ");
			queryString.append(String.format("%s.%s", query.leftTable.getTableName(), query.joinColumnLeft[i]));
			queryString.append(" = ");
			queryString.append(String.format("%s.%s", query.rightTable.getTableName(), query.joinColumnRight[i]));
			isFirst = false;
		}
		
		return new QuerySqlTable((SqlTableHost) query.leftTable.getTableHost(), shouldBeView, tableName,
				columns.toArray(new String[0]), dataTypes.toArray(new Class<?>[0]), new String(queryString),
				new String[]{query.leftTable.getTableName(), query.rightTable.getTableName()});
	}

	@Override
	public Class<NaturalJoinQuery> getQueryClass() {
		return NaturalJoinQuery.class;
	}

}
