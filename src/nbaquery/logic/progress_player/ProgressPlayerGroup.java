package nbaquery.logic.progress_player;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.GroupColumnInfo;
import nbaquery.data.query.GroupQuery;
import nbaquery.data.query.SortQuery;
import nbaquery.logic.LogicPipeline;
import nbaquery.logic.LogicWatcher;
import nbaquery.logic.infrustructure.MatchNaturalJoinPerformance;

public class ProgressPlayerGroup implements LogicPipeline{
	
	public TableHost tableHost;
	protected LogicWatcher base;
	protected Table table;
	
	public ProgressPlayerGroup(TableHost tableHost,MatchNaturalJoinPerformance base){
		this.tableHost = tableHost;
		this.base = new LogicWatcher(base);
	}
	
	public Table getTable(){
		if(base.checkDepenency() || tableHost.getTable("match").hasTableChanged(this))
		{
			SortQuery sort=new SortQuery(base.getTable(), "match_id", true);
			tableHost.performQuery(sort, "progress_player_group");
			Table intermediateTable = tableHost.getTable("progress_player_group");
		
			GroupQuery groupQuery = new GroupQuery(intermediateTable,  new String[]{"match_season", "player_name", "team_name_abbr"},
				
					new GroupColumnInfo("self_score_before", Float.class)
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
							Float sum = -1.f;
							Integer items = 0;
							if(rows.length>5)
							{
								sum = 0.f;
								for(int i=0;i<rows.length-5;i++)
								{
									Integer value = (Integer) self_score.getAttribute(rows[i]);
									if(value != null)
									{
										sum += value;
										items += 1;
									}
								}
								if(items > 0) sum = sum / items;
								else sum = -1.f;
							}
							getGroupColumn().setAttribute(resultRow, sum);	
						}
						
					},
					new GroupColumnInfo("self_score_now", Float.class)
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
							Float sum = -1.f;
							Integer items = 0;
							if(rows.length>5){
								sum = 0.f;
								for(int i=rows.length-5;i<rows.length;i++)
								{
									Integer value = (Integer) self_score.getAttribute(rows[i]);
									if(value != null)
									{
										sum += value;
										items += 1;
									}
								}
								if(items > 0) sum = sum / items;
								else sum = -1.f;
							}
							getGroupColumn().setAttribute(resultRow, sum);	
						}
					},
					new GroupColumnInfo("total_board_before", Float.class)
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
							Float sum = -1.f;
							Integer items = 0;
							if(rows.length>5){
								sum = 0.f;
								for(int i=0;i<rows.length-5;i++)
								{
									Integer value = (Integer) total_board.getAttribute(rows[i]);
									if(value != null)
									{
										sum += value;
										items += 1;
									}
								}
								if(items > 0) sum = sum / items;
								else sum = -1.f;
							}
							getGroupColumn().setAttribute(resultRow, sum);	
						}
						
					},
					new GroupColumnInfo("total_board_now", Float.class)
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
							Float sum = 0.f;
							Integer items = 0;
							if(rows.length>5){
								for(int i=rows.length-5;i<rows.length;i++)
								{
									Integer value = (Integer) total_board.getAttribute(rows[i]);
									if(value != null)
									{
										sum += value;
										items += 1;
									}
								}
								if(items > 0) sum = sum / items;
							}
							getGroupColumn().setAttribute(resultRow, sum);	
						}
					},
					
					new GroupColumnInfo("assist_before", Float.class)
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
							Float sum = 0.f;
							Integer items = 0;
							if(rows.length>5){
								for(int i=0;i<rows.length-5;i++)
								{
									Integer value = (Integer) assist.getAttribute(rows[i]);
									if(value != null)
									{
										sum += value;
										items += 1;
									}
								}
								if(items > 0) sum = sum / items;
							}
							getGroupColumn().setAttribute(resultRow, sum);	
						}
					},
					new GroupColumnInfo("assist_now", Float.class)
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
							Float sum = 0.f;
							Integer items = 0;
							if(rows.length>5){
								for(int i=rows.length-5;i<rows.length;i++)
								{
									Integer value = (Integer) assist.getAttribute(rows[i]);
									if(value != null)
									{
										sum += value;
										items += 1;
									}
								}
								if(items > 0) sum = sum / items;
							}
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
