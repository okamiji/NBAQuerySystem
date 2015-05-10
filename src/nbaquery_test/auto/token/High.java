package nbaquery_test.auto.token;

import nbaquery_test.auto.Console;
import nbaquery_test.auto.TestFacadeToken;

public class High implements TestFacadeToken
{
	@Override
	public String[] getTokens()
	{
		return new String[]{"-high"};
	}

	@Override
	public int params()
	{
		return 0;
	}

	@Override
	public void change(Console facade, String[] parameters, int beginIndex)
	{
		facade.queryMode &= Integer.MAX_VALUE ^ Console.HIGH_TYPE_BIT;
		facade.queryMode |= Console.HIGH;
	}
}
