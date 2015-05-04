package nbaquery_test.presentation;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;

import nbaquery.data.Row;
import nbaquery.launcher.Main;
import nbaquery.logic.match.NewMatchService;
import nbaquery.presentation3.DetailedInfoContainer;
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
		theFrame.add(new TodayMatchSubPanel(new DetailedInfoContainer()
		{
			@Override
			public void displayMatchInfo(int matchId, boolean s)
			{
				System.out.println(matchId);
			}

			@Override
			public void displayPlayerInfo(Row player, boolean s)
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
			public void displayTeamInfo(Row team, boolean s)
			{
				// TODO Auto-generated method stub
				
			}
			
		}, (NewMatchService)this.matchService, 600, 100));
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
	
	public void launch() throws Exception
	{
		//this.loadDataLayer("D:\\����һ����");
		this.loadDataLayer("D:\\dynamics");
		this.loadLogicLayer();
		this.loadPresentation();
	}
	
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
