package nbaquery_test.presentation;

import java.awt.Color;

import javax.swing.JFrame;

import nbaquery.launcher.Main;
import nbaquery.logic.team.NewTeamService;
import nbaquery.presentation3.team.CompareTeamSubPanel;

public class TestCompareTeamPanel extends Main
{
	JFrame theFrame;
	public void loadPresentation()
	{
		theFrame = new JFrame();
		theFrame.setSize(720, 600);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setUndecorated(true);
		theFrame.setVisible(true);
		theFrame.setBackground(new Color(0, 0, 0, 0));
		theFrame.add(new CompareTeamSubPanel((NewTeamService) this.teamService, new DetailedInfoContainerStub(teamService) , 720, 600, 25));
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
		new TestCompareTeamPanel().launch();
		}
		catch(Exception e)
		{
			
		}
	}
}