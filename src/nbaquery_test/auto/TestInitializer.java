package nbaquery_test.auto;

import java.io.File;

import nbaquery.data.file.FileTableHost;
import nbaquery.data.file.loader.MatchNaturalJoinPerformanceLoader;
import nbaquery.data.file.loader.PlayerLoader;
import nbaquery.data.file.loader.TeamLoader;
import nbaquery.launcher.Main;
import nbaquery.logic.LogicInstaller;

public class TestInitializer implements TestFacadeToken
{

	@Override
	public String[] getTokens()
	{
		return new String[]{"--datasource", "-datasource"};
	}

	@Override
	public int params()
	{
		return 1;
	}

	@Override
	public void change(TestFacade facade, String[] parameters, int beginIndex)
	{
		String dataSourcePath = parameters[beginIndex + 1];
		facade.tableHost = new FileTableHost(new File(dataSourcePath), 
				new Class<?>[]{TeamLoader.class, PlayerLoader.class, MatchNaturalJoinPerformanceLoader.class});
		Main main = new Main();
		main.host = facade.tableHost;
		boolean isLoading = true;
		while(isLoading) try
		{
			isLoading = false;
			isLoading |= facade.tableHost.getTable("player").hasTableChanged(this);
			isLoading |= facade.tableHost.getTable("team").hasTableChanged(this);
			isLoading |= facade.tableHost.getTable("match_natural_join_performance").hasTableChanged(this);
			if(isLoading) Thread.sleep(100);
		}
		catch(Exception e)
		{
			break;
		}
		
		LogicInstaller logicInstaller = new LogicInstaller();
		try {
			logicInstaller.install(null, main);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
