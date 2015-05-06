package nbaquery.launcher;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import nbaquery.data.TableHost;
import nbaquery.data.file.FileTableHost;
import nbaquery.data.file.loader.MatchNaturalJoinPerformanceLoader;
import nbaquery.data.file.loader.PlayerLoader;
import nbaquery.data.file.loader.TeamLoader;
import nbaquery.logic.match.MatchService;
import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.TeamService;
import nbaquery.presentation2.addon.GoodLookingScrollBar;
import nbaquery.presentation2.main.MainFrame;
//import nbaquery.presentation.MainFrame;

public class Main
{
	public TableHost host;
	public void loadDataLayer(String root) throws Exception
	{
		//host = new FileTableHost(new File(root));
		host = new FileTableHost(new File(root), new Class<?>[]{TeamLoader.class, 
			PlayerLoader.class, MatchNaturalJoinPerformanceLoader.class});
	}
	
	public TeamService teamService;
	public PlayerService playerService;
	public MatchService matchService;
	public void loadLogicLayer()
	{
		ILogicAssembler assembler = new LogicAssembler();
		assembler.assemble(host);
		this.teamService = assembler.getTeamService();
		this.playerService = assembler.getPlayerService();
		this.matchService = assembler.getMatchService();
	}
	
	MainFrame mainFrame;
	public void loadPresentation()
	{
		mainFrame = new MainFrame(this.playerService, this.teamService, this.matchService);
		mainFrame.run();
		
		try
		{
			GoodLookingScrollBar.scrollSlide = ImageIO.read(new File("Img2\\scrollslide.png"));
			GoodLookingScrollBar.upArrow = ImageIO.read(new File("Img2\\upArrow.png"));
			GoodLookingScrollBar.downArrow = ImageIO.read(new File("Img2\\downArrow.png"));
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//mainFrame.setVisible(true);
	}
	
	public void launch() throws Exception
	{
		//this.loadDataLayer("D:\\����һ����");
		this.loadDataLayer("D:\\dynamics");
		while(true) try
		{
			this.loadLogicLayer();
			break;
		}
		catch(Exception e)
		{
			System.out.println("Error detected while loading, retrying.");
		}
		this.loadPresentation();
	}
	
	public static void main(String[] arguments) throws Exception
	{
		Main main = new Main();
		main.launch();
	}
	
}
