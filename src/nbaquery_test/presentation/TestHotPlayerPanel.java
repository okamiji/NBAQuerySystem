package nbaquery_test.presentation;

import javax.swing.JFrame;

import nbaquery.launcher.Main;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.player.HotPlayerSubPanel;

public class TestHotPlayerPanel extends Main
{
	JFrame theFrame;
	public void loadPresentation()
	{
		theFrame = new JFrame();
		theFrame.setSize(400, 240);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setUndecorated(true);
		theFrame.setVisible(true);
		theFrame.add(new HotPlayerSubPanel((NewPlayerService) this.playerService, new DetailedInfoContainer()
		{

			@Override
			public void displayPlayerInfo(String playerName)
			{
				System.out.println(playerName);
			}

			@Override
			public void displayTeamInfo(String teamNameOrAbbr, boolean isAbbr)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void displayMatchInfo(int matchId)
			{
				// TODO Auto-generated method stub
				
			}
			
		}, 400, 230));
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
	
	public void launch() throws Exception
	{
		//this.loadDataLayer("D:\\迭代一数据");
		this.loadDataLayer("D:\\dynamics");
		this.loadLogicLayer();
		this.loadPresentation();
	}
	
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
