package nbaquery.logic.hot_player_today;

import java.text.SimpleDateFormat;
import java.util.Date;

import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.logic.LogicPipeline;
import nbaquery.logic.LogicWatcher;
import nbaquery.logic.infrustructure.DirectMatchNaturalJoinPerformance;
import nbaquery.logic.infrustructure.MatchNaturalJoinPerformance;


public class HotPlayerTodayPerformanceSelect implements LogicPipeline {

	public TableHost tableHost;
	protected LogicWatcher base;
	protected Table table;
	protected String date;
	
	public HotPlayerTodayPerformanceSelect(TableHost tableHost, MatchNaturalJoinPerformance base){
		this.tableHost = tableHost;
		this.base = new LogicWatcher(base);
	}
	
	public String getDate(){
		Date uDate=new Date();
		SimpleDateFormat df=new SimpleDateFormat("MM-dd");
		String result=df.format(uDate);
		return result;
	}
	
	@Override
	public Table getTable() {
		this.date = getDate();
		this.date = "02-03";
		if(base.checkDepenency())
		{
			try
			{
				SelectProjectQuery query = new SelectProjectQuery("match_natural_join_performance.match_date='%1'".replace("%1", date), base.getTable());
				tableHost.performQuery(query, "todayPerformance");
			}
			catch (Exception e)
			{
			
			}	
			table = tableHost.getTable("todayPerformance");
		}
		return table;
	}
	
}
