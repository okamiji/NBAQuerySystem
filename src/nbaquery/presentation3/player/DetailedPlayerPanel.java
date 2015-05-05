package nbaquery.presentation3.player;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import nbaquery.data.Image;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.logic.match.NewMatchService;
import nbaquery.presentation.resource.ImageIconResource;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.KeyValueDisplay;
import nbaquery.presentation3.match.CompareMatchSubPanel;

@SuppressWarnings("serial")
public class DetailedPlayerPanel extends JPanel
{
	static final String[] keyName = new String[]{"player_name", "team_name", "player_position", "player_birth", "player_age", "player_exp", "player_height", "player_weight", "player_school"};
	static final String[] keyDisplayName = new String[]{"球员名称", "所属队伍", "位置", "出生日期", "年龄", "球龄", "身高", "体重", "毕业学校"};
	
	KeyValueDisplay[] configuration = new KeyValueDisplay[keyName.length];
	
	static int upperHeight = 300;
	static int upperWidth = 250;
	
	public final CompareMatchSubPanel match;
	Row playerRow;
	
	public final JLabel actionDisplay = new JLabel();
	
	public DetailedPlayerPanel(final DetailedInfoContainer container, NewMatchService matchService, int width, int height)
	{
		this.setSize(width, height);
		this.setLayout(null);
		
		for(int i = 0; i < keyName.length; i ++) 
		{
			configuration[i] = new KeyValueDisplay(keyDisplayName[i], keyName[i]);
			configuration[i].setSize(upperWidth, upperHeight / keyName.length - 1);
			configuration[i].setLocation(width - upperWidth - 2, 2 + (upperHeight / keyName.length) * i);
			this.add(configuration[i]);
		}
		
		configuration[1].addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent me)
			{
				container.displayTeamInfo(playerRow, true);
			}
		});
		
		this.actionDisplay.setBounds(2, 2, width - upperWidth - 6, upperHeight);
		this.actionDisplay.setHorizontalAlignment(JLabel.CENTER);
		this.add(this.actionDisplay);
		
		match = new CompareMatchSubPanel(container, matchService, width - 4, height - upperHeight - 6, 170, true)
		{
			{
				this.enumerator.sectionPerEnumerator = 5;
			}
			Table reEnteredTable = null;
			@Override
			protected void reEnter()
			{
				for(int i = 0; i < keyName.length; i ++) configuration[i].setRow(playerRow);
				reEnteredTable = super.matchService.searchMatchesByPlayer(
						(String) playerRow.getDeclaredTable().getColumn("player_name").getAttribute(playerRow));
				Row[] rows = reEnteredTable.getRows();
				Row firstRow = rows[0];
				String season = (String) reEnteredTable.getColumn("match_season").getAttribute(firstRow);
				String date = (String) reEnteredTable.getColumn("match_date").getAttribute(firstRow);
				
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
					return super.matchService.searchMatchesByPlayer(
						(String) playerRow.getDeclaredTable().getColumn("player_name").getAttribute(playerRow));
				}
			}
		};
		match.setBounds(2, upperHeight + 4,  width - 2, height - upperHeight - 6);
		this.add(match);
	}
	
	public void setRow(Row row)
	{
		if(row != null)
		{
			this.playerRow = row;
			match.shouldRedoQuery = true;
			
			Object image = this.playerRow.getDeclaredTable().getColumn("player_action").getAttribute(playerRow);
			if(image != null)
				this.actionDisplay.setIcon(ImageIconResource.getImageIcon(((Image)image).toString()));
			
			else this.actionDisplay.setText("No Image");
		}
	}
}
