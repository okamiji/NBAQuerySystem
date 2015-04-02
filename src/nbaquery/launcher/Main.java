package nbaquery.launcher;

import java.io.File;

import nbaquery.data.TableHost;
import nbaquery.data.file.FileTableHost;
import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.TeamService;
import nbaquery.presentation.MainFrame;

public class Main
{
	TableHost host;
	public void loadDataLayer(String root) throws Exception
	{
		host = new FileTableHost(new File(root));
	}
	
	TeamService teamService;
	PlayerService playerService;
	public void loadLogicLayer()
	{
		ILogicAssembler assembler = new LogicAssembler();
		assembler.assemble(host);
		this.teamService = assembler.getTeamService();
		this.playerService = assembler.getPlayerService();
	}
	
	MainFrame mainFrame;
	public void loadPresentation()
	{
		mainFrame = new MainFrame(this.playerService, this.teamService);
		mainFrame.setVisible(true);
	}
	
	public static void main(String[] arguments) throws Exception
	{
		Main main = new Main();
		main.loadDataLayer("D:\\迭代一数据");
		main.loadLogicLayer();
		main.loadPresentation();
	}
}
