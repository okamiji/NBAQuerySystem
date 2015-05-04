package nbaquery_test.presentation;

import java.awt.Color;

import javax.swing.JFrame;

import nbaquery.data.Row;
import nbaquery.launcher.Main;
import nbaquery.logic.team.NewTeamService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.team.HotTeamSubPanel;

public class TestHotTeamPanel extends Main
{
	JFrame theFrame;
	public void loadPresentation()
	{
		theFrame = new JFrame();
		theFrame.setSize(400, 240);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setUndecorated(true);
		theFrame.setVisible(true);
		theFrame.setBackground(new Color(0, 0, 0, 0));
		theFrame.add(new HotTeamSubPanel((NewTeamService) this.teamService, new DetailedInfoContainer()
		{
			@Override
			public void displayMatchInfo(int matchId, boolean s)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void displayPlayerInfo(Row player, boolean s)
			{
			
			}

			@Override
			public void displayTeamInfo(Row team, boolean s)
			{
				System.out.println(team.getDeclaredTable().getColumn("team_name").getAttribute(team));
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
		new TestHotTeamPanel().launch();
		}
		catch(Exception e)
		{
			
		}
	}
}
