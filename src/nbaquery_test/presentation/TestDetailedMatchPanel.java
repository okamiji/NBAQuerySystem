package nbaquery_test.presentation;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;

import nbaquery.launcher.Main;
import nbaquery.logic.match.NewMatchService;
import nbaquery.presentation3.match.DetailedMatchPanel;
import nbaquery.presentation3.match.MatchComponent;

public class TestDetailedMatchPanel extends Main
{
	JFrame theFrame;
	@SuppressWarnings("serial")
	public void loadPresentation()
	{
		theFrame = new JFrame();
		theFrame.setSize(500, 700);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setUndecorated(true);
		theFrame.setVisible(true);
		theFrame.setBackground(new Color(0, 0, 0, 0));
		theFrame.add(new DetailedMatchPanel(new DetailedInfoContainerStub(teamService), (NewMatchService)this.matchService, 500, 700)
		{
			{
				this.setRow(this.matchService.searchForMatchesTable(null, null, null, true).getRows().next());
			}
		});
		theFrame.setAlwaysOnTop(true);
		MatchComponent.scoreLabelFont = new Font(Font.MONOSPACED, Font.PLAIN, 0).deriveFont(20.0f);
		MatchComponent.plainTextFont = new Font(Font.MONOSPACED, Font.PLAIN, 0).deriveFont(12.0f);
		refresh.start();
	}
	
	Thread refresh = new Thread()
	{
		public void run()
		{
			while(true) try
			{
				if(theFrame.isVisible()) 
					theFrame.repaint();
				Thread.sleep(10);
			}
			catch(Exception e)
			{
				
			}
		}
	};
	
	public static void main(String[] arguments) throws Exception
	{
		try
		{
			new TestDetailedMatchPanel().launch();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
