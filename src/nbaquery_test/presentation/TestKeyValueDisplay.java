package nbaquery_test.presentation;

import javax.swing.JFrame;

import nbaquery.data.Table;
import nbaquery.launcher.Main;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.presentation3.KeyValueDisplay;

public class TestKeyValueDisplay extends Main
{
	JFrame theFrame;
	public void loadPresentation()
	{
		theFrame = new JFrame();
		theFrame.setSize(300, 70);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setUndecorated(true);
		theFrame.setVisible(true);
		
		KeyValueDisplay component = new KeyValueDisplay("��������", "player_birth");
		component.setSize(theFrame.getSize());
		Table table = ((NewPlayerService)this.playerService).searchForTodayHotPlayers("self_score");
		component.setRow(table.getRows().next());
		
		theFrame.add(component);
		theFrame.setAlwaysOnTop(true);
		//component.shouldDisplayTime = true;
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
		new TestKeyValueDisplay().launch();
		}
		catch(Exception e)
		{
			
		}
	}
}
