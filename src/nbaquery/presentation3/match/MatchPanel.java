package nbaquery.presentation3.match;

import java.awt.Graphics;

import javax.swing.JPanel;

import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.logic.match.NewMatchService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.DisplayButton;
import nbaquery.presentation3.MainFrame;


@SuppressWarnings("serial")
public class MatchPanel extends JPanel
{
	
	public static final int HOT_SPOT = 0;
	public static final int SHOW = 1;
	public static final int MINIMIZE = 2;
	
	public int mode = HOT_SPOT;
	
	public final TodayMatchSubPanel todayMatch;
	public final CompareMatchSubPanel matchSubPanel;
	public final DisplayButton matchButton;
	
	public MatchPanel(DetailedInfoContainer infoContainer, NewMatchService playerService, DisplayButton matchButton)
	{
		this.setLayout(null);
		this.setBounds(0, 0, MainFrame.width - 1, MainFrame.height - 1);
		this.setBackground(MainFrame.transparent);
		
		this.todayMatch = new TodayMatchSubPanel(infoContainer, playerService, 600, 100);
		this.todayMatch.setBackground(MainFrame.transparent);
		this.todayMatch.setLocation(160, 60);
		this.add(todayMatch);
		
		this.matchSubPanel = new CompareMatchSubPanel(infoContainer, playerService, 720, 600, 300, false)
		{
			{
				this.displayTable.oddBackground = this.displayTable.evenBackground = MainFrame.transparent;
				this.setBackground(MainFrame.transparent);
			}
			
			Table reEnteredTable;
			
			@Override
			protected void reEnter()
			{
				reEnteredTable = this.matchService.searchForMatchesTable(null, null, null, true);
				Row[] rows = reEnteredTable.getRows();
				Row lastRow = rows[rows.length - 1];
				String season = (String) lastRow.getDeclaredTable().getColumn("match_season").getAttribute(lastRow);
				String date = (String) lastRow.getDeclaredTable().getColumn("match_date").getAttribute(lastRow);
				
				String[] fromAndTo = season.split("-");
				int from = Integer.parseInt(fromAndTo[0]) + 2000;
				int to = Integer.parseInt(fromAndTo[1]) + 2000;
				
				String[] monthAndDay = date.split("-");
				month = Integer.parseInt(monthAndDay[0]) - 1;
				
				if(month >= 6) year = from;
				else year = to;
			}

			@Override
			protected Table redoQuery()
			{
				if(reEnteredTable != null)
				{
					Table theTable = reEnteredTable;
					reEnteredTable = null;
					return theTable;
				}
				else
				{
					return this.matchService.searchForMatchesTable(null, null, null, true);
				}
			}
			
		};
		this.matchSubPanel.setLocation(45, 40);
		this.add(matchSubPanel);
		
		this.matchButton = matchButton;
		this.add(matchButton);
	}
	
	public void paint(Graphics g)
	{
		g.setColor(MainFrame.backgroundColor);
		if(mode == HOT_SPOT)
		{
			this.todayMatch.setVisible(true);
			this.matchSubPanel.setVisible(false);
			this.matchButton.setLocation(60, 110 - this.matchButton.getHeight()/2);
			g.fillRect(30, 50, 740, 120);
		}
		else if(mode == SHOW)
		{
			this.todayMatch.setVisible(false);
			this.matchSubPanel.setVisible(true);
			g.fillRect(30, 40, 740, 600);
			g.fillRect(30, 650, 740, 60);
			this.matchButton.setLocation(60, 680 - this.matchButton.getHeight() / 2);
			g.setColor(MainFrame.selectedShadow);
			g.fillRect(55, 710, 60, 5);
		}
		else
		{
			this.todayMatch.setVisible(false);
			this.matchSubPanel.setVisible(false);
			this.matchButton.setLocation(60, 680 - this.matchButton.getHeight() / 2);
		}
		super.paint(g);
	}
}