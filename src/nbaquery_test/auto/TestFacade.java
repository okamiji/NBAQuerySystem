package nbaquery_test.auto;

import java.io.PrintStream;
import java.util.TreeMap;

import test.data.PlayerHighInfo;

import nbaquery.data.TableHost;
import nbaquery_test.auto.token.*;
import nbaquery_test.auto.token.Number;

/**
 * Facade design pattern is used in automized test.
 * @author luohaoran
 */
public class TestFacade
{
	final TreeMap<String, TestFacadeToken> tokens = new TreeMap<String, TestFacadeToken>();
	
	public TableHost tableHost = null;
	
	public TestFacade()
	{
		this(new TestFacadeToken[]{new TestInitializer(), new TestTerminator(),
				new Player(), new Team(), new Average(), new Total(),new All(),
				new Filter(), new High(), new Hot(), new King(), new Low(),
				new Nofilter(), new NoNumber(), new Number(),new Sort()});
	}
	
	public static final int HAS_QUERY_BIT = 1;		//MASK 00000001
	public static final int NO_QUERY = 0;			//00000000
	
	public static final int MODEL_TYPE_BIT = 3;		//MASK 00000011
	public static final int PLAYER = 1; 			//00000001
	public static final int TEAM = 3; 				//00000011
	
	public static final int DATA_TYPE_BIT = 4;		//MASK 00000100
	public static final int AVERAGE = 0;			//00000000
	public static final int TOTAL = 4;				//00000100
	
	public static final int HOT_TYPE_BIT = 8;		//MASK 00001000
	public static final int ALL = 0;				//00000000
	public static final int HOT = 8;				//00001000
	
	public static final int KING = 1;				
	public static final int NOKING = 0;
	
	public static final int NUMBER_TYPE_BIT = 16;		//MASK 00010000
	public static final int NONUMBER = 0;				//00000000
	public static final int NUMBER = 16;				//00010000
	
	public static final int HIGH_TYPE_BIT = 32;		//MASK 00100000
	public static final int LOW = 0;				//00000000
	public static final int HIGH = 32;				//00100000
	
	public static final int FILTER_TYPE_BIT = 64;		//MASK 01000000
	public static final int NOFILTER = 0;				//00000000
	public static final int FILTER = 64;				//01000000
	
	public static final int SORT_TYPE_BIT = 128;		//MASK 10000000
	public static final int NOSORT = 0;				//00000000
	public static final int SORT = 128;				//10000000
	
	public String[] fields = new String[2];
	public String[] filterField = new String[]{null,null,null,null};
	
	public int dataNumber = 0;
	public int queryMode = 0;
	public int hotMode   = 0;
	boolean isGross=false;
	
	
	public TestFacade(TestFacadeToken[] tokens)
	{
		for(TestFacadeToken token : tokens)
		{
			String[] names = token.getTokens();
			for(String name : names) this.tokens.put(name, token);
		}
	}
	
	public void execute(PrintStream stdout, String[] arguments)
	{
		queryMode = NO_QUERY;
		int pointer = 0;
		
		while(pointer < arguments.length)
		{
			TestFacadeToken token = tokens.get(arguments[pointer]);
			if(token == null) break;
			token.change(this, arguments, pointer);
			pointer += (token.params() + 1);
			System.out.println(pointer);
		}
		
		if((queryMode & HAS_QUERY_BIT) != NO_QUERY)
		{
			int model = queryMode & MODEL_TYPE_BIT;
			int data = queryMode & DATA_TYPE_BIT;
			int hot = queryMode & HOT_TYPE_BIT;
			int number = queryMode & NUMBER_TYPE_BIT;
			int high = queryMode & HIGH_TYPE_BIT;
			int filter = queryMode & FILTER_TYPE_BIT;
			int sort = queryMode & SORT_TYPE_BIT;
			
			if(model == PLAYER) stdout.println("player");
			else if(model == TEAM) stdout.println("team");
			
			if(data == AVERAGE) stdout.println("average");
			else if(data == TOTAL) stdout.println("total");
		}
	}
	
	public static void main(String[] arguments) throws Exception
	{
		TestFacade facade = new TestFacade();
		facade.execute(System.out, new String[]{"--datasource", "D:\\迭代一数据"});
		facade.execute(System.out, new String[]{"-player","-hot","assist","-n","5"});
		facade.execute(System.out, new String[]{"-player", "-total"});
		facade.execute(System.out, new String[]{"-player", "-avg"});
		facade.execute(System.out, new String[]{"-team"});
		facade.execute(System.out, new String[]{"-team", "-total"});
		facade.execute(System.out, new String[]{"--shutdown"});
	}
	
	public final TreeMap<String, String> playerLookups = new TreeMap<String, String>();
	{
		playerLookups.put("score","self_score");
		playerLookups.put("rebound","total_board");
		playerLookups.put("assist","assist");
		playerLookups.put("position","");
		playerLookups.put("west","w");
		playerLookups.put("east","e");
		playerLookups.put("league","");
		playerLookups.put("age","player_age");
		playerLookups.put("point","self_score");
		playerLookups.put("blockshot","cap");
		playerLookups.put("steal","steal");
		playerLookups.put("foul","foul");
		playerLookups.put("fault","miss");
		playerLookups.put("minute","game_time_minute");
		playerLookups.put("efficent","efficiency");
		playerLookups.put("shot","shoot_rate");
		playerLookups.put("three","three_shoot_rate");
		playerLookups.put("penalty","foul_shoot_rate");
		playerLookups.put("doubleTwo",null);
		playerLookups.put("realShot","true_shoot_rate");
		playerLookups.put("GmSc","gmsc_efficiency");
		playerLookups.put("shotEfficient","shoot_efficiency");
		playerLookups.put("reboundEfficient","total_board_efficiency");
		playerLookups.put("offendReboundEfficient","attack_board_efficiency");
		playerLookups.put("defendReboundEfficient","defence_board_efficiency");
		playerLookups.put("assistEfficient","assist_rate");
		playerLookups.put("stealEfficient","steal_rate");
		playerLookups.put("blockShotEfficient","cap_rate");
		playerLookups.put("faultEfficient","miss_rate");
		playerLookups.put("frequency","usage");
	}
}
