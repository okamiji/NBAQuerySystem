package nbaquery.launcher;

import java.awt.Font;

import nbaquery.logic.match.NewMatchService;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.logic.team.NewTeamService;
import nbaquery.presentation3.MainFrame;
import nbaquery.presentation3.match.MatchComponent;

public class MainP3 extends Main
{
	public void loadPresentation()
	{
		new MainFrame((NewPlayerService) this.playerService, (NewTeamService)  this.teamService,
				(NewMatchService)this.matchService).setVisible(true);
		
		MatchComponent.scoreLabelFont = new Font(Font.MONOSPACED, Font.PLAIN, 0).deriveFont(20.0f);
		MatchComponent.plainTextFont = new Font(Font.MONOSPACED, Font.PLAIN, 0).deriveFont(12.0f);
	}
	
	public static void main(String[] arguments)
	{
		try
		{
			MainP3 main = new MainP3();
			main.launch();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
