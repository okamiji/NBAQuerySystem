package nbaquery.data.sql.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.TreeMap;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.query.GroupQuery;
import nbaquery.data.sql.BaseTableConstants;
import nbaquery.data.sql.MutableSqlRow;
import nbaquery.data.sql.MutableSqlTable;
import nbaquery.data.sql.QuerySqlTable;
import nbaquery.data.sql.SqlTableColumn;
import nbaquery.data.sql.SqlTableCursor;
import nbaquery.data.sql.SqlTableHost;

public class GroupAlgorithm extends SqlQueryAlgorithm<GroupQuery>
{
	static final TreeMap<String, Thread> queryThreads = new TreeMap<String, Thread>();
	
	@Override
	public synchronized Table perform(final String tableName, final GroupQuery query) throws Exception {
		final SqlTableHost tableHost = (SqlTableHost) query.table.getTableHost();
		
		final ArrayList<String> columns = new ArrayList<String>();
		final ArrayList<Class<?>> dataTypes = new ArrayList<Class<?>>();
		ArrayList<String> sqlTypes = new ArrayList<String>();
		
		final StringBuilder projections = new StringBuilder();
		final StringBuilder projectionQuestions = new StringBuilder();
		boolean isFirst = true;
		for(String columnName : query.collapseColumn)
		{
			Column column = query.table.getColumn(columnName);
			if(!isFirst)
			{
				projections.append(", ");
				projectionQuestions.append(", ");
			}
			projections.append(column.getColumnName());
			projectionQuestions.append("?");
			
			columns.add(column.getColumnName());
			dataTypes.add(column.getDataClass());
			sqlTypes.add(BaseTableConstants.sqlTypeMap.get(column.getDataClass()));
			isFirst = false;
		}
		
		for(int i = 0; i < query.derivedColumn.length; i ++)
		{
			columns.add(query.derivedColumn[i]);
			dataTypes.add(query.derivedClass[i]);
			sqlTypes.add(BaseTableConstants.sqlTypeMap.get(query.derivedClass[i]));
		}
		
		final MutableSqlTable mutable = new MutableSqlTable(tableHost,
				tableName, columns.toArray(new String[0]), dataTypes.toArray(new Class<?>[0]),
				sqlTypes.toArray(new String[0]), new String(projections), query.table.getTableName());
		
		if(tableHost.getLastestUpdate(mutable) > tableHost.getLastestUpdate(query.table))
		{
			Table originalTable = tableHost.getTable(tableName);
			return originalTable == null? mutable : originalTable;
		}
		
		if(queryThreads.get(tableName) == null)
		{
			Thread queryThread = new Thread()
			{
				public void run()
				{
					mutable.setTableLocked(true);
					
					try
					{
						if(query.table instanceof MutableSqlTable) while(((MutableSqlTable)query.table).getTableLocked())
						{
							System.out.println("Waiting for " + query.table.getTableName());
							try {	Thread.sleep(500);	} catch (InterruptedException e) {}
						}
						else while(((QuerySqlTable)query.table).isTableLocked())
						{
							System.out.println("Waiting for " + query.table.getTableName());
							try {	Thread.sleep(500);	} catch (InterruptedException e) {}
						}
						
						tableHost.connection.createStatement().execute(String.format("delete from %s", tableName));
						
						String legacyDenotion = query.table.getTableName();
						if(query.table instanceof QuerySqlTable && ((QuerySqlTable)query.table).query != null)
							legacyDenotion = String.format("(%s) as %s", ((QuerySqlTable)query.table).query, ((QuerySqlTable)query.table).getTableName());
						
						SqlTableColumn[] projectionColumns = new SqlTableColumn[query.collapseColumn.length];
						SqlTableColumn[] writeColumns = new SqlTableColumn[query.collapseColumn.length];
						
						for(int i = 0; i < projectionColumns.length; i ++)
						{
							projectionColumns[i] = new SqlTableColumn(query.table, columns.get(i), dataTypes.get(i), i + 1);
							writeColumns[i] = (SqlTableColumn) mutable.getColumn(columns.get(i));
						}
						
						ResultSet distinctProjections = 
								tableHost.connection.createStatement().executeQuery(String.format("select distinct %s from %s", new String(projections), legacyDenotion));
						PreparedStatement collapseSelection = 
								tableHost.connection.prepareStatement(String.format("select * from %s where (%s) = (%s)", legacyDenotion, new String(projections), new String(projectionQuestions)));
						
						query.retrieve(mutable);
						
						/**
						 * Select All Projected Columns From Original Table.
						 */
						while(distinctProjections.next())
						{
							MutableSqlRow resultRow = mutable.createRow();
							for(int i = 0; i < projectionColumns.length; i ++)
							{
								Object value = projectionColumns[i].converter.read(distinctProjections, i + 1);
								writeColumns[i].setAttribute(resultRow, value);
								projectionColumns[i].converter.write(collapseSelection, i + 1, value);
							}
							/**
							 * Find All Rows That Matches Current Columns.
							 */
							ResultSet results = collapseSelection.executeQuery();
							SqlTableCursor cursor = new SqlTableCursor(query.table, results);
							ArrayList<Row> rows = new ArrayList<Row>();
							while(cursor.hasNext()) rows.add(cursor.next());
							query.collapse(rows.toArray(new Row[0]), resultRow);
							resultRow.submit();
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					mutable.setTableLocked(false);
					queryThreads.remove(tableName);
				}
			};
			queryThreads.put(tableName, queryThread);
			queryThread.start();
		}
		return mutable;
	}

	@Override
	public Class<GroupQuery> getQueryClass() {
		return GroupQuery.class;
	}

}
