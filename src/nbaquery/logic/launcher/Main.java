package nbaquery.logic.launcher;

import java.io.File;

import nbaquery.data.TableHost;
import nbaquery.data.file.FileTableHost;
import nbaquery.logic.team.TeamService;

public class Main
{
	TableHost host;
	public void loadDataLayer(String root) throws Exception
	{
		host = new FileTableHost(new File(root));
	}
	
	TeamService teamService;
	public void loadLogicLayer()
	{
		ILogicAssembler assembler = new LogicAssembler();
		assembler.assemble(host);
		this.teamService = assembler.getTeamService();
	}
	
	public static void main(String[] arguments) throws Exception
	{
		Main main = new Main();
		main.loadDataLayer("D:\\迭代一数据");
		main.loadLogicLayer();
		
		String[][] string = main.teamService.searchForTeams(true, 4, false);
		for(int i = 0; i < string.length; i ++)
		{
			for(int j = 0; j < string[i].length; j ++)
				System.out.print(string[i][j] + " ");
			System.out.println();
		}
	}
}
