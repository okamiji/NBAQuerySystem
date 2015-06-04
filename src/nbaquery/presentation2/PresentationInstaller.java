package nbaquery.presentation2;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.w3c.dom.Node;

import nbaquery.launcher.Installer;
import nbaquery.launcher.Main;
import nbaquery.logic.match.MatchService;
import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.TeamService;
import nbaquery.presentation2.addon.GoodLookingScrollBar;

public class PresentationInstaller implements Installer<Object>
{

	@Override
	public Object install(Node documentNode, Object... params) throws Exception 
	{
		Main main = (Main) params[0];
		
		MainFrame mainFrame = new MainFrame((PlayerService)main.playerService, (TeamService)main.teamService, (MatchService)main.matchService);
		mainFrame.run();
		
		try
		{
			GoodLookingScrollBar.scrollSlide = ImageIO.read(new File("Img2/scrollslide.png".replace('/', File.separatorChar)));
			GoodLookingScrollBar.upArrow = ImageIO.read(new File("Img2/upArrow.png".replace('/', File.separatorChar)));
			GoodLookingScrollBar.downArrow = ImageIO.read(new File("Img2/downArrow.png".replace('/', File.separatorChar)));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}

}
