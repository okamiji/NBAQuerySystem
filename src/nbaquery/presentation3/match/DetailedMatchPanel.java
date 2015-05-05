package nbaquery.presentation3.match;

import javax.swing.JPanel;

import nbaquery.logic.match.NewMatchService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.table.DisplayTable;

@SuppressWarnings("serial")
public class DetailedMatchPanel extends JPanel
{
	MatchComponent matchComponent;
	
	static int logoArea = 100;
	static int quarterScoreArea = 100;
	
	DisplayTable quarter, performance;
	
	
	public DetailedMatchPanel(DetailedInfoContainer container, NewMatchService matchService, int width, int height)
	{
		super.setSize(width, height);
		super.setLayout(null);
		
		//XXX allocate tables.
		quarter = new DisplayTable();
		quarter.setSize(width - 4, quarterScoreArea);
		quarter.setLocation(2, logoArea + 4);
		this.add(quarter);
		
		performance = new DisplayTable();
		performance.setSize(width - 4, height - 8 - quarterScoreArea - logoArea);
		performance.setLocation(2, logoArea + quarterScoreArea + 6);
		this.add(performance);
	}
}
