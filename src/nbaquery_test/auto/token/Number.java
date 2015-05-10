package nbaquery_test.auto.token;

import nbaquery_test.auto.Console;
import nbaquery_test.auto.TestFacadeToken;

public class Number implements TestFacadeToken
{
	@Override
	public String[] getTokens()
	{
		return new String[]{"-n"};
	}

	@Override
	public int params()
	{
		return 1;
	}

	@Override
	public void change(Console facade, String[] parameters, int beginIndex)
	{
		facade.queryMode &= Integer.MAX_VALUE ^ Console.NUMBER_TYPE_BIT;
		facade.queryMode |= Console.NUMBER;
		facade.dataNumber = Integer.parseInt(parameters[beginIndex+1]);
	}
}
