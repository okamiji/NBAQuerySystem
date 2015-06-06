package nbaquery_test.presentation;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;

import nbaquery.data.Cursor;
import nbaquery.data.Row;
import nbaquery.launcher.Main;
import nbaquery.logic.match.NewMatchService;
import nbaquery.logic.team.NewTeamService;
import nbaquery.presentation3.match.MatchComponent;
import nbaquery.presentation3.team.DetailedTeamPanel;
import nbaquery_test.data.TestFileTableHost;

public class TestDetailedTeamPanel extends Main
{
	JFrame theFrame;
	@SuppressWarnings("serial")
	public void loadPresentation()
	{
		theFrame = new JFrame();
		theFrame.setSize(400, 600);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setUndecorated(true);
		theFrame.setVisible(true);
		theFrame.setBackground(new Color(0, 0, 0, 0));
		theFrame.add(new DetailedTeamPanel(new DetailedInfoContainerStub(teamService) , (NewMatchService) this.matchService, 400, 600)
		{
			{
				Cursor rows = ((NewTeamService)teamService).searchTodayHotTeams("self_score").getRows();
				Row rowZero = rows.next();
				this.setRow(rowZero);
				System.out.println(TestFileTableHost.convertTableIntoString(rowZero.getDeclaredTable()));
			}
		});
		
		MatchComponent.scoreLabelFont = new Font(Font.MONOSPACED, Font.PLAIN, 0).deriveFont(20.0f);
		MatchComponent.plainTextFont = new Font(Font.MONOSPACED, Font.PLAIN, 0).deriveFont(12.0f);
		theFrame.setAlwaysOnTop(true);
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
		new TestDetailedTeamPanel().launch();
		}
		catch(Exception e)
		{
			
		}
	}
}
