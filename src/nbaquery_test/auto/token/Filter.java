package nbaquery_test.auto.token;

import nbaquery_test.auto.TestFacade;
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
	public void change(TestFacade facade, String[] parameters, int beginIndex)
	{
		facade.queryMode &= Integer.MAX_VALUE ^ TestFacade.FILTER_TYPE_BIT;
		facade.queryMode |= TestFacade.FILTER;
		
		String[] filter = parameters[beginIndex+1].split(",");
		for(int i = 0;i<filter.length;i++){
			facade.filterField[i]=filter[i];
		}
	}
}
