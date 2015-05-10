package nbaquery_test.auto.token;

import nbaquery_test.auto.Console;
import nbaquery_test.auto.TestFacadeToken;

public class NoNumber implements TestFacadeToken
{
	@Override
	public String[] getTokens()
	{
		return new String[]{"-nonumber"};
	}

	@Override
	public int params()
	{
		return 0;
	}

	@Override
	public void change(Console facade, String[] parameters, int beginIndex)
	{
		facade.queryMode &= Integer.MAX_VALUE ^ Console.NUMBER_TYPE_BIT;
		facade.queryMode |= Console.NONUMBER;
		facade.dataNumber = 50;
	}
}
