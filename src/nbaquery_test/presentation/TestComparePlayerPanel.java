package nbaquery_test.presentation;

import java.awt.Color;

import javax.swing.JFrame;

import nbaquery.launcher.Main;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.presentation3.player.ComparePlayerSubPanel;

public class TestComparePlayerPanel extends Main
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
		theFrame.add(new ComparePlayerSubPanel((NewPlayerService) this.playerService, new DetailedInfoContainerStub(), 720, 600, 25));
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