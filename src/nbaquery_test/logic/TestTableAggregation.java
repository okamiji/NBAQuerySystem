package nbaquery_test.logic;

import java.io.File;
import java.util.Collection;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.file.FileTableHost;
import nbaquery.logic.TeamDeriveQuery;
import nbaquery.logic.average_player.AveragePlayer;
import nbaquery.logic.average_player.DerivedPlayerPerformance;
import nbaquery.logic.average_team.AverageTeam;
import nbaquery.logic.average_team.DerivedTeamPerformance;
import nbaquery.logic.gross_player.GrossPlayer;
import nbaquery.logic.gross_player.GrossPlayerPerformance;
import nbaquery.logic.gross_team.GrossRivalPerformance;
import nbaquery.logic.gross_team.GrossTeamNaturalJoin;
import nbaquery.logic.gross_team.GrossTeamPerformance;
import nbaquery.logic.infrustructure.MatchNaturalJoinPerformance;
import nbaquery.logic.infrustructure.MatchTeamPerformance;
import nbaquery.logic.infrustructure.PlayerPerformance;
import nbaquery.logic.infrustructure.RivalTeamNaturalJoin;
import nbaquery.logic.infrustructure.RivalTeamPerformance;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestTableAggregation
{
	static TableHost theHost;
	//static MatchTeamBasic logic;
	static MatchNaturalJoinPerformance joined;
	static MatchTeamPerformance aggregated;
	static RivalTeamPerformance rival;
	static GrossTeamPerformance grossPerformance;
	static GrossRivalPerformance grossRivalPerformance;
	static GrossTeamNaturalJoin grossNaturalJoin;
	static RivalTeamNaturalJoin rivalTeamNaturalJoin;
	static DerivedTeamPerformance derivedPerformance;
	static AverageTeam averageTeam;
	static PlayerPerformance playerPerformance;
	static GrossPlayerPerformance grossPlayerPerformance;
	static GrossPlayer grossPlayer;
	static DerivedPlayerPerformance dperformance;
	static AveragePlayer avgPlayer;

	@BeforeClass
	public static void setup() throws Exception
	{
		System.out.println("Initializing...");
		theHost = new FileTableHost(new File("D:\\����һ����"));
		joined = new MatchNaturalJoinPerformance(theHost);
		aggregated = new MatchTeamPerformance(theHost, joined);
		rival = new RivalTeamPerformance(theHost, aggregated);
		grossPerformance = new GrossTeamPerformance(theHost, aggregated);
		grossRivalPerformance = new GrossRivalPerformance(theHost, rival);
		grossNaturalJoin = new GrossTeamNaturalJoin(theHost, grossRivalPerformance, grossPerformance);
		rivalTeamNaturalJoin = new RivalTeamNaturalJoin(theHost, rival, aggregated);
		derivedPerformance = new DerivedTeamPerformance(theHost, rivalTeamNaturalJoin);
		averageTeam = new AverageTeam(theHost, derivedPerformance);
		playerPerformance = new PlayerPerformance(theHost, joined, aggregated, rival);
		grossPlayerPerformance = new GrossPlayerPerformance(theHost, playerPerformance);
		grossPlayer = new GrossPlayer(theHost, grossPlayerPerformance);		
		dperformance = new DerivedPlayerPerformance(theHost, playerPerformance);
		avgPlayer = new AveragePlayer(theHost, dperformance);
		
		System.out.println("Initialized!!!");
	}
	
	static Table table;
	
	@Test
	public void test0() throws Exception
	{
		table = aggregated.getTable();
	}
	
	@Test
	public void test1() throws Exception
	{
		table = rival.getTable();
	}
	
	@Test
	public void test2() throws Exception
	{
		table = grossPerformance.getTable();
	}
	
	@Test
	public void test3() throws Exception
	{
		table = grossRivalPerformance.getTable();
	}
	
	@Test
	public void test4() throws Exception
	{
		table = grossNaturalJoin.getTable();
	}
	
	@Test
	public void test5() throws Exception
	{
		TeamDeriveQuery derive = new TeamDeriveQuery(grossNaturalJoin.getTable());
		theHost.performQuery(derive, "derived");
		table = theHost.getTable("derived");
	}
	
	@Test
	public void test6() throws Exception
	{
		table = rivalTeamNaturalJoin.getTable();
	}
	
	@Test
	public void test7() throws Exception
	{
		table = derivedPerformance.getTable();
	}
	
	@Test
	public void test8() throws Exception
	{
		table = derivedPerformance.getTable();
	}
	
	@Test
	public void test9() throws Exception
	{
		table = averageTeam.getTable();
	}
	
	@Test
	public void testa() throws Exception
	{
		table = playerPerformance.getTable();
	}
	
	@Test
	public void testb() throws Exception
	{
		table = grossPlayerPerformance.getTable();
	}
	
	@Test
	public void testc() throws Exception
	{
		table = grossPlayer.getTable();
	}
	
	@Test
	public void testd() throws Exception
	{
		table = dperformance.getTable();
	}
	
	@Test
	public void teste() throws Exception
	{
		table = avgPlayer.getTable();
	}
	
	@After
	public void after()
	{
		out(table);
	}
	
	public static void out(Table table)
	{
		Collection<? extends Column> columns = table.getColumns();
		for(Column column : columns) System.out.print(column.getColumnName() + " ");
		System.out.println();
		
		int length = 0;
		for(Row row : table)
		{
			for(Column column : columns)
				System.out.print(column.getAttribute(row) + " ");
			System.out.println();
			length ++;
		}
		System.out.println("Generated " + length + " row(s) in total.");
	}
}