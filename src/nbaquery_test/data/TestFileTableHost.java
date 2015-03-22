package nbaquery_test.data;

import java.io.File;
import java.util.ArrayList;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.file.FileTableHost;
import nbaquery.data.query.DeriveColumnInfo;
import nbaquery.data.query.DeriveQuery;
import nbaquery.data.query.ExpressionDeriveColumnInfo;
import nbaquery.data.query.ExpressionDeriveQuery;
import nbaquery.data.query.GroupQuery;
import nbaquery.data.query.JoinQuery;
import nbaquery.data.query.Query;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.query.SortQuery;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestFileTableHost
{
	static Table match;
	static Table quarter_score;
	static Table performance;
	static Table player;
	static Table team;
	static FileTableHost host;
	
	static ArrayList<String> output = new ArrayList<String>();
	
	@BeforeClass
	public static void loadingHostFromFileSystem() throws Exception
	{
		host = new FileTableHost(new File("E:\\大二课程\\软工三\\tss迭代一要求\\CSEIII data\\迭代一数据"));
		
		match = host.getTable("match");
		quarter_score = host.getTable("quarter_score");
		performance = host.getTable("performance");
		player = host.getTable("player");
		team = host.getTable("team");
	}
	
	public static String convertTableIntoString(Table table)
	{
		StringBuilder builder = new StringBuilder();
		Column[] columns = table.getColumns().toArray(new Column[0]);
		
		for(int i = 0; i < columns.length; i ++)
		{
			if(i > 0) builder.append(' ');
			builder.append(columns[i].getColumnName());
		}
		int rec = 0;
		for(Row row : table.getRows())
		{
			rec ++;
			builder.append('\n');
			for(int i = 0; i < columns.length; i ++)
			{
				if(i > 0) builder.append(' ');
				builder.append(columns[i].getAttribute(row));
			}
		}
		builder.append('\n');
		builder.append(rec);
		builder.append(" record(s) generated in total.");
		
		return new String(builder);
	}
	
	@Test
	public void filterTeams() throws Exception
	{
		SelectProjectQuery query = new SelectProjectQuery("team.team_foundation > 1980",
				team, "team_name", "team_foundation");
		
		host.performQuery(query, "resultTeam");
		output.add("resultTeam");
	}
	
	@Test
	public void derivePerformanceRecord() throws Exception
	{
		Query query = new ExpressionDeriveQuery(performance, new DeriveColumnInfo[]{
				new ExpressionDeriveColumnInfo("three_shoot_accuracy", Float.class,
						"1.0F * performance.three_shoot_score / performance.three_shoot_count")
				,
				new ExpressionDeriveColumnInfo("shoot_accuracy", Float.class,
						"1.0F * performance.shoot_score / performance.shoot_count")
				}, "three_shoot_count", "three_shoot_score", "shoot_count", "shoot_score", "player_name");
		
		host.performQuery(query, "resultPerformance");
		output.add("resultPerformance");
	}
	
	@Test
	public void derivePerformanceRecord2() throws Exception
	{
		Query query = new DeriveQuery(performance, new DeriveColumnInfo[]{
				new DeriveColumnInfo("three_shoot_accuracy", Float.class),
				new DeriveColumnInfo("shoot_accuracy", Float.class),},
				"three_shoot_count", "three_shoot_score", "shoot_count", "shoot_score", "player_name")
		{

			Column three_shoot_accuracy;
			Column three_shoot_count;
			Column three_shoot_score;
			
			Column shoot_accuracy;
			Column shoot_count;
			Column shoot_score;
			
			public void retrieve(Table table)
			{
				three_shoot_accuracy = table.getColumn("three_shoot_accuracy");
				three_shoot_count = table.getColumn("three_shoot_count");
				three_shoot_score = table.getColumn("three_shoot_score");
				
				shoot_accuracy = table.getColumn("shoot_accuracy");
				shoot_count = table.getColumn("shoot_count");
				shoot_score = table.getColumn("shoot_score");
			}
			
			@Override
			public void derive(Row row)
			{
				three_shoot_accuracy.setAttribute(row, 1.0F * (int)three_shoot_score.getAttribute(row) / (int)three_shoot_count.getAttribute(row));
				shoot_accuracy.setAttribute(row, 1.0F * (int)shoot_score.getAttribute(row) / (int)shoot_count.getAttribute(row));
			}
			
		};
		
		host.performQuery(query, "resultPerformance2");
		output.add("resultPerformance2");
	}
	
	@Test
	public void aggregateMatchRecord() throws Exception
	{
		JoinQuery query = new JoinQuery("match.match_id = quarter_score.match_id", match, quarter_score,
				"match_host_abbr", "match_guest_abbr", "quarter_number", "quarter_host_score", "quarter_guest_score");
		
		host.performQuery(query, "resultMatchScore");
		output.add("resultMatchScore");
	}
	
	@Test
	public void sortMatchRecord() throws Exception
	{
		SortQuery query = new SortQuery(performance, "three_shoot_score", 100, true);
		
		host.performQuery(query, "resultSort");
		output.add("resultSort");
	}
	
	@Test
	public void sumupMatchRecord() throws Exception
	{
		GroupQuery query = new GroupQuery()
		{
			Column three_shoot_score;
			Column three_shoot_count;
			Column three_shoot_sum;
			Column three_shoot_score_sum;
			
			Column shoot_score;
			Column shoot_count;
			Column shoot_sum;
			Column shoot_score_sum;
			
			@Override
			public void retrieve(Table resultTable)
			{
				three_shoot_score = this.table.getColumn("three_shoot_score");
				three_shoot_count = this.table.getColumn("three_shoot_count");
				three_shoot_sum = resultTable.getColumn("three_shoot_sum");
				three_shoot_score_sum = resultTable.getColumn("three_shoot_score_sum");
				
				shoot_score = this.table.getColumn("shoot_score");
				shoot_count = this.table.getColumn("shoot_count");
				shoot_sum = resultTable.getColumn("shoot_sum");
				shoot_score_sum = resultTable.getColumn("shoot_score_sum");
			}

			@Override
			public void collapse(Row[] rows, Row resultRow)
			{
				Integer three_shoot_score_sum = 0;
				Integer three_shoot_sum = 0;
				
				Integer shoot_score_sum = 0;
				Integer shoot_sum = 0;
				
				for(Row row : rows)
				{
					three_shoot_score_sum += (Integer) three_shoot_score.getAttribute(row);
					three_shoot_sum += (Integer) three_shoot_count.getAttribute(row);
					
					shoot_score_sum += (Integer) shoot_score.getAttribute(row);
					shoot_sum += (Integer) shoot_count.getAttribute(row);
				}
				this.three_shoot_score_sum.setAttribute(resultRow, three_shoot_score_sum);
				this.three_shoot_sum.setAttribute(resultRow, three_shoot_sum);
				
				this.shoot_score_sum.setAttribute(resultRow, shoot_score_sum);
				this.shoot_sum.setAttribute(resultRow, shoot_sum);
			}
		};
		
		query.table = performance;
		query.collapseColumn = new String[]{"team_name_abbr", "match_id"};
		query.derivedColumn = new String[]{"three_shoot_sum", "three_shoot_score_sum", "shoot_sum", "shoot_score_sum"};
		query.derivedClass = new Class<?>[]{Integer.class, Integer.class, Integer.class, Integer.class};
		
		host.performQuery(query, "collapseResult");
		host.performQuery(new SortQuery(host.getTable("collapseResult"), "match_id"), "sortedCollapseResult");
		
		//output.add("collapseResult");
		output.add("sortedCollapseResult");
	}
	
	@AfterClass
	public static void displayResults()
	{
		for(String tableName : output)
		{
			System.out.println("####" + tableName + "####");
			System.out.println(convertTableIntoString(host.getTable(tableName)));
		}
	}
}
