package nbaquery_test.presentation;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;

import nbaquery.launcher.Main;
import nbaquery.logic.match.NewMatchService;
import nbaquery.presentation3.match.MatchComponent;
import nbaquery.presentation3.match.TodayMatchSubPanel;

public class TestTodayMatchesPanel extends Main
{
	JFrame theFrame;
	public void loadPresentation()
	{
		theFrame = new JFrame();
		theFrame.setSize(600, 100);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setUndecorated(true);
		theFrame.setVisible(true);
		theFrame.setBackground(new Color(0, 0, 0, 0));
		theFrame.add(new TodayMatchSubPanel(new DetailedInfoContainerStub(teamService), (NewMatchService)this.matchService, 600, 100));
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
			new TestTodayMatchesPanel().launch();
		}
		catch(Exception e)
		{
			
		}
	}
}
