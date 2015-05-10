package nbaquery_test.auto.token;

import nbaquery_test.auto.Console;
import nbaquery_test.auto.TestFacadeToken;

public class Team implements TestFacadeToken
{
	@Override
	public String[] getTokens()
	{
		return new String[]{"-team"};
	}

	@Override
	public int params()
	{
		return 0;
	}

	@Override
	public void change(Console facade, String[] parameters, int beginIndex)
	{
		facade.queryMode &= Integer.MAX_VALUE ^ Console.MODEL_TYPE_BIT;
		facade.queryMode |= Console.TEAM;
	}
}
