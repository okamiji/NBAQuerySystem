package nbaquery_test.auto;

import java.io.File;

import nbaquery.data.file.FileTableHost;
import nbaquery.data.file.loader.MatchNaturalJoinPerformanceLoader;
import nbaquery.data.file.loader.PlayerLoader;
import nbaquery.data.file.loader.TeamLoader;
import nbaquery.launcher.ILogicAssembler;
import nbaquery.launcher.LogicAssembler;

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
	public void change(Console facade, String[] parameters, int beginIndex)
	{
		String dataSourcePath = parameters[beginIndex + 1];
		facade.tableHost = new FileTableHost(new File(dataSourcePath), 
				new Class<?>[]{TeamLoader.class, PlayerLoader.class, MatchNaturalJoinPerformanceLoader.class});
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
		
		ILogicAssembler assembler = new LogicAssembler();
		assembler.assemble(facade.tableHost);
		facade.playerService = assembler.getNPS();
		facade.teamService = assembler.getNTS();
	}
}
