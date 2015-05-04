package nbaquery_test.presentation;

import java.awt.Color;

import javax.swing.JFrame;

import nbaquery.data.Table;
import nbaquery.launcher.Main;
import nbaquery.logic.match.NewMatchService;
import nbaquery.presentation3.match.CompareMatchSubPanel;

public class TestMatchCalendar extends Main
{
	JFrame theFrame;
	public void loadPresentation()
	{
		theFrame = new JFrame();
		theFrame.setSize(720, 600);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setUndecorated(true);
		theFrame.setVisible(true);
		//theFrame.setBackground(new Color(0, 0, 0, 0));
		NewMatchService service = ((NewMatchService)this.matchService);
		
		@SuppressWarnings("serial")
		CompareMatchSubPanel table = new CompareMatchSubPanel(service, theFrame.getWidth(), theFrame.getHeight(), 200)
		{
			{
				
				this.displayTable.oddBackground = this.displayTable.evenBackground = new Color(0, 0, 0, 0);
			}

			@Override
			protected Table redoQuery()
			{
				return this.matchService.searchForMatchesTable(null, null, null, true);
			}

			@Override
			protected void reEnter()
			{
				this.matchTableModel.switchToDate(2014, 0);
			}
		};
		
		theFrame.add(table);
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
		new TestMatchCalendar().launch();
		}
		catch(Exception e)
		{
			
		}
	}
}
