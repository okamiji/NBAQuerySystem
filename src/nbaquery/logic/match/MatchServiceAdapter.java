package nbaquery.logic.match;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.query.SortQuery;
import nbaquery.logic.infrustructure.DirectMatchNaturalJoinPerformance;

public class MatchServiceAdapter implements MatchService{

	TableHost tableHost;
	public String[] columnNames;
	
	public MatchServiceAdapter(TableHost tableHost,String[] columnNames){
		this.tableHost=tableHost;
		this.columnNames=columnNames;
	}
	
	@Override
	public String[][] searchForMatchs(int head, boolean isUp) {
		if(head < 0) head = 1;
		if(head > columnNames.length) return null;
		SortQuery sort = null;
		Table table = tableHost.getTable("match_natural_join_performance");
		sort= new SortQuery(table, columnNames[head], isUp);
		
		tableHost.performQuery(sort, "match_query_result");
		Table queryResult = tableHost.getTable("match_query_result");
		Row[] rows = queryResult.getRows();
		String[][] returnValue = new String[rows.length][columnNames.length];
		Column[] columns = new Column[columnNames.length];
		for(int i = 0; i < columnNames.length; i ++)
			columns[i] = queryResult.getColumn(columnNames[i]);
		for(int row = 0; row < rows.length; row ++)
			for(int column = 0; column < columns.length; column ++)
			{
				Object value = columns[column].getAttribute(rows[row]);
				if(value != null) returnValue[row][column] = value.toString();
			}
		tableHost.deleteTable("match_query_result");
		return returnValue;
		
	}

	@Override
	public String[] searchForOneMatch(int matchID) {
		SelectProjectQuery query = null;
		Table table = tableHost.getTable("match_natural_join_performance");
		try {
			query = new SelectProjectQuery("match_natural_join_performance.MATCH_ID=='" + matchID + "'", table);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		tableHost.performQuery(query, "match_query_result");
		Table queryResult = tableHost.getTable("match_query_result");
		Row[] rows = queryResult.getRows();
		
		Column[] columns = new Column[columnNames.length];
		if(rows.length == 1)
		{
			for(int i = 0; i < columnNames.length; i ++)
				columns[i] = queryResult.getColumn(columnNames[i]);
			Object[] values = rows[0].getAttributes();
			String[] returnValue = new String[values.length];
			for(int row = 0; row < rows.length; row ++)
				for(int column = 0; column < columns.length; column ++)
				{
					Object value = columns[column].getAttribute(rows[row]);
					if(value != null) returnValue[column] = value.toString();
				}
			return returnValue;
		}
		else return null;
	}

}
