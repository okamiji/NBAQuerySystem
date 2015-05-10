package nbaquery_test.auto.token;

import nbaquery_test.auto.Console;
import nbaquery_test.auto.TestFacadeToken;

public class Hot implements TestFacadeToken
{
	@Override
	public String[] getTokens()
	{
		return new String[]{"-hot"};
	}

	@Override
	public int params()
	{
		return 1;
	}

	@Override
	public void change(Console facade, String[] parameters, int beginIndex)
	{
		facade.queryMode &= Integer.MAX_VALUE ^ Console.HOT_TYPE_BIT;
		facade.queryMode |= Console.HOT;
		facade.hotMode = 0;
		facade.fields[0]=parameters[beginIndex+1];
	}
}
