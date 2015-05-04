package nbaquery.presentation3.match;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import nbaquery.data.Row;
import nbaquery.logic.match.NewMatchService;
import nbaquery.presentation3.DetailedInfoContainer;

@SuppressWarnings("serial")
public class TodayMatchSubPanel extends JPanel
{
	DetailedInfoContainer container;
	MatchEnumerator enumerator;
	NewMatchService matchService;
	
	public TodayMatchSubPanel(DetailedInfoContainer container, NewMatchService matchService, int width, int height)
	{
		this.matchService = matchService;
		this.container = container;
		
		this.setSize(width, height);
		this.setLayout(null);
		
		this.enumerator = new MatchEnumerator(container, width, height, true, false);
		this.enumerator.sectionPerEnumerator = 2;
		this.enumerator.setLocation(0, 0);
		this.add(this.enumerator);
	}
	
	public void paint(Graphics g)
	{
		if(this.matchService.shouldRedoQuery(this))
		{
			Row[] rows = this.matchService.listTodayMatches().getRows();
			ArrayList<Row> wrapped = new ArrayList<Row>();
			for(Row row : rows) wrapped.add(row);
			this.enumerator.setMatchStrip(new MatchStrip(0, wrapped));
		}
		super.paint(g);
	}
}
