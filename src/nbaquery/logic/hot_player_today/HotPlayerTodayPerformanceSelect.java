package nbaquery.logic.hot_player_today;

import java.text.SimpleDateFormat;
import java.util.Date;

import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.logic.LogicPipeline;
import nbaquery.logic.LogicWatcher;
import nbaquery.logic.infrustructure.DirectMatchNaturalJoinPerformance;


public class HotPlayerTodayPerformanceSelect implements LogicPipeline {

	public TableHost tableHost;
	protected LogicWatcher base;
	protected Table table;
	protected String date;
	
	public HotPlayerTodayPerformanceSelect(TableHost tableHost, DirectMatchNaturalJoinPerformance base){
		this.tableHost = tableHost;
		this.base = new LogicWatcher(base);
	}
	
	public String getDate(){
		Date uDate=new Date();
		SimpleDateFormat df=new SimpleDateFormat("<MM>-<dd>");
		String result=df.format(uDate);
		return result;
	}
	
	@Override
	public Table getTable() {
		this.date=getDate();
		if(base.checkDepenency())
		{
			try {
				SelectProjectQuery query = new SelectProjectQuery("base.match_date=date",base.getTable());
				tableHost.performQuery(query, "todayPerformance");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		return table;
	}
	
}
