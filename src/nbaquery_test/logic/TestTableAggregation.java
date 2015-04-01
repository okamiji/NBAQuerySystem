package nbaquery_test.logic;

import java.io.File;
import java.util.Collection;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.file.FileTableHost;
import nbaquery.data.query.SortQuery;
import nbaquery.logic.MatchNaturalJoinPerformance;
import nbaquery.logic.MatchTeamPerformance;
import nbaquery.logic.RivalTeamPerformance;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestTableAggregation
{
	static TableHost theHost;
	//static MatchTeamBasic logic;
	static MatchNaturalJoinPerformance joined;
	static MatchTeamPerformance aggregated;
	static RivalTeamPerformance rival;

	@BeforeClass
	public static void setup() throws Exception
	{
		System.out.println("Initializing...");
		theHost = new FileTableHost(new File("D:\\迭代一数据"));
		joined = new MatchNaturalJoinPerformance(theHost);
		aggregated = new MatchTeamPerformance(theHost, joined);
		rival = new RivalTeamPerformance(theHost, aggregated);
	}
	
	static Table table;
	@Test
	public void test() throws Exception
	{
		table = rival.getTable();
	}
	
	@AfterClass
	public static void after()
	{
		out(table);
	}
	
	public static void out(Table table)
	{
		Collection<? extends Column> columns = table.getColumns();
		for(Column column : columns) System.out.print(column.getColumnName() + " ");
		System.out.println();
		
		Row[] rows = table.getRows();
		
		for(int i = 0; i < rows.length; i ++)
		{
			for(Column column : columns)
				System.out.print(column.getAttribute(rows[i]) + " ");
			System.out.println();
		}
		System.out.println("Generated " + rows.length + " row(s) in total.");
	}
}