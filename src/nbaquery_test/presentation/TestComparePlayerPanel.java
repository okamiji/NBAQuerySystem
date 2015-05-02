package nbaquery_test.presentation;

import java.awt.Color;

import javax.swing.JFrame;

import nbaquery.data.Row;
import nbaquery.launcher.Main;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.player.ComparePlayerSubPanel;

public class TestComparePlayerPanel extends Main
{
	JFrame theFrame;
	public void loadPresentation()
	{
		theFrame = new JFrame();
		theFrame.setSize(600, 400);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setUndecorated(true);
		theFrame.setVisible(true);
		theFrame.setBackground(new Color(0, 0, 0, 0));
		theFrame.add(new ComparePlayerSubPanel((NewPlayerService) this.playerService, new DetailedInfoContainer()
		{
			@Override
			public void displayMatchInfo(int matchId)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void displayPlayerInfo(Row player)
			{
				System.out.println("=================================================================");
				System.out.println(player.getDeclaredTable().getColumn("player_name").getAttribute(player));
				System.out.println(player.getDeclaredTable().getColumn("player_age").getAttribute(player));
				System.out.println(player.getDeclaredTable().getColumn("player_exp").getAttribute(player));
				System.out.println(player.getDeclaredTable().getColumn("player_birth").getAttribute(player));
				System.out.println(player.getDeclaredTable().getColumn("player_school").getAttribute(player));
				System.out.println(player.getDeclaredTable().getColumn("player_height").getAttribute(player));
				System.out.println(player.getDeclaredTable().getColumn("player_weight").getAttribute(player));
				System.out.println("=================================================================");
			}

			@Override
			public void displayTeamInfo(Row team)
			{
				// TODO Auto-generated method stub
				
			}
			
		}, 600, 400, 15));
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
		new TestComparePlayerPanel().launch();
		}
		catch(Exception e)
		{
			
		}
	}
}