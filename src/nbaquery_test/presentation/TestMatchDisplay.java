package nbaquery_test.presentation;
import java.awt.Font;

import javax.swing.JFrame;

import nbaquery.data.Row;
import nbaquery.launcher.Main;
import nbaquery.logic.match.NewMatchService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.match.MatchComponent;

public class TestMatchDisplay extends Main
{
	JFrame theFrame;
	public void loadPresentation()
	{
		theFrame = new JFrame();
		theFrame.setSize(300, 100);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setUndecorated(true);
		theFrame.setVisible(true);
		//theFrame.setBackground(new Color(0, 0, 0, 0));
		MatchComponent component = new MatchComponent(new DetailedInfoContainer()
		{

			@Override
			public void displayPlayerInfo(Row player, boolean stacked)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void displayTeamInfo(Row team, boolean stacked)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void displayMatchInfo(int matchId, boolean stacked)
			{
				System.out.println(matchId);
			}
			
		}, ((NewMatchService)this.matchService)
				.searchForMatchesTable(null, null, null, true).getRows()[0]);
		component.setSize(theFrame.getSize());
		theFrame.add(component);
		theFrame.setAlwaysOnTop(true);
		MatchComponent.scoreLabelFont = new Font(Font.MONOSPACED, Font.PLAIN, 0).deriveFont(20.0f);
		MatchComponent.plainTextFont = new Font(Font.MONOSPACED, Font.PLAIN, 0).deriveFont(12.0f);
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
		new TestMatchDisplay().launch();
		}
		catch(Exception e)
		{
			
		}
	}
}
