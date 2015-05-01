package nbaquery_test.presentation;

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
		theFrame.setSize(400, 240);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setVisible(true);
		
		theFrame.add(new HotPlayerSubPanel((NewPlayerService) this.playerService, 400, 230));
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
		new TestHotPlayerPanel().launch();
	}
}
