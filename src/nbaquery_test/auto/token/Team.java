package nbaquery_test.auto.token;

import nbaquery_test.auto.TestFacade;
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
	public void change(TestFacade facade, String[] parameters, int beginIndex)
	{
		facade.queryMode &= Integer.MAX_VALUE ^ TestFacade.MODEL_TYPE_BIT;
		facade.queryMode |= TestFacade.TEAM;
	}
}
