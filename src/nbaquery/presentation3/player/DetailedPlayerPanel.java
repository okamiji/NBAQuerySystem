package nbaquery.presentation3.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nbaquery.data.Cursor;
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
	static final String[] keyDisplayName = new String[]{"��Ա����", "��������", "λ��", "��������", "����", "����", "���", "����", "��ҵѧУ"};
	
	KeyValueDisplay[] configuration = new KeyValueDisplay[keyName.length];
	
	static int upperHeight = 300;
	static int upperWidth = 250;
	
	public final CompareMatchSubPanel match;
	Row playerRow;
	
	public final JLabel actionDisplay = new JLabel()
	{
		public void paint(Graphics g)
		{
			ImageIcon icon = (ImageIcon) this.getIcon();
			if(icon == null) super.paint(g);
			else g.drawImage(icon.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
		}
	};
	
	public DetailedPlayerPanel(final DetailedInfoContainer container, NewMatchService matchService, int width, int height)
	{
		this.setSize(width, height);
		this.setLayout(null);
		
		for(int i = 0; i < keyName.length; i ++) 
		{
			if(keyName[i].equals("player_position"))
			{
				configuration[i] = new KeyValueDisplay(keyDisplayName[i], keyName[i])
				{
					public String convertValueToString(Object value)
					{
						if(value == null) return "";
						String pos = value.toString();
						if(pos.equals("C")) return "�з�";
						if(pos.equals("G")) return "����";
						return "ǰ��";
					}
				};
			}
			else configuration[i] = new KeyValueDisplay(keyDisplayName[i], keyName[i]);
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
				this.displayTable.oddBackground = this.displayTable.evenBackground = new Color(0, 0, 0, 0);
				this.enumerator.sectionPerEnumerator = 5;
			}
			Table reEnteredTable = null;
			@Override
			protected void reEnter()
			{
				reEnteredTable = super.matchService.searchMatchesByPlayer(
						(String) playerRow.getDeclaredTable().getColumn("player_name").getAttribute(playerRow));
				Cursor rows = reEnteredTable.getRows();
				Row firstRow = rows.next();
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
			
			for(int i = 0; i < keyName.length; i ++) configuration[i].setRow(playerRow);
			
			Object image = this.playerRow.getDeclaredTable().getColumn("player_action").getAttribute(playerRow);
			if(image != null)
				this.actionDisplay.setIcon(ImageIconResource.getImageIcon(((Image)image).toString()));
			else this.actionDisplay.setText("No Image");
		}
	}
}
