package nbaquery_test.auto.token;

import nbaquery_test.auto.Console;
import nbaquery_test.auto.TestFacadeToken;

public class Sort implements TestFacadeToken
{
	@Override
	public String[] getTokens()
	{
		return new String[]{"-sort"};
	}

	@Override
	public int params()
	{
		return 1;
	}

	@Override
	public void change(Console facade, String[] parameters, int beginIndex)
	{
		facade.queryMode &= Integer.MAX_VALUE ^ Console.SORT_TYPE_BIT;
		facade.queryMode |= Console.SORT;
		
		facade.sortField = parameters[beginIndex+1];
	}
}
