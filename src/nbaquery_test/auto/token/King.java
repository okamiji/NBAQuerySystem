package nbaquery_test.auto.token;

import nbaquery_test.auto.TestFacade;
import nbaquery_test.auto.TestFacadeToken;

public class King implements TestFacadeToken
{
	@Override
	public String[] getTokens()
	{
		return new String[]{"-king"};
	}

	@Override
	public int params()
	{
		return 2;
	}

	@Override
	public void change(TestFacade facade, String[] parameters, int beginIndex)
	{
		facade.queryMode &= Integer.MAX_VALUE ^ TestFacade.HOT_TYPE_BIT;
		facade.queryMode |= TestFacade.HOT;
		facade.hotMode = 1;
		facade.fields[0]=parameters[beginIndex+1];
		facade.fields[1]=parameters[beginIndex+2];
	}
}
