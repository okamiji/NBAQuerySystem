package nbaquery_test.auto.token;

import nbaquery_test.auto.TestFacade;
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
	public void change(TestFacade facade, String[] parameters, int beginIndex)
	{
		facade.queryMode &= Integer.MAX_VALUE ^ TestFacade.HOT_TYPE_BIT;
		facade.queryMode |= TestFacade.HOT;
		facade.hotMode = 0;
		facade.fields[0]=parameters[beginIndex+1];
	}
}
