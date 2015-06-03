package nbaquery_test.presentation;

import java.awt.Color;

import javax.swing.JFrame;

import nbaquery.launcher.Main;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.presentation3.player.HotPlayerSubPanel;

public class TestHotPlayerPanel extends Main
{
	JFrame theFrame;
	public void loadPresentation()
	{
		theFrame = new JFrame();
		theFrame.setSize(600, 240);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setUndecorated(true);
		theFrame.setVisible(true);
		theFrame.setBackground(new Color(0, 0, 0, 0));
		theFrame.add(new HotPlayerSubPanel((NewPlayerService) this.playerService, new DetailedInfoContainerStub(teamService) , 600, 230));
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
		new TestHotPlayerPanel().launch();
		}
		catch(Exception e)
		{
			
		}
	}
}
