package nbaquery_test.auto;

import java.io.PrintStream;
import java.util.TreeMap;

import nbaquery.data.TableHost;
import nbaquery_test.auto.token.*;

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
				new Player(), new Team(), new Average(), new Total()});
	}
	
	public static final int HAS_QUERY_BIT = 1;		//MASK 001
	public static final int NO_QUERY = 0;			//000
	
	public static final int MODEL_TYPE_BIT = 3;		//MASK 011
	public static final int PLAYER = 1; 			//001
	public static final int TEAM = 3; 				//011
	
	public static final int DATA_TYPE_BIT = 4;		//MASK 100
	public static final int AVERAGE = 0;			//000
	public static final int TOTAL = 4;				//100
	
	public int queryMode = 0;
	
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
		}
		
		if((queryMode & HAS_QUERY_BIT) != NO_QUERY)
		{
			int model = queryMode & MODEL_TYPE_BIT;
			int data = queryMode & DATA_TYPE_BIT;
			
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
		facade.execute(System.out, new String[]{"-player"});
		facade.execute(System.out, new String[]{"-player", "-total"});
		facade.execute(System.out, new String[]{"-player", "-average"});
		facade.execute(System.out, new String[]{"-team"});
		facade.execute(System.out, new String[]{"-team", "-total"});
		facade.execute(System.out, new String[]{"--shutdown"});
	}
}
