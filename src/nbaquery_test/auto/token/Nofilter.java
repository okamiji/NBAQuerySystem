package nbaquery_test.auto.token;

import nbaquery_test.auto.TestFacade;
import nbaquery_test.auto.TestFacadeToken;

public class Nofilter implements TestFacadeToken
{
	@Override
	public String[] getTokens()
	{
		return new String[]{"-nofilter"};
	}

	@Override
	public int params()
	{
		return 0;
	}

	@Override
	public void change(TestFacade facade, String[] parameters, int beginIndex)
	{
		facade.queryMode &= Integer.MAX_VALUE ^ TestFacade.FILTER_TYPE_BIT;
		facade.queryMode |= TestFacade.NOFILTER;
	}
}
