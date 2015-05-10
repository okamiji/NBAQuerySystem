package nbaquery_test.auto.token;

import nbaquery_test.auto.Console;
import nbaquery_test.auto.TestFacadeToken;

public class Filter implements TestFacadeToken
{
	@Override
	public String[] getTokens()
	{
		return new String[]{"-filter"};
	}

	@Override
	public int params()
	{
		return 1;
	}

	@Override
	public void change(Console facade, String[] parameters, int beginIndex)
	{
		facade.queryMode &= Integer.MAX_VALUE ^ Console.FILTER_TYPE_BIT;
		facade.queryMode |= Console.FILTER;
		
		facade.filterField = parameters[beginIndex+1];
	}
}
