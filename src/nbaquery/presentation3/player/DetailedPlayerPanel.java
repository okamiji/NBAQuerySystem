package nbaquery.presentation3.player;

import javax.swing.JPanel;

import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.logic.match.NewMatchService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.KeyValueDisplay;
import nbaquery.presentation3.match.CompareMatchSubPanel;

@SuppressWarnings("serial")
public class DetailedPlayerPanel extends JPanel
{
	static final String[] keyName = new String[]{"player_name", "team_name", "player_height", "player_weight"};
	static final String[] keyDisplayName = new String[]{"球员名称", "所属队伍", "身高", "体重"};
	
	KeyValueDisplay[] configuration = new KeyValueDisplay[keyName.length];
	
	static int upperSize = 300;
	static int upperWidth = 250;
	
	public final CompareMatchSubPanel match;
	Row playerRow;
	
	public DetailedPlayerPanel(DetailedInfoContainer container, NewMatchService matchService, int width, int height)
	{
		this.setSize(width, height);
		this.setLayout(null);
		
		for(int i = 0; i < keyName.length; i ++) 
		{
			configuration[i] = new KeyValueDisplay(keyDisplayName[i], keyName[i]);
			configuration[i].setSize(upperWidth, upperSize / keyName.length - 1);
			configuration[i].setLocation(width - upperWidth - 2, 2 + (upperSize / keyName.length) * i);
		}
		
		match = new CompareMatchSubPanel(container, matchService, width - 4, height - upperSize - 4, 300, true)
		{
			Table reEnteredTable;
			@Override
			protected void reEnter()
			{
				for(int i = 0; i < keyName.length; i ++) configuration[i].setRow(playerRow);
				reEnteredTable = super.matchService.searchMatchesByPlayer(
						(String) playerRow.getDeclaredTable().getColumn("player_name").getAttribute(playerRow));
				Row firstRow = reEnteredTable.getRows()[0];
				reEnteredTable.getColumn("match_season").getAttribute(firstRow);
			}

			@Override
			protected Table redoQuery()
			{
				return null;
			}
			
		};
	}
	
	public void setRow(Row row)
	{
		this.playerRow = row;
	}
}
