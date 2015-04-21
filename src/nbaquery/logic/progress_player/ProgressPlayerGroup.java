package nbaquery.logic.progress_player;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.DeriveColumnInfo;
import nbaquery.data.query.DeriveQuery;
import nbaquery.data.query.ExpressionDeriveColumnInfo;
import nbaquery.data.query.GroupColumnInfo;
import nbaquery.data.query.GroupQuery;
import nbaquery.data.query.Query;
import nbaquery.data.query.SortQuery;
import nbaquery.logic.LogicPipeline;
import nbaquery.logic.LogicWatcher;
import nbaquery.logic.NativeTablePipeline;
import nbaquery.logic.RateColumnInfo;
import nbaquery.logic.infrustructure.MatchNaturalJoinPerformance;
import nbaquery.logic.infrustructure.PlayerPerformance;

public class ProgressPlayerGroup implements LogicPipeline{
	
	public TableHost tableHost;
	protected LogicWatcher base;
	protected Table table;
	
	public ProgressPlayerGroup(TableHost tableHost,MatchNaturalJoinPerformance base){
		this.tableHost = tableHost;
		this.base = new LogicWatcher(base);
	
	}
	
	public Table getTable(){
		if(base.checkDepenency())
		{
			SortQuery sort=new SortQuery(base.getTable(), "match_id", true);
			tableHost.performQuery(sort, "progress_player_group");
			Table intermediateTable = tableHost.getTable("progress_player_group");
		
			GroupQuery groupQuery = new GroupQuery(intermediateTable, new String[]{"player_name","team_name_abbr"},
					new GroupColumnInfo("self_score_before", Integer.class)
					{
						Column self_score;

						@Override
						public void retrieve(Table originalTable,
								Table resultTable)
						{
							self_score = originalTable.getColumn("self_score");
							
						}

						@Override
						public void collapse(Row[] rows, Row resultRow)
						{
							
							Integer sum = 0;
							Row[] beforeRows=new Row[rows.length-5];
							if(rows.length>5){
								for(int i=0;i<rows.length-5;i++)
									beforeRows[i]=rows[i];
								for(Row row : beforeRows)
								{
									Integer value = (Integer) self_score.getAttribute(row);
									
									if(value != null) sum += value;
								}
								sum=sum/beforeRows.length;
							}
							else
								sum=1;
							getGroupColumn().setAttribute(resultRow, sum);	
						}
						
					},
					new GroupColumnInfo("self_score_now", Integer.class)
					{
						Column self_score;
						@Override
						public void retrieve(Table originalTable,
								Table resultTable)
						{
							self_score = originalTable.getColumn("self_score");
						}

						@Override
						public void collapse(Row[] rows, Row resultRow)
						{
							Integer sum = 0;
							Row[] afterRows=new Row[5];
							if(rows.length>5){
								for(int i=0;i<5;i++)
									afterRows[i]=rows[i];
								for(Row row : afterRows)
								{
									Integer value = (Integer) self_score.getAttribute(row);
									if(value != null) sum += value;
								}
								sum=sum/afterRows.length;
							}
							else
								sum=1;
							getGroupColumn().setAttribute(resultRow, sum);	
						}
					},
					new GroupColumnInfo("total_board_before", Integer.class)
					{
						Column total_board;

						@Override
						public void retrieve(Table originalTable,
								Table resultTable)
						{
							total_board = originalTable.getColumn("total_board");
						}

						@Override
						public void collapse(Row[] rows, Row resultRow)
						{
							Integer sum = 0;
							Row[] beforeRows=new Row[rows.length-5];
							if(rows.length>5){
								for(int i=0;i<rows.length-5;i++)
									beforeRows[i]=rows[i];
								for(Row row : beforeRows)
								{
									Integer value = (Integer) total_board.getAttribute(row);
									if(value != null) sum += value;
								}
								sum=sum/beforeRows.length;
							}
							else
								sum=1;
							getGroupColumn().setAttribute(resultRow, sum);	
						}
						
					},
					new GroupColumnInfo("total_board_now", Integer.class)
					{

						Column total_board;
						@Override
						public void retrieve(Table originalTable,
								Table resultTable)
						{
							total_board = originalTable.getColumn("total_board");
						}

						@Override
						public void collapse(Row[] rows, Row resultRow)
						{
							Integer sum = 0;
							Row[] afterRows=new Row[5];
							if(rows.length>5){
								for(int i=0;i<5;i++)
									afterRows[i]=rows[i];
								for(Row row : afterRows)
								{
									Integer value = (Integer) total_board.getAttribute(row);
									if(value != null) sum += value;
								}
								sum=sum/afterRows.length;
							}
							else
								sum=1;
							getGroupColumn().setAttribute(resultRow, sum);	
						}
					},
					
					new GroupColumnInfo("assist_before", Integer.class)
					{

						Column assist;

						@Override
						public void retrieve(Table originalTable,
								Table resultTable)
						{
							assist = originalTable.getColumn("assist");
						}

						@Override
						public void collapse(Row[] rows, Row resultRow)
						{
							Integer sum = 0;
							Row[] beforeRows=new Row[rows.length-5];
							if(rows.length>5){
								for(int i=0;i<rows.length-5;i++)
									beforeRows[i]=rows[i];
								for(Row row : beforeRows)
								{
									Integer value = (Integer) assist.getAttribute(row);
									if(value != null) sum += value;
								}
								sum=sum/beforeRows.length;
							}
							else
								sum=1;
							getGroupColumn().setAttribute(resultRow, sum);	
						}
					},
					new GroupColumnInfo("assist_now", Integer.class)
					{

						Column assist;
						@Override
						public void retrieve(Table originalTable,
								Table resultTable)
						{
							assist = originalTable.getColumn("assist");
						}

						@Override
						public void collapse(Row[] rows, Row resultRow)
						{
							Integer sum = 0;
							Row[] afterRows=new Row[5];
							if(rows.length>5){
								for(int i=0;i<5;i++)
									afterRows[i]=rows[i];
								for(Row row : afterRows)
								{
									Integer value = (Integer) assist.getAttribute(row);
									if(value != null) sum += value;
								}
								sum=sum/afterRows.length;
							}
							else
								sum=1;
							getGroupColumn().setAttribute(resultRow, sum);	
						}
					}
					);
			
			tableHost.performQuery(groupQuery, "progress_player_group");
			table = tableHost.getTable("progress_player_group");
		}
		return table;
	}
	
	
}
