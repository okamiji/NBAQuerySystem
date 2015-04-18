package nbaquery_test.auto;

import nbaquery.data.file.loader.FileMonitor;

public class TestTerminator implements TestFacadeToken
{

	@Override
	public String[] getTokens()
	{
		return new String[]{"--shutdown"};
	}

	@Override
	public int params()
	{
		return 0;
	}

	@Override
	public void change(TestFacade facade, String[] parameters, int beginIndex)
	{
		FileMonitor.isSystemRunning = false;
	}
	
}
